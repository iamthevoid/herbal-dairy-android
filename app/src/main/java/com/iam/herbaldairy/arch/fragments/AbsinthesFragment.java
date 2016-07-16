package com.iam.herbaldairy.arch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iam.herbaldairy.R;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.HerbAddDialog;
import com.iam.herbaldairy.widget.LinearLayoutManager;
import com.iam.herbaldairy.widget.assets.svg;

public class AbsinthesFragment extends Fragment implements Header.HeaderManipulation {

    private AbsintheAdapter listAdapter;
    private RecyclerView view;
    private LinearLayoutManager manager;
    private HerbAddDialog.Container container;
    private String[] herbs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.recycler_fragment, container, false);

        this.container = ((HerbAddDialog.Container)getActivity());

        herbs = getActivity().getResources().getStringArray(R.array.herbs);


        listAdapter = new AbsintheAdapter(getContext());
        manager = new com.iam.herbaldairy.widget.LinearLayoutManager(getContext());
        view.setAdapter(listAdapter);
        view.setLayoutManager(manager);

        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
        Log.d("onCreateView", "done");
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
        return "ABSINTHES";
    }

    @Override
    public String headerSubtitle() {
        return null;
    }

    public class AbsintheAdapter extends RecyclerView.Adapter<AbsintheAdapter.AbsintheVH> {

        LayoutInflater inflater;

        public AbsintheAdapter(Context context) {
            inflater = ((AppCompatActivity)context).getLayoutInflater();
        }

        @Override
        public AbsintheVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(AbsintheVH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class AbsintheVH extends RecyclerView.ViewHolder {

            public AbsintheVH(View itemView) {
                super(itemView);
            }
        }
    }
}
