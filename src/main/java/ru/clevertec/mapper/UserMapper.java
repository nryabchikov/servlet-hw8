package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.domain.User;
import ru.clevertec.dto.UserDTO;
import ru.clevertec.entity.UserEntity;

import java.util.List;

@Mapper
public interface UserMapper {
    User toUser(UserEntity userEntity);
    List<User> toUsers(List<UserEntity> userEntities);
    UserDTO toUserDTO(User user);
    List<UserDTO> toUserDTOs(List<User> users);
    UserEntity toUserEntity(User user);
    User toUser(UserDTO userDTO);
}
