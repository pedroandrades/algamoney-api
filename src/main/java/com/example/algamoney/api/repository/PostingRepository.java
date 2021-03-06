package com.example.algamoney.api.repository;

import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.repository.posting.PostingRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryQuery {
}
