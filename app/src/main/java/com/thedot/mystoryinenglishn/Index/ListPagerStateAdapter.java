package com.thedot.mystoryinenglishn.Index;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by okitoki on 2018. 1. 30..
 */

public class ListPagerStateAdapter extends FragmentStatePagerAdapter {
    private ArrayList<CategoryData> categorydata;

    public ListPagerStateAdapter(FragmentManager fm, ArrayList<CategoryData> _mTl) {
        super(fm);
        categorydata=_mTl;
    }

    public ArrayList<CategoryData> getData(){
        return categorydata;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return PageOneListFragment.newInstance(getData(),position);
    }

    public int getCount() {
        return categorydata.size();
    }

}
