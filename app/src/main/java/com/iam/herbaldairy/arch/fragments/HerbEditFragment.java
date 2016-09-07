package com.iam.herbaldairy.arch.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.util.Web;
import com.iam.herbaldairy.arch.db.DBCache;
import com.iam.herbaldairy.entities.Absinthe;
import com.iam.herbaldairy.entities.HerbOwned;
import com.iam.herbaldairy.entities.Type;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HerbEditFragment extends Fragment implements Header.HeaderManipulation {

    public HerbOwned herb;

    View view;

    Text name;
    Text type;
    EditText weight;
    EditText volumeFactor;
    EditText dryTime;
    EditText dryFactor;
    ImageView icon;

    Button reload;

    LinearLayout selectTypeButton;

    ListPopupWindow lpw;
    ArrayAdapter<String> adapter;

    ArrayList<String> types;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.herb_edit_fragment, container, false);

        types = DBCache.getInstance().typeNames();
        lpw = new ListPopupWindow(getContext());
        adapter = new ArrayAdapter<>(getContext(), R.layout.popup_item, types);

//        herb = Herb.list().get(getArguments().getInt(getString(R.string.herb_to_edit)));

        reload = (Button) view.findViewById(R.id.reload);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDataFromWiki(herb.name());
            }
        });
        icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(svg.down.drawable());
        name = (Text) view.findViewById(R.id.title);
        name.setText(herb.name());
        selectTypeButton = (LinearLayout) view.findViewById(R.id.selectType);
        type = (Text) view.findViewById(R.id.type);
        type.setText(herb.typedHerb().typeString());
        selectTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lpw.setAdapter(adapter);
                lpw.setWidth(150);
                lpw.setHeight(250);
                lpw.setAnchorView(view);
                lpw.setModal(true);
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        type.setText(types.get(i).toUpperCase());
                        lpw.dismiss();
                    }
                });
                lpw.show();
            }
        });
        weight = (EditText) view.findViewById(R.id.weight);
        weight.setText(herb.weight() + "");
        volumeFactor = (EditText) view.findViewById(R.id.volumeFactor);
        volumeFactor.setText(herb.typedHerb().volumeFactor() + "");
        dryTime = (EditText) view.findViewById(R.id.dryTime);
        dryTime.setText(herb.typedHerb().dryTime() + "");
        dryFactor = (EditText) view.findViewById(R.id.dryFactor);
        dryFactor.setText(herb.typedHerb().dryFactor() + "");

        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
        return view;
    }

    public void setHerb(HerbOwned herb) {
        this.herb = herb;
    }

    @Override
    public svg leftIcon() {
        return svg.toggle;
    }

    @Override
    public View.OnClickListener leftIconAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Header.ToggleClicker)getActivity()).onToggleClick();
            }
        };
    }

    @Override
    public svg rightIcon1() {
        return null;
    }

    @Override
    public View.OnClickListener rightIcon1Action() {
        return null;
    }

    @Override
    public svg rightIcon2() {
        return svg.select;
    }

    @Override
    public View.OnClickListener rightIcon2Action() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                herb.typedHerb().setDryFactor(Double.parseDouble(dryFactor.getText().toString().equals("") ? "0" : dryFactor.getText().toString()));
                herb.typedHerb().setDryTime(Double.parseDouble(dryTime.getText().toString().equals("") ? "0" : dryTime.getText().toString()));
                herb.typedHerb().setType(Type.valueOf(type.getText().toString().toLowerCase()));
                herb.setWeight(Integer.parseInt(weight.getText().toString().equals("") ? "" : weight.getText().toString()));
                herb.typedHerb().setVolumeFactor(Double.parseDouble(volumeFactor.getText().toString().equals("") ? "0" : volumeFactor.getText().toString()));

                Log.d("weight", herb.weight() + "");

                FragmentManager fm = getActivity().getSupportFragmentManager();

                fm.popBackStack();

                /*String herbs = */DBCache.getInstance().userHerbs().writeToPreferences(getContext());
                /*String absinthes = */DBCache.getInstance().userAbsinthes().writeToPreferences(getContext());
//                new Backup(herbs, absinthes).postToDB();
            }
        };
    }

    public void loadDataFromWiki(final String herb) {
        new AsyncTask<Void, Void, String>(){
            @Override
            protected void onPreExecute() {
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
                String imageUrl = imageSrcFromHTML(doc);
                String lat = latinNameFromHTML(doc);
                HerbEditFragment.this.herb.typedHerb().herb().setImageURL(imageUrl);
                HerbEditFragment.this.herb.typedHerb().herb().setLatinName(lat);

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

    @Override
    public String headerTextButton() {
        return null;
    }

    @Override
    public View.OnClickListener textAction() {
        return null;
    }

    @Override
    public String headerTitle() {
        return "EDIT HERB";
    }

    @Override
    public String headerSubtitle() {
        return null;
    }
}
