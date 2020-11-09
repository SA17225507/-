package es.source.code.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MyAdapter extends FragmentPagerAdapter {
    private int mCount = 3 ;
    private int[] mColors = new int[]{android.R.color.holo_orange_dark,android.R.color.holo_green_dark
            ,android.R.color.holo_blue_dark};
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return ViewPagerFragment.newInstance(position,mColors[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page" + (position + 1);
    }
}
