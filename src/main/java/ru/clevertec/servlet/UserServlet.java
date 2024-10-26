package ru.clevertec.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.config.JacksonConfig;
import ru.clevertec.domain.User;
import ru.clevertec.dto.UserDTO;
import ru.clevertec.exception.UserNotFoundException;
import ru.clevertec.mapper.UserMapper;
import ru.clevertec.mapper.UserMapperImpl;
import ru.clevertec.service.UserService;
import ru.clevertec.service.UserServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@Slf4j
@WebServlet("/users")
@RequiredArgsConstructor
public class UserServlet extends HttpServlet {
    private final UserMapper userMapper = new UserMapperImpl();
    private final UserService<User> userService = UserServiceImpl.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        log.info("Init method from UserServlet");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getParameter("id") != null) {
            log.info("doGet method invoke(get user by id).");
            getUserById(req, resp);
        } else {
            log.info("doGet method invoke(get all users).");
            getAllUsers(req, resp);
        }
    }

    private void getAllUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper objectMapper = JacksonConfig.objectMapper();
        List<UserDTO> userDTOS = userMapper.toUserDTOs(userService.findAllUsers());
        String jsonResponse = objectMapper.writeValueAsString(userDTOS);

        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonResponse);
            resp.setStatus(SC_OK);
        }
    }


    private void getUserById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String response = "";
        ObjectMapper objectMapper = JacksonConfig.objectMapper();
        int id = Integer.parseInt(req.getParameter("id"));

        try {
            UserDTO userDTO = userMapper.toUserDTO(userService.findById(id));
            if (userDTO != null) {
                response = objectMapper.writeValueAsString(userDTO);
                resp.setStatus(SC_OK);
            }
        } catch (UserNotFoundException ex) {
            response = String.format("User with id %d not found", id);
            resp.setStatus(SC_NOT_FOUND);
        }

        try (PrintWriter out = resp.getWriter()) {
            out.write(response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        ObjectMapper objectMapper = JacksonConfig.objectMapper();
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        log.info("doPost method invoke(add new user).");
        String json = objectMapper.writeValueAsString(userMapper.toUserDTO(
                userService.save(userMapper.toUser(userDTO))));

        try (PrintWriter out = resp.getWriter()) {
            out.write(json);
            resp.setStatus(SC_CREATED);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper objectMapper = JacksonConfig.objectMapper();
        UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
        log.info("doPut method invoke(replace user by id).");
        String response;
        int id = Integer.parseInt(req.getParameter("id"));

        try {
            response = objectMapper.writeValueAsString(userMapper.toUserDTO(
                    userService.updateById(id, userMapper.toUser(userDTO))));
            resp.setStatus(SC_OK);
        } catch (UserNotFoundException ex) {
            response = String.format("User with id %d not found", id);
            resp.setStatus(SC_NOT_FOUND);
        }

        try (PrintWriter out = resp.getWriter()) {
            out.write(response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.info("doDelete method invoke(delete user by id).");
        String response;
        int id = Integer.parseInt(req.getParameter("id"));

        try {
            userService.deleteById(id);
            response = String.format("User with id %d was successfully deleted.", id);
            resp.setStatus(SC_NO_CONTENT);
        } catch (UserNotFoundException ex) {
            response = String.format("User with id %d not found", id);
            resp.setStatus(SC_NOT_FOUND);
        }

        try (PrintWriter out = resp.getWriter()) {
            out.write(response);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        log.info("Destroy method from UserServlet");
    }
}


