package com.iam.herbaldairy.arch.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iam.herbaldairy.Editor;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.Time;
import com.iam.herbaldairy.entities.Absinth;
import com.iam.herbaldairy.entities.Herb;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.AddHerbDialog;
import com.iam.herbaldairy.widget.LinearLayoutManager;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AddAbsinthFragment extends Fragment implements Header.HeaderManipulation, AddHerbDialog.HasDataToReload {

    OpenFor openFor;
    RecyclerView view;
    AddAbsintheAdapter adapter;
    LinearLayoutManager manager;
    ArrayList<Herb> herbs = new ArrayList<>();
    ArrayList<Herb> newHerbs = new ArrayList<>();
    AddHerbDialog.Container container;

    ListPopupWindow listPopupWindow;
    ArrayAdapter<String> listPopupAdapter;
    AddHerbDialog dialog;
    String[] herbNames;
    String[] betterHerbsNames;

    Absinth absinth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "onCreate");
        openFor = OpenFor.valueOf(getArguments().getString(getString(R.string.edit_absinth_open_for)));
        if (openFor == OpenFor.Edit) {
            int position = getArguments().getInt(getString(R.string.edit_absinth_position));
            absinth = Absinth.absinthes().get(position);
            herbs = new ArrayList<>();
            for (Herb herb : absinth.herbs()) {
                Herb newHerb = new Herb(herb);
                newHerb.add(herb.weight());
                herbs.add(newHerb);
            }
            newHerbs = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.recycler_fragment, container, false);

        configurePopup();

        adapter = new AddAbsintheAdapter();
        manager = new LinearLayoutManager(getContext());
        view.setAdapter(adapter);
        view.setLayoutManager(manager);

        this.container = ((AddHerbDialog.Container)getActivity());
        ((Header.FragmentDataSender)getActivity()).onFragmentOpen(this);
        return view;
    }

    @Override
    public svg leftIcon() {
        return svg.back;
    }

    @Override
    public View.OnClickListener leftIconAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popBackStack();
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

                AddAbsintheAdapter.MainVH holder = adapter.data();
                Log.d("holder", holder + "");
                if (holder.spiritVolumeET.getText().toString().length() != 0 &&
                    holder.startDate() != null &&
                    herbs.size() > 0) {
                    final double spiritVolume = Double.parseDouble(holder.spiritVolumeET.getText().toString());
                    if (absinth == null) {
                        absinth = new Absinth(holder.startDate(), spiritVolume, herbs);
                    } else {
                        absinth.setSpiritVolume(spiritVolume);
                        absinth.setStartDate(holder.startDate());
                    }
                    final String sAlcPercent = holder.alcPercentET.getText().toString();
                    absinth.setAlcPercent(Integer.parseInt(sAlcPercent.length() == 0 ? "0" : sAlcPercent));
                    absinth.setComment(holder.commentET.getText().toString());
                    absinth.setDistillDate(holder.distillDate);
                    final String distillTemp = holder.distillTemperatureET.getText().toString();
                    absinth.setDistillTemperature(Integer.parseInt(distillTemp.length() == 0 ? "0" : distillTemp));
                    final String sResultVolume = holder.resultVolumeET.getText().toString();
                    absinth.setResultVolume(Double.parseDouble(sResultVolume.length() == 0 ? "0" : sResultVolume));
                    if (openFor == OpenFor.Add) {
                        Absinth.add(absinth);
                    } else {
                        absinth.validateHerbs(herbs, newHerbs);
                    }

                    Absinth.writeToPreferences(getContext());

                    popBackStack();
                } else {
                    Toast
                            .makeText(getContext(),
                                    "Убедитесь, что заполнены поля SPIRIT VOLUME," +
                                            " START DATE, и что добавлен хотя бы один компонент",
                                    Toast.LENGTH_LONG)
                            .show();
                }
            }
        };
    }

    private void popBackStack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction
                .replace(R.id.container, new AbsinthesFragment(), AbsinthesFragment.class.getCanonicalName())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
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
        return "Edit Absinthe";
    }

    @Override
    public String headerSubtitle() {
        return null;
    }

    @Override
    public void onSave(Herb herb) {
        if (!this.herbs.contains(herb)) {
            this.herbs.add(herb);
        } else {
            this.herbs.get(this.herbs.indexOf(herb)).add(herb.weight());
        }

        if (openFor == OpenFor.Edit) newHerbs.add(herb);
        onClose();
    }

    @Override
    public void onClose() {
        adapter.notifyDataSetChanged();
    }

    private class AddAbsintheAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        LayoutInflater inflater;
        MainVH dataVH;

        public AddAbsintheAdapter() {
            inflater = getActivity().getLayoutInflater();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case MAIN:
                    dataVH = dataVH == null ? new MainVH(inflater.inflate(R.layout.absinthe_edit_main_info, parent, false)) : dataVH;
                    return dataVH;
                case ADD:
                    return new AddVH(inflater.inflate(R.layout.herb_add_item, parent, false));
                case HERB:
                    return new HerbVH(inflater.inflate(R.layout.herb_list_item, parent, false));
            }
            throw new RuntimeException("Unknown viewHoldr type " + viewType);
        }

        public MainVH data() {
            return dataVH;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d("onBind", position + "");
            Log.d("onBind", isFooter(position) + "");
            Log.d("onBind", isHeader(position) + "");
            Log.d("onBind", herbs.size() + "");
            if (!isFooter(position) && !isHeader(position)) {
                ((HerbVH)holder).onBind(herbs.get(position-1));
            }
        }

        @Override
        public int getItemCount() {
            return herbs.size() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            return isHeader(position) ? MAIN : (isFooter(position) ? ADD : HERB);
        }

        public boolean isHeader(int position) {
            return position == 0;
        }

        public boolean isFooter(int position) {
            return position == herbs.size() + 1;
        }

        class HerbVH extends RecyclerView.ViewHolder {

            ImageView edit;
            Text title;
            Text latin;
            Text weight;
            Text volume;
            Text type;
            ImageView image;
            FrameLayout settingsButton;

            HerbVH(View view) {
                super(view);
                edit = (ImageView) view.findViewById(R.id.edit);
                edit.setImageDrawable(svg.settings.drawable());
                settingsButton = (FrameLayout) view.findViewById(R.id.setbutton);
                title = (Text) view.findViewById(R.id.title);
                latin = (Text) view.findViewById(R.id.latin);
                weight = (Text) view.findViewById(R.id.weight);
                volume = (Text) view.findViewById(R.id.volume);
                type = (Text) view.findViewById(R.id.type);
                image = (ImageView) view.findViewById(R.id.image);
            }

            void onBind(final Herb herb) {
                title.setText(herb.name());
                latin.setText(herb.latin());
                weight.setText(herb.weight() + "g.");
                volume.setText(herb.volumeString() + "l");
                type.setText(herb.typeString());
                Glide.with(getContext())
                        .load(herb.imageURL())
                        .centerCrop()
                        .into(image);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HerbEditFragment fragment = new HerbEditFragment();
                        fragment.setHerb(herb);
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

        public class MainVH extends RecyclerView.ViewHolder {

            EditText startDateET;
            EditText distilldateET;
            EditText spiritVolumeET;
            EditText resultVolumeET;
            EditText alcPercentET;
            EditText distillTemperatureET;
            EditText commentET;
            Button setddnow;
            Button setsdnow;
            View view;

            Date startDate;
            Date distillDate;

            public Date startDate() {
                try {
                    return Time.simpleDateFormat.parse(startDateET.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("AddAbsintheFragment.AddAbsintheAdapter.MainHolder.startDate() - date don't match format");
            }

            public MainVH(View itemView) {
                super(itemView);
                view = itemView;
                startDateET = (EditText) itemView.findViewById(R.id.startdate);
                distilldateET = (EditText) itemView.findViewById(R.id.distilldate);
                spiritVolumeET = (EditText) itemView.findViewById(R.id.spiritvolume);
                resultVolumeET = (EditText) itemView.findViewById(R.id.resultvolume);
                alcPercentET = (EditText) itemView.findViewById(R.id.alcpercent);
                alcPercentET.addTextChangedListener(Editor.integerTextWatcher(alcPercentET, 2));
                distillTemperatureET = (EditText) itemView.findViewById(R.id.distillTemperature);
                distillTemperatureET.addTextChangedListener(Editor.integerTextWatcher(distillTemperatureET, 3));
                commentET = (EditText) itemView.findViewById(R.id.comment);
                setddnow = (Button) itemView.findViewById(R.id.setDistillTime);
                setddnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        distillDate = new Date();
                        distilldateET.setText(Time.simpleDateFormat.format(distillDate));
                    }
                });
                setsdnow = (Button) itemView.findViewById(R.id.setStartTime);
                setsdnow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startDate = new Date();
                        startDateET.setText(Time.simpleDateFormat.format(startDate));
                    }
                });

                if (openFor == OpenFor.Edit) {
                    startDateET.setText(Time.simpleDateFormat.format(absinth.startInfuseDate()));
                    final Date dd = absinth.distillDate();
                    distilldateET.setText(dd == null ? "" : Time.simpleDateFormat.format(dd));
                    spiritVolumeET.setText(absinth.spiritVolume() + "");
                    resultVolumeET.setText(absinth.resultVolume() + "");
                    alcPercentET.setText(absinth.alcPercent() + "");
                    distillTemperatureET.setText(absinth.distillTemperature() + "");
                    commentET.setText(absinth.comment());
                }
            }
        }

        private class AddVH extends RecyclerView.ViewHolder {

            public AddVH(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listPopupWindow.setAdapter(listPopupAdapter);
                        listPopupWindow.setWidth(400);
                        listPopupWindow.setHeight(500);
                        listPopupWindow.setAnchorView(view);
                        listPopupWindow.setModal(true);
                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (Herb.containsHerbName(herbNames[i])) {
                                    dialog.setHerb(Herb.herbByName(herbNames[i]), AddHerbDialog.From.AddAbsintheFragmentByName);
                                }
                                dialog.setHerb(herbNames[i], AddHerbDialog.From.AddAbsintheFragmentByName);
                                dialog.setDataReloader(AddAbsinthFragment.this);
                                container.addView(dialog);
                                listPopupWindow.dismiss();
                            }
                        });
                        listPopupWindow.show();
                    }
                });
            }
        }

        private final int MAIN = 0;
        private final int HERB = 1;
        private final int ADD = 2;
    }

    private void configurePopup() {
        herbNames = getResources().getStringArray(R.array.herbs);
        betterHerbsNames = new String[herbNames.length];
        for (int i = 0, l = herbNames.length; i < l; i++) {
            betterHerbsNames[i] = herbNames[i] + " " + Herb.volumesStringForHerbs(Herb.herbsByName(herbNames[i]));
        }
        dialog = new AddHerbDialog(getActivity());
        listPopupAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.popup_item,
                betterHerbsNames
        );

        listPopupWindow = new ListPopupWindow(getActivity());
    }

    enum OpenFor {
        Edit,
        Add
    }
}
