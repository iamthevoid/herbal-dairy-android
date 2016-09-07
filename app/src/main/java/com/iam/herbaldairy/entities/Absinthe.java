package com.iam.herbaldairy.entities;

import android.util.Log;

import com.iam.herbaldairy.util.Calculator;
import com.iam.herbaldairy.util.Time;
import com.iam.herbaldairy.arch.db.DBCache;
import com.iam.herbaldairy.entities.interfaces.JSONSerializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Absinthe implements JSONSerializable, Comparable<Absinthe> {

    private double spiritVolume;
    private boolean done = false;
    private ArrayList<HerbOwned> herbs = new ArrayList<>();

    private double resultVolume;
    private int alcPercent;

    private String comment;
    private Date startInfuseDate;
    private Date distillDate;

    private int distillTemperature;
    private String name;

    public Absinthe(Date startInfuseDate, double spiritVolume, ArrayList<HerbOwned> herbs) {
        this.startInfuseDate = startInfuseDate;
        this.spiritVolume = spiritVolume;
        this.herbs = herbs;
        for (HerbOwned herb : herbs) {
            if (DBCache.getInstance().userHerbs().containsHerbName(herb.typedHerb().herb().name())) {
                DBCache.getInstance().userHerbs().herbByHerb(herb.typedHerb()).add(-herb.weight());
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



    @Override
    public JSONObject createJSON() {
        JSONObject result = new JSONObject();

        try {
            result.put(JSONKey.SpiritVolume.key, spiritVolume);
            JSONArray herbs = new JSONArray();
            for (HerbOwned herb : this.herbs) {
                herbs.put(herb.createJSON());
            }
            result.put(JSONKey.Herb.key, herbs);
            result.put(JSONKey.Name.key, name == null ? "" : name);
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

    public Absinthe(JSONObject jAbsinth) {
        herbs = new ArrayList<>();
        try {
            if (jAbsinth.has(JSONKey.SpiritVolume.key)) this.spiritVolume = jAbsinth.getDouble(JSONKey.SpiritVolume.key);
            JSONArray jHerbs = jAbsinth.getJSONArray(JSONKey.Herb.key);
            for (int i = 0, l = jHerbs.length(); i < l; i++) {
                herbs.add(new HerbOwned(jHerbs.getJSONObject(i)));
            }
            if (jAbsinth.has(JSONKey.ResultVolume.key)) this.resultVolume = jAbsinth.getLong(JSONKey.ResultVolume.key);
            if (jAbsinth.has(JSONKey.AlcoholPercent.key)) this.alcPercent = jAbsinth.getInt(JSONKey.AlcoholPercent.key);
            if (jAbsinth.has(JSONKey.Name.key)) this.name = jAbsinth.getString(JSONKey.Name.key);
            if (jAbsinth.has(JSONKey.Comment.key)) this.comment = jAbsinth.getString(JSONKey.Comment.key);
            if (jAbsinth.has(JSONKey.InfuseDate.key)) this.startInfuseDate = Time.dateFormat.parse(jAbsinth.getString(JSONKey.InfuseDate.key));
            if (jAbsinth.has(JSONKey.DistillDate.key)) {
                final String distillDateString = jAbsinth.getString(JSONKey.DistillDate.key);
                this.distillDate = distillDateString.equals("0") ? null : Time.dateFormat.parse(distillDateString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        int summ = 0;
        for (HerbOwned herb : herbs) {
            summ += herb.typedHerb().herb().name().hashCode();
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



    public ArrayList<HerbOwned> herbs() {
        return herbs;
    }

    public void validateHerbs(ArrayList<HerbOwned> herbs, ArrayList<HerbOwned> newHerbs) {
        for (HerbOwned herbOwned : herbs) {
            if (this.herbs.contains(herbOwned)) {
                final HerbOwned ownHerbAnalogOfChanging = this.herbs.get(this.herbs.indexOf(herbOwned));
                if (ownHerbAnalogOfChanging.weight() != herbOwned.weight()) {
                    int weight = herbOwned.weight() - ownHerbAnalogOfChanging.weight();
                    DBCache.getInstance().userHerbs().herbByHerb(herbOwned.typedHerb()).add(-weight);
                    ownHerbAnalogOfChanging.setWeight(herbOwned.weight());
                }
            } else {
                this.herbs.remove(herbOwned);
            }
        }

        for (HerbOwned absintheHerb : newHerbs) {
            if (!this.herbs.contains(absintheHerb)) {
                this.herbs.add(absintheHerb);
                DBCache.getInstance().userHerbs().herbByHerb(absintheHerb.typedHerb()).add(-absintheHerb.weight());
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

    @Override
    public int compareTo(Absinthe absinthe) {
        if (absinthe.startInfuseDate.before(this.startInfuseDate)) {
            return -1;
        } else if (absinthe.startInfuseDate.after(this.startInfuseDate)) {
            return 1;
        } else {
            return 0;
        }
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private enum JSONKey {

        SpiritVolume    ("spirit_volume"),
        Herb            ("herb"),
        Name            ("name"),
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
        Done
    }
}
