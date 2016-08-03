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
import com.iam.herbaldairy.Web;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.entities.Type;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AddHerbDialog extends RelativeLayout {

    private From from;
    private Herb herb;
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

    private String[] types = Type.names();


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

        popupAdapter = new ArrayAdapter<String>(context, R.layout.popup_item, types);
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
                            herb.add(getIntFromField(weightET));//, getIntFromField(volumeET));
                            herb.setType(type);
                            Herb.addToOwnHerbs(herb);
                            break;
                        case HerbFragmentByHerb:
                            if (herb.type() != type) {
                                herb = new Herb(herb, type);
                            }
                            herb.add(getIntFromField(weightET));//, getIntFromField(volumeET));
                            Herb.addToOwnHerbs(herb);
                            break;
                        case AddAbsintheFragmentByName:
                            herb.add(getIntFromField(weightET));
                            herb.setType(type);
                            break;
                        case AddAbsintheFragmentByHerb:
                            if (herb.type() != type) {
                                herb = new Herb(herb, type);
                            }
                            herb.add(getIntFromField(weightET));
                            break;
                    }
                    hasDataToReload.onSave(herb);
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
        final Herb inOwnHerbs = Herb.herbByName(herb);
        herbNameText.setText(herb);
        if (inOwnHerbs != null) {
            this.herb = new Herb(inOwnHerbs);
            latinNameText.setText(this.herb.latin());
            Log.d("weight", this.herb.weight() + "");
        } else {
            latinNameText.setText("");
            loadDataFromWiki(herb);
        }
    }

    public void setHerb(Herb herb, From from) {
        this.from = from;
        if (from == From.AddAbsintheFragmentByHerb) this.herb = new Herb(herb);
        else this.herb = herb;
        loaded = true;
        herbNameText.setText(herb.name());
        latinNameText.setText(herb.latin());
    }

    public interface Container {
        void addView(View view);
    }

    public interface HasDataToReload {
        void onSave(Herb herb);
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
                AddHerbDialog.this.herb = new Herb(herb);
//                AddHerbDialog.this.herb.setDescription(description);
                AddHerbDialog.this.herb.setImageURL(imageUrl);
                AddHerbDialog.this.herb.setLatinName(lat);
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
