package com.example.algamoney.api.service;

import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.stream.Stream;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person update(Long id, Person person) {
        Person savedPerson = personRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(person, savedPerson, "id");
        return personRepository.save(savedPerson);
    }

    public Person partialUpdate(Long id, Person person) {
        Person savedPerson = personRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
        BeanUtils.copyProperties(fieldComplete(person, savedPerson), savedPerson, "id");
        return personRepository.save(savedPerson);
    }

    public Person fieldComplete(Person person, Person savedPerson){
        Stream<Field> fields = Stream.of(savedPerson.getClass().getDeclaredFields());
        fields.forEach(field -> {
            try {
                field.setAccessible(true);
                if (field.get(person) == null)
                    field.set(person, field.get(savedPerson));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return person;
    }
}
