package ru.clevertec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private int age;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate dateOfBirth;
}
