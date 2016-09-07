package com.iam.herbaldairy.arch.db;

import com.iam.herbaldairy.entities.Herb;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DBHelper {


    private static final String db_url = "jdbc:mysql://89.108.107.53:3306/iam_herbdiary_ru";
    private static final String db_user = "iam";
    private static final String db_password = "GbpljcYf";

    public static ConnectionSource defaultConnection() {
        try {
            return new JdbcConnectionSource(db_url, db_user, db_password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable connect to database");
    }

}
