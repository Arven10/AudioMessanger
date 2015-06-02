package ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs.audioplayer.AudioPlayerTab;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            UserInformationTab userInformationTab = new UserInformationTab();
            return userInformationTab;
        } else {
            AudioPlayerTab audioPlayerTab = new AudioPlayerTab();
            return audioPlayerTab;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
