package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.terjarung.App;
import com.terjarung.R;
import yuku.afw.V;
import yuku.afw.widget.EasyAdapter;

import java.util.Random;

public class SellersActivity extends ActionBarActivity {

	ListView lsSellers;
	SellersAdapter adapter;

	long seed = new Random().nextLong();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sellers);

		lsSellers = V.get(this, R.id.lsSellers);
		lsSellers.setAdapter(adapter = new SellersAdapter());
		lsSellers.setOnItemClickListener((parent,view,position,id) -> {
			startActivity(new Intent(App.context, BuyActivity.class));
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
		public void bindView(final View view, final int position, final ViewGroup parent) {
			ImageView imgAvatar = V.get(view, R.id.imgAvatar);
			TextView tPrice = V.get(view, R.id.tPrice);
			TextView tArea = V.get(view, R.id.tArea);

			imgAvatar.setImageResource(pps[new Random(seed + position).nextInt(pps.length)]);
			tPrice.setText("$" + new Random(seed + position).nextInt(540));
			tArea.setText("Woodlands");
		}

		@Override
		public int getCount() {
			return 10;
		}
	}
}
