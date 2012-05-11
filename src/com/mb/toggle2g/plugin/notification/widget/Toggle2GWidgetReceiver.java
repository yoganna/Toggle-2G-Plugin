/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mb.toggle2g.plugin.notification.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Toggle2GWidgetReceiver extends BroadcastReceiver
{
	public static String COMMAND_ACTION = "com.mb.toggle.widget.COMMAND";
	public static String SET_ACTION = "com.mb.toggle.widget.SET";
	
	public static String currentSetting = null;
	public static long changeTimeout = 0;
	public static String changeToTimeout = null;
	

	@Override
	public void onReceive(Context context, Intent intent)
	{
		//Log.i(Toggle2G.TOGGLE2G, "user notification onReceive!");
		String stringExtra = intent.getStringExtra("setting");
		if ( stringExtra != null )
		{
			Log.i(ToggleAppWidgetProvider.TAG, "widget set to " + stringExtra);
	
			if ( "auto".equals( stringExtra ))
			{
				currentSetting = stringExtra;
			}
			else if ( "2g".equals( stringExtra ))
			{
				currentSetting = stringExtra;
			}
			else if ( "3g".equals( stringExtra ))
			{
				currentSetting = stringExtra;
			}
			
			ToggleAppWidgetProvider.updateWidgets();
		}
		
				
		stringExtra = intent.getStringExtra("command");
		if ( stringExtra != null )
		{
			changeToTimeout = stringExtra;
			changeTimeout = System.currentTimeMillis() + 10000;
			ToggleAppWidgetProvider.updateWidgets();
			
			Intent notifyIntent = new Intent(Toggle2GWidgetReceiver.COMMAND_ACTION);
			notifyIntent.putExtra("command", stringExtra);
			context.sendBroadcast(notifyIntent);
		}
	}
}
