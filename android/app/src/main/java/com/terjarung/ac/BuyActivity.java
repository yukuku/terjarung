package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.google.gson.Gson;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.Server;
import com.terjarung.rpc.YukuLayer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import yuku.afw.V;

public class BuyActivity extends ActionBarActivity {
	public static final int REQCODE_buy = 1;

	View bPay;
	String clientToken;
	YukuLayer.Phone phone;
	YukuLayer.SellersResult.Seller seller;
	View tBypass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy);

		downloadClientToken();

		phone = getIntent().getParcelableExtra("phone");
		seller = getIntent().getParcelableExtra("seller");

		bPay = V.get(this, R.id.bPay);
		bPay.setOnClickListener(v -> {
			final Intent intent = new Intent(App.context, BraintreePaymentActivity.class);

			final Customization customization = new Customization.CustomizationBuilder()
				.primaryDescription(phone.name)
				.secondaryDescription("from a seller around " + seller.area)
				.amount("$" + seller.price)
				.build();

			intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, this.clientToken);
			intent.putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);

			// REQUEST_CODE is arbitrary and is only used within this activity.
			startActivityForResult(intent, REQCODE_buy);
		});

		tBypass = V.get(this, R.id.tBypass);
		tBypass.setOnClickListener(v -> done());

		displayPay();
	}

	private void downloadClientToken() {
		Server.getYukuLayer().client_token(new Callback<String>() {
			@Override
			public void success(final String clientToken, final Response response) {
				BuyActivity.this.clientToken = clientToken;
				displayPay();
			}

			@Override
			public void failure(final RetrofitError error) {
				// try again
				if (isFinishing()) return;

				SystemClock.sleep(1000);
				downloadClientToken();
			}
		});
	}

	void displayPay() {
		runOnUiThread(() -> {
			bPay.setEnabled(clientToken != null);
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQCODE_buy) {
			if (resultCode == BraintreePaymentActivity.RESULT_OK) {
				String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);

				Server.getYukuLayer().payment_method_nonce(paymentMethodNonce, "" + seller.price, new Callback<String>() {
					@Override
					public void success(final String s, final Response response) {
						done();
					}

					@Override
					public void failure(final RetrofitError error) {

					}
				});
			}
		}
	}

	private void done() {
		Server.getYukuLayer().meetup_add(new Gson().toJson(phone), seller.user, new Callback<YukuLayer.Meetup>() {
			@Override
			public void success(final YukuLayer.Meetup meetup, final Response response) {

			}

			@Override
			public void failure(final RetrofitError error) {

			}
		});

		startActivity(new Intent(App.context, SuccessBuyActivity.class)
			.putExtra("phone", phone));
		setResult(RESULT_OK);
		finish();
	}
}
