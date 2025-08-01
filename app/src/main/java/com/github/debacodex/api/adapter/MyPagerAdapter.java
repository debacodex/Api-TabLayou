package com.github.debacodex.api.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private final List<Fragment> fragmentList = new ArrayList<>();
	private final List<String> fragmentTitleList = new ArrayList<>();
	
	public MyPagerAdapter(FragmentManager manager) {
		super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
	}
	
	@NonNull
	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}
	
	@Override
	public int getCount() {
		return fragmentList.size();
	}
	
	public void addFragment(Fragment fragment, String title) {
		fragmentList.add(fragment);
		fragmentTitleList.add(title);
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return fragmentTitleList.get(position);
	}
	
	public Fragment getCurrentFragment(int position) {
		return fragmentList.get(position);
	}
}