package com.iam.herbaldairy.entities.many_to_many;

import com.iam.herbaldairy.entities.HerbOwned;
import com.iam.herbaldairy.entities.User;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class HerbUser {

    private static final String FN_ID_USER = "user_id";
    private static final String FN_ID_HERB = "herb_id";


    @DatabaseField(foreign = true, dataType = DataType.INTEGER, columnName = FN_ID_USER)
    private User user;
    @DatabaseField(foreign = true, dataType = DataType.INTEGER, columnName = FN_ID_HERB)
    private HerbOwned herb;

    public HerbUser() {
        super();
        this.user = new User();
    }
}
