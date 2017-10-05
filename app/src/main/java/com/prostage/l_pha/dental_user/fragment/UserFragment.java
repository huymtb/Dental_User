package com.prostage.l_pha.dental_user.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.activity.MainActivity;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.model.server_model.user_model.ReservationModel;
import com.prostage.l_pha.dental_user.model.server_model.user_model.UserModel;
import com.prostage.l_pha.dental_user.server.DentalService;
import com.prostage.l_pha.dental_user.server.OnLoadResult;
import com.prostage.l_pha.dental_user.utils.SharedHelper;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.prostage.l_pha.dental_user.R.id.textViewReservationDate;
import static com.prostage.l_pha.dental_user.R.id.text_change_name;
import static com.prostage.l_pha.dental_user.R.id.text_change_password;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements OnLoadResult {

    private TextView txtUsername, txtFullName, txtFullNickName, txtBirthday, txtReservationDate, txtChangePassword, txtChangeName;

    private SharedHelper sharedHelper;

    private UserModel userModel;
    private ReservationModel reservationModel;
    private DentalService dentalService;

    private EditText firstName;
    private EditText firstNickName;
    private EditText lastName;
    private EditText lastNickName;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dentalService = new DentalService(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View userView = inflater.inflate(R.layout.fragment_user, container, false);

        txtUsername = (TextView) userView.findViewById(R.id.textViewUsername);
        txtFullName = (TextView) userView.findViewById(R.id.textViewFullName);
        txtFullNickName = (TextView) userView.findViewById(R.id.textViewFullNickName);
        txtBirthday = (TextView) userView.findViewById(R.id.textViewBirthday);
        txtReservationDate = (TextView) userView.findViewById(textViewReservationDate);
        txtChangeName = (TextView) userView.findViewById(text_change_name);
        txtChangePassword = (TextView) userView.findViewById(text_change_password);

        return userView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedHelper = new SharedHelper(getActivity());
        userModel = new UserModel();
        reservationModel = new ReservationModel();

        userModel = ((MainActivity) getActivity()).getUserModel();
        reservationModel = ((MainActivity) getActivity()).getReservationModel();

        txtUsername.setText(sharedHelper.getString(Constants.USERNAME) != null ? sharedHelper.getString(Constants.USERNAME) : "");
        txtFullName.setText((userModel.getFirstName() != null && userModel.getLastName() != null) ? (userModel.getFirstName() + " " + userModel.getLastName()) : "");
        txtFullNickName.setText((userModel.getFirstNickName() != null && userModel.getLastNickName() != null) ? (userModel.getFirstNickName() + " " + userModel.getLastNickName()) : "");
        txtBirthday.setText(userModel.getBirthday() != null ? UtilsHelper.subDate_yyyyMMdd(userModel.getBirthday()) : "");
        txtReservationDate.setText(reservationModel.getReservationDate() != null ? UtilsHelper.subDate_yyyyMMdd(reservationModel.getReservationDate()) + " " + UtilsHelper.subTime_yyyyMMdd(reservationModel.getReservationDate()) : "");

        txtChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeName();
            }
        });

        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePassword();
            }
        });
    }

    public void ChangePassword() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.text_change_pass);
        final EditText oldPass = new EditText(getActivity());
        final EditText newPass = new EditText(getActivity());
        final EditText confirmPass = new EditText(getActivity());

        oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        oldPass.setHint(R.string.text_enter_old_password);
        newPass.setHint(R.string.text_enter_new_password);
        confirmPass.setHint(R.string.text_enter_confirm_password);

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(oldPass);
        ll.addView(newPass);
        ll.addView(confirmPass);
        alertDialog.setView(ll);

        if ((TextUtils.isEmpty(oldPass.getText().toString()) || TextUtils.isEmpty(newPass.getText().toString()) || TextUtils.isEmpty(confirmPass.getText().toString()))
                || !newPass.getText().toString().equals(confirmPass.getText().toString())) {//newPassword.length() < 8 && !isValidPassword(newPassword)
            Toast.makeText(getActivity(), "Password Not Valid..!", Toast.LENGTH_SHORT).show();
        }

        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dentalService.requestApi(dentalService.setPassword(sharedHelper.getInt(Constants.GET_ID), newPass.getText().toString(), oldPass.getText().toString()), Constants.TAG_CHANGE_PASS, true);
                    }
                });

        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public void ChangeName() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.text_change_name);

        firstName = new EditText(getActivity());
        firstNickName = new EditText(getActivity());
        lastName = new EditText(getActivity());
        lastNickName = new EditText(getActivity());

        firstName.setHint(R.string.text_enter_first_name);
        firstNickName.setHint(R.string.text_enter_first_nickname);
        lastName.setHint(R.string.text_enter_last_name);
        lastNickName.setHint(R.string.text_enter_last_nickname);

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(firstName);
        ll.addView(firstNickName);
        ll.addView(lastName);
        ll.addView(lastNickName);
        alertDialog.setView(ll);

        if ((TextUtils.isEmpty(firstName.getText().toString()) || TextUtils.isEmpty(firstNickName.getText().toString())
                || TextUtils.isEmpty(lastName.getText().toString())) || TextUtils.isEmpty(lastNickName.getText().toString())) {//newPassword.length() < 8 && !isValidPassword(newPassword)
            Toast.makeText(getActivity(), "Name Not Valid..!", Toast.LENGTH_SHORT).show();
        }

        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dentalService.requestApi(dentalService.setName(sharedHelper.getInt(Constants.GET_ID), firstName.getText().toString(), firstNickName.getText().toString(),
                                lastName.getText().toString(), lastNickName.getText().toString(), UtilsHelper.subDateTime(reservationModel.getReservationDate())), Constants.TAG_CHANGE_NAME, true);
                    }
                });

        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onLoadComplete(String response, int idRequest) {
        switch (idRequest) {
            case Constants.TAG_CHANGE_PASS:
                Toast.makeText(getActivity(), "Change password has been successful", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).logOut();
                break;

            case Constants.TAG_CHANGE_NAME:
                Toast.makeText(getActivity(), "Change name has been successful", Toast.LENGTH_SHORT).show();
                txtFullName.setText(firstName.getText().toString() + " " + lastName.getText().toString());
                txtFullNickName.setText(firstNickName.getText().toString() + " " + lastNickName.getText().toString());
                break;
        }
    }

    @Override
    public void onLoadError(String response, int idRequest, int errorCode) {
        switch (idRequest) {
            case Constants.TAG_CHANGE_PASS:
                Toast.makeText(getActivity(), "Change password error..!", Toast.LENGTH_SHORT).show();
                break;

            case Constants.TAG_CHANGE_NAME:
                Toast.makeText(getActivity(), "Change name error..!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
