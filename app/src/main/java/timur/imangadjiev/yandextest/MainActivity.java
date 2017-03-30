package timur.imangadjiev.yandextest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;

import timur.imangadjiev.yandextest.adapters.fragmentAdapter;
import timur.imangadjiev.yandextest.fragments.bookmark;
import timur.imangadjiev.yandextest.fragments.fav_fr;
import timur.imangadjiev.yandextest.fragments.history_fr;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;

    void findView(){
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        viewPager.setAdapter(new fragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        TabLayoutHelper mTabLayoutHelper = new TabLayoutHelper(tabLayout, viewPager);
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            int iconId = -1;
            switch (i) {
                case 0:
                    iconId = R.drawable.ic_translate;
                    break;
                case 1:
                    iconId = R.drawable.ic_bookmark;
                    break;
                case 2:
                    iconId = R.drawable.ic_settings;
                    break;
            }
            if (i == 0){
                tabLayout.getTabAt(i).setIcon(iconId);
                tabLayout.getTabAt(i).getIcon().setAlpha(255);
            } else {
                tabLayout.getTabAt(i).setIcon(iconId);
                tabLayout.getTabAt(i).getIcon().setAlpha(60);
            }

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}

        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                tab.getIcon().setAlpha(255);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i != tab.getPosition()) {
                        tabLayout.getTabAt(i).getIcon().setAlpha(60);
                    }
                }
                if (tab.getPosition() == 1){
                    if (bookmark.tabLayout != null){
                        if (bookmark.tabLayout.getSelectedTabPosition() == 0){
                            if (history_fr.adapter != null) history_fr.refresh();
                        }
                        if (bookmark.tabLayout.getSelectedTabPosition() == 1){
                            if (fav_fr.adapter != null) fav_fr.refresh();
                        }
                    }
                }
            }
        });

    }

}
