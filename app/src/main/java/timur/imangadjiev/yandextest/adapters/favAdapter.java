package timur.imangadjiev.yandextest.adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import timur.imangadjiev.yandextest.MainActivity;
import timur.imangadjiev.yandextest.R;
import timur.imangadjiev.yandextest.SP;
import timur.imangadjiev.yandextest.fragments.translate;
import timur.imangadjiev.yandextest.items.itemHistory;

public class favAdapter extends RecyclerView.Adapter<favAdapter.ViewHolder> implements Filterable {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private List<itemHistory> objects = new ArrayList<>();
    private List<itemHistory> tempObjects = new ArrayList<>();
    private List<itemHistory> originalObjects = new ArrayList<>();
    private String from;

    public favAdapter(Context ctx, List<itemHistory> objects, String from){
        sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sp.edit();
        this.objects = objects;
        this.originalObjects = objects;
        this.from = from;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        TextView tvTranslateText;
        TextView tvLang;
        ImageView ivFav;

        ViewHolder(View v) {
            super(v);
            tvText = (TextView) v.findViewById(R.id.tvText);
            tvTranslateText = (TextView) v.findViewById(R.id.tvTranslateText);
            tvLang = (TextView) v.findViewById(R.id.tvLang);
            ivFav = (ImageView) v.findViewById(R.id.ivBookmark);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, parent, false);
        return new favAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (from.equals("no_fav")){
            holder.tvTranslateText.setVisibility(View.GONE);
            holder.tvLang.setVisibility(View.GONE);
            holder.ivFav.setVisibility(View.GONE);
            holder.tvText.setText(objects.get(position).getText());
        } else {
            holder.tvText.setText(objects.get(position).getText());
            holder.tvTranslateText.setText(objects.get(position).getTranslate_text());
            holder.tvLang.setText(objects.get(position).getLang().toUpperCase());

            if (isFavText(objects.get(position).getText())) {
                holder.ivFav.setImageResource(R.drawable.ic_bookmark_yellow_24dp);
            } else {
                holder.ivFav.setImageResource(R.drawable.ic_bookmark_black_24dp);
            }

            holder.ivFav.setTag(objects.get(position));
            holder.ivFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemHistory item = (itemHistory) v.getTag();
                    if (isFavText(item.getText())) {
                        if (from.equals("fav")) {
                            checkFav(item, holder.ivFav);
                            objects.remove(item);
                            notifyDataSetChanged();
                        } else {
                            checkFav(item, holder.ivFav);
                        }
                    } else {
                        checkFav(item, holder.ivFav);
                    }
                }
            });
        }
        holder.itemView.setTag(objects.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemHistory item = (itemHistory) v.getTag();
                if (MainActivity.tabLayout != null){
                    MainActivity.tabLayout.getTabAt(0).select();
                    translate.etText.setText(item.getText());
                    translate.transl(item.getLang());
                    translate.getDictionary(item.getLang());
                    translate.setTextFromTo();
                }
            }
        });
    }

    private boolean isFavText(String text){
        boolean ischeck = false;
        Gson gson = new Gson();
        String json = sp.getString(SP.FAVORITES,null);
        Type type = new TypeToken<ArrayList<itemHistory>>() {}.getType();
        ArrayList<itemHistory> arrayList;
        if (json == null) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = gson.fromJson(json, type);
        }
        if (arrayList.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getText().equals(text)) {
                    ischeck = true;
                }
            }
        } else {
            ischeck = false;
        }
        return ischeck;
    }

    private void checkFav(itemHistory item, ImageView ivBookmark){
        boolean isFav = false;
        Gson gson = new Gson();
        String json = sp.getString(SP.FAVORITES, null);
        Type type = new TypeToken<ArrayList<itemHistory>>() {}.getType();
        ArrayList<itemHistory> arrayList;
        if (json == null){
            arrayList = new ArrayList<>();
        } else {
            arrayList = gson.fromJson(json, type);
        }
        if (arrayList.size() != 0){
            for (int i=0; i < arrayList.size(); i++){
                if (arrayList.get(i).getText().equals(item.getText())){
                    isFav = true;
                    arrayList.remove(i);
                }
            }
        } else {
            isFav = false;
        }

        if (isFav){
            ivBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
        } else {
            ivBookmark.setImageResource(R.drawable.ic_bookmark_yellow_24dp);
            arrayList.add(item);
        }
        String toJson = gson.toJson(arrayList);
        editor.putString(SP.FAVORITES, toJson).apply();
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                tempObjects = new ArrayList<>();

                if(constraint == null || constraint.length() == 0 ){
                    tempObjects = new ArrayList<>(originalObjects);
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i=0; i < originalObjects.size(); i++){
                        String data = originalObjects.get(i).getText().toLowerCase();
                        if(data.contains(constraint)){
                            tempObjects.add(originalObjects.get(i));
                        }
                    }
                }
                objects = new ArrayList<>(tempObjects);
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

}
