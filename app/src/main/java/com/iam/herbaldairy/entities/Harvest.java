package com.iam.herbaldairy.entities;

import com.iam.herbaldairy.util.Time;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.ParseException;
import java.util.Date;

@DatabaseTable(tableName = "harvest")
public class Harvest {


    private static final String FN_ID_USER = "user_id";
    private static final String FN_ID_HERB = "herb_id";
    private static final String FN_WEIGHT = "weight";
    private static final String FN_DATE = "date";

    @DatabaseField(dataType = DataType.INTEGER, columnName = FN_ID_USER)
    public int userId;
    @DatabaseField(dataType = DataType.INTEGER, columnName = FN_ID_HERB)
    public int herbId;
    @DatabaseField(dataType = DataType.INTEGER, columnName = FN_WEIGHT)
    public int weight;
    @DatabaseField(dataType = DataType.STRING, columnName = FN_DATE)
    public String date;

    public Harvest() {
        userId = 0;
        herbId = 0;
        weight = 0;
        date = "";
    }

    public Harvest(int userId, int herbId, int weight, String date) {
        this.userId = userId;
        this.herbId = herbId;
        this.weight = weight;
        this.date = date;
    }

    public int weight() {
        return weight;
    }
    public int userId() {
        return userId;
    }
    public int herbId() {
        return herbId;
    }
    public String sDate() {
        return date;
    }
    public Date date() {
        try {
            return Time.dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("date formats in harvest date creation and date() are different");
    }
}
