package com.iam.herbaldairy.arch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.entities.Type;
import com.iam.herbaldairy.widget.AddHerbDialog;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

public class HerbEditFragment extends Fragment implements Header.HeaderManipulation {

    Herb herb;

    View view;

    Text name;
    Text type;
    EditText weight;
    EditText volumeFactor;
    EditText dryTime;
    EditText dryFactor;
    ImageView icon;

    LinearLayout selectTypeButton;

    ListPopupWindow lpw;
    ArrayAdapter<String> adapter;

    String[] types;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.herb_edit_fragment, container, false);

        types = Type.names();
        lpw = new ListPopupWindow(getContext());
        adapter = new ArrayAdapter<String>(getContext(), R.layout.popup_item, types);

        herb = Herb.list().get(getArguments().getInt(getString(R.string.herb_to_edit)));

        icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(svg.down.drawable());
        name = (Text) view.findViewById(R.id.title);
        name.setText(herb.name());
        selectTypeButton = (LinearLayout) view.findViewById(R.id.selectType);
        type = (Text) view.findViewById(R.id.type);
        type.setText(herb.typeString());
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
                        type.setText(types[i].toUpperCase());
                        lpw.dismiss();
                    }
                });
                lpw.show();
            }
        });
        weight = (EditText) view.findViewById(R.id.weight);
        weight.setText(herb.weight() + "");
        volumeFactor = (EditText) view.findViewById(R.id.volumeFactor);
        volumeFactor.setText(herb.volumeFactor() + "");
        dryTime = (EditText) view.findViewById(R.id.dryTime);
        dryTime.setText(herb.dryTime() + "");
        dryFactor = (EditText) view.findViewById(R.id.dryFactor);
        dryFactor.setText(herb.dryFactor() + "");

        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
        return view;
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

                herb.setDryFactor(Double.parseDouble(dryFactor.getText().toString()));
                herb.setDryTime(Double.parseDouble(dryTime.getText().toString()));
                herb.setType(Type.valueOf(type.getText().toString().toLowerCase()));
                herb.setWeight(Integer.parseInt(weight.getText().toString()));
                herb.setVolumeFactor(Double.parseDouble(volumeFactor.getText().toString()));

                FragmentManager fm = getActivity().getSupportFragmentManager();

                fm.popBackStack();

                Herb.writeToPreferences(getContext());
            }
        };
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
