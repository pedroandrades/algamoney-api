package com.example.algamoney.api.service;

import com.example.algamoney.api.model.Address;
import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonService.class)
@AutoConfigureMockMvc
public class PersonServiceTests {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Test
    public void updateTest(){
        Person personMock = new Person(1L, "person1", true, new Address());
        Person updatePersonMock = new Person(1L, "update", true, new Address());

        given(personRepository.findById(anyLong())).willReturn(Optional.of(personMock));
        given(personRepository.save(any())).willReturn(updatePersonMock);
        assertEquals(updatePersonMock, personService.update(1L, updatePersonMock));
    }

    @Test
    public void partialUpdateTest(){
        Person personMock = new Person(1L, "person1", true, new Address());
        Person updatePersonMock = new Person(1L, "update", true, new Address());

        given(personRepository.findById(anyLong())).willReturn(Optional.of(personMock));
        given(personRepository.save(any())).willReturn(updatePersonMock);
        assertEquals(updatePersonMock, personService.partialUpdate(1L, updatePersonMock));
    }
}
