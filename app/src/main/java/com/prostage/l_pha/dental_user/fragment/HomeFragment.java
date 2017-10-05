package com.prostage.l_pha.dental_user.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.MainActivity;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.model.server_model.admin_model.AdminModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.ReservationModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.UserModel;
import com.prostage.l_pha.dental_user.utils.SharedHelper;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

import static com.prostage.l_pha.dental_user.activity.MainActivity.REQUEST_CALL_PHONE_PERMISSIONS;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Button btnCall, btnChat;
    private TextView txtFullName, txtUsername, txtReservationDate, txtReservationTime;
    private SharedHelper sharedHelper;

    private UserModel userModel;
    private ReservationModel reservationModel;

    private AdminModel adminModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        btnCall = (Button) homeView.findViewById(R.id.buttonCall);
        btnChat = (Button) homeView.findViewById(R.id.buttonChat);
        txtFullName = (TextView) homeView.findViewById(R.id.textViewName);
        txtUsername = (TextView) homeView.findViewById(R.id.textViewID);
        txtReservationDate = (TextView) homeView.findViewById(R.id.textViewReservationDate);
        txtReservationTime = (TextView) homeView.findViewById(R.id.textViewReservationTime);

        return homeView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedHelper = new SharedHelper(getActivity());
        userModel = new UserModel();
        reservationModel = new ReservationModel();
        adminModel = new AdminModel();

        userModel = ((MainActivity) getActivity()).getUserModel();
        adminModel = ((MainActivity) getActivity()).getAdminModel();
        reservationModel = ((MainActivity) getActivity()).getReservationModel();

        txtUsername.setText(sharedHelper.getString(Constants.USERNAME) != null ? sharedHelper.getString(Constants.USERNAME) : "");
        txtFullName.setText((userModel.getFirstName() != null && userModel.getLastName() != null) ? (userModel.getFirstName() + " " + userModel.getLastName() + " 様") : "");
        if (reservationModel.getReservationDate() != null) {
            txtReservationDate.setText(UtilsHelper.subDate_yyyyMMdd(reservationModel.getReservationDate()));
            txtReservationTime.setText(UtilsHelper.subTime(reservationModel.getReservationDate()));
        } else {
            txtReservationDate.setText("");
            txtReservationTime.setText("");
        }

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setCurrentFragmentPosition(3);//neu fragment = 3 (chat) se tro ve home
                ((MainActivity) getActivity()).changeFragment(new ChatFragment(), true, true);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    showConfirmDialog(getString(R.string.btn_call_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + adminModel.getTel()));
                            startActivity(intent);
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSIONS);
                }
            }
        });
    }

    private void showConfirmDialog(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }
}

