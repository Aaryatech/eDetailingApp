package com.ats.edetailingapp.activity;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.util.Utility;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        pdfView = findViewById(R.id.pdfView);

        String filePath = getIntent().getStringExtra(Utility.FILE_PATH);
        File folder = new File(filePath);

        Log.e("PATH : ", "--------------------" + folder);
        Uri uri = Uri.fromFile(folder);
        try {

            if (folder.length() == 0) {
                Log.e("EMPTY FILE", "---------------------"+folder);
                Toast.makeText(this, "File Not Downloaded!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                pdfView.fromUri(uri).load();
                Log.e("FILE", "---------------------"+folder);
            }


        } catch (Exception e) {
            Log.e("ERROR : ", "************" + e.getMessage());
        }

    }
}
