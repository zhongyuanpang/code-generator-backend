package com.pzy.codegenerator.common.utils.dao;

import com.pzy.codegenerator.common.utils.Result;

import java.sql.Connection;

@FunctionalInterface
public interface SqlHandler {
    public abstract Result method(Connection connection, DaoAccessParam daoAccessParam) throws Exception;
}
