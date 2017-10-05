package com.prostage.l_pha.dental_user.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.LoginActivity;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.notification.AlarmReceiver;
import com.prostage.l_pha.dental_user.notification.BackgroundService;

import java.io.IOException;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by USER on 26-Jun-17.
 */

public class UtilsHelper {

    public static String subStr(String str, int firstIndex, int lastIndex) {
        return str.substring(firstIndex, lastIndex);
    }

    public static String subDateTime(String subStr) {//"2017-05-18 02:49:55" -> 2017-05-18 02:49
        String subDateTime = subStr.substring(0, 16);
        return subDateTime;
    }

    public static String subDate_yyyyMMdd(String subStr) {//"2017-05-18 02:49:55" -> 2017年05月18日
        String subYear = subStr.substring(0, 4);
        String subMonth = subStr.substring(5, 7);
        String subDay  = subStr.substring(8, 10);
        return subYear + "年" + subMonth + "月" + subDay + "日";
    }

    public static String subDate_ddMMyyy(String subStr) {//"2017-05-18 02:49:55" -> 2017年05月18日
        String subDay = subStr.substring(0, 2);
        String subMonth = subStr.substring(3, 5);
        String subYear  = subStr.substring(6, 10);
        return subYear + "年" + subMonth + "月" + subDay + "日";
    }

    public static String subTime_yyyyMMdd(String subStr) {//"2017-05-18 02:49:55" -> 2017年05月18日
        String subHH = subStr.substring(11, 13);
        String subMM = subStr.substring(14, 16);
        return subHH + "時" + subMM + "分";
    }

    public static String subTime(String subStr) {//"2017-05-18 02:49:55" -> 02:49
        return subStr.substring(11, 16);
    }

    public static String formatTime(int value) {
        return value < 10 ? "0" + value : value + "";//cong them 0 vao truoc value
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {

            ex.printStackTrace();
        }
        return p1;
    }

    public static void ShowDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_network_not_available)
                .setTitle(R.string.title_network_not_available)//no network connection
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //start service
    public static void enabledBroadcastReceiver(Context context) {
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, AlarmReceiver.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        context.startService(new Intent(context, BackgroundService.class));

        String alarm = Context.ALARM_SERVICE;
        AlarmManager am = (AlarmManager) context.getSystemService(alarm);
        Intent intent = new Intent("REFRESH_THIS");
        PendingIntent pi = PendingIntent.getBroadcast(context, 123456789, intent, 0);
        int type = AlarmManager.RTC_WAKEUP;
//        long interval = 1000 * 50;
        long interval = 0;
        am.setInexactRepeating(type, System.currentTimeMillis(), interval, pi);
    }

    //stop service
    public static void disabledBroadcastReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        context.stopService(new Intent(context, BackgroundService.class));

        String alarm = Context.ALARM_SERVICE;
        AlarmManager am = (AlarmManager) context.getSystemService(alarm);
        Intent intent = new Intent("REFRESH_THIS");
        PendingIntent pi = PendingIntent.getBroadcast(context, 123456789, intent, 0);
        am.cancel(pi);
//        int type = AlarmManager.RTC_WAKEUP;
//        long interval = 1000 * 50;
//        am.setInexactRepeating(type, System.currentTimeMillis(), interval, pi);
        Intent background = new Intent(context, AlarmReceiver.class);
        context.stopService(background);
    }

    public static void askToOpenGps(final Activity context){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.open_location_settings));
        alertDialog.setMessage(context.getString(R.string.gps_network_not_enabled));
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivityForResult(intent, 3001);
                dialogInterface.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
