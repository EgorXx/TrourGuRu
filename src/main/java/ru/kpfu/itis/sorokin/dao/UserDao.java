package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.User;

import java.sql.Connection;
import java.util.Optional;

public interface UserDao {
    User save(User user);

    User save(User user, Connection connection);

    Optional<User> findByEmail(String email);

    void updateProfile(Integer userId, String userName, String email);

    void updateProfile(Integer userId, String userName, String email, Connection connection);

    void changePassword(Integer userId, String passwordHash);

    User findById(Integer id);
}
