package com.iam.herbaldairy.arch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.iam.herbaldairy.DeleteGestureDetector;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.Time;
import com.iam.herbaldairy.entities.Absinth;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import java.util.Date;

public class AbsinthesFragment extends Fragment implements Header.HeaderManipulation {

    private AbsintheAdapter listAdapter;
    private RecyclerView view;
    private LinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Absinth.readFromPreferences(getContext());
        view = (RecyclerView) inflater.inflate(R.layout.recycler_fragment, container, false);

        listAdapter = new AbsintheAdapter(getContext());
        manager = new LinearLayoutManager(getContext());
        view.setAdapter(listAdapter);
        view.setLayoutManager(manager);
        listAdapter.notifyDataSetChanged();
        Log.d("absinthe cococo", Absinth.absinthes().size() + "");

        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
        Log.d("onCreateView", "done");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("fragment", "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("fragment", "onResume()");
        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("fragment", "onAttach()");
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
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.edit_absinth_open_for), AddAbsinthFragment.OpenFor.Add.name());
                showEditAbsintheFragment(bundle);
            }
        };
    }

    private void showEditAbsintheFragment(Bundle bundle) {
        FragmentManager manager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AddAbsinthFragment fragment = new AddAbsinthFragment();
        fragment.setArguments(bundle);
        transaction
                .replace(R.id.container, fragment, AddAbsinthFragment.class.getCanonicalName())
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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
        return "Absinthes";
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
            return new AbsintheVH(inflater.inflate(R.layout.absinthe_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(AbsintheVH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            System.out.println(Absinth.absinthes.size());
            return Absinth.absinthes.size();
        }

        public class AbsintheVH extends RecyclerView.ViewHolder implements View.OnTouchListener {

            protected GestureDetector detector;

            private Text date;
            private Text volume;
            private Text interval;
            private Text stage;

            private ImageView editIcon;
            private FrameLayout editButton;

            private RelativeLayout mainHolder;
            private FrameLayout deleteButton;
            private ImageView deleteIcon;

            public AbsintheVH(View itemView) {
                super(itemView);
                editIcon = (ImageView) itemView.findViewById(R.id.edit);
                editIcon.setImageDrawable(svg.settings.drawable());
                editButton = (FrameLayout) itemView.findViewById(R.id.setbutton);

                mainHolder = (RelativeLayout) itemView.findViewById(R.id.mainholder);
                mainHolder.setOnTouchListener(this);
                deleteButton = (FrameLayout) itemView.findViewById(R.id.delete);
                deleteIcon = (ImageView) itemView.findViewById(R.id.deleteicon);
                deleteIcon.setImageDrawable(svg.xwhite.drawable());
                date = (Text) itemView.findViewById(R.id.date);
                volume = (Text) itemView.findViewById(R.id.volume);
                interval = (Text) itemView.findViewById(R.id.interval);
                stage = (Text) itemView.findViewById(R.id.stage);
                detector = new GestureDetector(
                        itemView.getContext(),
                        new DeleteGestureDetector(itemView.getContext(), mainHolder, deleteButton)
                );
                mainHolder.bringToFront();
            }

            public void onBind(final int position) {
                final Absinth absinth = Absinth.absinthes.get(position);
                final Date idate = absinth.startInfuseDate();
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = Absinth.absinthes().indexOf(absinth);
                        Absinth.remove(absinth, getContext());
                        AbsintheAdapter.this.notifyDataSetChanged();
                    }
                });

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.edit_absinth_open_for), AddAbsinthFragment.OpenFor.Edit.name());
                        bundle.putInt(getString(R.string.edit_absinth_position), position);
                        showEditAbsintheFragment(bundle);
                    }
                });

                this.date.setText(Time.monthDay(idate) + " " + Time.monthName(idate) + ", " + Time.year(idate));
                volume.setText((!absinth.isDone() ? absinth.spiritVolume() : absinth.resultVolume()) + "l");
                interval.setText(absinth.getInfuseInterval() < 21 ? (absinth.getInfuseInterval() + "d") : ((absinth.getInfuseInterval() / 7) + "w"));
                stage.setText(absinth.stage().toString());
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        }
    }
}
