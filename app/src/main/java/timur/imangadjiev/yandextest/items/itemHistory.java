package timur.imangadjiev.yandextest.items;

import java.util.Date;

public class itemHistory {

    private String text;
    private String translate_text;
    private String lang;
    private Date date;

    public itemHistory(String text, String translate_text, String lang, Date date){
        super();
        this.text = text;
        this.translate_text = translate_text;
        this.lang = lang;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getTranslate_text() {
        return translate_text;
    }

    public String getLang() {
        return lang;
    }

    public Date getDate() {
        return date;
    }
}
