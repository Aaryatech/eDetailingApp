package com.ats.edetailingapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.activity.SelectionActivity;
import com.ats.edetailingapp.model.FilteredFileNameModel;
import com.ats.edetailingapp.sqlitedb.DatabaseHelper;
import com.ats.edetailingapp.util.FileOpener;
import com.ats.edetailingapp.util.Utility;

import java.io.File;
import java.util.ArrayList;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.MyViewHolder> {

    private ArrayList<FilteredFileNameModel> fileList;
    private Context context;

    public FileListAdapter(ArrayList<FilteredFileNameModel> fileList, Context context) {
        this.fileList = fileList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public LinearLayout linearLayout;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            linearLayout = view.findViewById(R.id.linearLayout);
            imageView = view.findViewById(R.id.imageView);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_file_list, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FilteredFileNameModel model = fileList.get(position);
        holder.tvName.setText(model.getFileName());

        final ArrayList<String> fileNameList = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            fileNameList.add(fileList.get(i).getFileName());
        }

        if (model.getIsDownloaded() == 1) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FileOpener(position, context, fileNameList).execute(new int[]{position});
            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alert = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete?");
                builder.setTitle("Delete Action");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String QUERY_GET_FILE_PATH = "SELECT " + DatabaseHelper.FILE_LOCAL_PATHS + ", " + DatabaseHelper.FILE_TYPE + " FROM " + DatabaseHelper.FILE_TABLE + " WHERE " + DatabaseHelper.FILE_TITLE + " = ?";

                        DatabaseHelper dbHelper = new DatabaseHelper(context, 1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery(QUERY_GET_FILE_PATH, new String[]{model.getFileName()});
                        cursor.moveToFirst();
                        String fName = cursor.getString(0);
                        File encFile = new File(Environment.getExternalStorageDirectory() + "/" + Utility.SIMPLE_HIDDEN_DIRECTORY, fName);
                        try {
                            encFile.delete();
                        } catch (Exception e) {

                        }
                        int i = db.delete(DatabaseHelper.FILE_TABLE, DatabaseHelper.FILE_TITLE + " = ?", new String[]{model.getFileName()});
                        if (i > 0) {
                            fileList.remove(position);
                            notifyDataSetChanged();
                        }
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

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }


}
