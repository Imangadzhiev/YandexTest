package timur.imangadjiev.yandextest;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import timur.imangadjiev.yandextest.items.itemTranslate;

public interface APITranslate {
    @POST("/api/v1.5/tr.json/translate")
    Call<itemTranslate> getData(@Query("key") String apiKey, @Query("text") String text, @Query("lang") String lang);
}