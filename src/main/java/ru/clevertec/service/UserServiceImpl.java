package ru.clevertec.service;

import ru.clevertec.domain.User;
import ru.clevertec.entity.UserEntity;
import ru.clevertec.exception.UserNotFoundException;
import ru.clevertec.mapper.UserMapper;
import ru.clevertec.mapper.UserMapperImpl;
import ru.clevertec.repository.UserRepository;
import ru.clevertec.repository.UserRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService<User> {
    private final UserMapper userMapper = new UserMapperImpl();
    private static UserServiceImpl userServiceImpl;
    private final UserRepository<UserEntity> userRepository = new UserRepositoryImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (userServiceImpl == null) {
            userServiceImpl = new UserServiceImpl();
        }
        return userServiceImpl;
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.toUsers(userRepository.findAll());
    }

    @Override
    public User findById(int id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()) {
            throw UserNotFoundException.byId(id);
        } else {
            return userMapper.toUser(optionalUserEntity.get());
        }
    }

    @Override
    public User save(User user) {
        return userMapper.toUser(userRepository.save(userMapper.toUserEntity(user)));
    }

    @Override
    public User updateById(int id, User user) {
        Optional<UserEntity> optionalUserEntity = userRepository.updateById(id, userMapper.toUserEntity(user));
        if (optionalUserEntity.isEmpty()) {
            throw UserNotFoundException.byId(id);
        } else {
            return userMapper.toUser(optionalUserEntity.get());
        }
    }

    @Override
    public void deleteById(int id) {
        if (userRepository.deleteById(id) == 0) {
            throw UserNotFoundException.byId(id);
        }
    }
}
