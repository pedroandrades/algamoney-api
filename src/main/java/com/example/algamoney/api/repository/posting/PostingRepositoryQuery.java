package com.example.algamoney.api.repository.posting;

import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.repository.filter.PostingFilter;

import java.util.List;

public interface PostingRepositoryQuery {

    public List<Posting> filter(PostingFilter postingFilter);
}
