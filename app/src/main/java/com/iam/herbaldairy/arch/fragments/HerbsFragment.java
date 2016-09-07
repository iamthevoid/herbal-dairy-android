package com.iam.herbaldairy.arch.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iam.herbaldairy.R;
import com.iam.herbaldairy.arch.db.DBCache;
import com.iam.herbaldairy.arch.root.DrawerActivity;
import com.iam.herbaldairy.entities.HerbOwned;
import com.iam.herbaldairy.util.WikiSearch;
import com.iam.herbaldairy.widget.AddHerbDialog;
import com.iam.herbaldairy.widget.Decorator;
import com.iam.herbaldairy.widget.Divider;
import com.iam.herbaldairy.widget.Header;
import com.iam.herbaldairy.widget.assets.svg;
import com.iam.herbaldairy.widget.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HerbsFragment extends Fragment implements Header.HeaderManipulation, AddHerbDialog.HasDataToReload {

    private WikiSearchAdapter adapter;
    private RecyclerView view;
    private HerbListAdapter listAdapter;
    private com.iam.herbaldairy.widget.LinearLayoutManager manager;
    private AddHerbDialog dialog;
    private AddHerbDialog.Container container;
    private String[] herbs;
    private Context context;
    private SearchTextWatcher textWatcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (RecyclerView) inflater.inflate(R.layout.recycler_fragment, container, false);
        view.addItemDecoration(new Divider(getContext(), R.drawable.gray_divider, new int[]{}, new int[]{0,0}));
        dialog = new AddHerbDialog(getActivity());

        context = getActivity();

        this.container = ((AddHerbDialog.Container)getActivity());

        herbs = getActivity().getResources().getStringArray(R.array.herbs);


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
        List<HerbOwned> list;

        HerbListAdapter(Context context) {

            inflater = ((AppCompatActivity)context).getLayoutInflater();
            list = DBCache.getInstance().userHerbs().list();
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
                HerbOwned herb = list.get(position);
                title.setText(herb.name());
                latin.setText(herb.latinName());
                weight.setText(herb.weight() + "g");
                volume.setText(herb.volumeString() + "l");
                type.setText(herb.typedHerb().typeString());
                Log.d("imageUrl", herb.typedHerb().herb().imageURL() + "fd");
                Glide.with(getContext())
                        .load(herb.typedHerb().herb().imageURL())
                        .centerCrop()
                        .into(image);
                settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HerbEditFragment fragment = new HerbEditFragment();
                        fragment.setHerb(list.get(position));
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
    public void onSave(HerbOwned herb) {
        int position = DBCache.getInstance().userHerbs().list().indexOf(herb);
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
                if (textWatcher == null) {
                    textWatcher = new SearchTextWatcher();
                    ((Header.SearchInHeader)getActivity()).setOnTextListener(textWatcher);
                }
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

    private class WikiSearchAdapter extends ArrayAdapter<String> {

        private ArrayList<WikiSearch> list;
        private LayoutInflater inflater;
        private int resource;

        public WikiSearchAdapter(Context context, int resource, ArrayList<WikiSearch> list) {
            super(context, resource);
            this.list = list;
            this.resource = resource;
            inflater = ((AppCompatActivity)context).getLayoutInflater();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(resource, parent, false);
            }

            ((Text)view).setText(list.get(position).name());
            return view;
        }
    }

    class SearchTextWatcher implements TextWatcher {

        private AsyncTask<String, Void, String> searchTask;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (searchTask != null && searchTask.getStatus() == AsyncTask.Status.RUNNING) {
                searchTask.cancel(true);
                searchTask = null;
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() != 0) {
                searchTask = new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        return WikiSearch.search(strings[0]);
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        final ArrayList<WikiSearch> results = WikiSearch.results(s);
                        final ListPopupWindow addHerbLpw = new ListPopupWindow(getActivity());
                        addHerbLpw.setWidth(Decorator.getScreenWidth());
                        addHerbLpw.setAnchorView(((DrawerActivity) context).getHeader());
                        addHerbLpw.setModal(true);

                        System.out.println(results.size());
                        if (results.size() > 0) {
                            System.out.println(context + " " + R.layout.popup_item + " " + results);
                            addHerbLpw.setAdapter(null);
                            final WikiSearchAdapter adapter = new WikiSearchAdapter(
                                    context,
                                    R.layout.popup_item,
                                    results
                            );
                            System.out.println(adapter.getCount());
                            addHerbLpw.setAdapter(adapter);
                            addHerbLpw.setHeight((int) (Decorator.getpixels(36)) * results.size());
                            addHerbLpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    final String herbName = results.get(i).name();
                                    if (DBCache.getInstance().userHerbs().containsHerbName(herbName)) {
                                        dialog.setHerb(DBCache.getInstance().userHerbs().herbByName(herbName), AddHerbDialog.From.HerbFragmentByHerb);
                                    }
                                    dialog.setHerb(herbName, AddHerbDialog.From.HerbFragmentByName);
                                    dialog.setDataReloader(HerbsFragment.this);
                                    Log.d("onItemClick", herbName);
                                    container.addView(dialog);
                                    addHerbLpw.dismiss();
                                }
                            });
                            addHerbLpw.show();
                        } else if (((Header.SearchInHeader) context).getSearch().getText().length() >= 3) {
                            Toast.makeText(context, "Ничего не найдено", Toast.LENGTH_SHORT).show();
                        }
                        requestEditTextFocus();

                    }

                    private void requestEditTextFocus() {
                        final EditText search = ((Header.SearchInHeader) context).getSearch();
                        search.post(new Runnable() {
                            public void run() {
                                search.requestFocusFromTouch();
                                InputMethodManager lManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                lManager.showSoftInput(search, 0);
                            }
                        });
                    }
                };
                searchTask.execute(editable.toString());
            }
        }
    };
}
