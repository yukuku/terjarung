package com.terjarung.rpc;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

public interface YukuLayer {

	@POST("/client_token")
	void client_token(Callback<String> clientToken);

	@POST("/payment-method-nonce")
	void payment_method_nonce(@Query("nonce") String nonce, Callback<String> result);

	public static class Plan {
		public long id;
		public String name;
		public String call;
		public String sms;
		public String monthly;
		public String quota;
	}

	@POST("/data/plans")
	void data_plans(Callback<Plan[]> result);

	public static class Phone {
		public long id;
		public String name;
		public String img;
		public int price;
	}

	public static class PhoneToSell {
		public long id;
		public String name;
		public String img;
		public int price_old;
		public int price_new;
	}

	@POST("/available_phones")
	void available_phones(Callback<Phone[]> result);

	@POST("/phones_to_sell")
	void phones_to_sell(Callback<PhoneToSell[]> result);
}
