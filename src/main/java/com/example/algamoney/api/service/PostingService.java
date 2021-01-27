package com.example.algamoney.api.service;

import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.repository.PersonRepository;
import com.example.algamoney.api.repository.PostingRepository;
import com.example.algamoney.api.service.exception.InactivePersonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;


@Service
public class PostingService {

    private final PostingRepository postingRepository;

    private final PersonRepository personRepository;

    @Autowired
    public PostingService(PostingRepository postingRepository, PersonRepository personRepository) {
        this.postingRepository = postingRepository;
        this.personRepository = personRepository;
    }

    public Posting save(Posting posting) {
        verifyIfPersonIsActive(personRepository.findById(posting.getPerson().getId()).orElseThrow(() -> new EmptyResultDataAccessException(1)));
        return postingRepository.save(posting);
    }

    public void verifyIfPersonIsActive(Person person) {
        if(!person.getActive()){
            throw new InactivePersonException();
        }
    }
}
