package com.terjarung.rpc;

import com.terjarung.BuildConfig;
import com.terjarung.storage.Prefkeys;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import yuku.afw.storage.Preferences;

public class Server {
	private static YukuLayer yukuLayer;

	public static synchronized YukuLayer getYukuLayer() {
		if (yukuLayer == null) {
			yukuLayer = newYukuLayer();
		}

		return yukuLayer;
	}

	static YukuLayer newYukuLayer() {
		final RequestInterceptor requestInterceptor = request -> {
			request.addHeader("User-Agent", "Terjarung Client " + BuildConfig.VERSION_CODE + " (" + BuildConfig.VERSION_NAME + ")");
			final String authtoken = Preferences.getString(Prefkeys.authtoken, null);
			if (authtoken != null) {
				request.addHeader("authtoken", authtoken);
			}
		};

		final RestAdapter restAdapter = new RestAdapter.Builder()
			.setLogLevel(RestAdapter.LogLevel.FULL)
			.setEndpoint(Preferences.getString(Prefkeys.baseurl, "http://10.0.3.2:20080"))
			.setRequestInterceptor(requestInterceptor)
			.build();

		return restAdapter.create(YukuLayer.class);
	}

	public static void reloadBaseurl() {
		yukuLayer = newYukuLayer();
	}
}
