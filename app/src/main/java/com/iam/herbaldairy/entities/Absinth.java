package com.iam.herbaldairy.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.iam.herbaldairy.Calculator;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.Time;
import com.iam.herbaldairy.widget.text.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Absinth implements JSONSerializable {

    public static String ABSINTH_PREFS = "absinth_prefs";
    public static ArrayList<Absinth> absinthes = new ArrayList<>();

    private double spiritVolume;
    private boolean done = false;
    private ArrayList<Herb> herbs = new ArrayList<>();

    private double resultVolume;
    private int alcPercent;

    private String comment;
    private Date startInfuseDate;
    private Date distillDate;

    private int distillTemperature;

    public Absinth(Date startInfuseDate, double spiritVolume, ArrayList<Herb> herbs) {
        this.startInfuseDate = startInfuseDate;
        this.spiritVolume = spiritVolume;
        this.herbs = herbs;
        for (Herb herb : herbs) {
            if (Herb.containsHerbName(herb.name())) {
                Herb.herbByHerb(herb).add(-herb.weight(), startInfuseDate);
            }
        }
    }

    public long getInfuseInterval() {
        Log.d("iinterval", Time.interval(startInfuseDate, distillDate, TimeUnit.DAYS) + "");
        return Time.interval(startInfuseDate, distillDate, TimeUnit.DAYS);
    }

    public double dilutedSpiritVolume(int percent) {
        return Calculator.volumeOfDilutedSpirit(spiritVolume, 96, percent);
    }

    public static ArrayList<Absinth> absinthes() {
        return absinthes;
    }

    public void setResultVolume(double resultVolume) {
        this.resultVolume = resultVolume;
    }

    public void setAlcPercent(int alcPercent) {
        this.alcPercent = alcPercent;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDistillDate(Date distillDate) {
        this.distillDate = distillDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDistillTemperature(int distillTemperature) {
        this.distillTemperature = distillTemperature;
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
        if (absinthes == null) absinthes = new ArrayList<>();
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

    public Date startInfuseDate() {
        return startInfuseDate;
    }

    public double spiritVolume() {
        return spiritVolume;
    }

    public double resultVolume() {
        return resultVolume;
    }

    public static void add(Absinth absinth) {
        System.out.println("absinthe " + !absinthes.contains(absinth));
        if (!absinthes.contains(absinth)) { absinthes.add(absinth); }
    }

    public static void remove(Absinth absinth, Context context) {
        ArrayList<Herb> herbs = absinth.herbs;
        for (Herb herb : herbs) {
            if (Herb.list().contains(herb)) {
                Herb.list().get(Herb.list().indexOf(herb)).add(herb.weight());
            }
        }

        absinthes.remove(absinth);
        Absinth.writeToPreferences(context);
    }

    public ArrayList<Herb> herbs() {
        return herbs;
    }

    public void validateHerbs(ArrayList<Herb> herbs, ArrayList<Herb> newHerbs) {
        for (Herb herb : herbs) {
            if (this.herbs.contains(herb)) {
                final Herb ownHerbAnalogOfChanging = this.herbs.get(this.herbs.indexOf(herb));
                if (ownHerbAnalogOfChanging.weight() != herb.weight()) {
                    int weight = herb.weight() - ownHerbAnalogOfChanging.weight();
                    Herb.herbByHerb(herb).add(-weight);
                    ownHerbAnalogOfChanging.setWeight(herb.weight());
                }
            } else {
                this.herbs.remove(herb);
            }
        }

        for (Herb herb : newHerbs) {
            if (!this.herbs.contains(herb)) {
                this.herbs.add(herb);
                Herb.herbByHerb(herb).add(-herb.weight());
            }
        }
    }

    public Date distillDate() {
        return distillDate;
    }

    public int alcPercent() {
        return alcPercent;
    }

    public int distillTemperature() {
        return distillTemperature;
    }

    public String comment() {
        return comment;
    }

    public void setStartDate(Date startDate) {
        this.startInfuseDate = startDate;
    }

    public void setSpiritVolume(double spiritVolume) {
        this.spiritVolume = spiritVolume;
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

    public Stage stage() {
        if (distillDate == null) {
            return Stage.Infuse;
        } else if (Time.today().equals(Time.clearHMSPrecision(distillDate))) {
            return Stage.Distill;
        } else if (Time.now().before(Time.addPeriod(distillDate, 1, TimeUnit.DAYS))) {
            return Stage.Colouring;
        } else if (Time.now().before(Time.addPeriod(distillDate, 14, TimeUnit.DAYS))) {
            return Stage.Aging;
        } else if (done) {
            return Stage.Done;
        } else {
            return Stage.Production;
        }
    }

    public enum Stage {
        Infuse,
        Distill,
        Colouring,
        Aging,
        Production,
        Done;
    }
}
