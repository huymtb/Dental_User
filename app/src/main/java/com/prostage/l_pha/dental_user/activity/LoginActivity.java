package com.prostage.l_pha.dental_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prostage.l_pha.dental_user.R;
import com.prostage.l_pha.dental_user.common.Constants;
import com.prostage.l_pha.dental_user.model.server_model.TokenModel;
import com.prostage.l_pha.dental_user.server.DentalService;
import com.prostage.l_pha.dental_user.server.OnLoadResult;
import com.prostage.l_pha.dental_user.utils.SharedHelper;
import com.prostage.l_pha.dental_user.utils.UtilsHelper;

public class LoginActivity extends AppCompatActivity implements OnLoadResult {

    private EditText edtUsername, edtPassword;
    private Button btnSignin;
    private CheckBox cbRemember;

    private DentalService dentalService;
    private SharedHelper sharedHelper;

    private String username = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedHelper = new SharedHelper(this);
        dentalService = new DentalService(this, this);

        addControls();
        addEvents();
    }

    private void addControls() {
        edtUsername = (EditText) findViewById(R.id.editTextEmail);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnSignin = (Button) findViewById(R.id.buttonSignin);
        cbRemember = (CheckBox) findViewById(R.id.checkBoxRemember);

        edtUsername.setText(sharedHelper.getString("hintUsername") != null ? sharedHelper.getString("hintUsername") : "");
        edtPassword.setText(sharedHelper.getString("hintPassword") != null ? sharedHelper.getString("hintPassword") : "");
    }

    private void addEvents() {
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                //kiem tra username rong
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getBaseContext(), R.string.text_username_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                //kiem tra password rong
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getBaseContext(), R.string.text_password_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                dentalService.requestApi(dentalService.login(username, password), Constants.TAG_LOGIN, true);
            }
        });
    }

    @Override
    public void onLoadComplete(String response, int idRequest) {
        Gson gson = new Gson();
        switch (idRequest) {
            case Constants.TAG_LOGIN:
                sharedHelper.remove("hintUsername");
                sharedHelper.remove("hintPassword");

                TokenModel tokenModel = gson.fromJson(response, TokenModel.class);
                if (tokenModel != null) {
                    if (cbRemember.isChecked()) {
                        sharedHelper.saveString("hintUsername", username);
                        sharedHelper.saveString("hintPassword", password);
                    }

                    sharedHelper.saveString(Constants.USERNAME, username);
                    sharedHelper.saveInt(Constants.GET_ID, tokenModel.getId());
                    sharedHelper.saveString(Constants.TOKEN, tokenModel.getToken());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        }
    }

    @Override
    public void onLoadError(String response, int idRequest, int errorCode) {
//        Toast.makeText(LoginActivity.this, getString(R.string.unknown_error_network), Toast.LENGTH_SHORT).show();
        UtilsHelper.ShowDialog(this);
    }
}

