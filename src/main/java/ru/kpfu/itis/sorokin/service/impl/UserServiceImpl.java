package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.UserService;
import ru.kpfu.itis.sorokin.util.PasswordUtil;

import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void signUp(UserSignUpDto userSignUpDto) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (!userSignUpDto.email().matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$")) {
            errors.put("email", "Несоответствующий формат почты");
        } else if (userDao.findByEmail(userSignUpDto.email()).isPresent()) {
            errors.put("email", "Пользователь с таким email уже есть, введите другой");
        }

        if (userSignUpDto.username().length() > 63) {
            errors.put("username", "Слишком длинное имя пользователя");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        User user = new User();

        String paswordHash = PasswordUtil.encrypt(userSignUpDto.password());

        user.setEmail(userSignUpDto.email());
        user.setName(userSignUpDto.username());
        user.setPasswordHash(paswordHash);
        user.setRole(userSignUpDto.role());

        userDao.save(user);
    }
}
