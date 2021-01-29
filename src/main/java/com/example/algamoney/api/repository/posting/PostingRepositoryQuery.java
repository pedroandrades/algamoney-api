package com.example.algamoney.api.repository.posting;

import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.repository.filter.PostingFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostingRepositoryQuery {

    public Page<Posting> filter(PostingFilter postingFilter, Pageable pageable);
}
