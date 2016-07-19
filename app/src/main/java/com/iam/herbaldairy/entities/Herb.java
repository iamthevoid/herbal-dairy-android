package com.iam.herbaldairy.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Herb implements JSONSerializable {

    private static final String HERBA_PREFS = "herba_prefs";
    private static final ArrayList<Herb> ownHerbs = new ArrayList<>();

    private String                  name;
    private String                  latinName;
    private int                     weight;
//    private int                     volume;
    private double                  volumeFactor;
    private double                  dryFactor;
    private double                  dryTime;
    private TreeMap<Date, Integer>    weightStamps;
    private Type type;

    private String                  imageUrl;
//    private String                  description;

    public Herb(String name) {
        this.name = name;
        this.weight = 0;
//        this.volume = 0;
        this.volumeFactor = 0;
        this.dryFactor = 0;
        this.dryTime = 0;
        weightStamps = new TreeMap<>();
        addTimeStamp(weight);
    }

    public Herb(JSONObject jHerba) {
        weightStamps = new TreeMap<>();
        try {
            name  = jHerba.getString(JSONKey.Name.key);
            weight  = jHerba.getInt(JSONKey.Weight.key);
            volumeFactor = jHerba.getDouble(JSONKey.VolumeFactor.key);
            type  = Type.valueOf(jHerba.getString(JSONKey.Type.key));
            latinName  = jHerba.getString(JSONKey.LatinName.key);
            dryFactor  = jHerba.getDouble(JSONKey.DryFactor.key);
            dryTime  = jHerba.getDouble(JSONKey.DryTime.key);
            imageUrl  = jHerba.getString(JSONKey.ImageURL.key);
            JSONArray ts = jHerba.getJSONArray(JSONKey.WeightStamps.key);

            final Date now = new Date(System.currentTimeMillis());
            for (int i = 0, l = ts.length(); i < l; i++) {
                JSONObject jEntry = ts.getJSONObject(i);
                Date date = now;
                try {
                    date = Time.dateFormat.parse(jEntry.getString(JSONKey.Date.key));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                weightStamps.put(date, jEntry.getInt(JSONKey.Weight.key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Herb(Herb herb) {
        this.name = herb.name();
        this.weight = 0;
        this.volumeFactor = 0;
        this.dryFactor = 0;
        this.dryTime = 0;
        this.latinName = herb.latinName;
        this.imageUrl = herb.imageURL();
//        this.description = herb.description();
        weightStamps = new TreeMap<>();
        addTimeStamp(weight);
    }

    public Herb(Herb herb, Type type) {
        this(herb);
        this.type = type;
    }

    public void add(int weight) {
        this.weight += weight;
        addTimeStamp(this.weight);
    }

    public void add(int weight, Date date) {
        this.weight += weight;
        addTimeStamp(this.weight, date);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDryFactor(double dryFactor) {
        this.dryFactor = dryFactor;
    }

    public void setDryTime(double dryTime) {
        this.dryTime = dryTime;
    }

    public void addTimeStamp(int weight) {
        weightStamps.put(new Date(System.currentTimeMillis()), weight);
    }

    public void addTimeStamp(int weight, Date date) {
        weightStamps.put(date, weight);
    }

    public boolean isActive() {
        return weight > 0;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jHerba = new JSONObject();
        try {
            jHerba.put(JSONKey.Name.key, name);
            jHerba.put(JSONKey.Weight.key, weight);
            jHerba.put(JSONKey.DryFactor.key, dryFactor);
            jHerba.put(JSONKey.DryTime.key, dryTime);
            jHerba.put(JSONKey.VolumeFactor.key, volumeFactor);
            jHerba.put(JSONKey.LatinName.key, latinName == null ? "" : latinName);
            jHerba.put(JSONKey.Type.key, type.name());
            jHerba.put(JSONKey.ImageURL.key, imageUrl == null ? "" : imageUrl);
//            jHerba.put(JSONKey.Description.key, description == null ? "" : description);
            JSONArray ts = new JSONArray();
            for (Map.Entry<Date, Integer> entry : weightStamps.entrySet()) {
                JSONObject jEntry = new JSONObject();
                jEntry.put(JSONKey.Weight.key, entry.getValue());
                jEntry.put(JSONKey.Date.key, Time.dateFormat.format(entry.getKey()));
                ts.put(jEntry);
            }
            jHerba.put(JSONKey.WeightStamps.key, ts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jHerba;
    }

    public static void writeToPreferences(Context context) {
        JSONArray userHerbas = new JSONArray();
        for (Herb herba : ownHerbs) {
            userHerbas.put(herba.getJSON());
        }

        SharedPreferences preferences = context.getSharedPreferences(HERBA_PREFS, Context.MODE_PRIVATE);
        preferences
                .edit()
                .putString(context.getString(R.string.own_herbas), userHerbas.toString())
                .apply();
    }

    public static void readFromPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(HERBA_PREFS, Context.MODE_PRIVATE);
        String herbas = preferences.getString(context.getString(R.string.own_herbas), "");
        if (!herbas.equals("")) {
            JSONArray userHerbas;
            try {
                userHerbas = new JSONArray(herbas);
                for (int i = 0, l = userHerbas.length(); i < l; i++) {
                    final Herb newHerb = new Herb(userHerbas.getJSONObject(i));
                    if (!ownHerbs.contains(newHerb)) ownHerbs.add(newHerb);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    public void setDescription(String description) {
//        this.description = description;
//    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }

    public static void addToOwnHerbs(Herb herb) {
        if (ownHerbs.contains(herb)) {
            ownHerbs.get(ownHerbs.indexOf(herb)).add(herb.weight);
        } else {
            ownHerbs.add(herb);
        }
    }

    public static void removeFromOwnHerbs(Herb herb, Date date) {
        if (ownHerbs.contains(herb)) {
            ownHerbs.get(ownHerbs.indexOf(herb)).add(-herb.weight(), date);
        }
    }

    public static ArrayList<Herb> list() {
        return ownHerbs;
    }

    public String name() {
        return name;
    }

    public double dryTime() {
        return dryTime;
    }

    public double dryFactor() {
        return dryFactor;
    }

    public String latin() {
        return latinName;
    }

    public int weight() {
        return weight;
    }

    public double volumeFactor() {
        return volumeFactor;
    }

    public String volumeString() {
        return String.format("%.2f", (double) weight * volumeFactor);
    }

    public String imageURL() {
        return imageUrl;
    }

    public Type type() {
        return type;
    }

    public String typeString() {
        return type.name();
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

//    public String description() {
//        return description;
//    }

    @Override
    public int hashCode() {
        return name.hashCode() + type.name().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }

    public static boolean containsHerbName(String herbName) {
        for (Herb herb : ownHerbs) {
            if (herb.name().equals(herbName)) {
                return true;
            }
        }
        return false;
    }

    public static Herb herbByName(String herbName) {
        for (Herb herb : ownHerbs) {
            if (herb.name().equals(herbName)) {
                return herb;
            }
        }
        return null;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        addTimeStamp(weight);
    }

    public void setVolumeFactor(double volumeFactor) {
        this.volumeFactor = volumeFactor;
    }

    private enum JSONKey {

        Name        ("name"),
        LatinName   ("latin_name"),
        Weight      ("weight"),
        VolumeFactor("volume_factor"),
        WeightStamps("weight_stamp"),
        Type        ("type"),
        ImageURL    ("image_url"),
        Date        ("date"),
        DryTime     ("dry_time"),
        DryFactor   ("dry_factor"),
        Description ("description");

        private final String key;

        JSONKey(String key) {
            this.key = key;
        }
    }
}
