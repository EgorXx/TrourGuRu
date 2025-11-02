package ru.kpfu.itis.sorokin.service.impl;

import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.dto.*;
import ru.kpfu.itis.sorokin.entity.Operator;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.exception.ValidationException;
import ru.kpfu.itis.sorokin.service.OperatorService;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;
import ru.kpfu.itis.sorokin.util.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public void updateProfile(OperatorUpdateInfoDto operatorUpdateInfoDto) throws ValidationException {
        Integer userId = operatorUpdateInfoDto.userId();
        String email = operatorUpdateInfoDto.email();
        String companyName = operatorUpdateInfoDto.companyName();
        String description = operatorUpdateInfoDto.description();

        validateUpdateProfile(userId, email, companyName, description);

        Connection connection = null;

        try {
            connection = DataBaseConnectionUtil.getConnection();

            connection.setAutoCommit(false);

            userDao.updateProfile(userId, null, email, connection);
            operatorDao.updateOperatorInfo(userId, companyName, description, connection);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }

            throw new ServiceException("Failed update operator profile", e);
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
    public List<OperatorInfoDto> getAllOperatorsWithPendingStatus() {
        try {
            return operatorDao.findAllByStatus(Status.PENDING);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed get operators with pending status", e);
        }
    }

    @Override
    public void approveOperator(Integer userId) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        Optional<Operator> operatorOptional = operatorDao.findByUserId(userId);

        if (!operatorOptional.isPresent()) {
            errors.put("operator", "Оператор не найден");
            throw new ValidationException(errors);
        }

        try {
            operatorDao.updateStatusByUserId(userId, Status.APPROVED);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed approve operator", e);
        }
    }

    @Override
    public void rejectOperator(Integer userId) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        Optional<Operator> operatorOptional = operatorDao.findByUserId(userId);

        if (!operatorOptional.isPresent()) {
            errors.put("operator", "Оператор не найден");
            throw new ValidationException(errors);
        }

        try {
            userDao.deleteById(userId);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed reject operator", e);
        }
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

    private void validateUpdateProfile(Integer userId, String email, String companyName, String description) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        if (!email.matches("^[\\w-\\.]+@[\\w-]+(\\.[\\w-]+)*\\.[a-z]{2,}$")) {
            errors.put("email", "Несоответствующий формат почты");
        } else {
            Optional<User> userOptional = userDao.findByEmail(email);
            if (userOptional.isPresent() && !userOptional.get().getId().equals(userId)) {
                errors.put("email", "Пользователь с таким email уже есть, введите другой");
            }
        }

        if (companyName == null || companyName.isEmpty()) {
            errors.put("companyName", "Название компании не может быть пустым");
        }

        if (companyName != null && companyName.length() > 63) {
            errors.put("companyName", "Слишком длинное название компании");
        }

        if (description != null && description.length() > 1023) {
            errors.put("description", "Слишком длинное описание компании");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
