package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.terjarung.App;
import com.terjarung.R;
import yuku.afw.V;


public class MainActivity extends ActionBarActivity {

	View bSell;
	View bBuy;

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
			startActivity(new Intent(App.context, PlansActivity.class));
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

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
