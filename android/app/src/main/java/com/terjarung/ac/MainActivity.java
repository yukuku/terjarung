package com.terjarung.ac;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.Server;
import com.terjarung.rpc.YukuLayer;
import com.terjarung.storage.Prefkeys;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import yuku.afw.V;
import yuku.afw.storage.Preferences;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends ActionBarActivity {

	View bSell;
	View bBuy;

	LinearLayout panelMeetups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bSell = V.get(this, R.id.bSell);
		bBuy = V.get(this, R.id.bBuy);

		bSell.setOnClickListener(v -> {
			startActivity(new Intent(App.context, PlansActivity.class));
		});
		bBuy.setOnClickListener(v -> {
			startActivity(new Intent(App.context, PhonesActivity.class));
		});

		panelMeetups = V.get(this, R.id.panelMeetups);
		reload();
	}

	void reload() {
		Server.getYukuLayer().my_meetups(new Callback<YukuLayer.Meetup[]>() {
			@Override
			public void success(final YukuLayer.Meetup[] meetups, final Response response) {
				runOnUiThread(() -> {
					panelMeetups.removeAllViews();

					for (final YukuLayer.Meetup meetup : meetups) {
						final View v = getLayoutInflater().inflate(R.layout.item_meetup, panelMeetups, false);

						TextView tPhone = V.get(v, R.id.tPhone);
						TextView tContacts = V.get(v, R.id.tContacts);
						Button bDelivered = V.get(v, R.id.bDelivered);

						tPhone.setText(meetup.phone.name);
						tContacts.setText(meetup.contacts);
						bDelivered.setOnClickListener(v2 -> {
							AtomicBoolean cancelled = new AtomicBoolean();

							ProgressDialog pd = ProgressDialog.show(MainActivity.this, null, "Processing...", true, true, dialog -> cancelled.set(true));
							Server.getYukuLayer().meetup_update_status(meetup.id, meetup.youare, new Callback<Boolean>() {
								@Override
								public void success(final Boolean aBoolean, final Response response) {
									pd.dismiss();
									if (cancelled.get()) return;

									reload();
								}

								@Override
								public void failure(final RetrofitError error) {
									pd.dismiss();
									if (cancelled.get()) return;
									new AlertDialog.Builder(MainActivity.this)
										.setMessage(error.getMessage())
										.setPositiveButton("OK", null)
										.show();
								}
							});

						});

						panelMeetups.addView(v);
					}
				});
			}

			@Override
			public void failure(final RetrofitError error) {
				new AlertDialog.Builder(MainActivity.this)
					.setMessage(error.getMessage())
					.setPositiveButton("OK", null)
					.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case R.id.menuLoginSeller:
				Preferences.setString(Prefkeys.authtoken, "seller1");
				return true;
			case R.id.menuLoginSeller2:
				Preferences.setString(Prefkeys.authtoken, "seller2");
				return true;
			case R.id.menuLoginSeller3:
				Preferences.setString(Prefkeys.authtoken, "seller3");
				return true;
			case R.id.menuLoginSeller4:
				Preferences.setString(Prefkeys.authtoken, "seller4");
				return true;
			case R.id.menuLoginBuyer:
				Preferences.setString(Prefkeys.authtoken, "buyer1");
				return true;
			case R.id.menuLoginBuyer2:
				Preferences.setString(Prefkeys.authtoken, "buyer2");
				return true;
			case R.id.menuHost:
				Preferences.setString(Prefkeys.baseurl, "http://10.0.3.2:20080");
				Server.reloadBaseurl();
				return true;
			case R.id.menuAppspot:
				Preferences.setString(Prefkeys.baseurl, "http://terjarung.appspot.com");
				Server.reloadBaseurl();
				return true;
		}

		reload();

		return super.onOptionsItemSelected(item);
	}
}
