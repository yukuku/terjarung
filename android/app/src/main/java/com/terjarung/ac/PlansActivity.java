package com.terjarung.ac;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
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

public class PlansActivity extends ActionBarActivity {

	View bAdd;
	SwipeListView lsPlans;
	PlansAdapter adapter;
	YukuLayer.Offer[] offers;

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

		lsPlans.setSwipeListViewListener(new BaseSwipeListViewListener() {
			@Override
			public void onDismiss(final int[] reverseSortedPositions) {
				for (final int position : reverseSortedPositions) {
					del(position);
				}
			}
		});
		reload();
	}

	void del(int position) {
		Server.getYukuLayer().my_offer_delete(offers[position].id, new Callback<Boolean>() {
			@Override
			public void success(final Boolean aBoolean, final Response response) {
			}

			@Override
			public void failure(final RetrofitError error) {
			}
		});

		YukuLayer.Offer[] offers2 = new YukuLayer.Offer[offers.length-1];
		System.arraycopy(offers, 0, offers2, 0, position);
		System.arraycopy(offers, position + 1, offers2, position, offers.length - position - 1);
		offers = offers2;
		adapter.notifyDataSetChanged();
	}

	void reload() {
		AtomicBoolean cancelled = new AtomicBoolean();

		ProgressDialog pd = ProgressDialog.show(this, null, "Processing...", true, true, dialog -> cancelled.set(true));
		Server.getYukuLayer().my_offers(new Callback<YukuLayer.Offer[]>() {
			@Override
			public void success(final YukuLayer.Offer[] offers, final Response response) {
				pd.dismiss();
				if (cancelled.get()) return;

				PlansActivity.this.offers = offers;
				adapter.notifyDataSetChanged();
			}

			@Override
			public void failure(final RetrofitError error) {
				pd.dismiss();
				if (cancelled.get()) return;
				new AlertDialog.Builder(PlansActivity.this)
					.setMessage(error.getMessage())
					.setPositiveButton("OK", null)
					.show();
			}
		});
	}
	class PlansAdapter extends EasyAdapter {
		@Override
		public View newView(final int position, final ViewGroup parent) {
			return getLayoutInflater().inflate(R.layout.item_plan, parent, false);
		}

		@Override
		public void bindView(final View view, final int position, final ViewGroup parent) {
			final YukuLayer.Offer o = offers[position];

			TextView tPlan = V.get(view, R.id.tPlan);
			TextView tExp = V.get(view, R.id.tExp);
			ImageView imgOp = V.get(view, R.id.imgOp);
			TextView tProfit = V.get(view, R.id.tProfit);

			tPlan.setText("" + o.plan.name);
			tExp.setText(o.exp);
			imgOp.setImageResource(new int[]{R.drawable.op1, R.drawable.op2, R.drawable.op3}[o.op == 0 ? 0 : (o.op - 1)]);
			tProfit.setText("$" + o.profit);
		}

		@Override
		public int getCount() {
			return offers == null ? 0 : offers.length;
		}
	}

}
