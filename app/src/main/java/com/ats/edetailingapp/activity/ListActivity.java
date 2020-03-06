package com.ats.edetailingapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.adapter.FileListAdapter;
import com.ats.edetailingapp.model.FileType;
import com.ats.edetailingapp.model.FileTypeListModel;
import com.ats.edetailingapp.model.FilteredFileNameModel;
import com.ats.edetailingapp.model.Lang;
import com.ats.edetailingapp.model.Tag2;
import com.ats.edetailingapp.sqlitedb.DatabaseHelper;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private Spinner spTag2, spSubType, spLang;
    private RecyclerView recyclerView;

    int typeId, tagId1, tag2Id, subTypeId,langId;


    DatabaseHelper dbHelper;

    ArrayList<String> tag2List = new ArrayList<>();
    ArrayList<Integer> tag2IdList = new ArrayList<>();

    ArrayList<Integer> fileTypeIdList = new ArrayList<>();
    ArrayList<String> fileTypeList = new ArrayList<>();

    ArrayList<Integer> langIdList = new ArrayList<>();
    ArrayList<String> langList = new ArrayList<>();

    ArrayList<String> fileNameList = new ArrayList<>();
    ArrayList<FilteredFileNameModel> filteredFileNameList = new ArrayList<>();

    FileListAdapter fileAdapter;

    private BroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        spTag2 = findViewById(R.id.spTag2);
        spSubType = findViewById(R.id.spSubType);
        spLang = findViewById(R.id.spLang);
        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = new DatabaseHelper(ListActivity.this, 1);

        try {
            tagId1 = getIntent().getIntExtra("tagId1", 0);
            typeId = getIntent().getIntExtra("typeId", 0);

            //---------------------TAG 2 LIST-----------------------------
            tag2List.clear();
            tag2IdList.clear();

            ArrayList<Tag2> tag2ArrayList = dbHelper.getAllTag2Data(tagId1);

            int position = 0;
            for (int i = 0; i < tag2ArrayList.size(); i++) {
                tag2IdList.add(tag2ArrayList.get(i).getT2Tid());
                tag2List.add(tag2ArrayList.get(i).getT2Tag());
                if (tag2ArrayList.get(i).getT2Tid() == typeId) {
                    position = i;
                }
            }

            ArrayAdapter<String> tag2Spiner = new ArrayAdapter<>(this, R.layout.layout_spinner, tag2List);
            spTag2.setAdapter(tag2Spiner);
            spTag2.setSelection(position);


        } catch (Exception e) {
            e.printStackTrace();
        }

        fileAdapter = new FileListAdapter(filteredFileNameList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(fileAdapter);

        spTag2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //----------------SUB TYPE LIST------------------------

                fileTypeIdList.clear();
                fileTypeList.clear();

                tag2Id = tag2IdList.get(position);

                ArrayList<FileTypeListModel> fileTypeArrayList = dbHelper.getFileTypeList(tagId1, tag2Id);

                for (int i = 0; i < fileTypeArrayList.size(); i++) {
                    fileTypeIdList.add(fileTypeArrayList.get(i).getFileTypeId());
                    fileTypeList.add(fileTypeArrayList.get(i).getFileType());
                }

                ArrayAdapter<String> typeSpiner = new ArrayAdapter<String>(ListActivity.this, R.layout.layout_spinner, fileTypeList);
                spSubType.setAdapter(typeSpiner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                langIdList.clear();
                langList.clear();

                subTypeId = fileTypeIdList.get(position);

                ArrayList<Lang> langArrayList = dbHelper.getAllLang(tagId1, tag2Id, subTypeId);

                for (int i = 0; i < langArrayList.size(); i++) {
                    langIdList.add(langArrayList.get(i).getLangId());
                    langList.add(langArrayList.get(i).getLangName());
                }

                ArrayAdapter<String> langSpiner = new ArrayAdapter<String>(ListActivity.this, R.layout.layout_spinner, langList);
                spLang.setAdapter(langSpiner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                fileNameList.clear();
                filteredFileNameList.clear();

                langId = langIdList.get(position);

                fileNameList = dbHelper.getFilteredTag2SubLangFiles(tag2Id, subTypeId, langId);

                filteredFileNameList = dbHelper.getFilteredTag2SubLangFilesWithDownloadStatus(tag2Id, subTypeId, langId);

                Log.e("FILE : ", "/*/*/**/*/***************" + filteredFileNameList);
                fileAdapter = new FileListAdapter(filteredFileNameList, ListActivity.this);
                recyclerView.setAdapter(fileAdapter);
                fileAdapter.notifyDataSetChanged();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_DATA")) {
                    handlePushNotification1(intent);
                }
            }
        };

    }


    @Override
    public void onPause() {
        Log.e("SUGGESTION", "  ON PAUSE");
        LocalBroadcastManager.getInstance(ListActivity.this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        filteredFileNameList.clear();

        filteredFileNameList = dbHelper.getFilteredTag2SubLangFilesWithDownloadStatus(tag2Id, subTypeId, langId);

        Log.e("FILE : ", "/*/*/**/*/***************" + filteredFileNameList);
        fileAdapter = new FileListAdapter(filteredFileNameList, ListActivity.this);
        recyclerView.setAdapter(fileAdapter);
        fileAdapter.notifyDataSetChanged();

        LocalBroadcastManager.getInstance(ListActivity.this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA"));

    }

    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification1", "------------------------------------**********");
        filteredFileNameList.clear();
        filteredFileNameList = dbHelper.getFilteredTag2SubLangFilesWithDownloadStatus(tag2Id, subTypeId, langId);
        fileAdapter = new FileListAdapter(filteredFileNameList, ListActivity.this);
        recyclerView.setAdapter(fileAdapter);
        fileAdapter.notifyDataSetChanged();

    }


}
