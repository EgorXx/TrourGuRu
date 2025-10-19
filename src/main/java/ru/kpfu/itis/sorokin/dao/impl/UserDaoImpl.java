package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    String sql_save = "INSERT INTO users (email, name, password_hash, role) VALUES (?, ?, ?, ?::user_role)";
    String sql_select_by_email = "SELECT id, email, name, password_hash, role FROM users WHERE email = ?";

    @Override
    public User save(User user) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql_save, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPasswordHash());
            preparedStatement.setString(4, user.getRole().name());

            int insertRows = preparedStatement.executeUpdate();

            if (insertRows == 0) {
                throw new DataAccessException("Failed save user, no insert row");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt("id"));
                    return user;
                } else {
                    throw new DataAccessException("Failed extract user id");
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed save user", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql_select_by_email)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new User(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("name"),
                            resultSet.getString("password_hash"),
                            Role.valueOf(resultSet.getString("role"))
                        )
                    );
                }
            } catch (SQLException e) {
                throw new DataAccessException("Failed select user by email", e);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed select user by email", e);
        }

        return Optional.empty();
    }


    @Override
    public User findById(Integer id) {
        return null;
    }
}
