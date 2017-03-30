package timur.imangadjiev.yandextest.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Recognition;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.RecognizerListener;
import ru.yandex.speechkit.SpeechKit;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;
import timur.imangadjiev.yandextest.MainApplication;
import timur.imangadjiev.yandextest.R;
import timur.imangadjiev.yandextest.SP;
import timur.imangadjiev.yandextest.adapters.dictAdapter;
import timur.imangadjiev.yandextest.config;
import timur.imangadjiev.yandextest.dialogs.chooseLanguage;
import timur.imangadjiev.yandextest.items.itemDict;
import timur.imangadjiev.yandextest.items.itemHistory;
import timur.imangadjiev.yandextest.items.itemTranslate;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class translate extends Fragment implements RecognizerListener, VocalizerListener {

    @SuppressLint("StaticFieldLeak")
    public static EditText etText;
    private ImageView ivClear;
    private ImageView ivMic;
    private ImageView ivSound;
    private ImageView ivSound2;
    private ImageView ivBookmark;
    @SuppressLint("StaticFieldLeak")
    private static TextView tvTranslateText;
    @SuppressLint("StaticFieldLeak")
    private static ListView listView;

    @SuppressLint("StaticFieldLeak")
    private static TextView tvTransliteFrom;
    @SuppressLint("StaticFieldLeak")
    private static TextView tvTransliteTo;

    private static SharedPreferences sp;
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;
    private static SharedPreferences.Editor editor;

    private Vocalizer vocalizer;
    private Recognizer recognizer;
    private static final int REQUEST_PERMISSION_CODE = 1;
    private int idSound;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SpeechKit.getInstance().configure(getContext(), config.API_KEY_SPEACH);
    }

    private void resetRecognizer() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer = null;
        }
    }

    private void resetVocalizer() {
        if (vocalizer != null) {
            vocalizer.cancel();
            vocalizer = null;
        }
    }

    private void createAndStartRecognizer() {
        final Context context = getContext();
        if (context == null) {
            return;
        }

        if (ContextCompat.checkSelfPermission(context, RECORD_AUDIO) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
        } else {
            resetRecognizer();
            recognizer = Recognizer.create(Recognizer.Language.RUSSIAN, Recognizer.Model.NOTES, translate.this);
            recognizer.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {
            createAndStartRecognizer();
        } else {
            Toast.makeText(getActivity(), "Record audio permission was not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        resetRecognizer();
        resetVocalizer();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sp.edit();
        ctx = getActivity();

        etText = (EditText) view.findViewById(R.id.etText);
        ivClear = (ImageView) view.findViewById(R.id.ivClear);
        ivMic = (ImageView) view.findViewById(R.id.ivMic);
        ivSound = (ImageView) view.findViewById(R.id.ivSound);
        ivSound2 = (ImageView) view.findViewById(R.id.ivSound2);
        ivBookmark = (ImageView) view.findViewById(R.id.ivBookmark);
        ImageView ivReverte = (ImageView) view.findViewById(R.id.ivRevert);
        tvTranslateText = (TextView) view.findViewById(R.id.tvTranslateText);
        tvTransliteFrom = (TextView) view.findViewById(R.id.tvTranslateFrom);
        tvTransliteTo = (TextView) view.findViewById(R.id.tvTranslateTo);
        listView = (ListView) view.findViewById(R.id.listView);

        ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (arrayList.get(i).getText().equals(etText.getText().toString())){
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
                    String lang = sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code));
                    arrayList.add(new itemHistory(etText.getText().toString(), tvTranslateText.getText().toString(), lang, new Date()));
                }
                String toJson = gson.toJson(arrayList);
                editor.putString(SP.FAVORITES, toJson).apply();
            }
        });

        ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndStartRecognizer();
            }
        });

        ivSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etText.getText().toString().length() > 0 && !etText.getText().toString().isEmpty()){
                    resetVocalizer();
                    switch (sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code))){
                        case "ru":
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, etText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                        case "uk":
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.UKRAINIAN, etText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                        case "tr":
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.TURKISH, etText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                        default:
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.ENGLISH, etText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                    }
                    idSound = 1;
                    vocalizer.start();
                }
            }
        });

        ivSound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTranslateText.getText() != null && !tvTranslateText.getText().toString().isEmpty()){
                    resetVocalizer();
                    switch (sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code))){
                        case "ru":
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, tvTranslateText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                        case "uk":
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.UKRAINIAN, tvTranslateText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                        case "tr":
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.TURKISH, tvTranslateText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                        default:
                            vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.ENGLISH, tvTranslateText.getText().toString(), true, Vocalizer.Voice.ALYSS);
                            break;
                    }
                    idSound = 2;
                    vocalizer.start();
                }
            }
        });

        tvTransliteFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new chooseLanguage().getDialog(getActivity(),0);
            }
        });

        tvTransliteTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new chooseLanguage().getDialog(getActivity(),1);
            }
        });

        setTextFromTo();

        ivReverte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = sp.getString(SP.TRANSLITE_FROM_TEXT, getString(R.string.default_translate_from));
                String to = sp.getString(SP.TRANSLITE_TO_TEXT, getString(R.string.default_translate_to));
                String from_code = sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code));
                String to_code = sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code));
                editor.putString(SP.TRANSLITE_FROM_TEXT, to).apply();
                editor.putString(SP.TRANSLITE_TO_TEXT, from).apply();
                editor.putString(SP.TRANSLITE_FROM_CODE, to_code).apply();
                editor.putString(SP.TRANSLITE_TO_CODE, from_code).apply();
                transl(sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code)));
                getDictionary(sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code)));
                if (isFavText()){
                    ivBookmark.setImageResource(R.drawable.ic_bookmark_yellow_24dp);
                } else {
                    ivBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                }
                setTextFromTo();
            }
        });

        textEmpty();

        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etText.setText("");
                textEmpty();
            }
        });

        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etText.getText().toString().isEmpty() && etText.getText().toString().length() > 0){
                    if (isFavText()){
                        ivBookmark.setImageResource(R.drawable.ic_bookmark_yellow_24dp);
                    } else {
                        ivBookmark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }

                    if (!isVisibleButtons()) {
                        textIs();
                    }
                    transl(sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code)));
                    getDictionary(sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code)));
                } else {
                    if (isVisibleButtons()) {
                        textEmpty();
                    }
                }
            }
        });

        return view;
    }

    static void setToHistory(itemHistory item){
        Gson gson = new Gson();
        String json = sp.getString(SP.HISTORY, null);
        Type type = new TypeToken<ArrayList<itemHistory>>() {}.getType();
        ArrayList<itemHistory> arrayList;
        if (json == null) {
            arrayList = new ArrayList<>();
        } else {
            arrayList = gson.fromJson(json, type);
        }
        if (arrayList.size() != 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (String.valueOf(arrayList.get(i).getText()).contains(item.getText())) {
                    arrayList.remove(i);
                }
            }
        }

        arrayList.add(item);

        String toJson = gson.toJson(arrayList);
        editor.putString(SP.HISTORY,toJson).apply();
    }

    private boolean isFavText(){
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
                if (arrayList.get(i).getText().equals(etText.getText().toString())) {
                    ischeck = true;
                }
            }
        } else {
            ischeck = false;
        }
        return ischeck;
    }

    private boolean isVisibleButtons(){
        return tvTranslateText.getVisibility() != View.GONE;
    }

    private void textIs(){
        AlphaAnimation alpha = new AlphaAnimation(0.5f, 1.0f);
        alpha.setDuration(1000);
        alpha.setFillAfter(true);

        ivClear.startAnimation(alpha);
        ivSound.startAnimation(alpha);

        tvTranslateText.setVisibility(View.VISIBLE);
        ivBookmark.setVisibility(View.VISIBLE);
        ivSound2.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    private void textEmpty(){
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.5f);
        alpha.setDuration(1000);
        alpha.setFillAfter(true);

        ivClear.startAnimation(alpha);
        ivSound.startAnimation(alpha);

        tvTranslateText.setVisibility(View.GONE);
        ivBookmark.setVisibility(View.GONE);
        ivSound2.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    public static void setTextFromTo(){
        tvTransliteFrom.setText(sp.getString(SP.TRANSLITE_FROM_TEXT, ctx.getString(R.string.default_translate_from)));
        tvTransliteTo.setText(sp.getString(SP.TRANSLITE_TO_TEXT, ctx.getString(R.string.default_translate_to)));
    }

    public static void transl(final String lang){
        if (!etText.getText().toString().isEmpty() && etText.getText().toString().length() > 0) {
            MainApplication.getApiTranslate().getData(
                    config.API_KEY_TRANSLATE,
                    etText.getText().toString(),
                    lang)
                    .enqueue(new Callback<itemTranslate>() {
                        @Override
                        public void onResponse(Call<itemTranslate> call, Response<itemTranslate> response) {
                            if (response.body() != null) {
                                String translate_text = "";
                                for (String s : response.body().getText()) {
                                    translate_text += s + "\n";
                                }
                                tvTranslateText.setText(translate_text);
                                setToHistory(new itemHistory(etText.getText().toString(), translate_text, lang, new Date()));
                            }
                        }

                        @Override
                        public void onFailure(Call<itemTranslate> call, Throwable t) {
                            tvTranslateText.setText(ctx.getString(R.string.error_translate) + "\n" + t.getMessage());
                        }
                    });
        }
    }

    public static void getDictionary(final String lang){
        if (!etText.getText().toString().isEmpty() && etText.getText().toString().length() > 0) {
            MainApplication.getApiDictionary().getData(
                    config.API_KEY_DICT,
                    lang,
                    etText.getText().toString(),
                    sp.getString(SP.LANGUAGE_INTERFACE, ctx.getString(R.string.default_translate_from_code))
            )
                    .enqueue(new Callback<itemDict>() {
                        @Override
                        public void onResponse(Call<itemDict> call, Response<itemDict> response) {
                            if (response.body() != null){
                                if (response.body().getDef() != null) {
                                    if (response.body().getDef().size() != 0) {
                                        dictAdapter dAdapter = new dictAdapter(ctx, response.body().getDef());
                                        listView.setAdapter(dAdapter);
                                    } else {
                                        listView.setAdapter(null);
                                    }
                                } else {
                                    listView.setAdapter(null);
                                }
                            } else {
                                listView.setAdapter(null);
                            }
                        }

                        @Override
                        public void onFailure(Call<itemDict> call, Throwable t) {
                            tvTranslateText.setText(tvTranslateText.getText().toString() + "\n" + ctx.getString(R.string.error_dict) + "\n" + t.getMessage());
                        }
                    });
        }
    }

    public static void setText(String text){
        etText.setText(text);
        String from = sp.getString(SP.TRANSLITE_FROM_TEXT, ctx.getString(R.string.default_translate_from));
        String to = sp.getString(SP.TRANSLITE_TO_TEXT, ctx.getString(R.string.default_translate_to));
        String from_code = sp.getString(SP.TRANSLITE_FROM_CODE, ctx.getString(R.string.default_translate_from_code));
        String to_code = sp.getString(SP.TRANSLITE_TO_CODE, ctx.getString(R.string.default_translate_to_code));
        editor.putString(SP.TRANSLITE_FROM_TEXT, to).apply();
        editor.putString(SP.TRANSLITE_TO_TEXT, from).apply();
        editor.putString(SP.TRANSLITE_FROM_CODE, to_code).apply();
        editor.putString(SP.TRANSLITE_TO_CODE, from_code).apply();
        transl(sp.getString(SP.TRANSLITE_FROM_CODE, ctx.getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, ctx.getString(R.string.default_translate_to_code)));
        getDictionary(sp.getString(SP.TRANSLITE_FROM_CODE, ctx.getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, ctx.getString(R.string.default_translate_to_code)));
        setTextFromTo();
    }

    @Override
    public void onRecordingBegin(Recognizer recognizer) {
        ivMic.setImageResource(R.drawable.ic_mic_pressed);
    }

    @Override
    public void onSpeechDetected(Recognizer recognizer) {

    }

    @Override
    public void onSpeechEnds(Recognizer recognizer) {

    }

    @Override
    public void onRecordingDone(Recognizer recognizer) {
        ivMic.setImageResource(R.drawable.ic_mic);
    }

    @Override
    public void onSoundDataRecorded(Recognizer recognizer, byte[] bytes) {

    }

    @Override
    public void onPowerUpdated(Recognizer recognizer, float v) {

    }

    @Override
    public void onPartialResults(Recognizer recognizer, Recognition recognition, boolean b) {

    }

    @Override
    public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
        etText.setText(recognition.getBestResultText());
        transl(sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code)));
        getDictionary(sp.getString(SP.TRANSLITE_FROM_CODE, getString(R.string.default_translate_from_code)) + "-" + sp.getString(SP.TRANSLITE_TO_CODE, getString(R.string.default_translate_to_code)));
    }

    @Override
    public void onError(Recognizer recognizer, Error error) {
        Toast.makeText(getActivity(), error.getString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSynthesisBegin(Vocalizer vocalizer) {

    }

    @Override
    public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {

    }

    @Override
    public void onPlayingBegin(Vocalizer vocalizer) {
        if (idSound == 1){
            ivSound.setImageResource(R.drawable.ic_sound_pressed);
        } else {
            ivSound2.setImageResource(R.drawable.ic_sound_pressed);
        }
    }

    @Override
    public void onPlayingDone(Vocalizer vocalizer) {
        if (idSound == 1){
            ivSound.setImageResource(R.drawable.ic_sound);
        } else {
            ivSound2.setImageResource(R.drawable.ic_sound);
        }
    }

    @Override
    public void onVocalizerError(Vocalizer vocalizer, Error error) {
        Toast.makeText(getActivity(), error.getString(), Toast.LENGTH_LONG).show();
    }
}