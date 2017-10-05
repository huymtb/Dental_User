package com.prostage.l_pha.dental_user.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.MainActivity;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.common.MyApplication;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.AdminModel;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.WorkingSetModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.UserModel;
import com.prostage.l_pha.dental_user.server.DentalService;
import com.prostage.l_pha.dental_user.server.OnLoadResult;
import com.prostage.l_pha.dental_user.utils.SharedHelper;

import java.lang.reflect.Type;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by USER on 20-Apr-17.
 */

public class BackgroundService extends Service implements OnLoadResult {

    private DatabaseReference mDatabaseRef;
    private SharedHelper sharedHelper;
    private DentalService dentalService;
    private UserModel userModel;
//    private AdminModel adminModel;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(getApplicationContext());
        mDatabaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIRE_URL);
        dentalService = new DentalService(getApplicationContext(), this);
        sharedHelper = new SharedHelper(getApplicationContext());
        userModel = new UserModel();
//        adminModel = new AdminModel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (sharedHelper.getString(Constants.TOKEN) != null) {
            dentalService.requestApi(dentalService.getUserInfoById(sharedHelper.getInt(Constants.GET_ID)), Constants.TAG_BACKGROUND_USER, false);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
}

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), restartPendingIntent);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLoadComplete(String response, int idRequest) {
        Gson gson = new Gson();
        switch (idRequest) {
            case Constants.TAG_BACKGROUND_USER: {
                userModel = gson.fromJson(response, UserModel.class);

                //Set on data change of firebase
                if (userModel != null) {
                    mDatabaseRef.child(Constants.CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 0;
                            for (DataSnapshot values : dataSnapshot.getChildren()) {
                                String senderId = (String) values.child("senderId").getValue();
                                boolean read = (boolean) values.child("read").getValue();
                                if (!read && senderId.equals(userModel.getAdminId() + ""))
                                    i++;
                            }
                            if (i != 0 && !MyApplication.isChatActivityOpen()) {
                                ShortcutBadger.applyCount(getApplicationContext(), i); //count message at icon

                                String subText = userModel.getFirstName() + " " + userModel.getLastName();//admin name
                                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);//large icon
                                Uri soundNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//sound

                                Notification.Builder notification = new Notification.Builder(getApplicationContext());

                                //active CALL
//                                Intent intentCall = new Intent(Intent.ACTION_CALL);
//                                intentCall.setData(Uri.parse("tel:" + adminModel.getTel()));
//                                PendingIntent pIntentCall = PendingIntent.getActivity(getBaseContext(), 0, intentCall, 0);
//                                notification.addAction(R.drawable.ic_call, "CALL", pIntentCall);

                                //active REPLY
                                Intent intentReply = new Intent(getBaseContext(), MainActivity.class);
                                intentReply.putExtra("REPLY", "VALUE");
                                PendingIntent pIntentReply = PendingIntent.getActivity(getBaseContext(), 0, intentReply, 0);
                                notification.addAction(R.drawable.ic_reply, "REPLY", pIntentReply);

                                notification.setContentTitle("Dental Userl");
                                notification.setContentText("You get " + i + " new message!");
                                notification.setSubText(subText);//admin name
                                notification.setSmallIcon(R.drawable.ic_notification);//small icon
                                notification.setLargeIcon(largeIcon);//large icon
                                notification.setTicker("New message!");
                                notification.setSound(soundNotification);//sound
                                notification.setLights(0xff00ff00, 500, 500);
                                notification.setStyle(new Notification.BigTextStyle().bigText("You get " + i + " new message!"));
                                notification.setContentIntent(pIntentReply);

                                // hide the notification after its selected
                                notification.setAutoCancel(true);
                                notification.build().flags |= Notification.FLAG_AUTO_CANCEL;

                                //lay ra dich vu thong bao co san cua he thong, xay dung thong bao va gui len he thong
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, notification.getNotification());
                                dentalService.requestApi(dentalService.getUserInfoById(sharedHelper.getInt(Constants.GET_ID)), Constants.TAG_BACKGROUND_ADMIN, false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                break;
            }

//            case Constants.TAG_BACKGROUND_ADMIN: {
//                adminModel = gson.fromJson(response, AdminModel.class);
//                break;
//            }
        }
    }

    @Override
    public void onLoadError(String response, int idRequest, int errorCode) {

    }

}