package com.iam.herbaldairy.entities.many_to_many;

import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.entities.Type;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "herb_type")
public class HerbType {

    private static final String FN_VOLUME_FACTOR = "volume_factor";
    private static final String FN_DRY_FACTOR = "dry_factor";
    private static final String FN_DRY_TIME = "dry_time";
    private static final String FN_HERB = "herb_id";
    private static final String FN_TYPE = "type_id";

    @DatabaseField(dataType = DataType.DOUBLE, columnName = FN_VOLUME_FACTOR)
    private double volumeFactor;
    @DatabaseField(dataType = DataType.DOUBLE, columnName = FN_DRY_FACTOR)
    private double dryFactor;
    @DatabaseField(dataType = DataType.DOUBLE, columnName = FN_DRY_TIME)
    private double dryTime;
    @DatabaseField(foreign = true, uniqueCombo = true, columnName = FN_TYPE)
    private Type type;
    @DatabaseField(foreign = true, uniqueCombo = true, columnName = FN_HERB)
    private Herb herb;

    public HerbType() {
        this.herb = new Herb();
        this.type = new Type();
        this.volumeFactor = 0;
        this.dryFactor = 0;
        this.dryTime = 0;
    }

    public HerbType(Herb herb, Type type) {
        this(herb);
        this.type = type;
    }

    public HerbType(HerbType herbTyped) {
        this(herbTyped.herb());
        this.type = herbTyped.type();
    }

    public HerbType(Herb herb) {
        this.herb = herb;
        volumeFactor = 0;
        dryFactor = 0;
        dryTime = 0;
    }

    public HerbType(String herb) {
        this(new Herb(herb));
    }

    private boolean isSimilarTo(HerbType herb) {
        return
                volumeFactor == herb.volumeFactor() &&
                        dryFactor == herb.dryFactor() &&
                        dryTime == herb.dryTime() &&
                        this.herb.growDescription().equals(herb.herb.growDescription()) &&
                        this.herb.gatherPeriod().equals(herb.herb.gatherPeriod()) &&
                        this.herb.description().equals(herb.herb.description()) &&
                        this.herb.imageURL().equals(herb.herb.imageURL());
    }

    /**
     * GETTERS
     */
    public double dryTime() {
        return dryTime;
    }
    public double dryFactor() {
        return dryFactor;
    }
    public double volumeFactor() {
        return volumeFactor;
    }
    public Type type() {
        return type;
    }
    public String typeString() {
        return type.name();
    }
    public Herb herb() {
        return herb;
    }


    /**
     * SETTERS
     */
    public void setType(Type type) {
        this.type = type;
    }
    public void setDryFactor(double dryFactor) {
        this.dryFactor = dryFactor;
    }
    public void setDryTime(double dryTime) {
        this.dryTime = dryTime;
    }
    public void setVolumeFactor(double volumeFactor) {
        this.volumeFactor = volumeFactor;
    }

    @Override
    public int hashCode() {
        return (herb.name() + type.name()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }


}
