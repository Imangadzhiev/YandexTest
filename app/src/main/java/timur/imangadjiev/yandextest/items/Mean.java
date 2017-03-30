
package timur.imangadjiev.yandextest.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mean {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

}
