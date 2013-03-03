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
package com.moarub.lumiaura.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.android.gms.maps.model.LatLng;
import com.moarub.lumiaura.R;
import com.moarub.lumiaura.ResourceGetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
	private String timestamp;
	private double[] coords;
	private String[] events;
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public double[] getCoords() {
		return coords;
	}
	public void setCoords(double[] coords) {
		this.coords = coords;
	}
	public String[] getEvents() {
		return events;
	}
	public void setEvents(String[] events) {
		this.events = events;
	}
	
	public String getLogString() {
		StringBuilder sbr = new StringBuilder();
		sbr.append(timestamp);
		sbr.append(" coords: ");
		sbr.append(coords[0]);
		sbr.append(", ");
		sbr.append(coords[1]);
		sbr.append(" events:");
		for(String e: events) {
			sbr.append(e);
			sbr.append(" ");
		}
		return sbr.toString();
	}
	public LatLng asLatLng() {
		return new LatLng(coords[1], coords[0]);
	}
	public String getEventsDesc() {
		StringBuilder result = new StringBuilder();
		
		for(int i= 0; i < events.length; i++) {
			if(i == 0) {
				char first = Character.toTitleCase(getLocalizedEvv(i).charAt(0));
				result.append(first);
				result.append(getLocalizedEvv(i).substring(1));
			} else {
				result.append(getLocalizedEvv(i));
			}
			
			if(events.length == 1) {
				
			} else if(i == events.length -2) {
				result.append(getLocalizedAnd());
			} else if(i == events.length -1) {
				result.append(".");
			} else {
				result.append(", ");
			}
		}
		
		return result.toString();
	}
	
	private String getLocalizedAnd() {
		return " ja ";
	}
	private String getLocalizedEvv(int i) {
		String sel = events[i];
		
		if(sel.equalsIgnoreCase("su")) {
			return ResourceGetter.getResourceString(R.string.salt);
		} else if(sel.equalsIgnoreCase("hi")) {
			return  ResourceGetter.getResourceString(R.string.sand);
		} else if(sel.equalsIgnoreCase("au")) {
			return ResourceGetter.getResourceString(R.string.plowing);
		} else if(sel.equalsIgnoreCase("kv")) {
			return ResourceGetter.getResourceString(R.string.light_traffic);
		}
		
		return sel;
	}
	
	public int getIcon() {
		boolean scoop = false;
		boolean sandbox = false;
		for(String s: events) {
			if(s.equalsIgnoreCase("su") || s.equalsIgnoreCase("hi")) {
				sandbox = true;
			} 
			if(s.equalsIgnoreCase("au") || s.equalsIgnoreCase("kv")) {
				scoop = true;
			}
		}
		
		if(scoop && sandbox) {
			return R.drawable.plowsand_noshadow;
		} else if(scoop) {
			return R.drawable.plow_scoop_nosand_noshadow;
		} else if(sandbox) {
			return R.drawable.plow_noscoop_noshadow;
		} else {
			return R.drawable.plow_noscoop_nosand_noshadow;
		}
	}
	public String getTimeString() {
		return timestamp;
	}
	
	
	
}
