package timur.imangadjiev.yandextest;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import timur.imangadjiev.yandextest.items.itemDict;

public interface APIDictionary {
    @POST("/api/v1/dicservice.json/lookup")
    Call<itemDict> getData(@Query("key") String apiKey, @Query("lang") String lang, @Query("text") String text, @Query("ui") String ui);
}