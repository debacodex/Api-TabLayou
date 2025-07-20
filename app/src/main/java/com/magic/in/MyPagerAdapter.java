package com.magic.in;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	public FragmentA fragmentA;
	public FragmentB fragmentB;
	
	public MyPagerAdapter(FragmentManager fm) {
		super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
	}
	
	@NonNull
	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
			fragmentA = new FragmentA();
			return fragmentA;
			case 1:
			fragmentB = new FragmentB();
			return fragmentB;
			default:
			return new FragmentA();
		}
	}
	
	public Fragment getCurrentFragment(int position) {
		if (position == 0) {
			return fragmentA;
			} else {
			return fragmentB;
		}
	}
	
	@Override
	public int getCount() {
		return 2;
	}
}