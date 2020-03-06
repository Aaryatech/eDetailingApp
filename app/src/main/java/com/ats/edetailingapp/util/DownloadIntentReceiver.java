package com.ats.edetailingapp.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * A {@link WakefulBroadcastReceiver} that simply accepts the intent to download the files and starts {@link DownloadService}
 */
public class DownloadIntentReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(), DownloadJobService.class.getName());
		startWakefulService(context, intent.setComponent(comp));
	}

}
