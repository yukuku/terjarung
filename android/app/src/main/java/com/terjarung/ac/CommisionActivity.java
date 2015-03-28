package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

public class CommisionActivity extends ActionBarActivity {
	static final String TAG = CommisionActivity.class.getSimpleName();

	ListView lsPhones;
	PhonesToSellAdapter adapter;
	YukuLayer.PhoneToSell[] phones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commision);

		lsPhones = V.get(this, R.id.lsPhones);
		lsPhones.setAdapter(adapter = new PhonesToSellAdapter());
		lsPhones.setOnItemClickListener((parent, view, position, id) -> {
			startActivity(new Intent(App.context, SellersActivity.class));
		});

		reload();
	}

	void reload() {
		Server.getYukuLayer().phones_to_sell(new Callback<YukuLayer.PhoneToSell[]>() {
			@Override
			public void success(final YukuLayer.PhoneToSell[] phoneToSells, final Response response) {
				CommisionActivity.this.phones = phoneToSells;
				adapter.notifyDataSetChanged();
			}

			@Override
			public void failure(final RetrofitError error) {
				Log.e(TAG, "eror !!! ", error);
			}
		});
	}

	class PhonesToSellAdapter extends EasyAdapter {

		@Override
		public View newView(final int position, final ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.item_phone_tosell, parent, false);
		}

		@Override
		public void bindView(final View view, final int position, final ViewGroup parent) {
			ImageView imgPhone = V.get(view, R.id.imgPhone);
			TextView tName = V.get(view, R.id.tName);
			TextView tPriceOld = V.get(view, R.id.tPriceOld);
			TextView tPriceNew = V.get(view, R.id.tPriceNew);

			final YukuLayer.PhoneToSell p = phones[position];
			Glide.with(CommisionActivity.this).load(p.img).into(imgPhone);
			tName.setText(p.name);
			tPriceOld.setText("$" + p.price_old);
			tPriceNew.setText("$" + p.price_new);
		}

		@Override
		public int getCount() {
			final int res = phones == null ? 0 : phones.length;
			Log.d(TAG, "count=" + res);
			return res;
		}
	}

}
