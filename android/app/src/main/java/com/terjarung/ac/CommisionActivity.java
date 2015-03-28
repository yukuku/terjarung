package com.terjarung.ac;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.Server;
import com.terjarung.rpc.YukuLayer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import yuku.afw.V;
import yuku.afw.widget.EasyAdapter;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommisionActivity extends ActionBarActivity {
	static final String TAG = CommisionActivity.class.getSimpleName();

	ListView lsPhones;
	TextView tProfit;
	PhonesToSellAdapter adapter;
	YukuLayer.PhoneToSell[] phones;
	int profit = 100;
	View bCancel;
	View bFinish;
	YukuLayer.Plan plan;
	int op;
	String exp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commision);

		lsPhones = V.get(this, R.id.lsPhones);
		lsPhones.setAdapter(adapter = new PhonesToSellAdapter());

		tProfit = V.get(this, R.id.tProfit);
		tProfit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

			}

			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

			}

			@Override
			public void afterTextChanged(final Editable s) {
				try {
					profit = Integer.parseInt(s.toString());
				} catch (NumberFormatException e) {
					profit = 0;
				}
				adapter.notifyDataSetChanged();
			}
		});

		tProfit.setText("" + profit);

		bCancel = V.get(this, R.id.bCancel);
		bCancel.setOnClickListener(v -> finish());
		bFinish = V.get(this, R.id.bFinish);
		bFinish.setOnClickListener(v -> {
			AtomicBoolean cancelled = new AtomicBoolean();

			ProgressDialog pd = ProgressDialog.show(this, null, "Processing...", true, true, dialog -> cancelled.set(true));
			Server.getYukuLayer().my_offer_add(new Gson().toJson(plan), op, exp, profit, new Callback<YukuLayer.SellersResult>() {
				@Override
				public void success(final YukuLayer.SellersResult sellersResult, final Response response) {
					pd.dismiss();
					if (cancelled.get()) return;

					startActivity(new Intent(App.context, SuccessSellActivity.class)
							.putExtra("plan", plan)
							.putExtra("op", op)
							.putExtra("exp", exp)
							.putExtra("profit", profit)
					);
				}

				@Override
				public void failure(final RetrofitError error) {
					pd.dismiss();
					if (cancelled.get()) return;
					new AlertDialog.Builder(CommisionActivity.this)
						.setMessage(error.getMessage())
						.setPositiveButton("OK", null)
						.show();
				}
			});
		});

		plan = getIntent().getParcelableExtra("plan");
		op = getIntent().getIntExtra("op", 0);
		exp = getIntent().getStringExtra("exp");

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
			tPriceNew.setText("$" + (p.price_old + profit));
		}

		@Override
		public int getCount() {
			final int res = phones == null ? 0 : phones.length;
			Log.d(TAG, "count=" + res);
			return res;
		}
	}

}
