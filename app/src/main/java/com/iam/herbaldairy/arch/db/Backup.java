package com.iam.herbaldairy.arch.db;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

@DatabaseTable(tableName = "backup")
public class Backup {

    @DatabaseField(canBeNull = false, unique = true, columnName = "id")
    public int id;

    @DatabaseField(dataType = DataType.STRING, id = true, columnName = "herbs")
    private String herbs;

    @DatabaseField(dataType = DataType.STRING, columnName = "absinthes")
    private String absinthes;

    public Backup() {
        id = 0;
        herbs = "";
        absinthes = "";
    }

    public Backup(String herbs, String absinthes) {
        id = 0;
        this.herbs = herbs;
        this.absinthes = absinthes;
    }

    public void postToDB () {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Dao<Backup, String> dao = DaoFactory.createDao(DBHelper.defaultConnection(), Backup.class);
                    if (dao.queryForAll().size() > 0) {
                        dao.delete(dao.queryForAll().get(0));
                        dao.create(Backup.this);
                    } else {
                        dao.create(Backup.this);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public String herbs() {
        return herbs;
    }

    public String absinthes() {
        return absinthes;
    }
}
