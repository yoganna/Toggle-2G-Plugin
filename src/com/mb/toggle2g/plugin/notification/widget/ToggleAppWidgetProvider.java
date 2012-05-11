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

import com.mb.toggle2g.plugin.notification.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

/**
 * A widget provider. We have a string that we pull from a preference in order
 * to show the configuration settings and the current time when the widget was
 * updated. We also register a BroadcastReceiver for time-changed and
 * timezone-changed broadcasts, and update then too.
 * 
 * <p>
 * See also the following files:
 * <ul>
 * <li>ExampleAppWidgetConfigure.java</li>
 * <li>ToggleBroadcastReceiver.java</li>
 * <li>res/layout/appwidget_configure.xml</li>
 * <li>res/layout/appwidget_provider.xml</li>
 * <li>res/xml/appwidget_provider.xml</li>
 * </ul>
 */
public class ToggleAppWidgetProvider extends AppWidgetProvider
{
	// log tag
	static final String TAG = "Toggle2G";
	static ToggleAppWidgetProvider provider;

	Handler handler = new Handler();
	boolean postedCallback = false;
	PhoneListener phoneListener = new PhoneListener();
	Context context;

	public ToggleAppWidgetProvider()
	{
		// TODO Auto-generated constructor stub
		Log.d(ToggleAppWidgetProvider.TAG, "ToggleAppWidgetProvider()");
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		Log.d(ToggleAppWidgetProvider.TAG, "onReceive=" + intent);
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		Log.d(ToggleAppWidgetProvider.TAG, "onUpdate");

		provider = this;
		this.context = context;

		getSetting();
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++)
		{
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	@Override
	public void onEnabled(Context context)
	{
		Log.d(TAG, "onEnabled");

		provider = this;

		this.context = context;
		getSetting();

		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName("com.mb.toggle2g.plugin.notification", ".widget.Toggle2GWidgetReceiver"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
	}

	@Override
	public void onDisabled(Context context)
	{
		Log.d(TAG, "onDisabled");

		// provider = null;

		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_NONE);

		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName("com.mb.toggle2g.plugin.notification", ".widget.Toggle2GWidgetReceiver"),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}

	void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
	{
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_toggle);
		Intent notifyIntent = null;

		String setting = Toggle2GWidgetReceiver.currentSetting;
		Log.d(TAG, "updateAppWidget setting=" + setting + ", appWidgetId=" + appWidgetId );
		if (setting == null)
		{
			views.setImageViewResource(R.id.appwidget_image, R.drawable.in_change);
			if ( !postedCallback )
			{
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						getSetting();
					}
				}, 10000);
				postedCallback = true;
			}
		}
		else if (Toggle2GWidgetReceiver.changeTimeout > System.currentTimeMillis() && !setting.equals(Toggle2GWidgetReceiver.changeToTimeout))
		{
			// Log.d(TAG, "updateAppWidget to waiting for change" );
			views.setImageViewResource(R.id.appwidget_image, R.drawable.in_change);
			
			if ( !postedCallback )
			{
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						getSetting();
					}
				}, 10000);
				postedCallback = true;
			}
		}
		else if ("auto".equals(setting))
		{
			// Log.d(TAG, "updateAppWidget with auto and to call 2g" );
			views.setImageViewResource(R.id.appwidget_image, R.drawable.in_auto);
			notifyIntent = new Intent(Toggle2GWidgetReceiver.SET_ACTION);
			notifyIntent.putExtra("command", "2g");
			Toggle2GWidgetReceiver.changeTimeout = 0;
			Toggle2GWidgetReceiver.changeToTimeout = null;
		}
		else if ("2g".equals(setting))
		{
			// Log.d(TAG, "updateAppWidget with 2g and to call 3g" );
			views.setImageViewResource(R.id.appwidget_image, R.drawable.in_2g);
			notifyIntent = new Intent(Toggle2GWidgetReceiver.SET_ACTION);
			notifyIntent.putExtra("command", "3g");
			Toggle2GWidgetReceiver.changeTimeout = 0;
			Toggle2GWidgetReceiver.changeToTimeout = null;
		}
		else
		// if ( "3g".equals( Toggle2GGetActivity.currentSetting ))
		{
			// Log.d(TAG, "updateAppWidget with 3g and to call auto" );
			views.setImageViewResource(R.id.appwidget_image, R.drawable.in_3g);
			notifyIntent = new Intent(Toggle2GWidgetReceiver.SET_ACTION);
			notifyIntent.putExtra("command", "auto");
			Toggle2GWidgetReceiver.changeTimeout = 0;
			Toggle2GWidgetReceiver.changeToTimeout = null;
		}

		if (notifyIntent != null)
		{
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.appwidget_image, pendingIntent);
		}

		// Tell the widget manager
		if (appWidgetId >= 0)
		{
			// Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId );
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		else
		{
			// For each widget that needs an update, get the text that we should
			// display:
			// - Create a RemoteViews object for it
			// - Tell the AppWidgetManager to show that views object for the
			// widget.
			ComponentName thisWidget = new ComponentName(context, ToggleAppWidgetProvider.class);
			int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
			if (appWidgetIds != null)
			{
				final int N = appWidgetIds.length;
				for (int i = 0; i < N; i++)
				{
					appWidgetId = appWidgetIds[i];
					// Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId
					// );
					updateAppWidget(context, appWidgetManager, appWidgetId);
				}
			}
			appWidgetManager.updateAppWidget(thisWidget, views);
		}
	}

	public class PhoneListener extends PhoneStateListener
	{
		@Override
		public void onDataConnectionStateChanged(int state, int networkType)
		{
			// TODO Auto-generated method stub
			super.onDataConnectionStateChanged(state, networkType);
			getSetting();
		}
	}

	public static void updateWidgets()
	{
		ToggleAppWidgetProvider app = provider;
		if (app != null && app.context != null)
		{
			AppWidgetManager manager = AppWidgetManager.getInstance(app.context);
			app.updateAppWidget(app.context, manager, -1);
		}
	}

	public void getSetting()
	{
		postedCallback = false;
		Log.i(ToggleAppWidgetProvider.TAG, "command:get");
		Intent notifyIntent = new Intent(Toggle2GWidgetReceiver.COMMAND_ACTION);
		notifyIntent.putExtra("command", "get");
		context.sendBroadcast(notifyIntent);
	}
}
