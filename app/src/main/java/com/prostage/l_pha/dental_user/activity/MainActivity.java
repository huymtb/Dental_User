package com.prostage.l_pha.dental_user.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.common.MyApplication;
import com.prostage.l_pha.dental_user.fragment.ChatFragment;
import com.prostage.l_pha.dental_user.fragment.HomeFragment;
import com.prostage.l_pha.dental_user.fragment.InfoFragment;
import com.prostage.l_pha.dental_user.fragment.MapFragment;
import com.prostage.l_pha.dental_user.fragment.UserFragment;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.AdminModel;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.WorkingSetModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.ReservationModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.UserModel;
import com.prostage.l_pha.dental_user.server.DentalService;
import com.prostage.l_pha.dental_user.server.OnLoadResult;
import com.prostage.l_pha.dental_user.utils.SharedHelper;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity implements OnLoadResult {

    private TextView titleBar;

    private boolean doubleBackToExitPressedOnce = false;
    private DatabaseReference mDatabaseRef;

    private SharedHelper sharedHelper;
    private DentalService dentalService;

    private boolean isOnScreen = true;

    private UserModel userModel;
    private ReservationModel reservationModel;

    private AdminModel adminModel;
    private List<WorkingSetModel> workingSetModels;

    private AHBottomNavigation bottomNavigation = null;
    private Fragment fragment = null;
    private int currentFragmentPosition;

    public static final int REQUEST_CALL_PHONE_PERMISSIONS = 111;
    public static final int REQUEST_LOCATION_PERMISSIONS = 112;
    public static final int REQUEST_MICRO_PERMISSIONS = 113;
    public static final int REQUEST_STORAGE_PERMISSIONS = 114;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        titleBar = (TextView) findViewById(R.id.titleBar);

        sharedHelper = new SharedHelper(MainActivity.this);
        dentalService = new DentalService(this, this);
        userModel = new UserModel();
        reservationModel = new ReservationModel();
        adminModel = new AdminModel();
        workingSetModels = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        dentalService.requestApi(dentalService.getUserInfoById(sharedHelper.getInt(Constants.GET_ID)), Constants.TAG_USER, true);
        UtilsHelper.enabledBroadcastReceiver(this);
    }

    //quay lai app
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setChatActivityOpen(true);
        checkMessageRead();
        isOnScreen = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.setChatActivityOpen(false);
        UtilsHelper.enabledBroadcastReceiver(this);
        isOnScreen = false;
    }

    //minimum app
    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.setChatActivityOpen(false);
        UtilsHelper.enabledBroadcastReceiver(this);
        isOnScreen = false;
    }

    //skill app
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setChatActivityOpen(false);
        UtilsHelper.enabledBroadcastReceiver(this);
        isOnScreen = false;
    }

    private void addControls() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create navigation items
        AHBottomNavigationItem imgHome = new AHBottomNavigationItem(getString(R.string.text_home_bottom), R.drawable.ic_home, R.color.white);
        AHBottomNavigationItem imgInfo = new AHBottomNavigationItem(getString(R.string.text_info_bottom), R.drawable.ic_info, R.color.white);
        AHBottomNavigationItem imgMap = new AHBottomNavigationItem(getString(R.string.text_map_bottom), R.drawable.ic_map, R.color.white);
        AHBottomNavigationItem imgChat = new AHBottomNavigationItem(getString(R.string.text_chat_bottom), R.drawable.ic_chat, R.color.white);
        AHBottomNavigationItem imgUser = new AHBottomNavigationItem(getString(R.string.text_contact_bottom), R.drawable.ic_user, R.color.white);

        //add navigation items
        bottomNavigation.addItem(imgHome);
        bottomNavigation.addItem(imgInfo);
        bottomNavigation.addItem(imgMap);
        bottomNavigation.addItem(imgChat);
        bottomNavigation.addItem(imgUser);

        // Set background color
        bottomNavigation.setDefaultBackgroundResource(R.color.toolbar_color_bottom);

        // Change colors default
        bottomNavigation.setAccentColor(ContextCompat.getColor(this, R.color.btn_text_color));//selected
        bottomNavigation.setInactiveColor(ContextCompat.getColor(this, R.color.white));//non selected

        //Manage titles
        //SHOW_WHEN_ACTIVE ALWAYS_SHOW
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.RED);
    }


    private void addEvents() {
        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        titleBar.setText(getString(R.string.toolbar_top_text));
                        break;

                    case 1:
                        fragment = new InfoFragment();
                        titleBar.setText(getString(R.string.toolbar_info_text));
                        break;

                    case 2:
                        fragment = new MapFragment();
                        titleBar.setText(getString(R.string.toolbar_top_text));
                        /*if (!userModel.getFree()) {
                            fragment = new MapFragment();
                            titleBar.setText(getString(R.string.toolbar_top_text));
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.upgrade_to_pro), Toast.LENGTH_SHORT).show();
                        }*/
                        break;

                    case 3:
                        fragment = new ChatFragment();
                        titleBar.setText(getString(R.string.toolbar_chat_text));
                        checkMessageRead();
                        /*if (!userModel.getFree()) {
                            fragment = new ChatFragment();
                            titleBar.setText(getString(R.string.toolbar_chat_text));
                            checkMessageRead();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.upgrade_to_pro), Toast.LENGTH_SHORT).show();
                        }*/
                        break;

                    case 4:
                        fragment = new UserFragment();
                        titleBar.setText(getString(R.string.toolbar_user_text));
                        break;
                }

                if (fragment != null) {
                    if (currentFragmentPosition < position) {
                        changeFragment(fragment, true, true);
                        currentFragmentPosition = position;
                    } else if (currentFragmentPosition > position) {
                        changeFragment(fragment, true, false);
                        currentFragmentPosition = position;
                    }
                }

                return true;
            }
        });
    }

    public void setCurrentFragmentPosition(int currentFragmentPosition) {
        this.currentFragmentPosition = currentFragmentPosition;
    }

    public void changeFragment(Fragment fragment, boolean hasAnimation, boolean isLeftToRight) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (hasAnimation) {
            if (!isLeftToRight)
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            else
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        transaction.replace(R.id.frameMain, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            dentalService.requestApi(dentalService.getUserInfoById(sharedHelper.getInt(Constants.GET_ID)), Constants.TAG_USER, true);
            return true;
        } else if (id == R.id.action_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("本当にログアウトしますか")
                    .setTitle("ログアウト")
                    .setCancelable(false)
                    .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            logOut();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        UtilsHelper.disabledBroadcastReceiver(getApplicationContext());
        ShortcutBadger.removeCount(getApplicationContext());
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();

        sharedHelper.clear();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //open chat fragment from notification
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String dataReply = intent.getStringExtra("REPLY");
            if (dataReply != null) {
                checkMessageRead();

                //Init chat fragment
                currentFragmentPosition = 3;
                bottomNavigation.setCurrentItem(3);
                changeFragment(new ChatFragment(), false, false);
            }
        }
    }

    //check message
    private void checkMessageUnread() {
        mDatabaseRef.child(Constants.CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (currentFragmentPosition == 3) {
                    return;
                }

                int i = 0;
                for (DataSnapshot values : dataSnapshot.getChildren()) {
                    String senderId = (String) values.child("senderId").getValue();
                    boolean read = (boolean) values.child("read").getValue();
                    if (!read && senderId.equals(userModel.getAdminId() + ""))
                        i++;
                }
                if (i != 0 && currentFragmentPosition != 3) {
                    bottomNavigation.setNotification(i + "", 3);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //check message
    private void checkMessageRead() {
        mDatabaseRef.child(Constants.CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //remove notification
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancelAll();
                if (currentFragmentPosition == 3 && isOnScreen) {
                    //remove count at icon
                    ShortcutBadger.removeCount(getApplicationContext());
                    bottomNavigation.setNotification("", 3);
                    for (DataSnapshot values : dataSnapshot.getChildren()) {
                        String keyId = values.getKey();
                        String senderId = (String) values.child("senderId").getValue();
                        if (senderId.equals(userModel.getAdminId() + ""))
                            mDatabaseRef.child(Constants.CHAT_REF).child(userModel.getAdminId() + "_" + sharedHelper.getString(Constants.USERNAME)).child(keyId).child("read").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //request permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // Check for ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    changeFragment(new MapFragment(), false, false);
                } else {
                    // Permission Denied
                    Toast.makeText(this, getString(R.string.some_permission_denied), Toast.LENGTH_SHORT).show();
                }
            }
            break;

            case REQUEST_CALL_PHONE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + adminModel.getTel()));
//                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.call_permission_denied), Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
            break;

            case REQUEST_MICRO_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, getString(R.string.micro_permission_denied), Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
            break;

            case REQUEST_STORAGE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // Check for ACCESS_FINE_LOCATION
                if (grantResults.length > 0 && (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(this, getString(R.string.some_permission_denied), Toast.LENGTH_SHORT).show();
                }
            }

            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    //tro ve fragment home khi nhan back hoac thoat khi nhan back 2 lan
    @Override
    public void onBackPressed() {
        boolean shouldLoadHomeFragOnBackPress = true;
        if (shouldLoadHomeFragOnBackPress) {
            if (currentFragmentPosition != 0) {
                initHome(true);
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getString(R.string.click_back_again_to_exit), Toast.LENGTH_SHORT).show();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    public void initHome(boolean hasAnimation) {
        currentFragmentPosition = 0;
        bottomNavigation.setCurrentItem(0);
        changeFragment(new HomeFragment(), hasAnimation, false);
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public ReservationModel getReservationModel() {
        return reservationModel;
    }

    public AdminModel getAdminModel() {
        return adminModel;
    }

    public List<WorkingSetModel> getListWorkingSetModel() {
        return workingSetModels;
    }

    //Receive from server
    @Override
    public void onLoadComplete(String response, int idRequest) {
        Gson gson = new Gson();
        switch (idRequest) {
            case Constants.TAG_USER:
                userModel = gson.fromJson(response, UserModel.class);
                if (userModel != null) {
                    Type type = new TypeToken<List<ReservationModel>>() {
                    }.getType();
                    List<ReservationModel> reservations = gson.fromJson(userModel.getReservationModels(), type);
                    if (reservations.size() > 0) {
                        reservationModel = reservations.get(0);
                    }
                    dentalService.requestApi(dentalService.getAdminInfoById(userModel.getAdminId()), Constants.TAG_ADMIN, false);
                }
                break;

            case Constants.TAG_ADMIN:
                adminModel = gson.fromJson(response, AdminModel.class);
                if (adminModel != null) {
                    Type type = new TypeToken<List<WorkingSetModel>>() {
                    }.getType();
                    workingSetModels = gson.fromJson(adminModel.getWorkingSet(), type);

                    addEvents();
                    initHome(false);
                    checkMessageUnread();
                    onNewIntent(getIntent());
                }
                break;
        }
    }

    @Override
    public void onLoadError(String response, int idRequest, int errorCode) {
//        Toast.makeText(MainActivity.this, getString(R.string.unknown_error_network), Toast.LENGTH_SHORT).show();
        UtilsHelper.ShowDialog(this);
    }
}
