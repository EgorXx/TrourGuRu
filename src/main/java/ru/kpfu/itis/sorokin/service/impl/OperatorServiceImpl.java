package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dto.OperatorSignUpDto;
import ru.kpfu.itis.sorokin.dto.OperatorViewDto;
import ru.kpfu.itis.sorokin.dto.UserSignUpDto;
import ru.kpfu.itis.sorokin.entity.Operator;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.OperatorService;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;
import ru.kpfu.itis.sorokin.util.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OperatorServiceImpl  implements OperatorService {
    UserDao userDao;
    OperatorDao operatorDao;

    public OperatorServiceImpl(UserDao userDao, OperatorDao operatorDao) {
        this.userDao = userDao;
        this.operatorDao = operatorDao;
    }

    @Override
    public void signUp(UserSignUpDto userSignUpDto, OperatorSignUpDto operatorSignUpDto) throws ValidationException {

        validateSignUpOperator(userSignUpDto, operatorSignUpDto);

        User user = new User();

        String paswordHash = PasswordUtil.encrypt(userSignUpDto.password());

        user.setEmail(userSignUpDto.email());
        user.setName(null);
        user.setPasswordHash(paswordHash);
        user.setRole(Role.OPERATOR);

        Operator operator = new Operator();
        operator.setCompanyName(operatorSignUpDto.companyName());
        operator.setStatus(Status.PENDING);

        Connection connection = null;
        try {
            connection = DataBaseConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            User userCreated = userDao.save(user, connection);
            Integer userId = userCreated.getId();

            operator.setUserId(userId);
            operatorDao.save(operator, connection);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }

            throw new ServiceException("Failed sign up Operator", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public OperatorViewDto findById(Integer userId) {
        Operator operator = operatorDao.findByUserId(userId)
                .orElseThrow(() -> new ServiceException("Operator with userId " + userId + " not found"));

        return new OperatorViewDto(
                userId,
                operator.getCompanyName(),
                operator.getDescription()
        );
    }

    private void validateSignUpOperator(UserSignUpDto userSignUpDto, OperatorSignUpDto operatorSignUpDto) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (!userSignUpDto.email().matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$")) {
            errors.put("email", "Несоответствующий формат почты");
        } else if (userDao.findByEmail(userSignUpDto.email()).isPresent()) {
            errors.put("email", "Пользователь с таким email уже есть, введите другой");
        }

        if (operatorSignUpDto.companyName().length() > 63) {
            errors.put("operator_name", "Слишком длинное имя компании");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
