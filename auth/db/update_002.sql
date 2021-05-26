create table employee
(
    id         serial primary key not null,
    name       varchar(2000),
    surename   varchar(2000),
    inn        varchar(15),
    datehiring timestamp
);

insert into employee (name, surename, inn, datehiring)
values ('Andrew', 'Markelov', '71274781274', now());
insert into employee (name, surename, inn, datehiring)
values ('Sergey', 'Bronnikov', '81283718246', now());


create table employee_person
(
    employee_id int,
    person_id   int,
    constraint fk_employee foreign key (employee_id) references employee (id),
    constraint pr_key primary key (employee_id, person_id)
);

insert into employee_person (employee_id, person_id)
values ('1', '1');