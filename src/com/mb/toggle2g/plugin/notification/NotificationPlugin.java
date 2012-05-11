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
package com.mb.toggle2g.plugin.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPlugin extends BroadcastReceiver
{
	String ACTION_ENABLE_3G = "com.mb.notification.ENABLE_3G";

	@Override
	public void onReceive(Context context, Intent intent)
	{
        boolean booleanExtra = intent.getBooleanExtra("show", false);
		Log.i("Toggle2G", "show plugin message = " + booleanExtra); 
		
		android.app.NotificationManager mNotificationManager = (android.app.NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if ( booleanExtra)
		{
			Intent notifyIntent = new Intent(ACTION_ENABLE_3G);
			PendingIntent intent1 = PendingIntent.getBroadcast(context, 0, notifyIntent, 0);
			
			int icon = R.drawable.only2g;
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon, "2G is still enabled", when);
			notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(context, "2G is still enabled", "Select to re-enable 3G now", intent1);
			
			mNotificationManager.notify(1, notification);
		}
		else
		{
			mNotificationManager.cancelAll();
		}
	}
}
