package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dto.UserSessionDto;
import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.entity.Operator;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.AuthException;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.UserService;
import ru.kpfu.itis.sorokin.util.PasswordUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final OperatorDao operatorDao;

    public UserServiceImpl(UserDao userDao, OperatorDao operatorDao) {
        this.userDao = userDao;
        this.operatorDao = operatorDao;
    }

    @Override
    public void signUp(UserSignUpDto userSignUpDto) throws ValidationException {

        validateSignUpUser(userSignUpDto);

        User user = new User();

        String paswordHash = PasswordUtil.encrypt(userSignUpDto.password());

        user.setEmail(userSignUpDto.email());
        user.setName(userSignUpDto.username());
        user.setPasswordHash(paswordHash);
        user.setRole(Role.USER);

        userDao.save(user);
    }

    @Override
    public Optional<UserSessionDto> login(String email, String password) throws ValidationException {
        validateAuthUser(email);

        Optional<User> userOptional = userDao.findByEmail(email);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            return Optional.empty();
        }

        if (user.getRole() == Role.USER || user.getRole() == Role.ADMIN) {
            return Optional.of(new UserSessionDto(user.getId(), user.getName(), user.getEmail(), user.getRole()));
        } else {
            Optional<Operator> operatorOptional = operatorDao.findByUserId(user.getId());

            if (operatorOptional.isEmpty()) {
                throw new ServiceException("Failed auth operator");
            }

            Operator operator = operatorOptional.get();

            if (operator.getStatus() == Status.APPROVED) {
                return Optional.of(new UserSessionDto(operator.getUserId(), operator.getCompanyName(), user.getEmail(), user.getRole()));
            } else if (operator.getStatus() == Status.PENDING) {
                throw new AuthException("Ваш аккаунт ожидает подтверждения администратором.");
            } else {
                throw new AuthException("Ваша заявка на регестрацию была отклонена, свяжитесь с администратором");
            }
        }
    }

    private void validateSignUpUser(UserSignUpDto userSignUpDto) throws ValidationException {
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
    }

    private void validateAuthUser(String email) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (!email.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$")) {
            errors.put("email", "Несоответствующий формат почты");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
