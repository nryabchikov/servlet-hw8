package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    private int id;
    private int age;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate dateOfBirth;
}
