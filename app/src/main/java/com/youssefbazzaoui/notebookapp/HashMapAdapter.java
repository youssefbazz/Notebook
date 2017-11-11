package com.youssefbazzaoui.notebookapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youssefbazzaoui.notebookapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by youssefbazzaoui on 28/10/2017.
 */

class HashMapAdapter extends BaseAdapter {
    public static final String FIRST_COLUMN = "BTATA";
    public static final String SECOND_COLUMN = "MATICHA";

    public ArrayList<HashMap<String, String>> list;
    private Activity activity;
    private TextView txtFirst;
    private TextView txtSecond;

    public HashMapAdapter(ListDataActivity listDataActivity, ArrayList<HashMap<String, String>> listData) {
        super();
        this.activity = listDataActivity;
        this.list = listData;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.column_row, null);

            txtFirst = (TextView) convertView.findViewById(R.id.french);
            txtSecond = (TextView) convertView.findViewById(R.id.english);

        }

        HashMap<String, String> map = list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));

        return convertView;
    }
}
