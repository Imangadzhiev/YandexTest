package timur.imangadjiev.yandextest.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import timur.imangadjiev.yandextest.R;
import timur.imangadjiev.yandextest.SP;
import timur.imangadjiev.yandextest.adapters.favAdapter;
import timur.imangadjiev.yandextest.items.itemHistory;

public class history_fr extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static RecyclerView rv;
    public static favAdapter adapter;
    private static SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_history, container, false);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sp.edit();

        ctx = getActivity();

        rv = (RecyclerView) view.findViewById(R.id.rv);

        EditText etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setHint(getString(R.string.search_in_history));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        refresh();

        return view;
    }

    public static void refresh(){
        Gson gson = new Gson();
        String json = sp.getString(SP.HISTORY, null);
        Type type = new TypeToken<ArrayList<itemHistory>>() {}.getType();
        ArrayList<itemHistory> arrayList;
        if (json == null){
            arrayList = new ArrayList<>();
        } else {
            arrayList = gson.fromJson(json, type);
        }
        if (arrayList.size() > 0){
            Collections.sort(arrayList, new Comparator<itemHistory>() {
                @Override
                public int compare(itemHistory o1, itemHistory o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
            adapter = new favAdapter(ctx, arrayList, "history");
            LinearLayoutManager llm = new LinearLayoutManager(ctx);
            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
        } else {
            arrayList.add(new itemHistory(ctx.getString(R.string.no_history),"","",new Date()));
            adapter = new favAdapter(ctx, arrayList, "no_fav");
            LinearLayoutManager llm = new LinearLayoutManager(ctx);
            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);
            rv.setLayoutManager(llm);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fav_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                editor.putString(SP.HISTORY, null).apply();
                refresh();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
