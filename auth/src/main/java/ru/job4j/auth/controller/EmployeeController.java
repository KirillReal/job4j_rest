package ru.job4j.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.EmployeeRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeRepository repo;

    EmployeeController(final EmployeeRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public List<Employee> findAll() {
        return StreamSupport.stream(this.repo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable int id) {
        return this.repo.findById(id).isPresent()
                ? new ResponseEntity<>(this.repo.findById(id).get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/")
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return employee != null
                ? new ResponseEntity<>(this.repo.save(employee), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/")
    public ResponseEntity<Employee> update(@RequestBody Employee employee) {
        return employee != null
                ? new ResponseEntity<>(this.repo.save(employee), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (this.repo.findById(id).isPresent()) {
            this.repo.delete(this.repo.findById(id).get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}/persons")
    public ResponseEntity<Set<Person>> findPersonsByEmployeeId(@PathVariable int id) {
        Set<Person> res = null;
        if (this.repo.findById(id).isPresent()) {
            res = this.repo.findById(id).get().getPersons();
        }
        return  res != null
                ? new ResponseEntity<>(res, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/persons")
    public ResponseEntity<Person> addPerson(@PathVariable int id,
                                            @RequestBody Person person) {
        if (this.repo.findById(id).isPresent()) {
            this.repo.findById(id).get().addPerson(person);
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping("/{id}/persons/{pid}")
    public ResponseEntity<Void> deletePerson(@PathVariable int id,
                                             @PathVariable int pid) {
        if (this.repo.findById(id).isPresent()) {
            Set<Person> persons = this.repo.findById(id).get().getPersons();
            persons.removeIf(p -> p.getId() == pid);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
