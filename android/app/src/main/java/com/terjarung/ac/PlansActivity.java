package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.terjarung.App;
import com.terjarung.R;
import yuku.afw.V;
import yuku.afw.widget.EasyAdapter;

public class PlansActivity extends ActionBarActivity {

	View bAdd;
	ListView lsPlans;
	PlansAdapter adapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plans);

		bAdd = V.get(this, R.id.bAdd);
		bAdd.setOnClickListener(v -> {
			startActivity(new Intent(App.context, AddPlanActivity.class));
		});

		lsPlans = V.get(this, R.id.lsPlans);
		lsPlans.setEmptyView(V.get(this, R.id.empty));
		lsPlans.setAdapter(adapter = new PlansAdapter());
	}

	class PlansAdapter extends EasyAdapter {
		@Override
		public View newView(final int position, final ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.item_plan, parent, false);
		}

		@Override
		public void bindView(final View view, final int position, final ViewGroup parent) {

		}

		@Override
		public int getCount() {
			return 2;
		}
	}

}
