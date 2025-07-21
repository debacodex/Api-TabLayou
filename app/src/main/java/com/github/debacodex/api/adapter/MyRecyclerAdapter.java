package com.github.debacodex.api.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import com.github.debacodex.api.model.DataItem;
import java.util.List;
import com.github.debacodex.api.R;
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
	private Context context;
	private List<DataItem> dataItemList;
	private String searchText = "";
	
	public MyRecyclerAdapter(Context context, List<DataItem> dataItemList) {
		this.context = context;
		this.dataItemList = dataItemList;
	}
	
	public void setSearchText(String searchText) {
		this.searchText = searchText.toLowerCase();
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
		.inflate(R.layout.item_recycler, parent, false);
		return new MyViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
		DataItem item = dataItemList.get(position);
		
		// Highlight title
		highlightText(holder.titleTextView, item.getTitle(), searchText);
		
		// Highlight description
		highlightText(holder.descriptionTextView, item.getDescription(), searchText);
		
		// Load image with Glide
		Glide.with(context)
		.load(item.getImageUrl())
		.placeholder(R.drawable.ic_launcher_foreground)
		.error(R.drawable.ic_launcher_background)
		.into(holder.imageView);
	}
	
	private void highlightText(TextView textView, String fullText, String searchText) {
		if (!searchText.isEmpty() && fullText.toLowerCase().contains(searchText)) {
			String lowerFullText = fullText.toLowerCase();
			String lowerSearchText = searchText.toLowerCase();
			
			SpannableString spannableString = new SpannableString(fullText);
			int startPos = lowerFullText.indexOf(lowerSearchText);
			int endPos = startPos + lowerSearchText.length();
			
			// Highlight all occurrences
			while (startPos >= 0) {
				spannableString.setSpan(
				new ForegroundColorSpan(Color.RED),
				startPos,
				endPos,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
				);
				startPos = lowerFullText.indexOf(lowerSearchText, endPos);
				endPos = startPos + lowerSearchText.length();
			}
			
			textView.setText(spannableString);
			} else {
			textView.setText(fullText);
		}
	}
	
	@Override
	public int getItemCount() {
		return dataItemList.size();
	}
	
	public static class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView titleTextView;
		TextView descriptionTextView;
		
		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			imageView = itemView.findViewById(R.id.imageView);
			titleTextView = itemView.findViewById(R.id.titleTextView);
			descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
		}
	}
}