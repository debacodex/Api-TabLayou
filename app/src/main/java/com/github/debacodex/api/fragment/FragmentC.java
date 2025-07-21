package com.github.debacodex.api.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.github.debacodex.api.adapter.MyRecyclerAdapter;
import com.github.debacodex.api.helper.SearchableFragment;
import com.github.debacodex.api.model.DataItem;
import com.github.debacodex.api.service.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.github.debacodex.api.R;

public class FragmentC extends Fragment implements SearchableFragment {
	private RecyclerView recyclerView;
	private ProgressBar progressBar;
	private MyRecyclerAdapter adapter;
	private List<DataItem> dataItemList = new ArrayList<>();
	private List<DataItem> filteredList = new ArrayList<>();
	private TextView emptyView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_c, container, false);

		recyclerView = view.findViewById(R.id.recyclerView);
		progressBar = view.findViewById(R.id.progressBar);
		// In onCreateView
		emptyView = view.findViewById(R.id.emptyView);

		setupRecyclerView();
		fetchDataFromApi();

		return view;
	}

	private void setupRecyclerView() {
		adapter = new MyRecyclerAdapter(getActivity(), filteredList);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(adapter);
	}

	private void fetchDataFromApi() {
		progressBar.setVisibility(View.VISIBLE);
		String url = "https://raw.githubusercontent.com/debacodex/ApiData/refs/heads/main/Test.json";

		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
			progressBar.setVisibility(View.GONE);
			try {
				for (int i = 0; i < response.length(); i++) {
					JSONObject jsonObject = response.getJSONObject(i);
					DataItem item = new DataItem(jsonObject.getString("name"), jsonObject.getString("description"),
							jsonObject.getString("imageUrl"));
					dataItemList.add(item);
				}
				filteredList.addAll(dataItemList);
				adapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}, error -> {
			progressBar.setVisibility(View.GONE);
			error.printStackTrace();
		});

		VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
	}

	@Override
	public void onSearch(String query) {
		filteredList.clear();
		if (query.isEmpty()) {
			filteredList.addAll(dataItemList);
		} else {
			String searchPattern = query.toLowerCase().trim();
			for (DataItem item : dataItemList) {
				if (item.getTitle().toLowerCase().contains(searchPattern)
						|| item.getDescription().toLowerCase().contains(searchPattern)) {
					filteredList.add(item);
				}
			}
		}

		// Update empty view based on search results
		if (filteredList.isEmpty() && !query.isEmpty()) {
			emptyView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
			emptyView.setText("No results found for: " + query);
		} else if (filteredList.isEmpty()) {
			emptyView.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
			emptyView.setText("No data available");
		} else {
			emptyView.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
		}
		adapter.setSearchText(query);
		adapter.notifyDataSetChanged();
	}
}