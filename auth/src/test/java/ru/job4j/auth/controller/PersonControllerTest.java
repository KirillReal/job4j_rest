package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository repository;

    @AfterEach
    public void clear() {
        repository.deleteAll();
    }

    private Person createTestPerson(String login, String password) {
        Person emp = Person.of(login, password);
        return repository.save(emp);
    }

    @Test
    public void whenCreatedThenStatus201() throws Exception {
        Person person = Person.of("Login", "Password");
        mockMvc.perform(
                post("/person/")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.login").value("Login"))
                .andExpect(jsonPath("$.password").value("Password"));
    }

    @Test
    public void whenGetUserInfoByIdThenStatus200() throws Exception {
        int id = createTestPerson("Login", "Password").getId();
        mockMvc.perform(
                get("/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.login").value("Login"))
                .andExpect(jsonPath("$.password").value("Password"));
    }

    @Test
    public void whenGetUserInfoByIdThenStatus404() throws Exception {
        mockMvc.perform(
                get("/persons/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateUserThenStatus200() throws Exception {
        int id = createTestPerson("Login", "Password").getId();
        Person personTest = Person.of("Логин", "Пароль");
        personTest.setId(id);
        mockMvc.perform(
                put("/person/")
                        .content(objectMapper.writeValueAsString(personTest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(
                get("/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("Логин"))
                .andExpect(jsonPath("$.password").value("Пароль"));
    }

    @Test
    public void whenDeleteUserThenStatus200() throws Exception {
        int id = createTestPerson("Login", "Password").getId();
        mockMvc.perform(
                delete("/person/{id}", id))
                .andExpect(status().isOk());
        mockMvc.perform(
                get("/persons/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetAllPersonsThenStatus200() throws Exception {
        Person p1 = createTestPerson("login1", "password1");
        Person p2 = createTestPerson("login2", "password2");

        mockMvc.perform(
                get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(p1, p2))));
    }
}