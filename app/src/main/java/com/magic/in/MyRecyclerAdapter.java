package com.magic.in;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import java.util.Locale;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

	private Context context;
	private List<DataItem> dataItems;
	private String searchText = "";

	public MyRecyclerAdapter(Context context, List<DataItem> dataItems) {
		this.context = context;
		this.dataItems = dataItems;

	}

	public void setSearchText(String searchText) {
		this.searchText = searchText.toLowerCase();
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		DataItem item = dataItems.get(position);

		//	holder.title.setText(item.getTitle());
		//	holder.description.setText(item.getDescription());

		// Highlight title
		highlightText(holder.title, item.getTitle(), searchText);

		// Highlight description
		highlightText(holder.description, item.getDescription(), searchText);

		// Load image with Glide
		Glide.with(context).load(item.getImageUrl()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error)
				.into(holder.image);

		int anim = R.anim.slide_in_right;
		holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), anim));
		holder.itemContainer.setOnClickListener(v -> {

			Intent intent = new Intent(context, DetailActivity.class);

			intent.putExtra("item_title", item.getTitle());

			intent.putExtra("item_image", item.getImageUrl());
			intent.putExtra("item_description", item.getDescription());
			context.startActivity(intent);

			if (context instanceof androidx.appcompat.app.AppCompatActivity) {
				((androidx.appcompat.app.AppCompatActivity) context).overridePendingTransition(R.anim.fade_out,
						R.anim.fade_out);
			}

		});
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
				spannableString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
		return dataItems.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		ImageView image;
		TextView title;
		TextView description;
		LinearLayout itemContainer;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			image = itemView.findViewById(R.id.itemImage);
			title = itemView.findViewById(R.id.itemTitle);
			description = itemView.findViewById(R.id.itemDescription);
			itemContainer = itemView.findViewById(R.id.item_container);
		}
	}
}