package com.terjarung.rpc;

import android.os.Parcel;
import android.os.Parcelable;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

public interface YukuLayer {

	@POST("/client_token")
	void client_token(Callback<String> clientToken);

	@POST("/payment-method-nonce")
	void payment_method_nonce(@Query("nonce") String nonce, @Query("amount") String amount, Callback<String> result);

	public static class Plan implements Parcelable {

		public long id;
		public String name;
		public String call;
		public String sms;
		public String monthly;
		public String quota;

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(this.id);
			dest.writeString(this.name);
			dest.writeString(this.call);
			dest.writeString(this.sms);
			dest.writeString(this.monthly);
			dest.writeString(this.quota);
		}

		public Plan() {
		}

		private Plan(Parcel in) {
			this.id = in.readLong();
			this.name = in.readString();
			this.call = in.readString();
			this.sms = in.readString();
			this.monthly = in.readString();
			this.quota = in.readString();
		}

		public static final Parcelable.Creator<Plan> CREATOR = new Parcelable.Creator<Plan>() {
			public Plan createFromParcel(Parcel source) {
				return new Plan(source);
			}

			public Plan[] newArray(int size) {
				return new Plan[size];
			}
		};
	}

	@POST("/data/plans")
	void data_plans(@Query("op") int op, Callback<Plan[]> result);

	public static class Phone implements Parcelable {
		public long id;
		public String name;
		public String img;
		public int price;

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(this.id);
			dest.writeString(this.name);
			dest.writeString(this.img);
			dest.writeInt(this.price);
		}

		public Phone() {
		}

		private Phone(Parcel in) {
			this.id = in.readLong();
			this.name = in.readString();
			this.img = in.readString();
			this.price = in.readInt();
		}

		public static final Parcelable.Creator<Phone> CREATOR = new Parcelable.Creator<Phone>() {
			public Phone createFromParcel(Parcel source) {
				return new Phone(source);
			}

			public Phone[] newArray(int size) {
				return new Phone[size];
			}
		};
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

	public static class SellersResult {
		public Phone phone;
		public Seller[] sellers;

		public static class Seller implements Parcelable {

			public String area;
			public int price;

			@Override
			public int describeContents() {
				return 0;
			}

			@Override
			public void writeToParcel(Parcel dest, int flags) {
				dest.writeString(this.area);
				dest.writeInt(this.price);
			}

			public Seller() {
			}

			private Seller(Parcel in) {
				this.area = in.readString();
				this.price = in.readInt();
			}

			public static final Parcelable.Creator<Seller> CREATOR = new Parcelable.Creator<Seller>() {
				public Seller createFromParcel(Parcel source) {
					return new Seller(source);
				}

				public Seller[] newArray(int size) {
					return new Seller[size];
				}
			};
		}
	}

	@POST("/sellers")
	void sellers(Callback<SellersResult> result);

}
