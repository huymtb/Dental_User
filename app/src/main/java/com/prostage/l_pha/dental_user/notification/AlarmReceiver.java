package com.prostage.l_pha.dental_user.notification;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by USER on 20-Apr-17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        System.out.println("Alarma Reciver Called");

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, BackgroundService.class);
            context.startService(serviceIntent);
        }

        if (isMyServiceRunning(this.context, BackgroundService.class)) {
            System.out.println("alredy running no need to start again");
        } else {
            Intent background = new Intent(context, BackgroundService.class);
            context.startService(background);
        }
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                if ((serviceClass.getName()).equals(services.get(i).service.getClassName()) && services.get(i).pid != 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
