package com.example.algamoney.api.controller;

import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.PersonRepository;
import com.example.algamoney.api.service.PersonService;
import com.example.algamoney.api.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;

    private final PersonService personService;

    @Autowired
    public PersonController(PersonRepository personRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @GetMapping
    public List<Person> findAll(){
        return personRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        Person savedPerson = personRepository.save(person);
        return ResponseEntity.created(HeaderUtil.addLocation(savedPerson.getId())).body(savedPerson);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Person> person = personRepository.findById(id);
        return person.isPresent() ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        personRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person person){
        return ResponseEntity.ok(personService.update(id, person));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> partialUpdate(@Valid @PathVariable Long id, @RequestBody Person person) {
        return ResponseEntity.ok(personService.partialUpdate(id, person));
    }
}
