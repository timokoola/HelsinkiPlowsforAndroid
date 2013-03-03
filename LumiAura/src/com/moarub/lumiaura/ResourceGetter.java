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

import android.content.Context;

public class ResourceGetter {
	private Context fContext;
	private static ResourceGetter gInstance;
	
	public ResourceGetter(Context ctxt) {
		fContext = ctxt;
	}
	
	public static void createInstance(Context ctxt) {
		if (gInstance == null) {
			gInstance = new ResourceGetter(ctxt);
		}
	}
	
	public static String getResourceString(int id) {
		if (gInstance != null) {
			return gInstance.fContext.getResources().getString(id);
		} else {
			throw new RuntimeException("No ResourceGetter Instance");
		}
	}
	
	
}
