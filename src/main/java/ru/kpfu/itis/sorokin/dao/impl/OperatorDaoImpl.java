package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.OperatorDao;
import ru.kpfu.itis.sorokin.dto.OperatorInfoDto;
import ru.kpfu.itis.sorokin.entity.Operator;
import ru.kpfu.itis.sorokin.entity.Status;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OperatorDaoImpl implements OperatorDao {
    private static final String SQL_SAVE = "INSERT INTO operator (user_id, company_name, description, status) VALUES (?, ?, ?, ?::general_status)";

    private static final String SQL_FIND_BY_USER_ID = "SELECT user_id, company_name, description, status FROM operator WHERE user_id = ?";

    private static final String SQL_UPDATE_INFO = """
            UPDATE operator
            SET company_name = ?, description = ?
            WHERE user_id = ?
            """;

    private static final String SQL_FIND_ALL_PENDING = """
            SELECT user_id, email, company_name, status
            FROM operator INNER JOIN users ON operator.user_id = users.id
            WHERE status = 'PENDING'
            """;

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

    @Override
    public Optional<Operator> findByUserId(Integer userId) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_USER_ID)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new Operator(
                                    userId,
                                    resultSet.getString("company_name"),
                                    resultSet.getString("description"),
                                    Status.valueOf(resultSet.getString("status"))
                            )
                    );
                }
            } catch (SQLException e) {
                throw new DataAccessException("Failed select operator by user_id");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select operator by user_id");
        }

        return Optional.empty();
    }

    @Override
    public void updateOperatorInfo(Integer userId, String companyName, String description) {
        try (Connection connection = DataBaseConnectionUtil.getConnection()) {

            updateOperatorInfo(userId, companyName, description, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Failed update profile operator", e);
        }
    }

    @Override
    public void updateOperatorInfo(Integer userId, String companyName, String description, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_INFO)) {

            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, userId);

            int updateRow = preparedStatement.executeUpdate();

            if (updateRow == 0) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed update profile operator", e);
        }
    }

    @Override
    public List<OperatorInfoDto> findAllPendingStatus() {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_PENDING)) {

            List<OperatorInfoDto> operators = new ArrayList<>();

            while (resultSet.next()) {
                operators.add(new OperatorInfoDto(
                        resultSet.getInt("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("company_name"),
                        Status.valueOf(resultSet.getString("status"))
                        ));
            }

            return operators;

        } catch (SQLException e) {
            throw new DataAccessException("Failed select operators", e);
        }
    }
}
