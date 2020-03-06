package com.ats.edetailingapp.util;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ats.edetailingapp.sqlitedb.DatabaseHelper;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.io.File;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DownloadService extends IntentService {

    private int id = 1;
    private static boolean isDownloading = false;
    private boolean downloadedAtLeastOne = false;

    private static final String QUERY_GET_FILE_REMOTE_PATHS = "SELECT DISTINCT(" + DatabaseHelper.FILE_REMOTE_PATHS + "), " + DatabaseHelper.FILE_LOCAL_PATHS + ", " + DatabaseHelper.FILE_TITLE + " FROM " + DatabaseHelper.FILE_TABLE + " WHERE " + DatabaseHelper.FILE_IS_ACTIVE + " = 1";
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            Log.e("DOWNLOAD SERVICE", "------------------onHandleIntent");
            isDownloading = true;
            downloadedAtLeastOne = false;
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setContentTitle("Downloading Files").setContentText("Download in progress").setSmallIcon(android.R.drawable.stat_sys_download);
            DatabaseHelper dbHelper = new DatabaseHelper(DownloadService.this, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor fileList = db.rawQuery(QUERY_GET_FILE_REMOTE_PATHS, null);
            int i = 0;
            try {
                while (fileList.moveToNext()) {
                    File dir = new File(Environment.getExternalStorageDirectory(), Utility.SIMPLE_HIDDEN_DIRECTORY);
                    dir.getParentFile().mkdirs();
                    File encFile;
                    String fileLocalPath;
                    fileLocalPath = fileList.getString(1);
                    if (fileLocalPath == null)
                        fileLocalPath = "." + new Date().getTime();
                    encFile = new File(dir, fileLocalPath);
                    if (!encFile.exists()) {
                        SyncHttpClient fileClient = new SyncHttpClient();
                        File plainFile = new File(dir, "." + Integer.toString(i));
                        mBuilder.setContentTitle("Downloading File").setContentText(fileList.getString(2));
                        Notification note = mBuilder.build();
                        startForeground(id, note);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            String ch_id = "id_download";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel mChannel = new NotificationChannel(ch_id, "downloading", importance);
                            // Configure the notification channel.
                            mChannel.setDescription("downloading");
                            mChannel.enableLights(true);
                            // Sets the notification light color for notifications posted to this
                            // channel, if the device supports this feature.
                            mChannel.setLightColor(Color.RED);
                            mNotifyManager.createNotificationChannel(mChannel);

                            mNotifyManager.notify(id, note);


                        } else {
                            mNotifyManager.notify(id, note);
                        }


                        //java.lang.AssertionError: Cannot create parent directories for requested File location

                        fileClient.post(DownloadService.this, fileList.getString(0), new RequestParams(), new FileAsyncHttpResponseHandler(plainFile) {
                        /*	@Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                mBuilder.setProgress((int)totalSize, (int)bytesWritten, false);
                                Notification note = mBuilder.build();
                                note.flags = Notification.FLAG_ONGOING_EVENT;
                                mNotifyManager.notify(id, note);
                            }*/

                            @Override
                            public void onProgress(long bytesWritten, long totalSize) {
                                super.onProgress(bytesWritten, totalSize);
                                int totalbytes = (int) (long) totalSize;
                                mBuilder.setProgress(totalbytes, (int) (long) bytesWritten, false);
                                Notification note = mBuilder.build();
                                note.flags = Notification.FLAG_ONGOING_EVENT;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                    String ch_id = "id_download";
                                    int importance = NotificationManager.IMPORTANCE_HIGH;
                                    NotificationChannel mChannel = new NotificationChannel(ch_id, "downloading", importance);
                                    // Configure the notification channel.
                                    mChannel.setDescription("downloading");
                                    mChannel.enableLights(true);
                                    // Sets the notification light color for notifications posted to this
                                    // channel, if the device supports this feature.
                                    mChannel.setLightColor(Color.RED);
                                    mNotifyManager.createNotificationChannel(mChannel);

                                    mNotifyManager.notify(id, note);


                                } else {
                                    mNotifyManager.notify(id, note);
                                }
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
                            crypt.Encrypt(encFile, plainFile);
                        } catch (Exception e) {
                            Log.e("Larry", "", e);
                        } finally {
                            plainFile.delete();
                        }
                        db.beginTransaction();
                        try {
                            ContentValues values = new ContentValues();
                            values.put(DatabaseHelper.FILE_LOCAL_PATHS, encFile.getName());
                            db.update(DatabaseHelper.FILE_TABLE, values, DatabaseHelper.FILE_REMOTE_PATHS + " = ?", new String[]{fileList.getString(0)});
                            db.setTransactionSuccessful();
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


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String ch_id = "id_download";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(ch_id, "downloading", importance);
                // Configure the notification channel.
                mChannel.setDescription("downloading");
                mChannel.enableLights(true);
                // Sets the notification light color for notifications posted to this
                // channel, if the device supports this feature.
                mChannel.setLightColor(Color.RED);
                mNotifyManager.createNotificationChannel(mChannel);

                mNotifyManager.notify(id, note);


            } else {
                mNotifyManager.notify(id, note);
            }

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
