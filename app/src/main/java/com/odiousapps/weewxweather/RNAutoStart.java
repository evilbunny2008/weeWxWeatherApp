package com.odiousapps.weewxweather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RNAutoStart extends BroadcastReceiver
{
    public void onReceive(Context c, Intent i)
    {
        Common.LogMessage("Broadcast intent detected " + i.getAction());

        Common.LogMessage("RNAutostart: i=" + i.toString());
        Common common = new Common(c);
	    common.getWeather();

	    long[] ret = common.getPeriod();
	    long period = ret[0];
	    long wait = ret[1];
	    if(period <= 0)
		    return;

	    long start = Math.round((double)System.currentTimeMillis() / (double)period) * period + wait;

	    Common.LogMessage("weewxstart == " + start);
	    Common.LogMessage("weewxperiod == " + period);
	    Common.LogMessage("weewxwait == " + wait);

	    AlarmManager mgr = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(c, UpdateCheck.class);

        if(mgr != null)
        {
	        PendingIntent pi = PendingIntent.getBroadcast(c, 0, myIntent, 0);
	        mgr.setExact(AlarmManager.RTC_WAKEUP, start, pi);
        }

        Common.LogMessage("onReceive() end");
    }
}