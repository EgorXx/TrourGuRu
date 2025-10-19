package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.User;

import java.sql.Connection;
import java.util.Optional;

public interface UserDao {
    User save(User user);

    User save(User user, Connection connection);

    Optional<User> findByEmail(String email);

    User findById(Integer id);
}
