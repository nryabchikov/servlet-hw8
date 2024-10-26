package ru.clevertec.repository;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.db.DatabaseConnection;
import ru.clevertec.entity.UserEntity;
import ru.clevertec.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepositoryImpl implements UserRepository<UserEntity> {

    @Override
    public List<UserEntity> findAll() {
        List<UserEntity> users = new ArrayList<>();
        String query = "select * from users";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                UserEntity userEntity = UserEntity.builder()
                        .id(resultSet.getInt("id"))
                        .age(resultSet.getInt("age"))
                        .name(resultSet.getString("name"))
                        .surname(resultSet.getString("surname"))
                        .patronymic(resultSet.getString("patronymic"))
                        .dateOfBirth(resultSet.getDate("date_of_birth").toLocalDate())
                        .build();

                users.add(userEntity);
            }
            log.info("Get all users: " + users);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public Optional<UserEntity> findById(int id) {
        UserEntity userEntity = null;
        String query = """
                select * from users where id=?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userEntity = UserEntity.builder()
                            .id(resultSet.getInt("id"))
                            .age(resultSet.getInt("age"))
                            .name(resultSet.getString("name"))
                            .surname(resultSet.getString("surname"))
                            .patronymic(resultSet.getString("patronymic"))
                            .dateOfBirth(resultSet.getDate("date_of_birth").toLocalDate())
                            .build();
                    log.info("Find user by id: " + userEntity);
                } else {
                    log.info("User with id: " + id + " not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(userEntity);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        String query = """
                insert into users(age, name, surname, patronymic, date_of_birth) values (?, ?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userEntity.getAge());
            statement.setString(2, userEntity.getName());
            statement.setString(3, userEntity.getSurname());
            statement.setString(4, userEntity.getPatronymic());
            statement.setDate(5, Date.valueOf(userEntity.getDateOfBirth()));
            statement.executeUpdate();

            log.info("Save user: " + userEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userEntity;
    }

    @Override
    public Optional<UserEntity> updateById(int id, UserEntity userEntity) {
        String query = """
                update users
                set age=?, name=?, surname=?, patronymic=?, date_of_birth=?
                where id=?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userEntity.getAge());
            statement.setString(2, userEntity.getName());
            statement.setString(3, userEntity.getSurname());
            statement.setString(4, userEntity.getPatronymic());
            statement.setDate(5, Date.valueOf(userEntity.getDateOfBirth()));
            statement.setInt(6, id);
            int changedRows = statement.executeUpdate();

            if (changedRows == 0) {
                userEntity = null;
                log.info("User with id: " + id + " wasn't updated.");
            } else {
                log.info("User with id: " + id + " was successfully updated.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(userEntity);
    }

    @Override
    public int deleteById(int id) {
        int deletedRows = 0;
        String query = "delete from users where id=?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                log.info("User with id: " + id + " wasn't deleted.");
                throw UserNotFoundException.byId(id);
            } else {
                log.info("User with id: " + id + " was successfully deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedRows;
    }
}
