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

@JsonIgnoreProperties(ignoreUnknown = true)
public class SnowPlow {
	private String id;
	private Location last_loc;
	private Location[] history;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Location getLast_loc() {
		return last_loc;
	}
	public void setLast_loc(Location last_loc) {
		this.last_loc = last_loc;
	}
	public String getLogString() {
		StringBuilder result = new StringBuilder();
		result.append("id; ");
		result.append(id);
		result.append(last_loc.getLogString());
		return result.toString();
	}
	public Location[] getHistory() {
		return history;
	}
	public void setHistory(Location[] history) {
		this.history = history;
	}
	
	
	
}
