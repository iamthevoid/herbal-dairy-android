package com.iam.herbaldairy.arch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.HerbAddDialog;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import java.util.List;

public class HerbFragment extends Fragment implements Header.HeaderManipulation, HerbAddDialog.HasDataToReload {

    private ArrayAdapter<String> adapter;
    private RecyclerView view;
    private HerbListAdapter listAdapter;
    private com.iam.herbaldairy.widget.LinearLayoutManager manager;
    private HerbAddDialog dialog;
    private ListPopupWindow addHerbLpw;
    private HerbAddDialog.Container container;
    private String[] herbs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.recycler_fragment, container, false);
        dialog = new HerbAddDialog(getActivity());

        this.container = ((HerbAddDialog.Container)getActivity());

        herbs = getActivity().getResources().getStringArray(R.array.herbs);
        adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.popup_item,
                herbs
        );

        addHerbLpw = new ListPopupWindow(getActivity());

        listAdapter = new HerbListAdapter(getContext());
        manager = new com.iam.herbaldairy.widget.LinearLayoutManager(getContext());
        view.setAdapter(listAdapter);
        view.setLayoutManager(manager);

        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
        Log.d("onCreateView", "done");
        return view;
    }

    private class HerbListAdapter extends RecyclerView.Adapter<HerbListAdapter.HerbaVH> {

        LayoutInflater inflater;
        List<Herb> list;

        HerbListAdapter(Context context) {

            inflater = ((AppCompatActivity)context).getLayoutInflater();
            list = Herb.list();
        }

        @Override
        public HerbaVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HerbaVH(inflater.inflate(R.layout.herba_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(HerbaVH holder, int position) {
            holder.onBind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class HerbaVH extends RecyclerView.ViewHolder {

            Text edit;
            Text title;
            Text latin;
            Text weight;
            Text volume;
            Text type;
            ImageView image;

            HerbaVH(View view) {
                super(view);
                edit = (Text) view.findViewById(R.id.edit);
                title = (Text) view.findViewById(R.id.title);
                latin = (Text) view.findViewById(R.id.latin);
                weight = (Text) view.findViewById(R.id.weight);
                volume = (Text) view.findViewById(R.id.volume);
                type = (Text) view.findViewById(R.id.type);
                image = (ImageView) view.findViewById(R.id.image);
            }

            void onBind(Herb herb) {
                title.setText(herb.name());
                latin.setText(herb.latin());
                weight.setText(herb.weight() + "g.");
                volume.setText(herb.volumeString() + "l");
                type.setText(" (" + herb.typeString() + ")");
                Glide.with(getContext())
                        .load(herb.imageURL())
                        .centerCrop()
                        .into(image);
            }

        }
    }

    @Override
    public void reloadData() {
        RecyclerView.Adapter adapter = view.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public svg leftIcon() {
        return svg.toggle;
    }

    @Override
    public View.OnClickListener leftIconAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return svg.plus;
    }

    @Override
    public View.OnClickListener rightIcon2Action() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHerbLpw.setAdapter(adapter);
                addHerbLpw.setWidth(400);
                addHerbLpw.setHeight(500);
                addHerbLpw.setAnchorView(view);
                addHerbLpw.setModal(true);
                addHerbLpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (Herb.containsHerbName(herbs[i])) {
                            dialog.setHerb(Herb.herbByName(herbs[i]), HerbAddDialog.From.HerbFragmentByHerb);
                        }
                        dialog.setHerb(herbs[i]);
                        dialog.setDataReloader(HerbFragment.this);
                        Log.d("onItemClick", herbs[i]);
                        container.addView(dialog);
                        addHerbLpw.dismiss();
                    }
                });
                addHerbLpw.show();

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
        return "HERBS";
    }

    @Override
    public String headerSubtitle() {
        return null;
    }
}
