package com.iam.herbaldairy.entities;

import com.iam.herbaldairy.arch.db.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@DatabaseTable(tableName = "user")
public class User {

    private static int ID_COUNTER = 1;

    private String SSS = "g5df65g7df5gdhdgbfssjdkgf69";

    public static boolean authorized        = true;
    public static String current_login      = "iam";
    public static String current_password   = "GbpljcYf";
    public static String current_email      = "alesetar@gmail.com";
    public static int current_id            = 1;
    public static User user                 = new User(current_login, current_password, current_email);

    private final String FIELD_NAME_ID          = "id";
    private final String FIELD_NAME_LOGIN       = "login";
    private final String FIELD_NAME_PASSWORD    = "password";
    private final String FIELD_NAME_EMAIL       = "email";

    @DatabaseField(dataType = DataType.STRING, columnName = FIELD_NAME_LOGIN)
    public String login;

    @DatabaseField(generatedId = true)//, unique = true, columnName = FIELD_NAME_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, columnName = FIELD_NAME_PASSWORD)
    public String password;

    @DatabaseField(dataType = DataType.STRING, columnName = FIELD_NAME_EMAIL)
    public String email;



    public User() {
        id = 1;
        login = "";
        password = "";
        email = "";
    }

    public User(String login, String password, String email) {
        this.id = ID_COUNTER++;
        this.login = login;
        this.email = email;
        this.password = toSHA256(password);
    }

    public String toSHA256(String forHash) {
        String hashtext = null;
        try {
            byte[] bytesOfMessage = (forHash + SSS).getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(bytesOfMessage);
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            hashtext = bigInt.toString(16);
// Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }

    public void writeUserDataToDB() {
        try {
            ConnectionSource connectionSource = DBHelper.defaultConnection();

            Dao<User, String> dao = DaoManager.createDao(connectionSource, User.class);

            if (!dao.queryForAll().contains(this)) dao.create(this);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return (login + email).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
