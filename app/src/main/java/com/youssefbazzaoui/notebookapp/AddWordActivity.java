package com.youssefbazzaoui.notebookapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youssefbazzaoui.notebookapp.R;

public class AddWordActivity extends AppCompatActivity {

    private static final String TAG = "AddWordActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editTextEn;
    private EditText editTextFr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextEn = (EditText) findViewById(R.id.editTextEn);
        editTextFr = (EditText) findViewById(R.id.editTextFr);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String engEntry = editTextEn.getText().toString();
                String frEntry = editTextFr.getText().toString();
                if (editTextEn.length() != 0 && editTextFr.length() != 0) {
                    AddData(engEntry, frEntry);
                    editTextEn.setText("");
                    editTextFr.setText("");
                } else {
                    toastMessage("You must put something in the text field!");
                }

            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWordActivity.this, ListDataActivity.class);
                //Intent intent = new Intent(AddWordActivity.this, com.youssefbazzaoui.notebookapp.MainDomainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void AddData(String english, String french) {
        boolean insertData = mDatabaseHelper.addData(english, french);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
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
