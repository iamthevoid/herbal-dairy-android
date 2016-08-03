package com.iam.herbaldairy.arch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.widget.AddHerbDialog;
import com.iam.herbaldairy.widget.Divider;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import java.util.List;

public class HerbFragment extends Fragment implements Header.HeaderManipulation, AddHerbDialog.HasDataToReload {

    private ArrayAdapter<String> adapter;
    private RecyclerView view;
    private HerbListAdapter listAdapter;
    private com.iam.herbaldairy.widget.LinearLayoutManager manager;
    private AddHerbDialog dialog;
    private ListPopupWindow addHerbLpw;
    private AddHerbDialog.Container container;
    private String[] herbs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.recycler_fragment, container, false);
        view.addItemDecoration(new Divider(getContext(), R.drawable.gray_divider, new int[]{}, new int[]{0,0}));
        dialog = new AddHerbDialog(getActivity());

        this.container = ((AddHerbDialog.Container)getActivity());

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
            return new HerbaVH(inflater.inflate(R.layout.herb_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(HerbaVH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class HerbaVH extends RecyclerView.ViewHolder {

            ImageView edit;
            FrameLayout settingsButton;
            Text title;
            Text latin;
            Text weight;
            Text volume;
            Text type;
            ImageView image;

            HerbaVH(View view) {
                super(view);
                settingsButton = (FrameLayout) view.findViewById(R.id.setbutton);
                edit = (ImageView) view.findViewById(R.id.edit);
                edit.setImageDrawable(svg.settings.drawable());
                title = (Text) view.findViewById(R.id.title);
                latin = (Text) view.findViewById(R.id.latin);
                weight = (Text) view.findViewById(R.id.weight);
                volume = (Text) view.findViewById(R.id.volume);
                type = (Text) view.findViewById(R.id.type);
                image = (ImageView) view.findViewById(R.id.image);
            }

            void onBind(final int position) {
                Herb herb = Herb.list().get(position);
                title.setText(herb.name());
                latin.setText(herb.latin());
                weight.setText(herb.weight() + "g");
                volume.setText(herb.volumeString() + "l");
                type.setText(herb.typeString());
                Log.d("imageUrl", herb.imageURL() + "fd");
                Glide.with(getContext())
                        .load(herb.imageURL())
                        .centerCrop()
                        .into(image);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HerbEditFragment fragment = new HerbEditFragment();
                        fragment.setHerb(Herb.list().get(position));
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft
                                .replace(R.id.container, fragment, HerbEditFragment.class.getCanonicalName())
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }

        }
    }

    @Override
    public void onSave(Herb herb) {
        int position = Herb.list().indexOf(herb);
        RecyclerView.Adapter adapter = view.getAdapter();
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onClose() {
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

                ((Header.SearchInHeader)getActivity()).switchSearch();

//                addHerbLpw.setAdapter(adapter);
//                addHerbLpw.setWidth(280);
//                addHerbLpw.setHeight(500);
//                addHerbLpw.setAnchorView(view);
//                addHerbLpw.setModal(true);
//                addHerbLpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        if (Herb.containsHerbName(herbs[i])) {
//                            dialog.setHerb(Herb.herbByName(herbs[i]), AddHerbDialog.From.HerbFragmentByHerb);
//                        }
//                        dialog.setHerb(herbs[i], AddHerbDialog.From.HerbFragmentByName);
//                        dialog.setDataReloader(HerbFragment.this);
//                        Log.d("onItemClick", herbs[i]);
//                        container.addView(dialog);
//                        addHerbLpw.dismiss();
//                    }
//                });
//                addHerbLpw.show();
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
        return "Herbs";
    }

    @Override
    public String headerSubtitle() {
        return null;
    }
}
