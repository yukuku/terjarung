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

public class PhonesActivity extends ActionBarActivity {
	ListView lsPhones;
	PhonesAdapter adapter;
	YukuLayer.Phone[] phones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phones);

		lsPhones = V.get(this, R.id.lsPhones);
		lsPhones.setAdapter(adapter = new PhonesAdapter());
		lsPhones.setOnItemClickListener((parent, view, position, id) -> {
			startActivity(new Intent(App.context, SellersActivity.class)
				.putExtra("phone", phones[position])
			);
		});

		reload();
	}

	void reload() {
		Server.getYukuLayer().available_phones(new Callback<YukuLayer.Phone[]>() {
			@Override
			public void success(final YukuLayer.Phone[] phones, final Response response) {
				PhonesActivity.this.phones = phones;
				adapter.notifyDataSetChanged();
			}

			@Override
			public void failure(final RetrofitError error) {

			}
		});
	}

	class PhonesAdapter extends EasyAdapter {

		@Override
		public View newView(final int position, final ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.item_phone, parent, false);
		}

		@Override
		public void bindView(final View view, final int position, final ViewGroup parent) {
			ImageView imgPhone = V.get(view, R.id.imgPhone);
			TextView tName = V.get(view, R.id.tName);
			TextView tPrice = V.get(view, R.id.tPrice);

			final YukuLayer.Phone p = phones[position];
			Glide.with(PhonesActivity.this).load(p.img).into(imgPhone);
			tName.setText(p.name);
			tPrice.setText("$" + p.price);
		}

		@Override
		public int getCount() {
			return phones == null ? 0 : phones.length;
		}
	}
}
