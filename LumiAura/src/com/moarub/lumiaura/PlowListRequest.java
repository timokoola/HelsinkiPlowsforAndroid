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

import android.net.Uri;
import android.util.Log;

import com.moarub.lumiaura.model.SnowPlow;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class PlowListRequest extends SpringAndroidSpiceRequest<SnowPlow[]> {

	private String fId;
	private int fHours;

	public PlowListRequest() {
		super(SnowPlow[].class);
	}

	public PlowListRequest(String id) {
		super(SnowPlow[].class);
		fId = id;
	}

	@Override
	public SnowPlow[] loadDataFromNetwork() throws Exception {
		Uri.Builder uriB = Uri
				.parse("http://dev.stadilumi.fi/api/v1/snowplow/").buildUpon();
		if (fId != null) {
			uriB.appendPath(fId);
		} else {
			uriB.appendQueryParameter("since", "-" + fHours + "hours");
		}

		String url = uriB.build().toString();
		
		Log.d("Fetching URL", url);

		return getRestTemplate().getForObject(url, SnowPlow[].class);
	}

	public void setHours(int i) {
		fHours = i;
	}

}
