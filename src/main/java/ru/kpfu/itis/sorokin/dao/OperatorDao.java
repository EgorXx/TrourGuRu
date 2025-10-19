package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.entity.Operator;

import java.sql.Connection;

public interface OperatorDao {
    Operator save(Operator operator);

    Operator save(Operator operator, Connection connection);
}
