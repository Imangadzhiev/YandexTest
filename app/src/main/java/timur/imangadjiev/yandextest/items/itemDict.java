
package timur.imangadjiev.yandextest.items;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class itemDict {

    @SerializedName("def")
    @Expose
    private List<Def> def = null;

    public List<Def> getDef() {
        return def;
    }

}
