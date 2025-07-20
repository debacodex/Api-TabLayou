package com.magic.in;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

public class MainActivity extends AppCompatActivity {

	private ViewPager viewPager;
	private TabLayout tabLayout;
	private MyPagerAdapter pagerAdapter;
	private SearchView searchView;
	private String currentQuery = "";

	private boolean doubleBackToExitPressedOnce = false;
	private Handler handler = new Handler();
	private Runnable resetBackPressFlag = () -> doubleBackToExitPressedOnce = false;

	private String[] tabTitles = { "Fragment A", "Fragment B", "Fragment C" };
	private MagicIndicator magicIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Setup Toolbar
		setSupportActionBar(findViewById(R.id.toolbar));

		// Initialize ViewPager and TabLayout
		viewPager = findViewById(R.id.viewPager1);
		tabLayout = findViewById(R.id.tabLayout);
		magicIndicator = findViewById(R.id.magic_indicator);
		setupMagicIndicator();

		// Setup ViewPager with adapter
		pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		tabLayout.setupWithViewPager(viewPager);

		// Set tab titles
		tabLayout.getTabAt(0).setText("Fragment A");
		tabLayout.getTabAt(1).setText("Fragment B");

	}

	// MagicIndicator
	private void setupMagicIndicator() {
		CommonNavigator commonNavigator = new CommonNavigator(this);
		commonNavigator.setSkimOver(true);
		//	commonNavigator.setScrollPivotX(20.0f);
		//commonNavigator.setAdjustMode(true);
		int padding = UIUtil.getScreenWidth(this) / 50;
		//	commonNavigator.setRightPadding(padding);
		commonNavigator.setLeftPadding(padding);

		commonNavigator.setAdapter(new CommonNavigatorAdapter() {
			@Override
			public int getCount() {
				return tabTitles.length;
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
				clipPagerTitleView.setText(tabTitles[index]);
				clipPagerTitleView.setTextColor(getResources().getColor(R.color.magic_indicator_text));
				clipPagerTitleView.setClipColor(Color.parseColor("#FF4081"));
				//clipPagerTitleView.setBackgroundResource(R.drawable.ripple_effect);

				clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						viewPager.setCurrentItem(index);
						//	drawerLayout.closeDrawer(GravityCompat.START);
						//clipPagerTitleView.setBackgroundResource(R.drawable.ripple_effect);

					}
				});
				return clipPagerTitleView;
			}

			@Override
			public IPagerIndicator getIndicator(Context context) {
				WrapPagerIndicator indicator = new WrapPagerIndicator(context);
				indicator.setFillColor(Color.parseColor("#1BFF4081"));
				return indicator;
			}
		});
		magicIndicator.setNavigator(commonNavigator);

		ViewPagerHelper.bind(magicIndicator, viewPager);

		
		
				// Handle tab changes
				tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
					@Override
					public void onTabSelected(TabLayout.Tab tab) {
						// Re-apply current search filter when tab changes
						filterFragments(currentQuery);
					}
		
					@Override
					public void onTabUnselected(TabLayout.Tab tab) {
					//	filterFragments(currentQuery);
					}
		
					@Override
					public void onTabReselected(TabLayout.Tab tab) {
					//	filterFragments(currentQuery);
					}
				});
				
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		// Setup SearchView
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setQueryHint("Search...");

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				currentQuery = newText;
				filterFragments(newText);
				return true;
			}
		});

		return true;
	}

	private void filterFragments(String query) {
		Fragment currentFragment = pagerAdapter.getCurrentFragment(viewPager.getCurrentItem());

		if (currentFragment instanceof FragmentA) {
			((FragmentA) currentFragment).filter(query);
		} else if (currentFragment instanceof FragmentB) {
			((FragmentB) currentFragment).filter(query);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_refresh) {
			// Clear search and refresh data
			if (searchView != null) {
				searchView.setQuery("", false);
				currentQuery = "";
				searchView.clearFocus();
			}

			// Refresh both fragments
			if (pagerAdapter.fragmentA != null) {
				pagerAdapter.fragmentA.refreshData();
			}
			if (pagerAdapter.fragmentB != null) {
				pagerAdapter.fragmentB.refreshData();
			}

			Toast.makeText(this, "Data refreshed", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Snackbar.make(findViewById(android.R.id.content), "Press BACK again to exit", Snackbar.LENGTH_SHORT).show();

		handler.postDelayed(resetBackPressFlag, 2000); // 2 seconds to press again
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(resetBackPressFlag);
	}

}