package ru.job4j.auth.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String surename;
    private String inn;
    private Timestamp datehiring;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employee_person",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> persons = new HashSet<>();

    public static Employee of(int id, String name, String surename, String inn) {
        Employee e = new Employee();
        e.id = id;
        e.name = name;
        e.surename = surename;
        e.inn = inn;
        e.datehiring = new Timestamp(System.currentTimeMillis());
        return e;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSureName() {
        return surename;
    }

    public void setSureName(String surename) {
        this.surename = surename;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public Timestamp getDateHiring() {
        return datehiring;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sureName='" + surename + '\'' +
                ", inn=" + inn +
                ", dateHiring=" + datehiring +
                ", persons=" + persons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                inn == employee.inn &&
                Objects.equals(name, employee.name) &&
                Objects.equals(surename, employee.surename) &&
                Objects.equals(datehiring, employee.datehiring) &&
                Objects.equals(persons, employee.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surename, inn, datehiring, persons);
    }
}
