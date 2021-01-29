package com.example.algamoney.api.controller;

import com.example.algamoney.api.exception.AlgamoneyExceptionHandler;
import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.repository.PostingRepository;
import com.example.algamoney.api.repository.filter.PostingFilter;
import com.example.algamoney.api.service.PostingService;
import com.example.algamoney.api.service.exception.InactivePersonException;
import com.example.algamoney.api.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postings")
public class PostingController {

    private final PostingRepository postingRepository;

    private final PostingService postingService;

    private final MessageSource messageSource;

    @Autowired
    public PostingController(PostingRepository postingRepository, PostingService postingService, MessageSource messageSource) {
        this.postingRepository = postingRepository;
        this.postingService = postingService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public Page<Posting> findAll(PostingFilter postingFilter, Pageable pageable){
        return postingRepository.filter(postingFilter, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Posting> posting = postingRepository.findById(id);
        return posting.isPresent() ? ResponseEntity.ok(posting) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Posting> create(@Valid @RequestBody Posting posting) {
        Posting savedPosting = postingService.save(posting);
        return ResponseEntity.created(HeaderUtil.addLocation(savedPosting.getId())).body(savedPosting);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        postingRepository.deleteById(id);
    }

    @ExceptionHandler({InactivePersonException.class})
    public ResponseEntity<Object> handleInactivePersonException(InactivePersonException ex){
        String userMessage = messageSource.getMessage("inactive.person.message", null, LocaleContextHolder.getLocale());
        String devMessage = ex.toString();
        List<AlgamoneyExceptionHandler.Error> errors = Collections.singletonList(new AlgamoneyExceptionHandler.Error(userMessage, devMessage));
        return ResponseEntity.badRequest().body(errors);
    }
}
