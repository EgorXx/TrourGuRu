package ru.kpfu.itis.sorokin.dao;

import ru.kpfu.itis.sorokin.dto.OperatorInfoDto;
import ru.kpfu.itis.sorokin.entity.Operator;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface OperatorDao {
    Operator save(Operator operator);

    Operator save(Operator operator, Connection connection);

    Optional<Operator> findByUserId(Integer userId);

    void updateOperatorInfo(Integer userId, String companyName, String description);

    void updateOperatorInfo(Integer userId, String companyName, String description, Connection connection);

    List<OperatorInfoDto> findAllPendingStatus();
}
