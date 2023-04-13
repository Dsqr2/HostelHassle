package com.example.serviceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<historyClass> {
    private final Context mContext;
    private final int mResource;
    ArrayList<historyClass> mobjects;
    private int lastPosition = -1;
    private static class ViewHolder {
        TextView service;
        TextView status;
        TextView date;
    }
    public CustomAdapter(Context context, int resource, ArrayList<historyClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mobjects=objects;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String service = getItem(position).getService();
        String date = getItem(position).getDate();
        String status = getItem(position).getStatus();

        //Create the person object with the information
        historyClass rclass = new historyClass(service,date,status);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.service = (TextView) convertView.findViewById(R.id.textView2);
            holder.status = (TextView) convertView.findViewById(R.id.textView4);
            holder.date=(TextView)convertView.findViewById(R.id.textView3);
            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        lastPosition = position;

        holder.service.setText(rclass.getService());
        holder.status.setText(rclass.getStatus());
        holder.date.setText(rclass.getDate());
        return convertView;
    }
}
