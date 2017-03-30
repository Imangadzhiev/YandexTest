package timur.imangadjiev.yandextest.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import timur.imangadjiev.yandextest.R;
import timur.imangadjiev.yandextest.SP;
import timur.imangadjiev.yandextest.fragments.translate;

public class chooseLanguage {

    public void getDialog(final Context ctx, final int from){
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        final SharedPreferences.Editor editor = sp.edit();

        LayoutInflater li = LayoutInflater.from(ctx);
        View promptsView = li.inflate(R.layout.dialog_choose_translite, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        if (from == 0) {
            alertDialogBuilder.setTitle(ctx.getResources().getString(R.string.translate_from));
        } else {
            alertDialogBuilder.setTitle(ctx.getResources().getString(R.string.translate_to));
        }
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton(ctx.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();

        ListView listView = (ListView) promptsView.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_single_choice, ctx.getResources().getStringArray(R.array.language_text));
        listView.setAdapter(adapter);

        for (int i=0; i < ctx.getResources().getStringArray(R.array.language_text).length; i++){
            if (from == 0) {
                if (ctx.getResources().getStringArray(R.array.language_text)[i].equals(sp.getString(SP.TRANSLITE_FROM_TEXT, ctx.getResources().getString(R.string.default_translate_from)))) {
                    listView.setItemChecked(i, true);
                    listView.setSelection(i);
                }
            } else {
                if (ctx.getResources().getStringArray(R.array.language_text)[i].equals(sp.getString(SP.TRANSLITE_TO_TEXT, ctx.getResources().getString(R.string.default_translate_to)))) {
                    listView.setItemChecked(i, true);
                    listView.setSelection(i);
                }
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (from == 0) {
                    editor.putString(SP.TRANSLITE_FROM_CODE, ctx.getResources().getStringArray(R.array.language_code)[position]).apply();
                    editor.putString(SP.TRANSLITE_FROM_TEXT, ctx.getResources().getStringArray(R.array.language_text)[position]).apply();
                } else {
                    editor.putString(SP.TRANSLITE_TO_CODE, ctx.getResources().getStringArray(R.array.language_code)[position]).apply();
                    editor.putString(SP.TRANSLITE_TO_TEXT, ctx.getResources().getStringArray(R.array.language_text)[position]).apply();
                }
                translate.setTextFromTo();
                translate.transl(sp.getString(SP.TRANSLITE_FROM_CODE, ctx.getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, ctx.getString(R.string.default_translate_to_code)));
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
