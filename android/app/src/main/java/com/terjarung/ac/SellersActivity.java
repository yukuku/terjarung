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
	ImageView imgPhone;
	TextView tName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellers);

		lsSellers = V.get(this, R.id.lsSellers);
		lsSellers.setAdapter(adapter = new SellersAdapter());
		lsSellers.setOnItemClickListener((parent, view, position, id) -> {
			startActivity(new Intent(App.context, BuyActivity.class)
				.putExtra("phone", phone)
				.putExtra("seller", adapter.getItem(position)));
		});

		imgPhone = V.get(this, R.id.imgPhone);
		tName = V.get(this, R.id.tName);

		phone = getIntent().getParcelableExtra("phone");
		final YukuLayer.Phone p = phone;
		Glide.with(SellersActivity.this).load(p.img).into(imgPhone);
		tName.setText(p.name);

		reload();
	}

	void reload() {
		Server.getYukuLayer().sellers(phone.id, new Callback<YukuLayer.SellersResult>() {
			@Override
			public void success(final YukuLayer.SellersResult sellersResult, final Response response) {
				sellers = sellersResult.sellers;

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
			return getLayoutInflater().inflate(R.layout.item_seller, parent, false);
		}

		@Override
		public void bindView(final View view, int position, final ViewGroup parent) {

			ImageView imgAvatar = V.get(view, R.id.imgAvatar);
			TextView tPrice = V.get(view, R.id.tPrice);
			TextView tArea = V.get(view, R.id.tArea);

			imgAvatar.setImageResource(pps[new Random(sellers[position].user.hashCode()).nextInt(pps.length)]);
			tPrice.setText("$" + sellers[position].price);
			tArea.setText(sellers[position].area);
		}

		@Override
		public YukuLayer.SellersResult.Seller getItem(final int position) {
			return sellers[position];
		}

		@Override
		public int getCount() {
			return sellers == null ? 0 : sellers.length;
		}

	}
}
