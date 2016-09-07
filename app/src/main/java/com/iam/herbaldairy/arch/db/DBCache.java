package com.iam.herbaldairy.arch.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.entities.Absinthe;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.entities.HerbOwned;
import com.iam.herbaldairy.entities.Type;
import com.iam.herbaldairy.entities.many_to_many.HerbType;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.spring.DaoFactory;
import com.mysql.jdbc.exceptions.MySQLDataException;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

final public class DBCache {

    private UserAbsinthes userAbsinthes;

    private DBCache() {}
    private static volatile DBCache instance;

    public static DBCache getInstance() {
        DBCache localInstance = instance;
        if (localInstance == null) {
            synchronized (DBCache.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DBCache();
                }
            }
        }
        return localInstance;
    }

    private HashSet<Herb> herbs;
    private ArrayList<HerbType> herbTypes;
    private ArrayList<Type> types;
    private ArrayList<String> typeNames;

    public void init(Context context, InitCompletionHandler initCompletionHandler) {
        initChain(context, initCompletionHandler);
    }

    private void initChain(final Context context, final InitCompletionHandler initCompletionHandler) {
        initHerbs(context, initCompletionHandler);
    }

    private void initHerbs(final Context context, final InitCompletionHandler initCompletionHandler) {
        herbs = new HashSet<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Dao<Herb, String> dao = DaoFactory.createDao(DBHelper.defaultConnection(), Herb.class);
                    List<Herb> list = dao.queryForAll();
                    for (Herb herb : list) {
                        DBCache.this.herbs.add(herb);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                initTypes(context, initCompletionHandler);
            }
        }.execute();
    }

    private void initTypes(final Context context, final InitCompletionHandler initCompletionHandler) {
        types = new ArrayList<>();
        typeNames = new ArrayList<>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Dao<Type, String> dao = DaoFactory.createDao(DBHelper.defaultConnection(), Type.class);
                    List<Type> list = dao.queryForAll();

                    for (Type type : list) {
                        DBCache.this.types.add(type);
                        DBCache.this.typeNames.add(type.name());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                userHerbs = new UserHerbs();
                userHerbs.readFromPreferences(context);
                userAbsinthes = new UserAbsinthes();
                userAbsinthes.readFromPreferences(context);
                initCompletionHandler.onFinish();
            }
        }.execute();
    }

    public void addHerbToDB(final Herb herb) {
        if (herbs == null) {
            System.out.println(herbs);
            new AsyncTask<Void, Void, HashSet<Herb>>() {
                @Override
                protected HashSet<Herb> doInBackground(Void... voids) {
                    try {
                        Dao<Herb, String> hDao = DaoFactory.createDao(DBHelper.defaultConnection(), Herb.class);

                        List<Herb> herbs = hDao.queryForAll();
                        HashSet<Herb> hSet = new HashSet<>();
                        for (Herb h : herbs) {
                            hSet.add(h);
                        }
                        if (!hSet.contains(herb)) {
                            hDao.create(herb);
                        }
                        return hSet;
                    } catch (SQLException e) {
                        return new HashSet<>();
                    }
                }

                @Override
                protected void onPostExecute(HashSet<Herb> herbs) {
                    DBCache.this.herbs = herbs;
                }
            }.execute();
        } else {
            System.out.println(herbs.contains(herb));
            if (!herbs.contains(herb)) {
                herbs.add(herb);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Dao<Herb, String> hDao = DaoFactory.createDao(DBHelper.defaultConnection(), Herb.class);
                            hDao.create(herb);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            }
        }
    }

    public ArrayList<Type> types() {
        return getInstance().types;
    }

    public ArrayList<String> typeNames() {
        return typeNames;
    }

    public void addTypedHerbToDB(final HerbType herbType) {
        System.out.println(herbType.herb() + " " + herbType.type());
        if (herbTypes == null) {
            new AsyncTask<Void, Void, ArrayList<HerbType>>() {
                @Override
                protected ArrayList<HerbType> doInBackground(Void... voids) {
                    try {
                        Dao<HerbType, String> dao = DaoFactory.createDao(DBHelper.defaultConnection(), HerbType.class);
                        List<HerbType> list = dao.queryForAll();
                        if (!list.contains(herbType)) {
                            dao.create(herbType);
                        } else {
                            for (HerbType ht : list) {
                                if (ht.equals(herbType)) {
                                    dao.update(ht);
                                }
                            }
                        }

                        ArrayList<HerbType> hts = new ArrayList<>();
                        for (HerbType ht : list) {
                            hts.add(ht);
                        }
                        return hts;
                    } catch (SQLException e) {
                        return new ArrayList<>();
                    }

                }

                @Override
                protected void onPostExecute(ArrayList<HerbType> herbTypes) {
                    DBCache.this.herbTypes = herbTypes;
                }
            }.execute();
        } else {

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Dao<HerbType, String> hDao = DaoFactory.createDao(DBHelper.defaultConnection(), HerbType.class);
                            if (!herbTypes.contains(herbType)) {
                                herbTypes.add(herbType);
                                hDao.create(herbType);
                            } else {
                                for (HerbType ht : herbTypes) {
                                    if (ht.equals(herbType)) hDao.update(ht);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
        }
    }

    /**
     * InitCompletionHandler needs for closure - make some operations after initialization
     * has finished.
     */

    public interface InitCompletionHandler {
        void onFinish();
    }

    /**
     * type of Singleton, contains user herbs. Initialize on application launch and never more (has
     * private constructor). userHerbs() returns userHerbs for access to it's fields and methods.
     */

    public UserHerbs userHerbs() {
        return userHerbs;
    }

    private UserHerbs userHerbs;

    public final class UserHerbs {

        private UserHerbs() {}

        /**
         * STORING DATA AND SERIALIZATION
         *
         * methods for rw data to/from device
         * methods for JSON data serialization
         * constructor for deserialize data from json
         * enum for store JSON keys
         *
         * HERBA_PREFS - name for shared prefs on device
         */
        private static final String HERBA_PREFS = "herba_prefs";
        /**
         * writeToPreferences(Context context)
         *
         * Write data to shared preferences on device
         * @param context - application context for access to mobile storage
         */
        public String writeToPreferences(Context context) {
            JSONArray userHerbas = new JSONArray();
            for (HerbOwned herba : ownHerbs) {
                userHerbas.put(herba.createJSON());
            }

            SharedPreferences preferences = context.getSharedPreferences(HERBA_PREFS, Context.MODE_PRIVATE);
            preferences
                    .edit()
                    .putString(context.getString(R.string.own_herbas), userHerbas.toString())
                    .apply();

            return userHerbas.toString();
        }
        /**
         * readFromPreferences(Context context)
         *
         * Read data from shared preferences on device and storing it in RAM
         * @param context - application context for access to mobile storage
         */
        public void readFromPreferences(Context context) {
            ownHerbs = new ArrayList<>();
            SharedPreferences preferences = context.getSharedPreferences(HERBA_PREFS, Context.MODE_PRIVATE);
            final String herbas = preferences.getString(context.getString(R.string.own_herbas), "");
            instantiateUserHerbsFromString(herbas);
        }

        public void instantiateUserHerbsFromString(String herbas) {
            if (!herbas.equals("")) {
                JSONArray userHerbas;
                try {
                    userHerbas = new JSONArray(herbas);
                    for (int i = 0, l = userHerbas.length(); i < l; i++) {
                        final HerbOwned newHerb = new HerbOwned(userHerbas.getJSONObject(i));
                        addToOwnHerbs(newHerb);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        /**
         * Static array of UserHerbs in RAM for fast access and operate. Methods for access to array,
         * adding/removing herb to array, check if array contain herb name, return herbs with similar
         * type, access to array elements by herb name or herb data
         */
        private ArrayList<HerbOwned> ownHerbs = new ArrayList<>();

        public ArrayList<HerbOwned> list() {
            return ownHerbs;
        }

        public void addToOwnHerbs(HerbOwned herb) {
            if (ownHerbs.contains(herb)) {
                ownHerbs.get(ownHerbs.indexOf(herb)).add(herb.weight());
            } else {
                ownHerbs.add(herb);
            }
        }

        public boolean contains(Herb herb) {
            for (HerbOwned h : ownHerbs) {
                if (h.typedHerb().herb().hashCode() == herb.hashCode()) return true;
            }
            return false;
        }

        public void removeFromOwnHerbs(HerbOwned herb, Date date) {
            if (ownHerbs.contains(herb)) {
                ownHerbs.get(ownHerbs.indexOf(herb)).add(-herb.weight());
            }
        }

        public boolean containsHerbName(String herbName) {
            for (HerbOwned herb : ownHerbs) {
                if (herb.typedHerb().herb().name().equals(herbName)) {
                    return true;
                }
            }
            return false;
        }

        public ArrayList<HerbOwned> herbsByName(String name) {
            ArrayList<HerbOwned> h = new ArrayList<>();
            for (HerbOwned herb : ownHerbs) {
                if (herb.typedHerb().herb().name().equals(name)) {
                    h.add(herb);
                }
            }
            return h;
        }

        public HerbOwned herbByName(String herbName) {
            for (HerbOwned herb : ownHerbs) {
                if (herb.typedHerb().herb().name().equals(herbName)) {
                    return herb;
                }
            }
            return null;
        }

        public HerbOwned herbByHerb(HerbType herbTyped) {
            for (HerbOwned h : ownHerbs) {
                if (h.typedHerb().equals(herbTyped)) {
                    return h;
                }
            }
            throw new RuntimeException("You have not " + herbTyped.type().name() + " of herbTyped " + herbTyped.herb().name());
        }
    }


    /**
     * type of Singleton, contains user absinthes. Initialize on application launch and never more
     * (has private constructor). userAbsinthes() returns userAbsinthes for access to it's fields
     * and methods.
     */

    public final class UserAbsinthes {
        private UserAbsinthes() {}

        public String ABSINTH_PREFS = "absinth_prefs";
        public ArrayList<Absinthe> absinthes = new ArrayList<>();

        public String writeToPreferences(Context context) {
            JSONArray jAbsinthes = new JSONArray();
            for (Absinthe absinthe : absinthes) {
                jAbsinthes.put(absinthe.createJSON());
            }

            SharedPreferences preferences = context.getSharedPreferences(ABSINTH_PREFS, Context.MODE_PRIVATE);
            preferences
                    .edit()
                    .putString(context.getString(R.string.own_absinthes), jAbsinthes.toString())
                    .apply();

            return jAbsinthes.toString();
        }

        public void readFromPreferences(Context context) {
            if (absinthes == null) absinthes = new ArrayList<>();
            SharedPreferences preferences = context.getSharedPreferences(ABSINTH_PREFS, Context.MODE_PRIVATE);
            String sAbsinthes = preferences.getString(context.getString(R.string.own_absinthes), "");
            instantiateFromString(sAbsinthes);
        }

        public void instantiateFromString(String sAbsinthes) {
            if (!sAbsinthes.equals("")) {
                JSONArray userAbsinthes;
                try {
                    userAbsinthes = new JSONArray(sAbsinthes);
                    for (int i = 0, l = userAbsinthes.length(); i < l; i++) {
                        final Absinthe newAbsinthe = new Absinthe(userAbsinthes.getJSONObject(i));
                        if (!absinthes.contains(newAbsinthe)) absinthes.add(newAbsinthe);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Collections.sort(absinthes);
            }
        }

        public ArrayList<Absinthe> absinthes() {
            return absinthes;
        }


        public void add(Absinthe absinthe) {
            System.out.println("absinthe " + !absinthes.contains(absinthe));
            if (!absinthes.contains(absinthe)) { absinthes.add(absinthe); }
        }

        public void remove(Absinthe absinthe, Context context) {
            ArrayList<HerbOwned> herbs = absinthe.herbs();
            for (HerbOwned herb : herbs) {
                if (DBCache.getInstance().userHerbs().contains(herb.typedHerb().herb())) {
                    DBCache.getInstance().userHerbs().herbByHerb(herb.typedHerb()).add(herb.weight());
                }
            }
            absinthes.remove(absinthe);
            writeToPreferences(context);
        }

    }

    public UserAbsinthes userAbsinthes() {
        return userAbsinthes;
    }

}
