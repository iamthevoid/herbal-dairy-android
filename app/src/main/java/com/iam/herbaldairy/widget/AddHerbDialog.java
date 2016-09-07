package com.iam.herbaldairy.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.util.Web;
import com.iam.herbaldairy.arch.db.DBCache;
import com.iam.herbaldairy.entities.HerbOwned;
import com.iam.herbaldairy.entities.Type;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AddHerbDialog extends RelativeLayout {

    private From from;
    private HerbOwned herbOwned;
    private String description = "";
    private String imageUrl = "";

    private HasDataToReload hasDataToReload;

    private boolean loaded = false;

    private CircularProgressView loadHerbDetailsProgress;
    private Text herbNameText;
    private Text latinNameText;
    private Button closeDialogBTN;
    private Button addHerbBTN;
//    private ImageView closeDialogBTN;
    private ImageView downImageView;
    private Text typeText;

    private EditText weightET;
    private RelativeLayout selectTypeBTN;

    private PopupMenu popupMenu;
    private ArrayAdapter<String> popupAdapter;

    private ArrayList<String> types = DBCache.getInstance().typeNames();


    public AddHerbDialog(Context context) {
        super(context);
        init(context);
    }

    public AddHerbDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        ((AppCompatActivity)context).getLayoutInflater().inflate(R.layout.dialog_add_herb, this);

        weightET = (EditText) findViewById(R.id.weight);

        popupAdapter = new ArrayAdapter<>(context, R.layout.popup_item, types);
        selectTypeBTN = (RelativeLayout) findViewById(R.id.selectType);
        selectTypeBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_herb_type, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        typeText.setText(item.getTitle().toString().toUpperCase());
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        herbNameText = (Text) findViewById(R.id.title);
        loadHerbDetailsProgress = (CircularProgressView) findViewById(R.id.progress);
        loadHerbDetailsProgress.setIndeterminate(true);
        loadHerbDetailsProgress.startAnimation();
        loadHerbDetailsProgress.setVisibility(INVISIBLE);
        latinNameText = (Text) findViewById(R.id.titleLatin);
        typeText = (Text) findViewById(R.id.type);
        addHerbBTN = (Button) findViewById(R.id.add);
        addHerbBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loaded) {
                    final String stringType = typeText.getText().toString().toLowerCase();
                    final Type type = Type.valueOf(stringType);
                    switch (from) {
                        case HerbFragmentByName:
                            herbOwned.add(getIntFromField(weightET));//, getIntFromField(volumeET));
                            herbOwned.typedHerb().setType(type);
                            DBCache.getInstance().userHerbs().addToOwnHerbs(herbOwned);
                            break;
                        case HerbFragmentByHerb:
                            if (herbOwned.typedHerb().type() != type) {
                                herbOwned = new HerbOwned(herbOwned, type);
                            }
                            herbOwned.add(getIntFromField(weightET));//, getIntFromField(volumeET));
                            DBCache.getInstance().userHerbs().addToOwnHerbs(herbOwned);
                            break;
                        case AddAbsintheFragmentByName:
                            herbOwned.add(getIntFromField(weightET));
                            herbOwned.typedHerb().setType(type);
                            break;
                        case AddAbsintheFragmentByHerb:
                            if (herbOwned.typedHerb().type() != type) {
                                herbOwned = new HerbOwned(herbOwned, type);
                            }
                            herbOwned.add(getIntFromField(weightET));
                            break;
                    }
                    DBCache.getInstance().addTypedHerbToDB(herbOwned.typedHerb());
                    hasDataToReload.onSave(herbOwned);
                    close();
                } else {
                    Toast.makeText(context, "Подождите окончания загрузки данных", Toast.LENGTH_SHORT).show();
                }
            }
        });
        closeDialogBTN = (Button) findViewById(R.id.close);
        closeDialogBTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClose", "onClose");
                close();
            }
        });
        downImageView = (ImageView) findViewById(R.id.dropdown);
        downImageView.setImageDrawable(svg.down.drawable());
    }

    public void setDataReloader(HasDataToReload hasDataToReload) {
        this.hasDataToReload = hasDataToReload;
    }

    private int getIntFromField(EditText field) {
        final String weightString = field.getText().toString();
        return weightString.equals("") ? 0 : Integer.parseInt(weightString);
    }

    private void close() {
        hasDataToReload.onClose();
        ViewGroup viewGroup = (ViewGroup) this.getParent();
        viewGroup.removeAllViews();
    }

    public void setHerb(String herb, From from) {
        this.from = from;
        HerbOwned inOwnHerbs;
        if (from == From.AddAbsintheFragmentByName) {
            inOwnHerbs = new HerbOwned(DBCache.getInstance().userHerbs().herbByName(herb));
        } else {
            inOwnHerbs = DBCache.getInstance().userHerbs().herbByName(herb);
        }

        herbNameText.setText(herb);
        if (inOwnHerbs != null) {
            this.herbOwned = new HerbOwned(inOwnHerbs);
            latinNameText.setText(this.herbOwned.typedHerb().herb().latinName());
            Log.d("weight", this.herbOwned.weight() + "");
        } else {
            latinNameText.setText("");
            loadDataFromWiki(herb);
        }
    }

    public void setHerb(HerbOwned herb, From from) {
        this.from = from;
        if (from == From.AddAbsintheFragmentByHerb) this.herbOwned = new HerbOwned(herb);
        else this.herbOwned = herb;
        loaded = true;
        herbNameText.setText(this.herbOwned.name());
        latinNameText.setText(this.herbOwned.latinName());
    }

    public interface Container {
        void addView(View view);
    }

    public interface HasDataToReload {
        void onSave(HerbOwned herb);
        void onClose();
    }

    public void loadDataFromWiki(final String herb) {
        new AsyncTask<Void, Void, String> (){
            @Override
            protected void onPreExecute() {
                loadHerbDetailsProgress.setVisibility(VISIBLE);
            }

            @Override
            protected String doInBackground(Void[] objects) {

                String herbUrl = null;
                try {
                    herbUrl = URLEncoder.encode(herb.replaceAll(" ", "_"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Web.GET(Web.url.wikipediaRu + herbUrl, null);
            }

            @Override
            protected void onPostExecute(String s) {
                Document doc = Jsoup.parse(s);
                String lat = latinNameFromHTML(doc);
                imageUrl = imageSrcFromHTML(doc);
//                description = s;
                latinNameText.setText(lat);
                loadHerbDetailsProgress.setVisibility(INVISIBLE);
                loaded = true;
                AddHerbDialog.this.herbOwned = new HerbOwned(herb);
//                AddHerbDialog.this.herbOwned.setDescription(description);
                final Herb herb = AddHerbDialog.this.herbOwned.typedHerb().herb();
                herb.setImageURL(imageUrl);
                herb.setLatinName(lat);
                DBCache.getInstance().addHerbToDB(herb);
                loaded = true;

            }
        }.execute();
    }

    @NonNull
    private String imageSrcFromHTML(Document doc) {
        return "https:" + doc.select("table[class=infobox] a[class=image] img").attr("src");
    }

    private String latinNameFromHTML(Document doc) {
        return doc.select("div.mw-content-ltr p i span[lang=la]").get(0).text();
    }

    public enum From {
        HerbFragmentByName,
        HerbFragmentByHerb,
        AddAbsintheFragmentByName,
        AddAbsintheFragmentByHerb;
    }
}
