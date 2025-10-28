package ru.kpfu.itis.sorokin.dao.impl;

import ru.kpfu.itis.sorokin.dao.UserDao;
import ru.kpfu.itis.sorokin.entity.Role;
import ru.kpfu.itis.sorokin.entity.User;
import ru.kpfu.itis.sorokin.exception.DataAccessException;
import ru.kpfu.itis.sorokin.exception.ServiceException;
import ru.kpfu.itis.sorokin.util.DataBaseConnectionUtil;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_SAVE = "INSERT INTO users (email, name, password_hash, role) VALUES (?, ?, ?, ?::user_role)";
    private static final String SQL_SELECTED_BY_EMAIL = "SELECT id, email, name, password_hash, role FROM users WHERE email = ?";
    private static final String SQL_UPDATE_PROFILE = """
            UPDATE users
            SET name = ?, email = ?
            WHERE id = ?
            """;

    private static final String SQL_CHANGE_PASSWORD = """
            UPDATE users
            SET password_hash = ?
            WHERE id = ?
            """;

    @Override
    public User save(User user) {
        try (Connection connection = DataBaseConnectionUtil.getConnection()) {
            return save(user, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Failed save user", e);
        }
    }

    @Override
    public User save(User user, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS)) {

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
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECTED_BY_EMAIL)) {

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
    public void updateProfile(Integer userId, String userName, String email) {
        try (Connection connection = DataBaseConnectionUtil.getConnection()) {
            updateProfile(userId, userName, email, connection);
        } catch (SQLException e) {
            throw new DataAccessException("Failed update profile user", e);
        }
    }

    @Override
    public void updateProfile(Integer userId, String userName, String email, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PROFILE)) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, email);
            preparedStatement.setInt(3, userId);

            int updateRow = preparedStatement.executeUpdate();

            if (updateRow == 0) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed update profile user", e);
        }
    }

    @Override
    public void changePassword(Integer userId, String passwordHash) {
        try (Connection connection = DataBaseConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHANGE_PASSWORD)) {

            preparedStatement.setString(1, passwordHash);
            preparedStatement.setInt(2, userId);

            int updateRow = preparedStatement.executeUpdate();

            if (updateRow == 0) {
                throw new SQLException();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed change password user", e);
        }
    }


    @Override
    public User findById(Integer id) {
        return null;
    }
}
