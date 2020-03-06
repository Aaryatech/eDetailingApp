package com.ats.edetailingapp.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ats.edetailingapp.model.FileDatum;
import com.ats.edetailingapp.model.FileType;
import com.ats.edetailingapp.model.FileTypeListModel;
import com.ats.edetailingapp.model.FilteredFileNameModel;
import com.ats.edetailingapp.model.FtDatum;
import com.ats.edetailingapp.model.Lang;
import com.ats.edetailingapp.model.MDatum;
import com.ats.edetailingapp.model.Tag1;
import com.ats.edetailingapp.model.Tag2;
import com.ats.edetailingapp.model.UserDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;

    public static final String TAG1_TABLE = "tag1_table";
    public static final String TAG2_TABLE = "tag2_table";
    public static final String FILE_TYPE_TABLE = "tbl_file_type";
    public static final String LANG_TABLE = "tbl_lang";
    public static final String FILE_TABLE = "file_table";
    public static final String USER_TABLE = "user_table";
    public static final String FT_TABLE = "ft_table";
    public static final String M_T1_T2 = "m_t1_t2";

    public static final String TAG1_TID = "t1_tid";
    public static final String TAG1 = "t1_tag";
    public static final String TAG2_TID = "t2_tid";
    public static final String TAG2 = "t2_tag";
    public static final String F_TYPE_ID = "ftype_id";
    public static final String FILE_TYPE = "file_type";
    public static final String LANG_ID = "lang_id";
    public static final String LANG_NAME = "lang_name";

    public static final String FILE_ID = "f_id";
    public static final String FILE_TITLE = "f_title";
    public static final String FILE_LOCAL_PATHS = "f_local_paths";
    public static final String FILE_REMOTE_PATHS = "f_remote_paths";
    public static final String FILE_IS_ACTIVE = "f_is_active";
    public static final String FILE_IS_DOWNLOADED = "f_is_downloaded";

    public static final String USER_ID = "u_id";
    public static final String USER_NAME = "u_name";
    public static final String USER_PASSWORD = "u_pass";
    public static final String USER_TID1 = "u_tid1";

    public static final String FT_ID = "ft_id";
    public static final String FT_TID1 = "ft_tid1";
    public static final String FT_TID2 = "ft_tid2";
    public static final String FT_F_TYPE_ID = "f_type_id";
    public static final String FT_LANG_ID = "ft_lang_id";
    public static final String FT_FID = "ft_fid";

    public static final String M_ID_ALT = "id";
    public static final String M_ID = "m_id";
    public static final String M_TID1 = "m_tid1";
    public static final String M_TID2 = "m_tid2";

    private static final String CREATE_TAG1_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TAG1_TABLE
            + "("
            + TAG1_TID
            + " INTEGER PRIMARY KEY, "
            + TAG1
            + " VARCHAR UNIQUE NOT NULL);";

    private static final String CREATE_TAG2_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TAG2_TABLE
            + "("
            + TAG2_TID
            + " INTEGER PRIMARY KEY, "
            + TAG2
            + " VARCHAR UNIQUE NOT NULL);";

    private static final String CREATE_FILE_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + FILE_TYPE_TABLE
            + "("
            + F_TYPE_ID
            + " INTEGER PRIMARY KEY, "
            + FILE_TYPE
            + " VARCHAR UNIQUE NOT NULL);";

    private static final String CREATE_LANG_TABLE = "CREATE TABLE IF NOT EXISTS "
            + LANG_TABLE
            + "("
            + LANG_ID
            + " INTEGER PRIMARY KEY, "
            + LANG_NAME
            + " VARCHAR UNIQUE NOT NULL);";

    private static final String CREATE_FILE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + FILE_TABLE
            + "("
            + FILE_ID
            + " INTEGER PRIMARY KEY, "
            + FILE_TITLE
            + " VARCHAR NOT NULL, "
            + FILE_LOCAL_PATHS
            + " VARCHAR, "
            + FILE_REMOTE_PATHS
            + " VARCHAR, "
            + FILE_TYPE
            + " VARCHAR, "
            + FILE_IS_ACTIVE
            + " INTEGER, "
            + FILE_IS_DOWNLOADED
            + " INTEGER);";

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + USER_TABLE
            + "("
            + USER_ID
            + " INTEGER PRIMARY KEY, "
            + USER_NAME
            + " VARCHAR UNIQUE NOT NULL, "
            + USER_PASSWORD
            + " VARCHAR NOT NULL, "
            + USER_TID1
            + " INTEGER);";

    private static final String CREATE_FT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + FT_TABLE
            + "("
            + FT_ID
            + " INTEGER PRIMARY KEY, "
            + FT_TID1
            + " INTEGER, "
            + FT_TID2
            + " INTEGER, "
            + FT_F_TYPE_ID
            + " INTEGER, "
            + FT_LANG_ID
            + " INTEGER, "
            + FT_FID
            + " INTEGER NOT NULL);";

    private static final String CREATE_M_T1_T2 = "CREATE TABLE IF NOT EXISTS "
            + M_T1_T2
            + "("
            + M_ID
            + " INTEGER PRIMARY KEY, "
            + M_TID1
            + " INTEGER, "
            + M_TID2
            + " INTEGER);";

    public DatabaseHelper(Context context, int version) {
        super(context, "filesInfo", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper : ", CREATE_TAG1_TABLE);
        Log.d("DatabaseHelper : ", CREATE_TAG2_TABLE);
        Log.d("DatabaseHelper : ", CREATE_FILE_TYPE_TABLE);
        Log.d("DatabaseHelper : ", CREATE_LANG_TABLE);
        Log.d("DatabaseHelper : ", CREATE_FILE_TABLE);
        Log.d("DatabaseHelper : ", CREATE_USER_TABLE);
        Log.d("DatabaseHelper : ", CREATE_FT_TABLE);
        Log.d("DatabaseHelper : ", CREATE_M_T1_T2);
        db.execSQL(CREATE_TAG1_TABLE);
        db.execSQL(CREATE_TAG2_TABLE);
        db.execSQL(CREATE_FILE_TYPE_TABLE);
        db.execSQL(CREATE_LANG_TABLE);
        db.execSQL(CREATE_FILE_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_FT_TABLE);
        db.execSQL(CREATE_M_T1_T2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, null, null);
        db.delete(TAG1_TABLE, null, null);
        db.delete(TAG2_TABLE, null, null);
        db.delete(FILE_TYPE_TABLE, null, null);
        db.delete(LANG_TABLE, null, null);
        db.delete(M_T1_T2, null, null);
        db.delete(FT_TABLE, null, null);
        db.delete(FILE_TABLE, null, null);
        db.close();
    }


    //--------------------USER----------------------------

    public void addUser(int userId, String username, String pass, int tId1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, userId);
        values.put(USER_NAME, username);
        values.put(USER_PASSWORD, pass);
        values.put(USER_TID1, tId1);

        db.insertWithOnConflict(USER_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public int updateUser(int user_tid1, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_TID1, user_tid1);

        // updating row
        return db.update(USER_TABLE, values, USER_ID + "=" + userId, null);

    }


    public UserDataModel getUserData() {
        UserDataModel user = new UserDataModel();

        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                user.setUserId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.settId1(cursor.getInt(3));
            } while (cursor.moveToNext());
        }
        Log.e("USER ", "********************************" + user);
        return user;
    }

    //--------------------TAG1----------------------------

    public void addTag1(Tag1 model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TAG1_TID, model.getT1Tid());
        values.put(TAG1, model.getT1Tag());

        db.insertWithOnConflict(TAG1_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public Tag1 getTag1Data() {
        Tag1 tag1 = new Tag1();

        String query = "SELECT * FROM " + TAG1_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                tag1.setT1Tid(cursor.getInt(0));
                tag1.setT1Tag(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        Log.e("TAG1", "********************************" + tag1);
        return tag1;
    }

    public Tag1 getTag1(int userId) {
        Tag1 tag1 = new Tag1();

        String QUERY_GET_SUB_LIST1 = "SELECT DISTINCT(" + TAG1_TID + "), " + TAG1 + " FROM " + TAG1_TABLE + ", " + USER_TABLE + " WHERE " + TAG1_TID + " = " + USER_TID1 + " AND " + USER_ID + " = " + userId + " order by " + TAG1;

        Log.e("QUERY : ", "---------------" + QUERY_GET_SUB_LIST1);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_GET_SUB_LIST1, null);

        if (cursor != null && cursor.moveToFirst()) {
            tag1 = new Tag1(cursor.getInt(0), cursor.getString(1));
            cursor.close();
        }
        Log.e("TAG1 : ", "-----------********" + tag1);
        return tag1;
    }


    //--------------------TAG2----------------------------

    public void addTag2(Tag2 model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TAG2_TID, model.getT2Tid());
        values.put(TAG2, model.getT2Tag());

        db.insertWithOnConflict(TAG2_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Tag2> getAllTag2() {
        ArrayList<Tag2> tag2List = new ArrayList<>();

        String query = "SELECT * FROM " + TAG2_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Tag2 tag2 = new Tag2();
                tag2.setT2Tid(cursor.getInt(0));
                tag2.setT2Tag(cursor.getString(1));

                tag2List.add(tag2);
            } while (cursor.moveToNext());
        }


        Log.e("TAG2 LIST", "********************************" + tag2List);
        return tag2List;
    }


    public ArrayList<Tag2> getAllTag2Data(int tId1) {
        ArrayList<Tag2> tag2List = new ArrayList<>();

        String query = "SELECT DISTINCT(" + TAG2_TID + "), " + TAG2 + " FROM " + TAG2_TABLE + ", " + M_T1_T2 + " WHERE " + TAG2_TID + " = " + M_TID2 + " AND " + M_TID1 + " = " + tId1 + " order by " + TAG2;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Tag2 tag2 = new Tag2();
                tag2.setT2Tid(cursor.getInt(0));
                tag2.setT2Tag(cursor.getString(1));

                tag2List.add(tag2);
            } while (cursor.moveToNext());
        }


        Log.e("TAG2 LIST", "********************************" + tag2List);
        return tag2List;
    }


    //--------------------FILE TYPE----------------------------

    public void addFileType(FileType model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(F_TYPE_ID, model.getFtypeId());
        values.put(FILE_TYPE, model.getFileType());

        db.insertWithOnConflict(FILE_TYPE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    //--------------------LANG----------------------------

    public void addLang(Lang model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LANG_ID, model.getLangId());
        values.put(LANG_NAME, model.getLangName());

        db.insertWithOnConflict(LANG_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<Lang> getAllLang(int tId1, int tId2, int fileTypeId) {
        ArrayList<Lang> langList = new ArrayList<>();

        String query = "SELECT DISTINCT(" + LANG_ID + "), " + LANG_NAME + " FROM " + LANG_TABLE + ", " + FT_TABLE + " WHERE " + LANG_ID + " = " + FT_LANG_ID + " AND " + FT_TID1 + " =  " + tId1 + " AND " + FT_TID2 + " =  " + tId2 + " AND " + FT_F_TYPE_ID + " = " + fileTypeId + " order by " + LANG_NAME + " desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Lang lang = new Lang();
                lang.setLangId(cursor.getInt(0));
                lang.setLangName(cursor.getString(1));

                langList.add(lang);
            } while (cursor.moveToNext());
        }
        Log.e("LANG LIST", "********************************" + langList);
        return langList;
    }


    //--------------------M DATA----------------------------

    public void addMData(MDatum model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(M_ID, model.getId());
        values.put(M_TID1, model.getT1Tid());
        values.put(M_TID2, model.getT2Tid());

        db.insertWithOnConflict(M_T1_T2, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<MDatum> getAllMData() {
        ArrayList<MDatum> mDataList = new ArrayList<>();

        String query = "SELECT * FROM " + M_T1_T2;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                MDatum mDatum = new MDatum();
                mDatum.setId(cursor.getInt(0));
                mDatum.setT1Tid(cursor.getInt(1));
                mDatum.setT2Tid(cursor.getInt(2));

                mDataList.add(mDatum);
            } while (cursor.moveToNext());
        }
        Log.e("M_DATA LIST", "********************************" + mDataList);
        return mDataList;
    }

    //--------------------FT DATA----------------------------

    public void addFtData(FtDatum model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FT_ID, model.getFtId());
        values.put(FT_TID1, model.getT1Tid());
        values.put(FT_TID2, model.getT2Tid());
        values.put(FT_F_TYPE_ID, model.getfTypeId());
        values.put(FT_LANG_ID, model.getLangId());
        values.put(FT_FID, model.getfId());

        db.insertWithOnConflict(FT_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<FtDatum> getFileTypeData() {
        ArrayList<FtDatum> ftDataList = new ArrayList<>();

        String query = "SELECT * FROM " + FT_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                FtDatum ftDatum = new FtDatum();
                ftDatum.setFtId(cursor.getInt(0));
                ftDatum.setT1Tid(cursor.getInt(1));
                ftDatum.setT2Tid(cursor.getInt(2));
                ftDatum.setfTypeId(cursor.getInt(3));
                ftDatum.setLangId(cursor.getInt(4));
                ftDatum.setfId(cursor.getInt(5));

                ftDataList.add(ftDatum);
            } while (cursor.moveToNext());
        }
        Log.e("F_DATA LIST", "********************************" + ftDataList);
        return ftDataList;
    }


    //--------------------FILE DATA----------------------------

    public void addFileData(FileDatum model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FILE_ID, model.getfId());
        values.put(FILE_TITLE, model.getfTitle());
        values.put(FILE_LOCAL_PATHS, "null");
        values.put(FILE_REMOTE_PATHS, model.getfPath());
        values.put(FILE_TYPE, model.getfPath().substring(model.getfPath().lastIndexOf(".")));
        values.put(FILE_IS_ACTIVE, model.getIsActive());
        values.put(FILE_IS_DOWNLOADED, 0);

        db.insertWithOnConflict(FILE_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public int updateFileData(String title, int isActive, int fileId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FILE_TITLE, title);
        values.put(FILE_IS_ACTIVE, isActive);

        // updating row
        return db.update(FILE_TABLE, values, FILE_ID + "=" + fileId, null);
    }

    public ArrayList<FileDatum> getAllFileData(String fPath) {
        ArrayList<FileDatum> fileList = new ArrayList<>();

        String query = "SELECT * FROM " + FILE_TABLE + " WHERE " + FILE_REMOTE_PATHS + " = '" + fPath + "'";

        Log.e("QUERY : ", "--------------" + query);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                FileDatum fileData = new FileDatum();
                fileData.setfId(cursor.getInt(0));
                fileData.setfTitle(cursor.getString(1));
                fileData.setfPath(cursor.getString(2));
                fileData.setfFullPath(cursor.getString(3));
                fileData.setfType(cursor.getString(4));
                fileData.setIsActive(cursor.getInt(5));

                fileList.add(fileData);
            } while (cursor.moveToNext());
        }
        Log.e("FILE DATA", "***************00000000000000*****************" + fileList);
        return fileList;
    }

    public ArrayList<String> getFileList(int fileId) {

        ArrayList<String> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + FILE_TITLE + ")" + " FROM " + FILE_TABLE + ", " + FT_TABLE + " WHERE " + FILE_IS_ACTIVE + " = 1 AND " + FILE_LOCAL_PATHS + " IS NOT NULL AND " + FILE_ID + " = " + fileId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_GET_FILE_LIST, null);

        if (cursor.moveToFirst()) {
            do {
                fileList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Log.e("FILE LIST", "********************************" + fileList);
        return fileList;
    }

    public ArrayList<FileDatum> getFileDataArray() {
        ArrayList<FileDatum> ftDataList = new ArrayList<>();

        String query = "SELECT * FROM " + FILE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                FileDatum ftDatum = new FileDatum();
                ftDatum.setfId(cursor.getInt(0));
                ftDatum.setfTitle(cursor.getString(1));
                ftDatum.setfPath(cursor.getString(2));
                ftDatum.setfFullPath(cursor.getString(3));
                ftDatum.setfType(cursor.getString(4));
                ftDatum.setIsActive(cursor.getInt(5));

                ftDataList.add(ftDatum);
            } while (cursor.moveToNext());
        }
        Log.e("FILE LIST", "********************************" + ftDataList);
        return ftDataList;
    }


    //-----------------OTHER--------------------------------------

    public ArrayList<FileTypeListModel> getFileTypeList(int tId1, int tId2) {

        ArrayList<FileTypeListModel> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + F_TYPE_ID + "), " + FILE_TYPE + " FROM " + FILE_TYPE_TABLE + ", " + FT_TABLE + " WHERE " + F_TYPE_ID + " = " + FT_F_TYPE_ID + " AND " + FT_TID1 + " = " + tId1 + " AND " + FT_TID2 + " = " + tId2;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_GET_FILE_LIST, null);

        if (cursor.moveToFirst()) {
            do {
                FileTypeListModel model = new FileTypeListModel();
                model.setFileTypeId(cursor.getInt(0));
                model.setFileType(cursor.getString(1));
                fileList.add(model);
            } while (cursor.moveToNext());
        }

        Log.e("FILE TYPE LIST", "********************************" + fileList);
        return fileList;
    }

    public ArrayList<String> getFiles() {

        ArrayList<String> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + FILE_TITLE + ")" + " FROM " + FILE_TABLE + ", " + FT_TABLE + " WHERE " + FILE_IS_ACTIVE + " = 1 AND " + FILE_LOCAL_PATHS + " IS NOT NULL AND " + FILE_ID + " = " + FT_FID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_GET_FILE_LIST, null);

        if (cursor.moveToFirst()) {
            do {
                fileList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Log.e("FILE LIST", "********************************" + fileList);
        return fileList;
    }

    public ArrayList<String> getFilteredTag2Files(int tId2) {

        ArrayList<String> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + FILE_TITLE + ")" + " FROM " + FILE_TABLE + ", " + FT_TABLE + " WHERE " + FILE_IS_ACTIVE + " = 1 AND " + FILE_LOCAL_PATHS + " IS NOT NULL AND " + FILE_ID + " = " + FT_FID;

        String QUERY_FILTER_PROJ_LIST = QUERY_GET_FILE_LIST + " AND ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_FILTER_PROJ_LIST + FT_TID2 + " = " + tId2 + " ORDER BY " + FILE_TITLE, null);

        if (cursor.moveToFirst()) {
            do {
                fileList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Log.e("FILE LIST", "********************************" + fileList);
        return fileList;
    }

    public ArrayList<String> getFilteredTag2SubFiles(int tId2, int subId) {

        ArrayList<String> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + FILE_TITLE + ")" + " FROM " + FILE_TABLE + ", " + FT_TABLE + " WHERE " + FILE_IS_ACTIVE + " = 1 AND " + FILE_LOCAL_PATHS + " IS NOT NULL AND " + FILE_ID + " = " + FT_FID;

        String QUERY_FILTER_PROJ_LIST = QUERY_GET_FILE_LIST + " AND ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_FILTER_PROJ_LIST + DatabaseHelper.FT_TID2 + " = " + tId2 + " AND " + DatabaseHelper.FT_F_TYPE_ID + " = " + subId + " ORDER BY " + DatabaseHelper.FILE_TITLE, null);

        if (cursor.moveToFirst()) {
            do {
                fileList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Log.e("FILE LIST", "********************************" + fileList);
        return fileList;
    }

    public ArrayList<String> getFilteredTag2SubLangFiles(int tId2, int subId, int langId) {

        ArrayList<String> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + FILE_TITLE + ")" + " FROM " + FILE_TABLE + ", " + FT_TABLE + " WHERE " + FILE_IS_ACTIVE + " = 1 AND " + FILE_LOCAL_PATHS + " IS NOT NULL AND " + FILE_ID + " = " + FT_FID;

        String QUERY_FILTER_PROJ_LIST = QUERY_GET_FILE_LIST + " AND ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_FILTER_PROJ_LIST + DatabaseHelper.FT_TID2 + " = " + tId2 + " AND " + DatabaseHelper.FT_F_TYPE_ID + " = " + subId + " AND " + DatabaseHelper.FT_LANG_ID + " = " + langId + " ORDER BY " + DatabaseHelper.FILE_TITLE, null);

        if (cursor.moveToFirst()) {
            do {
                fileList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        Log.e("FILE LIST", "********************************" + fileList);
        return fileList;
    }


    public ArrayList<FilteredFileNameModel> getFilteredTag2SubLangFilesWithDownloadStatus(int tId2, int subId, int langId) {

        ArrayList<FilteredFileNameModel> fileList = new ArrayList<>();

        String QUERY_GET_FILE_LIST = "SELECT DISTINCT(" + FILE_TITLE + ") , " + FILE_IS_DOWNLOADED + " FROM " + FILE_TABLE + ", " + FT_TABLE + " WHERE " + FILE_IS_ACTIVE + " = 1 AND " + FILE_LOCAL_PATHS + " IS NOT NULL AND " + FILE_ID + " = " + FT_FID;

        String QUERY_FILTER_PROJ_LIST = QUERY_GET_FILE_LIST + " AND ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_FILTER_PROJ_LIST + DatabaseHelper.FT_TID2 + " = " + tId2 + " AND " + DatabaseHelper.FT_F_TYPE_ID + " = " + subId + " AND " + DatabaseHelper.FT_LANG_ID + " = " + langId + " ORDER BY " + DatabaseHelper.FILE_TITLE, null);

        if (cursor.moveToFirst()) {
            do {
                FilteredFileNameModel model = new FilteredFileNameModel();
                model.setFileName(cursor.getString(0));
                model.setIsDownloaded(cursor.getInt(1));

                fileList.add(model);
            } while (cursor.moveToNext());
        }

        Log.e("FILE LIST", "********************************" + fileList);
        return fileList;
    }





}
