package com.example.algamoney.api.controller;

import com.example.algamoney.api.model.Category;
import com.example.algamoney.api.repository.CategoryRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryRepository categoryRepository;

    private final String url = "/categories/";

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
    }

    @Test
    public void findAllTest() throws Exception {
        List<Category> categoryListMock = Arrays.asList(new Category(1L, "category1"),
                                                        new Category(2L, "category2"),
                                                        new Category(3L, "category3"));

        given(categoryRepository.findAll()).willReturn(categoryListMock);

        mvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(categoryListMock.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(categoryListMock.get(1).getName())));
    }

    @Test
    public void createTest() throws Exception {
        Category categoryMock = new Category(1L, "category1");

        given(categoryRepository.save(any())).willReturn(categoryMock);

        mvc.perform(post(url).content(mapper.writeValueAsString(categoryMock))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost" + url + categoryMock.getId()))
                .andExpect(jsonPath("name", is(categoryMock.getName())));
    }

    @Test
    public void findByIdTest() throws Exception {
        Category categoryMock = new Category(1L, "category1");
        Optional<Category> categoryMockOptional = Optional.of(categoryMock);

        given(categoryRepository.findById(categoryMock.getId())).willReturn(categoryMockOptional);

        mvc.perform(get(url + categoryMock.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(categoryMock.getName())));
    }

    @Test
    public void findByNonExistentIdTest() throws Exception {

        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        mvc.perform(get(url + 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
