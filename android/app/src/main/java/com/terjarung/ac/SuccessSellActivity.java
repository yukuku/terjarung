package com.terjarung.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.terjarung.App;
import com.terjarung.R;
import com.terjarung.rpc.YukuLayer;
import yuku.afw.V;

public class SuccessSellActivity extends ActionBarActivity {
	YukuLayer.Plan plan;
	int op;
	String exp;
	int profit;
	View bDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success_sell);

		plan = getIntent().getParcelableExtra("plan");
		op = getIntent().getIntExtra("op", 0);
		exp = getIntent().getStringExtra("exp");
		profit = getIntent().getIntExtra("profit", 0);

		TextView tPlan = V.get(this, R.id.tPlan);
		ImageView imgOp = V.get(this, R.id.imgOp);
		TextView tProfit = V.get(this, R.id.tProfit);
		TextView tExp = V.get(this, R.id.tExp);

		tExp.setText(exp);
		tProfit.setText("$" + profit);
		imgOp.setImageResource(new int[]{R.drawable.op1, R.drawable.op2, R.drawable.op3}[op == 0 ? 0 : (op-1)]);
		tPlan.setText(plan.name);

		bDone = V.get(this, R.id.bDone);
		bDone.setOnClickListener(v -> {
			startActivity(new Intent(App.context, MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		});
	}
}
