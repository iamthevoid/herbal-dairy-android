package com.iam.herbaldairy.arch.root;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.arch.db.Backup;
import com.iam.herbaldairy.arch.db.DBCache;
import com.iam.herbaldairy.entities.Absinthe;
import com.iam.herbaldairy.entities.User;
import com.iam.herbaldairy.widget.assets.svg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class ShieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shield_activity);

        SharedPreferences h = getSharedPreferences("herba_prefs", MODE_PRIVATE);
        for (Map.Entry<String, ?> entry : h.getAll().entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            System.out.println(entry.getKey().equals(getString(R.string.own_herbas)));
        }
//        final String string = h.getString(getString(R.string.own_herbas), "+");
//        System.out.println(string);

        ((ImageView) findViewById(R.id.logo)).setImageDrawable(svg.mint.drawable());

        DBCache.getInstance().init(getApplicationContext(), new DBCache.InitCompletionHandler() {
            @Override
            public void onFinish() {
                new AsyncTask<Void, Void, Backup>() {
                    @Override
                    protected Backup doInBackground(Void... voids) {
                        User user = new User("iam", "GbpljcYf", "alesetar@gmail.com");
                        user.writeUserDataToDB();
                        System.out.println(user.toSHA256("rant"));

//                        try {
//                            Dao<Backup, String> dao = DaoFactory.createDao(DBHelper.defaultConnection(), Backup.class);
//                            return dao.queryForAll().get(0);
//                        } catch (SQLException e) {
//                            e.printStackTrace();

                        return null;
//                        }
                    }

                    @Override
                    protected void onPostExecute(Backup aVoid) {
                        finish();
                        System.out.println("shield");
                        final ShieldActivity context = ShieldActivity.this;
                        context.startActivity(new Intent(context, DrawerActivity.class));
//                        HerbUser.instantiateUserHerbsFromString(aVoid.herbs());
//                        Absinthe.instantiateFromString(aVoid.absinthes());
//                        Absinthe.writeToPreferences(context);
//                        HerbUser.writeToPreferences(context);
                        DBCache.getInstance().userHerbs().readFromPreferences(context);
                        DBCache.getInstance().userAbsinthes().readFromPreferences(context);
                    }
                }.execute();
            }
        });
    }
}
