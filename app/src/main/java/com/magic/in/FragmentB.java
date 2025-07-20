package com.magic.in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.magic.in.R;
public class FragmentB extends Fragment {
	
	private RecyclerView recyclerView;
	private ProgressBar progressBar;
	private TextView emptyView;
	private MyRecyclerAdapter adapter;
	private List<DataItem> dataItems = new ArrayList<>();
	private List<DataItem> filteredItems = new ArrayList<>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_b, container, false);
		
		recyclerView = view.findViewById(R.id.recyclerViewB);
		progressBar = view.findViewById(R.id.progressBarB);
		emptyView = view.findViewById(R.id.emptyViewB);
		
		setupRecyclerView();
		loadData();
		
		return view;
	}
	
	private void setupRecyclerView() {
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new MyRecyclerAdapter(getContext(), filteredItems);
		recyclerView.setAdapter(adapter);
	}
	
	private void loadData() {
		progressBar.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		
		String url = "https://jsonplaceholder.typicode.com/photos";
		
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
		new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				progressBar.setVisibility(View.GONE);
				try {
					dataItems.clear();
					for (int i = 0; i < Math.min(response.length(), 20); i++) {
						JSONObject jsonObject = response.getJSONObject(i);
						DataItem item = new DataItem(jsonObject.getString("title"),
						"ID: " + jsonObject.getInt("id"), jsonObject.getString("thumbnailUrl"));
						dataItems.add(item);
					}
					filteredItems.clear();
					filteredItems.addAll(dataItems);
					adapter.notifyDataSetChanged();
					
					if (filteredItems.isEmpty()) {
						emptyView.setVisibility(View.VISIBLE);
					}
					} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressBar.setVisibility(View.GONE);
				Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
				emptyView.setVisibility(View.VISIBLE);
			}
		});
		
		RequestQueue requestQueue = Volley.newRequestQueue(getContext());
		requestQueue.add(jsonArrayRequest);
	}
	
	
	
	public void filter(String query) {
		filteredItems.clear();
		
		if (query.isEmpty()) {
			filteredItems.addAll(dataItems);
			} else {
			String lowerCaseQuery = query.toLowerCase();
			for (DataItem item : dataItems) {
				if (item.getTitle().toLowerCase().contains(lowerCaseQuery)
				|| item.getDescription().toLowerCase().contains(lowerCaseQuery)) {
					filteredItems.add(item);
				}
			}
		}
		
		if (filteredItems.isEmpty() && !query.isEmpty())
		
		{
			emptyView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
			} else {
			emptyView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
		}
		adapter.notifyDataSetChanged();
		adapter.setSearchText(query);
		
		
		
	}
	
	public void refreshData() {
		loadData();
	}
	
}