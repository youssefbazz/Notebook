package com.youssefbazzaoui.notebookapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.youssefbazzaoui.notebookapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    private LinearLayout mListIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mListIndex = (LinearLayout) findViewById(R.id.side_index);


        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
        populateListIndex();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    private void populateListIndex() {
        char c = 'A';
        //LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>();
        indexList.add("*");
        while (c != '[') {
            indexList.add("" + c);
            c += 1;
        }
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                populateListView(((TextView) v).getText().toString());
                                            }
                                        }
            );
            mListIndex.addView(textView);
        }
    }

    private void populateListView() {
        populateListView(null);
    }

    private void populateListView(String filter) {
        if ("*".equals(filter)) filter = null;
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<HashMap<String, String>> listData = new ArrayList<HashMap<String, String>>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            HashMap item = new HashMap<String, String>();
            String id = data.getString(0);
            if (filter == null || id.startsWith(filter.toLowerCase())) {
                item.put(HashMapAdapter.FIRST_COLUMN, data.getString(0));
                item.put(HashMapAdapter.SECOND_COLUMN, data.getString(1));
                listData.add(item);
            }
        }
        //mTable.
        //create the list adapter and set the adapter
        ListAdapter adapter = new HashMapAdapter(this, listData);
        //ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String eng = ((HashMap<String, String>) adapterView.getItemAtPosition(i)).get(HashMapAdapter.FIRST_COLUMN);
                String fr = ((HashMap<String, String>) adapterView.getItemAtPosition(i)).get(HashMapAdapter.SECOND_COLUMN);
                Log.d(TAG, "onItemClick: You Clicked on " + eng);
/*
                Cursor data = mDatabaseHelper.getItemID(eng); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){*/
                //Log.d(TAG, "onItemClick: The ID is: " + itemID);
                Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                editScreenIntent.putExtra("eng", eng);
                editScreenIntent.putExtra("fr", fr);
                startActivity(editScreenIntent);/*
                }
                else{
                    toastMessage("No ID associated with that name");
                }*/
            }
        });
    }

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}