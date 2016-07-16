package com.iam.herbaldairy.entities;

import android.content.Context;
import android.content.SharedPreferences;

import com.iam.herbaldairy.Calculator;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Absinth implements JSONSerializable {

    private static String ABSINTH_PREFS = "absinth_prefs";
    private static ArrayList<Absinth> absinthes = new ArrayList<>();

    private double spiritVolume;
    private ArrayList<Herb> herbs = new ArrayList<>();

    private long resultVolume;
    private int alcPercent;

    private String comment;
    private Date startInfuseDate;
    private Date distillDate;

    public long getInfuseInterval(TimeUnit timeUnit) {
        Date date = distillDate == null ? new Date(System.currentTimeMillis()) : distillDate;
        long diffInMillies = date.getTime() - startInfuseDate.getTime();
        return timeUnit.convert(diffInMillies,timeUnit);
    }

    public double dilutedSpiritVolume(int percent) {
        return Calculator.volumeOfDilutedSpirit(spiritVolume, 96, percent);
    }

    public static void writeToPreferences(Context context) {
        JSONArray jAbsinthes = new JSONArray();
        for (Absinth absinth : absinthes) {
            jAbsinthes.put(absinth.getJSON());
        }

        SharedPreferences preferences = context.getSharedPreferences(ABSINTH_PREFS, Context.MODE_PRIVATE);
        preferences
                .edit()
                .putString(context.getString(R.string.own_absinthes), jAbsinthes.toString())
                .apply();
    }

    public static void readFromPreferences(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ABSINTH_PREFS, Context.MODE_PRIVATE);
        String sAbsinthes = preferences.getString(context.getString(R.string.own_absinthes), "");
        if (!sAbsinthes.equals("")) {
            JSONArray userAbsinthes;
            try {
                userAbsinthes = new JSONArray(sAbsinthes);
                for (int i = 0, l = userAbsinthes.length(); i < l; i++) {
                    final Absinth newAbsinth = new Absinth(userAbsinthes.getJSONObject(i));
                    if (!absinthes.contains(newAbsinth)) absinthes.add(newAbsinth);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public JSONObject getJSON() {
        JSONObject result = new JSONObject();

        try {
            result.put(JSONKey.SpiritVolume.key, spiritVolume);
            JSONArray herbs = new JSONArray();
            for (Herb herb : this.herbs) {
                herbs.put(herb.getJSON());
            }
            result.put(JSONKey.Herb.key, herbs);
            result.put(JSONKey.ResultVolume.key, resultVolume);
            result.put(JSONKey.AlcoholPercent.key, alcPercent);
            result.put(JSONKey.Comment.key, comment == null ? "" : comment);
            result.put(JSONKey.InfuseDate.key, Time.dateFormat.format(startInfuseDate));
            result.put(JSONKey.DistillDate.key, distillDate == null ? 0 : Time.dateFormat.format(distillDate));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Absinth(JSONObject jAbsinth) {
        herbs = new ArrayList<>();
        try {
            this.spiritVolume = jAbsinth.getDouble(JSONKey.SpiritVolume.key);
            JSONArray jHerbs = jAbsinth.getJSONArray(JSONKey.Herb.key);
            for (int i = 0, l = jHerbs.length(); i < l; i++) {
                herbs.add(new Herb(jHerbs.getJSONObject(i)));
            }
            this.resultVolume = jAbsinth.getLong(JSONKey.ResultVolume.key);
            this.alcPercent = jAbsinth.getInt(JSONKey.AlcoholPercent.key);
            this.comment = jAbsinth.getString(JSONKey.Comment.key);
            this.startInfuseDate = Time.dateFormat.parse(jAbsinth.getString(JSONKey.InfuseDate.key));
            final String distillDateString = jAbsinth.getString(JSONKey.DistillDate.key);
            this.distillDate = distillDateString.equals("0") ? null : Time.dateFormat.parse(distillDateString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        int summ = 0;
        for (Herb herb : herbs) {
            summ += herb.name().hashCode();
            summ += herb.weight();
        }
        return startInfuseDate.hashCode() + summ;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    private enum JSONKey {

        SpiritVolume    ("spirit_volume"),
        Herb            ("herb"),
        ResultVolume    ("result_volume"),
        AlcoholPercent  ("alc_percent"),
        Comment         ("comment"),
        InfuseDate      ("infuse_date"),
        DistillDate     ("distill_date");

        private final String key;

        JSONKey(String key) {
            this.key = key;
        }
    }
}
