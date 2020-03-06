package com.ats.edetailingapp.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.activity.PDFViewActivity;
import com.ats.edetailingapp.activity.VideoActivity;
import com.ats.edetailingapp.sqlitedb.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileOpener extends AsyncTask<Object, Void, String[]> {

    Intent intent;
    File encFile, tempDir, tempFile;
    String fName, fType, mimeType;
    int id;
    CommonDialog commonDialog;
    Context context;
    DatabaseHelper dbHelper;
    List<String> fileDisplayList = new ArrayList<String>();


    private static final String QUERY_GET_FILE_PATH = "SELECT " + DatabaseHelper.FILE_LOCAL_PATHS + ", " + DatabaseHelper.FILE_TYPE + " FROM " + DatabaseHelper.FILE_TABLE + " WHERE " + DatabaseHelper.FILE_TITLE + " = ?";


    public FileOpener(int id) {
        this.id = id;
    }

    public FileOpener(int id, Context context) {
        this.id = id;
        this.context = context;
        dbHelper = new DatabaseHelper(context, 1);
    }

    public FileOpener(int id, Context context, List<String> fileDisplayList) {
        this.id = id;
        this.context = context;
        this.fileDisplayList = fileDisplayList;
        dbHelper = new DatabaseHelper(context, 1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        commonDialog = new CommonDialog(context, "Opening File", "Please be patient, this may take a while...");
        commonDialog.show();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY_GET_FILE_PATH, new String[]{fileDisplayList.get(id)});
        cursor.moveToFirst();
        fName = cursor.getString(0);
        fType = cursor.getString(1);
        db.close();
    }

    @Override
    protected String[] doInBackground(Object... params) {
        try {
            Cryptic crypt = new Cryptic(context);
            encFile = new File(Environment.getExternalStorageDirectory() + "/" + Utility.SIMPLE_HIDDEN_DIRECTORY, fName);
            tempDir = new File(Environment.getExternalStorageDirectory() + "/" + Utility.SIMPLE_HIDDEN_TEMP_DIRECTORY);
            tempDir.mkdirs();
            try {
                if (fType.equalsIgnoreCase(".pdf")) {
                    tempFile = File.createTempFile("tempVid", ".pdf", tempDir);
                    crypt.Decrypt(encFile, tempFile);
                } else if (fType.equalsIgnoreCase(".flv") || fType.equalsIgnoreCase(".mp4") || fType.equalsIgnoreCase(".webm") || fType.equalsIgnoreCase(".3gp")) {
                    tempFile = File.createTempFile("tempVid", null, tempDir);
                    crypt.Decrypt(encFile, tempFile);
                } else {
                    tempFile = File.createTempFile("tempVid", fType, tempDir);
                    crypt.Decrypt(encFile, tempFile);
                }
            } catch (Exception e) {
                Log.e("Larry", "/*/*/*/*/*", e);
                Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
           // Toast.makeText(context, "An error occured.\\nPlease wait for the file to be downloaded.\\n\" + \"If the problem persists, please contact your administrator", Toast.LENGTH_SHORT).show();
        }

        return new String[]{fName, fType, tempFile.getAbsolutePath()};
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        commonDialog.dismiss();
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        try {
            commonDialog.dismiss();
            fName = result[0];
            fType = result[1];
            if (fType.equalsIgnoreCase(".pdf")) {
                File file = new File(result[2]);
                if (file.exists()) {

//                    Log.e("FILE NAME","--------------------"+result[2]);
//                    Toast.makeText(context, ""+result[2], Toast.LENGTH_SHORT).show();

                    intent = new Intent(context, PDFViewActivity.class);
                    intent.putExtra(Utility.FILE_PATH,
                            result[2]);
                }
            } else if (fType.equalsIgnoreCase(".flv") || fType.equalsIgnoreCase(".mp4") || fType.equalsIgnoreCase(".webm") || fType.equalsIgnoreCase(".3gp")) {
                intent = new Intent(context, VideoActivity.class);
                intent.putExtra(Utility.FILE_PATH, result[2]);
            } else {
                mimeType = "application/*";
                if (fType.equalsIgnoreCase(".jpg") || fType.equalsIgnoreCase(".png") || fType.equalsIgnoreCase(".jpeg") || fType.equalsIgnoreCase(".bmp")) {
                    mimeType = "image/*";
                } else if (fType.equalsIgnoreCase(".doc")) {
                    mimeType = context.getString(R.string.doc);
                } else if (fType.equalsIgnoreCase(".docx")) {
                    mimeType = context.getString(R.string.docx);
                } else if (fType.equalsIgnoreCase(".ppt")) {
                    mimeType = context.getString(R.string.ppt);
                } else if (fType.equalsIgnoreCase(".pps")) {
                    mimeType = context.getString(R.string.pps);
                } else if (fType.equalsIgnoreCase(".pptx")) {
                    mimeType = context.getString(R.string.pptx);
                } else if (fType.equalsIgnoreCase(".ppsx")) {
                    mimeType = context.getString(R.string.ppsx);
                } else if (fType.equalsIgnoreCase(".xls")) {
                    mimeType = context.getString(R.string.xls);
                } else if (fType.equalsIgnoreCase(".xlsx")) {
                    mimeType = context.getString(R.string.xlsx);
                }
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setDataAndTypeAndNormalize(Uri.fromFile(tempFile), mimeType);
            }
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }


}
