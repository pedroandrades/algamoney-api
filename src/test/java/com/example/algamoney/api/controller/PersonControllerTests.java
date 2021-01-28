package com.example.algamoney.api.controller;

import com.example.algamoney.api.model.Address;
import com.example.algamoney.api.model.Person;
import com.example.algamoney.api.repository.PersonRepository;
import com.example.algamoney.api.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
@AutoConfigureMockMvc
public class PersonControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private PersonService personService;

    private final String url = "/persons/";

    private ObjectMapper mapper;

    private Person personMock;

    @Before
    public void setUp() {
        personMock = new Person(1L, "person1", true, new Address());
        this.mapper = new ObjectMapper();
    }

    @Test
    public void findAllTest() throws Exception {
        List<Person> personListMock = Arrays.asList(new Person(1L, "person1", true, new Address()),
                                                    new Person( 2L, "person2", true, new Address()),
                                                    new Person(3L, "person3", false, new Address()));
        given(personRepository.findAll()).willReturn(personListMock);

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(personListMock.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(personListMock.get(1).getName())));
    }

    @Test
    public void createTest() throws Exception {
        given(personRepository.save(any())).willReturn(personMock);

        mvc.perform(post(url).content(mapper.writeValueAsString(personMock))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost" + url + personMock.getId()))
                .andExpect(jsonPath("name", is(personMock.getName())));
    }

    @Test
    public void findByIdTest() throws Exception {
        Optional<Person> personMockOptional = Optional.of(personMock);

        given(personRepository.findById(personMock.getId())).willReturn(personMockOptional);

        mvc.perform(get(url + personMock.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(personMock.getName())));
    }

    @Test
    public void findByNonExistentIdTest() throws Exception {
        given(personRepository.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(get(url + 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTest() throws Exception {
        mvc.perform(delete(url + 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateTest() throws Exception {
        Person personMockUpdate = personMock;
        personMockUpdate.setName("updateTest");

        given(personService.update(anyLong(), any())).willReturn(personMockUpdate);

        mvc.perform(put(url + 1L).content(mapper.writeValueAsString(personMockUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(personMockUpdate.getName())))
                .andExpect(status().isOk());

    }

    @Test
    public void partialUpdateTest() throws Exception {
        Person personMockUpdate = personMock;
        personMockUpdate.setName("updateTest");

        given(personService.partialUpdate(anyLong(), any())).willReturn(personMockUpdate);

        mvc.perform(patch(url + 1L).content(mapper.writeValueAsString(personMockUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(personMockUpdate.getName())))
                .andExpect(status().isOk());
    }

}
