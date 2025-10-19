package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.entity.Operator;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;

public class OperatorDaoImpl implements OperatorDao {
    private static final String SQL_SAVE = "INSERT INTO operator (user_id, company_name, description, status) VALUES (?, ?, ?, ?::general_status)";

    @Override
    public Operator save(Operator operator) {
        try (Connection connection = DataBaseConnectionUtil.getConnection()) {
            return save(operator, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Failed save operator");
        }
    }

    @Override
    public Operator save(Operator operator, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE)) {

            preparedStatement.setInt(1, operator.getUserId());
            preparedStatement.setString(2, operator.getCompanyName());
            preparedStatement.setString(3, operator.getDescription());
            preparedStatement.setString(4, operator.getStatus().name());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save operator, no insert rows");
            }

            return operator;
        } catch (SQLException e) {
            throw new DataAccessException("Failed save operator", e);
        }
    }
}
