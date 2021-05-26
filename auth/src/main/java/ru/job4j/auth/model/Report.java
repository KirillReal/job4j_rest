package ru.job4j.auth.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Report {
    private int id;

    private String name;

    private Timestamp created;

    private Person person;

    public static Report of(int id, String name, Person person) {
        Report r = new Report();
        r.id = id;
        r.name = name;
        r.person = person;
        r.created = new Timestamp(System.currentTimeMillis());
        return r;
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

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", person=" + person +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id &&
                Objects.equals(name, report.name) &&
                Objects.equals(created, report.created) &&
                Objects.equals(person, report.person);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, created, person);
    }
}
