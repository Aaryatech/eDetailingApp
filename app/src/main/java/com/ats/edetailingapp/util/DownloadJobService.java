package com.ats.edetailingapp.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.sqlitedb.DatabaseHelper;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.io.File;
import java.util.Date;

public class DownloadJobService extends JobIntentService {

    private int id = 1;
    private static boolean isDownloading = false;
    private boolean downloadedAtLeastOne = false;

    private static final String QUERY_GET_FILE_REMOTE_PATHS = "SELECT DISTINCT(" + DatabaseHelper.FILE_REMOTE_PATHS + "), " + DatabaseHelper.FILE_LOCAL_PATHS + ", " + DatabaseHelper.FILE_TITLE + " FROM " + DatabaseHelper.FILE_TABLE + " WHERE " + DatabaseHelper.FILE_IS_ACTIVE + " = 1";
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;

    public DownloadJobService() {
        super();
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DownloadJobService.class, 1, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Log.e("DOWNLOAD SERVICE", "------------------onHandleIntent");
        isDownloading = true;
        downloadedAtLeastOne = false;
        //  mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //  mBuilder = new NotificationCompat.Builder(this);
        //   mBuilder.setContentTitle("Downloading Files").setContentText("Download in progress").setSmallIcon(android.R.drawable.stat_sys_download);
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String id1 = "id_product1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder = new NotificationCompat.Builder(this, "id_product");
        } else {
            mBuilder = new NotificationCompat.Builder(this);
        }


        DatabaseHelper dbHelper = new DatabaseHelper(DownloadJobService.this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor fileList = db.rawQuery(QUERY_GET_FILE_REMOTE_PATHS, null);
        int i = 0;
        try {
            while (fileList.moveToNext()) {
                Log.e("FILELIST : ", "--------------------------------------REMOTE PATH : " + fileList.getString(0) + "---------LOCAL PATH : " + fileList.getString(1) + "--------------TITLE : " + fileList.getString(2));
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator, Utility.SIMPLE_HIDDEN_DIRECTORY);
                dir.getParentFile().mkdirs();
                File encFile;
                String fileLocalPath;
                fileLocalPath = fileList.getString(1);
                Log.e("FILE LOCAL PATH", "---------------" + fileLocalPath);

                if (fileLocalPath.equalsIgnoreCase("null"))
                    fileLocalPath = "." + new Date().getTime();
                encFile = new File(dir, fileLocalPath);
                if (!encFile.exists()) {
                    Log.e("ENC FILE", "------------------------" + encFile + "  NOT EXIST");
                    SyncHttpClient fileClient = new SyncHttpClient();
                    File plainFile = new File(dir, "." + Integer.toString(i));
                    // mBuilder.setContentTitle("Downloading File").setContentText(fileList.getString(2));
                    Notification note = mBuilder.build();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Log.e("ANDROID", "----------------------------------O");

                        int importance = NotificationManager.IMPORTANCE_NONE;
                        NotificationChannel mChannel = new NotificationChannel(id1, "Downloading File", importance);
                        mChannel.setDescription(fileList.getString(2));
                        mChannel.enableLights(true);
                        mChannel.setLightColor(Color.RED);
                        mNotifyManager.createNotificationChannel(mChannel);

                        mBuilder.setSmallIcon(android.R.drawable.stat_sys_download) //your app icon
                                .setBadgeIconType(android.R.drawable.stat_sys_download) //your app icon
                                .setChannelId(id1)
                                .setContentTitle("Downloading File")
                                .setAutoCancel(true)
                                .setNumber(1)
                                .setColor(255)
                                .setContentText(fileList.getString(2))
                                .setWhen(System.currentTimeMillis());

                        startForeground(id, mBuilder.build());

                        mNotifyManager.notify(id, mBuilder.build());

                    } else {

                        mBuilder.setAutoCancel(true)
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setContentTitle("Downloading Files")
                                .setContentText(fileList.getString(2));
                        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        startForeground(id, mBuilder.build());

                        mNotifyManager.notify(id, mBuilder.build());

                        //mNotifyManager.notify(id, note);
                    }

                    fileClient.post(DownloadJobService.this, fileList.getString(0), new RequestParams(), new FileAsyncHttpResponseHandler(plainFile) {

                        @Override
                        public void onProgress(long bytesWritten, long totalSize) {
                            super.onProgress(bytesWritten, totalSize);
                            int totalbytes = (int) (long) totalSize;
                            mBuilder.setProgress(totalbytes, (int) (long) bytesWritten, false);
                            Notification note = mBuilder.build();
                            note.flags = Notification.FLAG_ONGOING_EVENT;
                            mNotifyManager.notify(id, note);

                        }

                        @Override
                        public void onFailure(int arg0, cz.msebera.android.httpclient.Header[] arg1, Throwable arg2, File arg3) {

                        }

                        @Override
                        public void onSuccess(int arg0, cz.msebera.android.httpclient.Header[] arg1, File arg2) {

                        }
                    });
                    downloadedAtLeastOne = true;
                    mBuilder.setProgress(0, 0, true);
                    Cryptic crypt = new Cryptic(getApplicationContext());
                    try {
                        Log.e("CRYPT BLOCK", "-----------------------------");
                        crypt.Encrypt(encFile, plainFile);
                        Log.e("CRYPT BLOCK", "*******************************");
                    } catch (Exception e) {
                        Log.e("Larry", "", e);
                    } finally {
                        plainFile.delete();
                    }
                    db.beginTransaction();
                    try {
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.FILE_LOCAL_PATHS, encFile.getName());
                        values.put(DatabaseHelper.FILE_IS_DOWNLOADED, 1);
                        db.update(DatabaseHelper.FILE_TABLE, values, DatabaseHelper.FILE_REMOTE_PATHS + " = ?", new String[]{fileList.getString(0)});
                        Log.e("UPDATE REMOTE PATH", "---------------" + encFile.getName());
                        db.setTransactionSuccessful();

                        Intent pushNotificationIntent = new Intent();
                        pushNotificationIntent.setAction("REFRESH_DATA");
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotificationIntent);

                    } catch (Exception e) {
                        Log.e("Larry", "", e);
                    } finally {
                        db.endTransaction();
                    }
                }
                i++;
            }
        } catch (Exception e) {
            Log.e("Larry", "Error", e);
        }
        mBuilder.setContentText("Download complete").setProgress(0, 0, false);
        Notification note = mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done).build();
        note.flags = Notification.FLAG_AUTO_CANCEL;

        mNotifyManager.notify(id, note);


        isDownloading = false;
        if (!downloadedAtLeastOne) {
            mNotifyManager.cancel(id);
        }
        db.close();
        stopForeground(true);
        DownloadIntentReceiver.completeWakefulIntent(intent);

    }

    public static boolean isDownloading() {
        return isDownloading;
    }


}
