package com.ats.edetailingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.constant.Constants;
import com.ats.edetailingapp.model.LoginModel;
import com.ats.edetailingapp.sqlitedb.DatabaseHelper;
import com.ats.edetailingapp.util.CommonDialog;
import com.ats.edetailingapp.util.CustomSharedPreference;
import com.ats.edetailingapp.util.PermissionsUtil;
import com.ats.edetailingapp.util.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edUsername, edPassword;
    private Button btnLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (PermissionsUtil.checkAndRequestPermissions(this)) {

        }

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        db = new DatabaseHelper(LoginActivity.this, 1);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            String username = edUsername.getText().toString();
            String pass = edPassword.getText().toString();

            if (username.isEmpty()) {
                edUsername.setError("required");
            } else if (pass.isEmpty()) {
                edUsername.setError(null);
                edPassword.setError("required");
            } else {

                loginUser(username, pass, Utility.getImei(LoginActivity.this));

            }
        }
    }


    public void loginUser(final String username, final String pass, String imei) {

        Log.e("PARAM : ", "--------------username : " + username + "_________ Pass : " + pass + "_______________ IMEI : " + imei);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<LoginModel> listCall = Constants.myInterface.doLogin(username, pass, imei);
            listCall.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Login Data : ", "------------" + response.body());

                            LoginModel data = response.body();
                            if (data == null) {
                                Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                if (data.getError() == 0) {

                                    CustomSharedPreference.putInt(LoginActivity.this, CustomSharedPreference.KEY_USER_ID, data.getUserid());
                                    CustomSharedPreference.putString(LoginActivity.this, CustomSharedPreference.KEY_USERNAME, username);
                                    CustomSharedPreference.putString(LoginActivity.this, CustomSharedPreference.KEY_PASSWORD, pass);

                                    db.addUser(data.getUserid(), username, pass,0);

                                    startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }


}
