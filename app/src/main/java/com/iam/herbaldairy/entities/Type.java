package com.iam.herbaldairy.entities;


import com.iam.herbaldairy.arch.db.DBCache;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "type")
public class Type {

    private final String ID_FIELD_NAME = "id";
    private final String DESCRIPTION_FIELD_NAME = "name";

    public Type() {
        id = 0;
        description = "none";
    }

    public Type(String description, int id) {
        this.description = description;
        this.id = id;
    }

    @DatabaseField(dataType = DataType.STRING, columnName = DESCRIPTION_FIELD_NAME)
    public String description;

    @DatabaseField(canBeNull = false, unique = true, uniqueCombo =  true, id = true, columnName = ID_FIELD_NAME)
    public int id;

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    public static Type valueOf(String stringType) {
        for (Type tc : DBCache.getInstance().types()) {
            if (tc.description.equals(stringType)) {
                return tc;
            }
        }
        throw new RuntimeException("Unknown type " + stringType);
    }

    public String name() {
        return description;
    }
}
