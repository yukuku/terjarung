package com.terjarung.ac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.terjarung.App;
import com.terjarung.R;

public class AddPlanActivity extends ActionBarActivity {
	ViewPager viewPager;

	public static Intent createIntent() {
		return new Intent(App.context, AddPlanActivity.class);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new TutorialAdapter(getSupportFragmentManager()));
	}

	class TutorialAdapter extends FragmentStatePagerAdapter {

		public TutorialAdapter(final FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(final int position) {
			switch (position) {
				case 0:
					return new Page1Fragment();
				case 1:
					return new Page2Fragment();
				case 2:
					return new Page3Fragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}
	}

	static class Page1Fragment extends Fragment {
		@Override
		public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
			final View res = inflater.inflate(R.layout.fragment_page1, container, false);
			return res;
		}
	}

	static class Page2Fragment extends Fragment {
		@Override
		public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
			final View res = inflater.inflate(R.layout.fragment_page2, container, false);
			return res;
		}
	}

	static class Page3Fragment extends Fragment {
		Activity activity;

		@Override
		public void onAttach(final Activity activity) {
			super.onAttach(activity);
			this.activity = activity;
		}

		@Override
		public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
			final View res = inflater.inflate(R.layout.fragment_page3, container, false);
			return res;
		}

		@Override
		public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);

		}
	}
}
