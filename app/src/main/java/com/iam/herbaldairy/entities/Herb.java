package com.iam.herbaldairy.entities;

import android.content.Context;
import android.content.SharedPreferences;

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
    private int                     volume;
    private TreeMap<Date, int[]>    weightStamps;
    private Type type;

    private String                  imageUrl;
    private String                  description;

    public Herb(String name) {
        this.name = name;
        this.weight = 0;
        this.volume = 0;
        weightStamps = new TreeMap<>();
        addTimeStamp(weight, volume);
    }

    public Herb(JSONObject jHerba) {
        weightStamps = new TreeMap<>();
        try {
            name  = jHerba.getString(JSONKey.Name.key);
            weight  = jHerba.getInt(JSONKey.Weight.key);
            volume  = jHerba.getInt(JSONKey.Volume.key);
            type  = Type.valueOf(jHerba.getString(JSONKey.Type.key));
            latinName  = jHerba.getString(JSONKey.LatinName.key);
            description  = jHerba.getString(JSONKey.Description.key);
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

                weightStamps.put(date, new int[]{jEntry.getInt(JSONKey.Weight.key), jEntry.getInt(JSONKey.Volume.key)});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Herb(Herb herb, Type type) {
        this.name = herb.name();
        this.weight = 0;
        this.volume = 0;
        this.latinName = herb.latinName;
        this.imageUrl = herb.imageURL();
        this.description = herb.description();
        this.type = type;
        weightStamps = new TreeMap<>();
        addTimeStamp(weight, volume);
    }


    public void add(int weight, int volume) {
        this.weight += weight;
        this.volume += volume;
        addTimeStamp(weight, volume);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void addTimeStamp(int weight, int volume) {
        weightStamps.put(new Date(System.currentTimeMillis()), new int[]{weight,volume});
    }

    public boolean isActive() {
        return weight > 0;
    }

    public JSONObject getJSON() {
        JSONObject jHerba = new JSONObject();
        try {
            jHerba.put(JSONKey.Name.key, name);
            jHerba.put(JSONKey.Weight.key, weight);
            jHerba.put(JSONKey.Volume.key, volume);
            jHerba.put(JSONKey.LatinName.key, latinName == null ? "" : latinName);
            jHerba.put(JSONKey.Type.key, type.name());
            jHerba.put(JSONKey.ImageURL.key, imageUrl == null ? "" : imageUrl);
            jHerba.put(JSONKey.Description.key, description == null ? "" : description);
            JSONArray ts = new JSONArray();
            for (Map.Entry<Date, int[]> entry : weightStamps.entrySet()) {
                JSONObject jEntry = new JSONObject();
                jEntry.put(JSONKey.Weight.key, entry.getValue()[0]);
                jEntry.put(JSONKey.Volume.key, entry.getValue()[1]);
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageUrl = imageURL;
    }

    public static void addToOwnHerbs(Herb herb) {
        if (ownHerbs.contains(herb)) {
            ownHerbs.get(ownHerbs.indexOf(herb)).add(herb.weight, herb.volume);
        } else {
            ownHerbs.add(herb);
        }
    }

    public static ArrayList<Herb> list() {
        return ownHerbs;
    }

    public String name() {
        return name;
    }

    public String latin() {
        return latinName;
    }

    public int weight() {
        return weight;
    }

    public double volume() {
        return volume;
    }

    public String volumeString() {
        return String.format("%.2f", (double)volume / 1000);
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

    private String description() {
        return description;
    }

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

    private enum JSONKey {

        Name        ("name"),
        LatinName   ("latin_name"),
        Weight      ("weight"),
        Volume      ("volume"),
        WeightStamps("weight_stamp"),
        Type        ("type"),
        ImageURL    ("image_url"),
        Date        ("date"),
        Description ("description");

        private final String key;

        JSONKey(String key) {
            this.key = key;
        }
    }
}
