package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.Server;
import com.terjarung.rpc.YukuLayer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import yuku.afw.V;
import yuku.afw.widget.EasyAdapter;

public class AddPlanActivity extends ActionBarActivity {
	ViewPager viewPager;

	View bPrev;
	View bNext;

	TutorialAdapter adapter;

	public static Intent createIntent() {
		return new Intent(App.context, AddPlanActivity.class);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_plan);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(adapter = new TutorialAdapter(getSupportFragmentManager()));

		bPrev = V.get(this, R.id.bPrev);
		bNext = V.get(this, R.id.bNext);

		bPrev.setOnClickListener(v -> {
			if (viewPager.getCurrentItem() != 0) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
			}
		});
		bNext.setOnClickListener(v -> {
			if (viewPager.getCurrentItem() == adapter.getCount() - 1) {
				done();
			} else {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			}
		});

		final ViewPager.OnPageChangeListener onpagechange = new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(final int position) {
				setTitle(adapter.getPageTitle(position));

				bPrev.setEnabled(position != 0);
			}

			@Override
			public void onPageScrollStateChanged(final int state) {

			}
		};
		viewPager.setOnPageChangeListener(onpagechange);
		onpagechange.onPageSelected(0);
	}

	void done() {
		// TODO verify
		startActivity(new Intent(App.context, CommisionActivity.class));
	}

	class TutorialAdapter extends FragmentStatePagerAdapter {

		public TutorialAdapter(final FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(final int position) {
			switch (position) {
				case 0:
					return "Your service provider";
				case 1:
					return "Your current plan";
			}
			return null;
		}

		@Override
		public Fragment getItem(final int position) {
			switch (position) {
				case 0:
					return new Page1Fragment();
				case 1:
					return new Page2Fragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	public static class Page1Fragment extends Fragment {
		@Override
		public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_page1, container, false);
		}

		@Override
		public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			final View panelOperator1 = V.get(view, R.id.panelOperator1);
			final View panelOperator2 = V.get(view, R.id.panelOperator2);
			final View panelOperator3 = V.get(view, R.id.panelOperator3);

			View.OnClickListener vo = v -> {
				panelOperator1.setBackgroundColor(0x0);
				panelOperator2.setBackgroundColor(0x0);
				panelOperator3.setBackgroundColor(0x0);
				v.setBackgroundColor(0x20000000);
			};

			panelOperator1.setOnClickListener(vo);
			panelOperator2.setOnClickListener(vo);
			panelOperator3.setOnClickListener(vo);
		}
	}

	public static class Page2Fragment extends Fragment {
		Spinner cbPlans;
		private YukuLayer.Plan[] plans;
		private LayoutInflater inflater;

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
			this.inflater = inflater;
			final View res = inflater.inflate(R.layout.fragment_page2, container, false);
			return res;
		}

		@Override
		public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

			cbPlans = V.get(view, R.id.cbPlans);

			Server.getYukuLayer().data_plans(new Callback<YukuLayer.Plan[]>() {

				@Override
				public void success(final YukuLayer.Plan[] plans, final Response response) {
					Page2Fragment.this.plans = plans;
					cbPlans.setAdapter(new EasyAdapter() {
						@Override
						public View newView(final int position, final ViewGroup parent) {
							return inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
						}

						@Override
						public View newDropDownView(final int position, final ViewGroup parent) {
							return inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
						}

						@Override
						public void bindView(final View view, final int position, final ViewGroup parent) {
							TextView text1 = V.get(view, android.R.id.text1);
							text1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
							text1.setText(plans[position].name);
						}

						@Override
						public int getCount() {
							return plans.length;
						}
					});
				}

				@Override
				public void failure(final RetrofitError error) {

				}
			});

			TextView tPlanDetails = V.get(view, R.id.tPlanDetails);

			cbPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
					final YukuLayer.Plan p = plans[position];
					tPlanDetails.setText(String.format("$%s / month\n%s mins outgoing calls\n%s local SMS/MMS\n%s local data", p.monthly, p.call, p.sms, p.quota));
				}

				@Override
				public void onNothingSelected(final AdapterView<?> parent) {
					tPlanDetails.setText("");
				}
			});
		}
	}
}