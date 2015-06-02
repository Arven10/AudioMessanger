package ua.kharkiv.nure.dorozhan.musicmessanger.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ua.kharkiv.nure.dorozhan.musicmessanger.R;
import ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs.SlidingTabLayout;
import ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs.ViewPagerAdapter;

/**
 * Created by Dorozhan on 02.06.2015.
 */
public class MultiWindowActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mPager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private static final CharSequence TITLES[]={"Information","Music Player"};
    private static final int NUMBER_OF_TABS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_window);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(), TITLES, NUMBER_OF_TABS);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(mPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
