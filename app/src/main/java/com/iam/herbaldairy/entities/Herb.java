package com.iam.herbaldairy.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "herb")
public class Herb {

    protected final static String WIKI_IMAGE_URL_PREFIX = "https://upload.wikimedia.org/wikipedia/commons/thumb/";

    private static final String FN_ID = "id";
    private static final String FN_NAME = "name";
    private static final String FN_LATIN_NAME = "latin_name";
    private static final String FN_GROW_DESCRIPTION = "grow_description";
    private static final String FN_GATHER_PERIOD = "gather_period";
    private static final String FN_IMAGE_URL = "image_url";
    private static final String FN_DESCRIPTION = "description";

    @DatabaseField(canBeNull = false, unique = true, id = true, uniqueCombo = true, columnName = FN_ID)
    public int id;

    @DatabaseField(dataType = DataType.STRING, id = true, columnName = FN_NAME)
    private String name;

    @DatabaseField(dataType = DataType.STRING, columnName = FN_LATIN_NAME)
    private String latinName;

    @DatabaseField(dataType = DataType.STRING, columnName = FN_GROW_DESCRIPTION)
    private String growDescription;

    @DatabaseField(dataType = DataType.STRING, columnName = FN_GATHER_PERIOD)
    private String gatherPeriod;

    @DatabaseField(dataType = DataType.STRING, columnName = FN_DESCRIPTION)
    private String description;

    @DatabaseField(dataType = DataType.STRING, columnName = FN_IMAGE_URL)
    private String imageUrl;

    public Herb() {
        name = "";
        latinName = "";
        growDescription = "";
        gatherPeriod = "";
        description = "";
        imageUrl = "";
    }

    public Herb(String name) {
        this.name = name;
    }

    public Herb(Herb herb) {
        this.name = herb.name();
        this.latinName = herb.latinName();
        this.imageUrl = herb.imageURL();
        this.growDescription = herb.growDescription();
        this.gatherPeriod = herb.gatherPeriod();
        this.description = herb.description();
    }


    /**
     * SETTERS
     */
    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL.replace(WIKI_IMAGE_URL_PREFIX, "");
    }
    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }
    public void setGrowDescription(String growDescription) {
        this.growDescription = growDescription;
    }
    public void setGatherPeriod(String gatherPeriod) {
        this.gatherPeriod = gatherPeriod;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * GETTERS
     */
    public String name() {
        return name;
    }
    public String growDescription() {
        return growDescription;
    }
    public String gatherPeriod() {
        return gatherPeriod;
    }
    public String description() {
        return description;
    }
    public String latinName() {
        return latinName;
    }
    public String imageURL() {
        return WIKI_IMAGE_URL_PREFIX + imageUrl;
    }
    public int id() {
        return id;
    }
}
