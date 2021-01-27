package com.example.algamoney.api.controller;

import com.example.algamoney.api.model.Posting;
import com.example.algamoney.api.repository.PostingRepository;
import com.example.algamoney.api.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postings")
public class PostingController {

    private final PostingRepository postingRepository;

    @Autowired
    public PostingController(PostingRepository postingRepository) {
        this.postingRepository = postingRepository;
    }

    @GetMapping
    public List<Posting> findAll(){
        return postingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Posting> posting = postingRepository.findById(id);
        return posting.isPresent() ? ResponseEntity.ok(posting) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Posting> create(@Valid @RequestBody Posting posting) {
        Posting savedPosting = postingRepository.save(posting);
        return ResponseEntity.created(HeaderUtil.addLocation(savedPosting.getId())).body(savedPosting);
    }
}
