package com.youssefbazzaoui.notebookapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.youssefbazzaoui.notebookapp.R;

import java.util.ArrayList;

public class MainDomainActivity extends AppCompatActivity {

    Button btnNewDomain;
    DatabaseHelper mDatabaseHelper;
    private EditText txtDomain;
    private ListView lstDomains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_domain);

        mDatabaseHelper = new DatabaseHelper(this);

        btnNewDomain = (Button) findViewById(R.id.btnNewDomain);
        txtDomain = (EditText) findViewById(R.id.txtDomain);
        lstDomains = (ListView) findViewById(R.id.lstDomains);


        btnNewDomain.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(this.getClass().toString(), "noum " + txtDomain.getText().toString());
                Log.i(this.getClass().toString(), "got click " + txtDomain.getText().toString());
                mDatabaseHelper.addDomain(txtDomain.getText().toString());
                poppulateDomains();
            }
        });

        lstDomains.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String domain = ((String) adapterView.getItemAtPosition(i));
                Intent editScreenIntent = new Intent(MainDomainActivity.this, EditDomainActivity.class);
                editScreenIntent.putExtra("domain", domain);
                startActivity(editScreenIntent);
            }
        });
        poppulateDomains();

    }

    @Override
    protected void onResume() {
        super.onResume();
        poppulateDomains();
    }

    private void poppulateDomains() {
        Cursor result = mDatabaseHelper.getDomains();

        ArrayList<String> arr = new ArrayList<String>();
        while (result.moveToNext()) {
            arr.add(result.getString(1));
        }
        lstDomains.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr));

    }
}
