package timur.imangadjiev.yandextest.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import timur.imangadjiev.yandextest.fragments.bookmark;
import timur.imangadjiev.yandextest.fragments.settings;
import timur.imangadjiev.yandextest.fragments.translate;

public class fragmentAdapter extends FragmentPagerAdapter {

    private Fragment translate;
    private Fragment bookmark;
    private Fragment settings;

    public fragmentAdapter(FragmentManager fm) {
        super(fm);
        translate = new translate();
        bookmark = new bookmark();
        settings = new settings();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return translate;
            case 1:
                return bookmark;
            case 2:
                return settings;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
