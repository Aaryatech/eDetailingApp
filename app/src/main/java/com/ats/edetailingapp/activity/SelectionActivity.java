package com.ats.edetailingapp.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.adapter.HomeMenuAdapter;
import com.ats.edetailingapp.constant.Constants;
import com.ats.edetailingapp.model.FileDatum;
import com.ats.edetailingapp.model.LoginModel;
import com.ats.edetailingapp.model.SelectionDataModel;
import com.ats.edetailingapp.model.Tag1;
import com.ats.edetailingapp.model.Tag2;
import com.ats.edetailingapp.sqlitedb.DatabaseHelper;
import com.ats.edetailingapp.util.CommonDialog;
import com.ats.edetailingapp.util.Cryptic;
import com.ats.edetailingapp.util.CustomSharedPreference;
import com.ats.edetailingapp.util.DownloadJobService;
import com.ats.edetailingapp.util.DownloadService;
import com.ats.edetailingapp.util.PermissionsUtil;
import com.ats.edetailingapp.util.Utility;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectionActivity extends AppCompatActivity {

    String username, pass, imei;
    int userId;

    DatabaseHelper dbHelper;

    HomeMenuAdapter adapter;
    ArrayList<Tag2> menuList = new ArrayList<>();

    private GridView gridView;
    private ImageView ivSync, ivLogout;

    List<String> fileIdList = new ArrayList<String>();
    List<String> fileDisplayList = new ArrayList<String>();

    Intent intent;

    private static final String QUERY_GET_FILE_PATH = "SELECT " + DatabaseHelper.FILE_LOCAL_PATHS + ", " + DatabaseHelper.FILE_TYPE + " FROM " + DatabaseHelper.FILE_TABLE + " WHERE " + DatabaseHelper.FILE_TITLE + " = ?";


    //---------------IMAGE----------------
    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, Utility.SIMPLE_HIDDEN_DIRECTORY);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        if (PermissionsUtil.checkAndRequestPermissions(this)) {

        }


        gridView = findViewById(R.id.gridView);
        ivSync = findViewById(R.id.ivSync);
        ivLogout = findViewById(R.id.ivLogout);

        dbHelper = new DatabaseHelper(SelectionActivity.this, 1);

        try {
            username = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_USERNAME);
            pass = CustomSharedPreference.getString(this, CustomSharedPreference.KEY_PASSWORD);
            imei = Utility.getImei(this);
            userId = CustomSharedPreference.getInt(this, CustomSharedPreference.KEY_USER_ID);

            Log.e("USER ID : ", "----------------" + userId);
            if (userId == 0) {
                startActivity(new Intent(SelectionActivity.this, LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            Log.e("USER ID : ", "-------ERROR---------" + e.getMessage());
        }


//        dbHelper.getTag1Data();
//        dbHelper.getAllTag2();
//        dbHelper.getUserData();
//        dbHelper.getAllMData();
//        dbHelper.getFileTypeData();
        dbHelper.getFileDataArray();

        createFolder();

        refreshList();


        try {
            Tag1 tag1 = dbHelper.getTag1(userId);
            Log.e("TAG2  List: ", "--------------" + dbHelper.getAllTag2Data(tag1.getT1Tid()));
            Log.e("FILE TYPE ", "----------------" + dbHelper.getFileTypeList(tag1.getT1Tid(), 1));
            Log.e("LANG  ", "----------------" + dbHelper.getAllLang(tag1.getT1Tid(), 1, 2));
            Log.e("FILES  ", "----------------" + dbHelper.getFilteredTag2Files(1));
            Log.e("SUB FILES  ", "----------------" + dbHelper.getFilteredTag2SubFiles(1, 6));
            Log.e("SUB LANG FILES  ", "----------------" + dbHelper.getFilteredTag2SubLangFiles(1, 6, 1));

            fileDisplayList = dbHelper.getFiles();

        } catch (Exception e) {
            Log.e("SELECTION ACTIVITY", "-------------EXCEPTION : " + e.getMessage());
            e.printStackTrace();

        }

        ivSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllData(username, pass, imei, userId);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
                builder.setMessage("Are you sure you want to logout?");
                builder.setTitle("Logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CustomSharedPreference.deletePreference(SelectionActivity.this);
                        dbHelper.removeAll();
                        Intent intent = new Intent(SelectionActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert = builder.create();
                alert.show();
            }
        });

    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    public void getAllData(final String username, final String pass, String imei, final int userId) {

        Log.e("PARAM : ", "--------------username : " + username + "_________ Pass : " + pass + "_______________ IMEI : " + imei);

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<SelectionDataModel> listCall = Constants.myInterface.getAllData(username, pass, imei, userId);
            listCall.enqueue(new Callback<SelectionDataModel>() {
                @Override
                public void onResponse(Call<SelectionDataModel> call, Response<SelectionDataModel> response) {
                    try {
                        if (response.body() != null) {

                            Log.e("Selection Data : ", "------------" + response.body());

                            Log.e("FILE DATA : ", "----------------" + response.body().getFileData());

                            SelectionDataModel data = response.body();
                            if (data == null) {
                                Toast.makeText(SelectionActivity.this, "Unable to login!", Toast.LENGTH_SHORT).show();
                                commonDialog.dismiss();
                            } else {

                                if (data.getError() == 0) {

                                    dbHelper.updateUser(data.getTag1().getT1Tid(), userId);

                                    dbHelper.addTag1(data.getTag1());

                                    for (int i = 0; i < data.getTag2().size(); i++) {
                                        dbHelper.addTag2(data.getTag2().get(i));
                                    }

                                    for (int i = 0; i < data.getFileType().size(); i++) {
                                        dbHelper.addFileType(data.getFileType().get(i));
                                    }

                                    for (int i = 0; i < data.getLang().size(); i++) {
                                        dbHelper.addLang(data.getLang().get(i));
                                    }

                                    for (int i = 0; i < data.getMData().size(); i++) {
                                        dbHelper.addMData(data.getMData().get(i));
                                    }

                                    for (int i = 0; i < data.getFtData().size(); i++) {
                                        dbHelper.addFtData(data.getFtData().get(i));
                                    }


                                    for (int i = 0; i < data.getFileData().size(); i++) {
                                        Log.e("F PATH", "-------------" + data.getFileData().get(i).getfPath());
                                        ArrayList<FileDatum> fileDataList = dbHelper.getAllFileData(data.getFileData().get(i).getfPath());
                                        if (fileDataList.size() <= 0) {
                                            dbHelper.addFileData(data.getFileData().get(i));
                                            fileIdList.add("" + data.getFileData().get(i).getfId());
                                        } else {
                                            dbHelper.updateFileData(data.getFileData().get(i).getfTitle(), data.getFileData().get(i).getIsActive(), data.getFileData().get(i).getfId());
                                            fileIdList.add("" + data.getFileData().get(i).getfId());
                                        }
                                    }

                                    String fileIdString = "(";
                                    for (int j = 0; j < fileIdList.size(); j++) {
                                        if (j == fileIdList.size() - 1)
                                            fileIdString += fileIdList.get(j);
                                        else
                                            fileIdString += fileIdList.get(j) + " ,";
                                    }
                                    fileIdString += ")";
                                    Log.e("FILE ID LIST", "where clause------------------- : " + fileIdString);

                                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                                    db.beginTransaction();
                                    try {
                                        Log.e("TRY BLOCK", "--------------------------------------------");
                                        Cursor c = db.query(true, DatabaseHelper.FILE_TABLE, new String[]{DatabaseHelper.FILE_LOCAL_PATHS}, DatabaseHelper.FILE_ID + " NOT IN " + fileIdString, null, null, null, null, null);
                                        while (c.moveToNext()) {
                                            File file = new File(Environment.getExternalStorageDirectory() + "/" + Utility.SIMPLE_HIDDEN_DIRECTORY + "/" + c.getString(0));
                                            file.delete();
                                        }
                                        db.delete(DatabaseHelper.FILE_TABLE, DatabaseHelper.FILE_ID + " NOT IN " + fileIdString, null);
                                        db.setTransactionSuccessful();
                                        Log.e("DB", "------------------------------------------------------");
                                    } finally {
                                        db.endTransaction();
                                    }


                                } else {
                                    Toast.makeText(SelectionActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                                }

                                commonDialog.dismiss();
                            }
                        } else {
                            commonDialog.dismiss();
                            Log.e("Data Null : ", "-----------");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Exception : ", "-----------" + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        if (!DownloadJobService.isDownloading()) {
                            DownloadJobService.enqueueWork(SelectionActivity.this, new Intent(SelectionActivity.this, DownloadJobService.class));
                            SelectionActivity.this.sendBroadcast(new Intent(Utility.DOWNLOAD_FILES_INTENT));
                        }
                        Log.e("FINALLY : ", "sent broadcast... now exiting");
                        refreshList();

                        String dateStr = (new Date()).toString();
                        CustomSharedPreference.putString(SelectionActivity.this, CustomSharedPreference.KEY_LAST_SYNCED, dateStr);

                        int versionCode = 0;
                        String versionName = "1";

                        try {
                            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;

                            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                        } catch (PackageManager.NameNotFoundException e1) {
                            e1.printStackTrace();
                        }
//                        getSupportActionBar().setSubtitle("Last Synchronized on " + dateStr + "\tVersion : " + versionName + "." + versionCode);

                    }
                }

                @Override
                public void onFailure(Call<SelectionDataModel> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("onFailure : ", "-----------" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshList() {

        try {
            Tag1 tag1 = dbHelper.getTag1(userId);

            menuList.clear();
            menuList.addAll(dbHelper.getAllTag2Data(tag1.getT1Tid()));

            adapter = new HomeMenuAdapter(menuList, this, tag1.getT1Tid());
            gridView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("SELECTION ACTIVITY", "-------------EXCEPTION : " + e.getMessage());
            e.printStackTrace();

        }

    }

    @Override
    public void onBackPressed() {
        alertOnExit();
    }

    private void alertOnExit() {
        AlertDialog alert = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setTitle("Exit the app");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SelectionActivity.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.deleteFolderContents(Environment.getExternalStorageDirectory() + File.separator + Utility.SIMPLE_HIDDEN_TEMP_DIRECTORY);
    }


}
