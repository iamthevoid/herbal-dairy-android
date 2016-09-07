package com.iam.herbaldairy.entities;

import com.iam.herbaldairy.entities.interfaces.JSONSerializable;
import com.iam.herbaldairy.entities.many_to_many.HerbType;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HerbOwned implements JSONSerializable {

    private static final String FN_ID_TYPED_HERB = "herb_id";
    private static final String FN_WEIGHT = "weight";

    @DatabaseField(dataType = DataType.INTEGER, columnName = FN_WEIGHT)
    protected int weight;
    @DatabaseField(foreign = true, dataType = DataType.INTEGER, columnName = FN_ID_TYPED_HERB)
    protected HerbType herbTyped;

    public HerbOwned(String herb) {
        weight = 0;
        herbTyped = new HerbType(herb);
    }

    public HerbOwned(HerbOwned herb, Type type) {
        weight = herb.weight();
        herbTyped = new HerbType(new Herb(herb.typedHerb().herb()), type);
    }

    public void add(int weight) {
        this.weight += weight;
    }

    public HerbOwned() {
        this.herbTyped = new HerbType();
        weight = 0;
    }

    public HerbOwned(JSONObject jHerba) {
        try {
            Herb herb = new Herb(jHerba.getString(JSONKey.Name.key));
            herb.setDescription(jHerba.getString(JSONKey.Description.key));
            herb.setGrowDescription(jHerba.getString(JSONKey.GrowDescription.key));
            herb.setGatherPeriod(jHerba.getString(JSONKey.GatherPeriod.key));
            herb.setImageURL(jHerba.getString(JSONKey.ImageURL.key).replace(Herb.WIKI_IMAGE_URL_PREFIX, ""));
            herb.setLatinName(jHerba.getString(JSONKey.LatinName.key));
            HerbType herbTyped = new HerbType(herb, Type.valueOf(jHerba.getString(JSONKey.Type.key)));
            herbTyped.setVolumeFactor(jHerba.getDouble(JSONKey.VolumeFactor.key));
            herbTyped.setDryFactor(jHerba.getDouble(JSONKey.DryFactor.key));
            herbTyped.setDryTime(jHerba.getDouble(JSONKey.DryTime.key));
            this.herbTyped = herbTyped;
            if (jHerba.has(JSONKey.Weight.key)) weight = jHerba.getInt(JSONKey.Weight.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HerbOwned(HerbOwned herbOwned) {
        this.herbTyped = new HerbType(herbOwned.typedHerb());
        this.weight = 0;
    }

    public static String volumesStringForHerbs(ArrayList<HerbOwned> herbs) {
        final int size = herbs.size();
        if (size > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("( ");
            if (size == 1) {
                final HerbOwned userHerb = herbs.get(0);
                sb.append(userHerb.typedHerb().type().name().subSequence(0,2));
                sb.append(". ");
                sb.append(userHerb.weight).append("g");
            } else {
                for (int i = 0; i < size; i++) {
                    final HerbOwned userHerb = herbs.get(i);
                    sb.append(userHerb.typedHerb().type().name().subSequence(0,2));
                    sb.append(". ");
                    sb.append(userHerb.weight()).append("g");
                    if (i != size - 1) sb.append(", ");
                }
            }
            sb.append(" )");
            return sb.toString();
        } else {
            return "";
        }
    }

    // GETTERS
    public String volumeString() {
        return String.format("%.2f", (double) weight * herbTyped.volumeFactor() / 1000);
    }
    public int weight() {
        return weight;
    }
    public boolean isActive() {
        return weight > 0;
    }

    // SETTERS
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public HerbType typedHerb() {
        return herbTyped;
    }

    @Override
    public JSONObject createJSON() {
        JSONObject jHerba = new JSONObject();
        Herb herb = this.herbTyped.herb();
        try {
            jHerba.put(JSONKey.Name.key, herb.name());
            jHerba.put(JSONKey.GrowDescription.key, herb.growDescription() == null ? "" : herb.growDescription());
            jHerba.put(JSONKey.GatherPeriod.key, herb.gatherPeriod() == null ? "" : herb.gatherPeriod());
            jHerba.put(JSONKey.Description.key, herb.description() == null ? "" : herb.description());
            jHerba.put(JSONKey.Weight.key, weight);
            jHerba.put(JSONKey.DryFactor.key, herbTyped.dryFactor());
            jHerba.put(JSONKey.DryTime.key, herbTyped.dryTime());
            jHerba.put(JSONKey.VolumeFactor.key, herbTyped.volumeFactor());
            jHerba.put(JSONKey.LatinName.key, herb.latinName() == null ? "" : herb.latinName());
            jHerba.put(JSONKey.Type.key, herbTyped.type().name());
            //TODO TEMPEST. Replace method must be removed
            jHerba.put(JSONKey.ImageURL.key, herb.imageURL() == null ? "" : herb.imageURL().replace(Herb.WIKI_IMAGE_URL_PREFIX, ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jHerba;
    }

    public String name() {
        return typedHerb().herb().name();
    }

    public String latinName() {
        return typedHerb().herb().latinName();
    }

    public void setHerbTyped(HerbType herbTyped) {
        this.herbTyped = herbTyped;
    }

    /**
     * ENUM defines JSON Keys for JSON serialization
     */
    public enum JSONKey {

        Name                ("name"),
        LatinName           ("latin_name"),
        Weight              ("weight"),
        VolumeFactor        ("volume_factor"),
        WeightStamps        ("weight_stamp"),
        Type                ("type"),
        ImageURL            ("image_url"),
        Date                ("date"),
        DryTime             ("dry_time"),
        DryFactor           ("dry_factor"),
        GrowDescription("cultivate_description"),
        GatherPeriod        ("gather_period"),
        UserID              ("user_id"),
        HerbID              ("herb_id"),
        Description         ("description");

        protected final String key;

        JSONKey(String key) {
            this.key = key;
        }
    }

    @Override
    public int hashCode() {
        return (herbTyped.herb().name() + herbTyped.type()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}
