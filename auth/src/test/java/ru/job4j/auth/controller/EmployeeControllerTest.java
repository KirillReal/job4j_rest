package ru.job4j.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeController employeeController;

    @Test
    public void whenGetAllEmployees() throws Exception {
        Employee e1 = Employee.of(1, "andrew", "kirillovykh", "777456361522");
        Employee e2 = Employee.of(2, "maxim", "galkin", "777456365234");

        e1.addPerson(Person.of(1, "parsentev", "123"));

        when(employeeController.findAll()).thenReturn(Arrays.asList(e1, e2));

        mockMvc.perform(get("/employee/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("andrew")))
                .andExpect(jsonPath("$[0].sureName", is("kirillovykh")))
                .andExpect(jsonPath("$[0].inn", is("777456361522")))
                .andExpect(jsonPath("$[0].persons", hasSize(1)))
                .andExpect(jsonPath("$[0].persons[0].login", is("parsentev")))
                .andExpect(jsonPath("$[0].persons[0].password", is("123")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("maxim")))
                .andExpect(jsonPath("$[1].sureName", is("galkin")))
                .andExpect(jsonPath("$[1].inn", is("777456365234")))
                .andExpect(jsonPath("$[1].persons", hasSize(0)));

        verify(employeeController, times(1)).findAll();
        verifyNoMoreInteractions(employeeController);
    }

    @Test
    public void whenGetById() throws Exception {
        Employee e1 = Employee.of(1, "andrew", "kirillovykh", "777456361522");
        e1.addPerson(Person.of(1, "parsentev", "123"));

        when(employeeController.findById(anyInt())).thenReturn(new ResponseEntity<>(e1, HttpStatus.OK));

        mockMvc.perform(get("/employee/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("andrew")))
                .andExpect(jsonPath("$.sureName", is("kirillovykh")))
                .andExpect(jsonPath("$.inn", is("777456361522")))
                .andExpect(jsonPath("$.persons[0].login", is("parsentev")))
                .andExpect(jsonPath("$.persons[0].password", is("123")));

        verify(employeeController, times(1)).findById(anyInt());
        verifyNoMoreInteractions(employeeController);
    }

    @Test
    public void whenGetPersonsByEmployeeId() throws Exception {
        Employee e1 = Employee.of(1, "andrew", "kirillovykh", "777456361522");
        Person p = Person.of(1, "parsentev", "123");
        e1.addPerson(p);

        when(employeeController.findPersonsByEmployeeId(anyInt())).thenReturn(new ResponseEntity<>(e1.getPersons(), HttpStatus.OK));

        mockMvc.perform(get("/employee/1/persons"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is("parsentev")))
                .andExpect(jsonPath("$[0].password", is("123")));

        verify(employeeController, times(1)).findPersonsByEmployeeId(anyInt());
        verifyNoMoreInteractions(employeeController);
    }

    @Test
    public void whenAddEmployee() throws Exception {
        Employee e1 = Employee.of(3, "a", "a", "777456361521");
        e1.addPerson(Person.of(1, "parsentev", "123"));

        when(employeeController.create(any(Employee.class))).thenReturn(new ResponseEntity<>(e1, HttpStatus.OK));

        mockMvc.perform(post("/employee/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"a\",\"sureName\":\"a\",\"inn\":\"777456361521\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(employeeController, times(1)).create(any(Employee.class));
        ArgumentCaptor<Employee> arg = ArgumentCaptor.forClass(Employee.class);
        verify(employeeController).create(arg.capture());
        assertEquals(arg.getValue().getName(), "a");
        assertEquals(arg.getValue().getSureName(), "a");
        assertEquals(arg.getValue().getInn(), "777456361521");
    }

    @Test
    public void whenAddPersonToEmployee() throws Exception {
        Employee e1 = Employee.of(3, "a", "a", "777456361521");
        Person p = Person.of(1, "a", "123");
        e1.addPerson(p);

        when(employeeController.addPerson(anyInt(),any(Person.class))).thenReturn(new ResponseEntity<>(p, HttpStatus.OK));

        mockMvc.perform(post("/employee/1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"a\",\"password\":\"123\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(employeeController, times(1)).addPerson(anyInt(), any(Person.class));
        ArgumentCaptor<Person> arg = ArgumentCaptor.forClass(Person.class);
        verify(employeeController).addPerson(anyInt(), arg.capture());
        assertEquals(arg.getValue().getLogin(), "a");
        assertEquals(arg.getValue().getPassword(), "123");
    }

    @Test
    public void whenUpdate() throws Exception {
        when(employeeController.update(any(Employee.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(put("/employee/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"andrew\",\"sureName\":\"a\",\"inn\":\"777456361521\"}"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(employeeController, times(1)).update(any(Employee.class));
        ArgumentCaptor<Employee> arg = ArgumentCaptor.forClass(Employee.class);
        verify(employeeController).update(arg.capture());
        assertEquals("andrew", arg.getValue().getName());
        assertEquals("a", arg.getValue().getSureName());
        assertEquals("777456361521", arg.getValue().getInn());
    }

    @Test
    public void whenDelete() throws Exception {
        when(employeeController.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/employee/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(employeeController, times(1)).delete(anyInt());
        verifyNoMoreInteractions(employeeController);
    }

    @Test
    public void whenDeletePerson() throws Exception {
        when(employeeController.delete(anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(delete("/employee/1/persons/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(employeeController, times(1)).deletePerson(anyInt(), anyInt());
        verifyNoMoreInteractions(employeeController);
    }
}