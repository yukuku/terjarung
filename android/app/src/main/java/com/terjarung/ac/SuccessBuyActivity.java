package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.YukuLayer;
import yuku.afw.V;

public class SuccessBuyActivity extends ActionBarActivity {

	TextView tNameTemplate;
	ImageView imgPhone;
	YukuLayer.Phone phone;
	View bDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success_buy);

		phone = getIntent().getParcelableExtra("phone");

		imgPhone = V.get(this, R.id.imgPhone);
		Glide.with(this).load(phone.img).into(imgPhone);

		tNameTemplate = V.get(this, R.id.tNameTemplate);
		tNameTemplate.setText(String.format(tNameTemplate.getText().toString(), phone.name));

		bDone = V.get(this, R.id.bDone);
		bDone.setOnClickListener(v -> {
			startActivity(new Intent(App.context, MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		});
	}
}
