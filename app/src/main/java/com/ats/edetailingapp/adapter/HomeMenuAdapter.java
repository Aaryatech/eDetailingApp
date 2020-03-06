package com.ats.edetailingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.activity.ListActivity;
import com.ats.edetailingapp.model.Tag2;

import java.util.ArrayList;

public class HomeMenuAdapter extends BaseAdapter {

    private ArrayList<Tag2> menuList;
    private Context context;
    private int tagId1;
    private static LayoutInflater inflater = null;

    public HomeMenuAdapter(ArrayList<Tag2> menuList, Context context, int tagId1) {
        this.menuList = menuList;
        this.context = context;
        this.tagId1 = tagId1;
        this.inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class Holder {
        TextView tvName;
        LinearLayout linearLayout;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        View rowView = convertView;

        if (rowView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.adapter_home_menu, null);

            holder.tvName = rowView.findViewById(R.id.tvName);
            holder.linearLayout = rowView.findViewById(R.id.linearLayout);
            holder.imageView = rowView.findViewById(R.id.imageView);

            rowView.setTag(holder);

        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.tvName.setText(menuList.get(position).getT2Tag());

        if (menuList.get(position).getT2Tag().equalsIgnoreCase("Aqua")) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.aqua));
        } else if (menuList.get(position).getT2Tag().equalsIgnoreCase("cattle-th")) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cattle_th));
        } else if (menuList.get(position).getT2Tag().equalsIgnoreCase("cattle-nt")) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.cattle_nu));
        } else if (menuList.get(position).getT2Tag().equalsIgnoreCase("sheep")) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sheep));
        } else if (menuList.get(position).getT2Tag().equalsIgnoreCase("companion")) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.companion));
        }else if (menuList.get(position).getT2Tag().equalsIgnoreCase("poultry")) {
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.poultry));
        }else{
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.virbaclogo));
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("tagId1", tagId1);
                intent.putExtra("typeId", menuList.get(position).getT2Tid());
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
