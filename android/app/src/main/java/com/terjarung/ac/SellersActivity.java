package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.Server;
import com.terjarung.rpc.YukuLayer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import yuku.afw.V;
import yuku.afw.widget.EasyAdapter;

import java.util.Random;

public class SellersActivity extends ActionBarActivity {

	ListView lsSellers;
	SellersAdapter adapter;
	YukuLayer.SellersResult.Seller[] sellers;
	YukuLayer.Phone phone;

	long seed = new Random().nextLong();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellers);

		lsSellers = V.get(this, R.id.lsSellers);
		lsSellers.setAdapter(adapter = new SellersAdapter());
		lsSellers.setOnItemClickListener((parent, view, position, id) -> {
			startActivity(new Intent(App.context, BuyActivity.class));
		});

		reload();
	}

	void reload() {
		Server.getYukuLayer().sellers(new Callback<YukuLayer.SellersResult>() {
			@Override
			public void success(final YukuLayer.SellersResult sellersResult, final Response response) {
				sellers = sellersResult.sellers;
				phone = sellersResult.phone;
				adapter.notifyDataSetChanged();
			}

			@Override
			public void failure(final RetrofitError error) {
			}
		});
	}


	class SellersAdapter extends EasyAdapter {

		int[] pps = {
			R.drawable.profpic01,
			R.drawable.profpic02,
			R.drawable.profpic03,
			R.drawable.profpic04,
			R.drawable.profpic05,
			R.drawable.profpic06,
			R.drawable.profpic07,
			R.drawable.profpic08,
			R.drawable.profpic09,
			R.drawable.profpic10,
		};

		@Override
		public View newView(final int position, final ViewGroup parent) {
			final int type = getItemViewType(position);
			return getLayoutInflater().inflate(type == 0 ? R.layout.item_seller_heading : R.layout.item_seller, parent, false);
		}

		@Override
		public int getItemViewType(final int position) {
			if (position == 0) return 0;
			return 1;
		}

		@Override
		public void bindView(final View view,  int position, final ViewGroup parent) {
			final int type = getItemViewType(position);

			if (type == 0) {
				ImageView imgPhone = V.get(view, R.id.imgPhone);
				TextView tName = V.get(view, R.id.tName);

				final YukuLayer.Phone p = phone;
				Glide.with(SellersActivity.this).load(p.img).into(imgPhone);
				tName.setText(p.name);
			} else {
				ImageView imgAvatar = V.get(view, R.id.imgAvatar);
				TextView tPrice = V.get(view, R.id.tPrice);
				TextView tArea = V.get(view, R.id.tArea);

				position = position -1;
				imgAvatar.setImageResource(pps[new Random(seed + position).nextInt(pps.length)]);
				tPrice.setText("$" + sellers[position].price);
				tArea.setText(sellers[position].area);
			}
		}

		@Override
		public int getCount() {
			return sellers == null ? 0 : sellers.length + 1;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isEnabled(final int position) {
			return getItemViewType(position) == 1;
		}
	}
}
