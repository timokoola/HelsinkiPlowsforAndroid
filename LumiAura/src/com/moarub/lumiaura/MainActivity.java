/*******************************************************************************
 * Copyright 2013 Moarub Oy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.moarub.lumiaura;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.moarub.lumiaura.model.SnowPlow;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends FragmentActivity {
	private static final String CAMERA_STATE = "CAMERA_STATE";
	private static final double HELSINKI_LNG = 24.9375;
	private static final double HELSINKI_LAT = 60.1708;
	private static final LatLng HELSINKI = new LatLng(HELSINKI_LAT,
			HELSINKI_LNG);
	private GoogleMap fMap;
	SupportMapFragment fMf;
	private SpiceManager fContentManager = new SpiceManager(
			JacksonSpringAndroidSpiceService.class);
	private HashMap<Marker, String> fMarkers = new HashMap<Marker, String>();
	private boolean fTwo;
	private boolean fTwentyFour;
	private boolean fFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ResourceGetter.createInstance(this);
		setContentView(R.layout.activity_main);
		initMaps(savedInstanceState);
		fTwo = true;
	}

	private void initMaps(Bundle savedInstanceState) {
		SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		fMap = mf.getMap();
		fMap.setMyLocationEnabled(true);

		fMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				CameraUpdate newCps = CameraUpdateFactory
						.newCameraPosition(new CameraPosition(marker
								.getPosition(), 4.f, 30.f, 0));

				fMap.animateCamera(newCps);
				fMap.animateCamera(CameraUpdateFactory.zoomIn());
				fMap.animateCamera(CameraUpdateFactory.zoomIn());

				requestPath(fMarkers.get(marker));
				return false;
			}

		});

		CameraPosition cps = null;
		if (savedInstanceState != null) {
			cps = savedInstanceState.getParcelable(CAMERA_STATE);
			logCameraPosition(cps);
		}

		if (fMap != null) {
			if (cps == null) {
				initCamera();
			} else {
				fMap.moveCamera(CameraUpdateFactory.newCameraPosition(cps));
			}
		}

		logSnowPlows(true);
	}

	public void requestPath(String idStr) {
		SinglePlowRequest request = new SinglePlowRequest(idStr);
		String cacheKey = "path" + idStr;
		fContentManager.execute(request, cacheKey, DurationInMillis.ONE_MINUTE,
				new HistoryListener());
	}

	private void logSnowPlows(boolean useCache) {
		PlowListRequest request = new PlowListRequest();
		int hours = fTwo ? 2 : 24;
		request.setHours(hours);

		if (!useCache) {
			fContentManager.removeAllDataFromCache();
		}

		fContentManager.execute(request, "plows" + hours,
				DurationInMillis.ONE_MINUTE, new ListPlowsListener());
	}

	@Override
	protected void onStart() {
		super.onStart();
		fContentManager.start(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		fContentManager.shouldStop();
	}

	/*
	 * public void putMarkers(Collection<MarkerOptions> markers) {
	 * for(MarkerOptions mo: markers) { Marker m = fMap.addMarker(mo);
	 * m.setDraggable(false); fMarkers.put(m, m.getId());
	 * fMap.animateCamera(CameraUpdateFactory.newLatLng(mo.getPosition())); } }
	 */

	public void putMarker(MarkerOptions mo, String id) {
		Marker m = fMap.addMarker(mo);
		m.setDraggable(false);
		fMarkers.put(m, id);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		CameraPosition cps = fMap.getCameraPosition();
		logCameraPosition(cps);
		outState.putParcelable(CAMERA_STATE, cps);
	}


	private void logCameraPosition(CameraPosition cps) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh: {
			logSnowPlows(false);
			break;
		}
		case R.id.twentyfour: {
			fFirst = fTwo;
			fTwo = false;
			fTwentyFour = true;
			logSnowPlows(false);
			break;
		}
		case R.id.twoh: {
			fFirst = fTwentyFour;
			fTwo = true;
			fTwentyFour = false;
			logSnowPlows(false);
			break;
		}
		case R.id.Helsinki: {
			initCamera();
			break;
		}

		}

		return super.onOptionsItemSelected(item);
	}

	public void addPoints(ArrayList<LatLng> fPoints, int cs) {
		PolylineOptions pl = new PolylineOptions();
		pl.color(cs);
		for (LatLng ps : fPoints) {
			pl.add(ps);
		}
		fMap.addPolyline(pl);
	}

	private class ListPlowsListener implements RequestListener<SnowPlow[]> {


		@Override
		public void onRequestFailure(SpiceException arg0) {
			Toast.makeText(MainActivity.this,
					getResources().getText(R.string.request_failed) + arg0.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onRequestSuccess(SnowPlow[] snowplows) {
			if (snowplows == null) {
				return;
			}

			ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
			fMap.clear();
			fMarkers.clear();

			Toast.makeText(MainActivity.this,
					getResources().getText(R.string.number_of_plows).toString() + snowplows.length, Toast.LENGTH_LONG)
					.show();

			for (SnowPlow sp : snowplows) {
				MarkerOptions mo = new MarkerOptions();
				mo.title(sp.getLast_loc().getEventsDesc());
				mo.snippet(sp.getId() + "  " + sp.getLast_loc().getTimeString());
				mo.position(new LatLng(sp.getLast_loc().getCoords()[1], sp
						.getLast_loc().getCoords()[0]));
				mo.anchor(0.5f, 380.f / 512.f);
				mo.icon(BitmapDescriptorFactory.fromResource(sp.getLast_loc()
						.getIcon()));
				markers.add(mo);
				putMarker(mo, sp.getId());
			}

			if (fFirst) {
				initCamera();
				fFirst = false;
			}
		}

	}

	private class HistoryListener implements RequestListener<SnowPlow> {

		@Override
		public void onRequestFailure(SpiceException arg0) {
			Toast.makeText(MainActivity.this,
					getResources().getText(R.string.request_failed) + arg0.getMessage(), Toast.LENGTH_SHORT)
					.show();

		}

		@Override
		public void onRequestSuccess(SnowPlow snowplow) {
			if (snowplow == null || snowplow.getHistory().length < 2) {
				return;
			}

			PolylineOptions plo = new PolylineOptions();
			int startColor = Color.BLACK;// getColor(snowplow.getLast_loc().getEvents());
			LatLng startLoc = snowplow.getHistory()[0].asLatLng();
			plo.width(2.f);
			plo.add(startLoc);
			plo.color(startColor);

			for (int i = 1; i < snowplow.getHistory().length; i++) {
				int currColor = getColor(snowplow.getHistory()[i].getEvents());
				LatLng currLoc = snowplow.getHistory()[i].asLatLng();
				plo.add(currLoc);
				if (currColor != startColor) {
					fMap.addPolyline(plo);
					plo = new PolylineOptions();
					startColor = currColor;
					startLoc = currLoc;
				}
			}
			if (plo != null) {
				fMap.addPolyline(plo);
			}

		}

	}

	public int getColor(String[] events) {
		return Color.BLACK;
	}

	private void initCamera() {
		if (fMarkers.values().size() < 1) {
			fMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HELSINKI, 13));
		} else {
			double s = 0;
			double n = 0;
			double e = 0;
			double w = 0;

			for (Marker m : fMarkers.keySet()) {
				s = Math.min(s != 0 ? s : m.getPosition().latitude,
						m.getPosition().latitude);
				n = Math.max(n != 0 ? n : m.getPosition().latitude,
						m.getPosition().latitude);
				w = Math.min(w != 0 ? w : m.getPosition().longitude,
						m.getPosition().longitude);
				e = Math.max(e != 0 ? e : m.getPosition().longitude,
						m.getPosition().longitude);
			}

			LatLng sw = new LatLng(s, w);
			LatLng ne = new LatLng(n, e);
			fMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
					new LatLngBounds(sw, ne), 10));

			CameraPosition cmp = new CameraPosition.Builder(
					fMap.getCameraPosition()).tilt(30.f).build();
			fMap.animateCamera(CameraUpdateFactory.newCameraPosition(cmp));

		}

	}

}
