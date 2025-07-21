package com.github.debacodex.api.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.github.debacodex.api.adapter.MyPagerAdapter;
import com.github.debacodex.api.fragment.FragmentA;
import com.github.debacodex.api.fragment.FragmentB;
import com.github.debacodex.api.fragment.FragmentC;
import com.github.debacodex.api.helper.SearchableFragment;
import com.google.android.material.tabs.TabLayout;
import com.github.debacodex.api.R;
public class MainActivity extends AppCompatActivity {
	
	private Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	private MyPagerAdapter adapter;
	private SearchView searchView;
	private final Handler searchHandler = new Handler();
	private Runnable searchRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize Toolbar
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Initialize ViewPager and TabLayout
		viewPager = findViewById(R.id.viewPager);
		setupViewPager(viewPager);
		
		tabLayout = findViewById(R.id.tabLayout);
		tabLayout.setupWithViewPager(viewPager);
		
		// Setup tabs with icons
		setupTabIcons();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		
		// Setup SearchView
		MenuItem searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchItem.getActionView();
		setupSearchView();
		
		return true;
	}
	
	private void setupViewPager(ViewPager viewPager) {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new FragmentA(), "Tab 1");
		adapter.addFragment(new FragmentB(), "Tab 2");
		adapter.addFragment(new FragmentC(), "Tab 3");
		viewPager.setAdapter(adapter);
		
		// Update search when tab changes
		viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (searchView != null && !searchView.isIconified()) {
					String query = searchView.getQuery().toString();
					performSearch(query);
				}
			}
		});
	}
	
	private void setupTabIcons() {
		tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher_foreground);
		tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_foreground);
		tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher_foreground);
	}
	
	private void setupSearchView() {
		searchView.setQueryHint("Search...");
		searchView.setIconifiedByDefault(false);
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// Remove any pending search operations
				searchHandler.removeCallbacks(searchRunnable);
				performSearch(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// Remove any pending search operations
				searchHandler.removeCallbacks(searchRunnable);
				
				// Delay search to avoid too many requests while typing
				searchRunnable = () -> performSearch(newText);
				searchHandler.postDelayed(searchRunnable, 300); // 300ms delay
				return true;
			}
		});
	}
	
	private void performSearch(String query) {
		if (adapter != null) {
			Fragment currentFragment = adapter.getCurrentFragment(viewPager.getCurrentItem());
			if (currentFragment instanceof SearchableFragment) {
				((SearchableFragment) currentFragment).onSearch(query);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// Clean up handler to avoid memory leaks
		searchHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
}