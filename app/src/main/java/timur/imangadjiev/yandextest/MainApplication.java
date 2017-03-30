package timur.imangadjiev.yandextest;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {

    private static APITranslate apiTranslate;
    private static APIDictionary apiDictionary;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofitTr = new Retrofit.Builder()
                .baseUrl(config.URL_API_TRANSLATE) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        Retrofit retrofitDict = new Retrofit.Builder()
                .baseUrl(config.URL_API_DICT) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        apiTranslate = retrofitTr.create(APITranslate.class);
        apiDictionary = retrofitDict.create(APIDictionary.class);
    }

    public static APITranslate getApiTranslate(){
        return apiTranslate;
    }

    public static APIDictionary getApiDictionary(){
        return apiDictionary;
    }

}
