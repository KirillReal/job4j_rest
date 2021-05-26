package ru.job4j.auth.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
