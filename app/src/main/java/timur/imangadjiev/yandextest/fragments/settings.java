package timur.imangadjiev.yandextest.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import timur.imangadjiev.yandextest.R;
import timur.imangadjiev.yandextest.SP;

public class settings extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sp.edit();

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        if (sp.getString(SP.LANGUAGE_INTERFACE, getString(R.string.default_translate_from_code)).equals("ru")){
            radioGroup.check(R.id.rbRussian);
        } else {
            radioGroup.check(R.id.rbEnglish);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rbRussian){
                    editor.putString(SP.LANGUAGE_INTERFACE, "ru").apply();
                } else {
                    editor.putString(SP.LANGUAGE_INTERFACE, "en").apply();
                }
            }
        });

        return view;
    }

}
