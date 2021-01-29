package com.example.algamoney.api.repository.posting;

import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.model.Posting_;
import com.example.algamoney.api.repository.filter.PostingFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PostingRepositoryImpl implements PostingRepositoryQuery{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Posting> filter(PostingFilter postingFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Posting> criteria = builder.createQuery(Posting.class);
        Root<Posting> root = criteria.from(Posting.class);
        Predicate[] predicates = createRestriction(postingFilter, builder, root);
        criteria.where(predicates);
        TypedQuery<Posting> query = manager.createQuery(criteria);
        addPageableRestrictions(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(postingFilter));
    }

    private Predicate[] createRestriction(PostingFilter postingFilter, CriteriaBuilder builder, Root<Posting> root) {
        List<Predicate> predicates = new ArrayList<>();
        if(!ObjectUtils.isEmpty(postingFilter.getDescription())){
            predicates.add(builder.like(
                    builder.lower(root.get(Posting_.description)), "%" + postingFilter.getDescription().toLowerCase() + "%"
            ));
        }
        if(postingFilter.getDueDateAfter() != null){
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get(Posting_.dueDate), postingFilter.getDueDateAfter())
            );
        }
        if(postingFilter.getDueDateBefore() != null){
            predicates.add(
                    builder.lessThanOrEqualTo(root.get(Posting_.dueDate), postingFilter.getDueDateBefore())
            );
        }

        return predicates.toArray(new Predicate[0]);
    }

    private void addPageableRestrictions(TypedQuery<Posting> query, Pageable pageable) {
        int actualPage = pageable.getPageNumber();
        int totalRegistersByPage = pageable.getPageSize();
        int firstRegister = actualPage * totalRegistersByPage;

        query.setFirstResult(firstRegister);
        query.setMaxResults(totalRegistersByPage);
    }

    private Long total(PostingFilter postingFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Posting> root = criteria.from(Posting.class);

        Predicate[] predicates = createRestriction(postingFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
