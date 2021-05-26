package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
