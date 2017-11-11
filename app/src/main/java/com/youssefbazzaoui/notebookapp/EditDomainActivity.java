package com.youssefbazzaoui.notebookapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youssefbazzaoui.notebookapp.R;

import java.util.ArrayList;

public class EditDomainActivity extends AppCompatActivity {

    private EditText editText;
    private Button buttonUpdate;
    private Button buttonDelete;
    private LinearLayout domainWord;
    private DatabaseHelper mDatabaseHelper;
    private String selectedDomain;
    private ArrayList<String> domainWords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_domain);
        editText = (EditText) findViewById(R.id.editTextDomain);
        buttonUpdate = (Button) findViewById(R.id.btnUpdomain);
        buttonDelete = (Button) findViewById(R.id.btnDelDomain);
        domainWord = (LinearLayout) findViewById(R.id.domainWord);

        mDatabaseHelper = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedDomain = receivedIntent.getStringExtra("domain");
        editText.setText(selectedDomain);
        populateworddomain();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseHelper.updateDomain(editText.getText().toString(), selectedDomain);
                toastMessage("domain updated ");


            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteDomain(selectedDomain);
                toastMessage("domain deleted ");
            }
        });
    }

    private void loadWordsByDomain() {
        domainWords = new ArrayList<String>();
        Cursor resultat = mDatabaseHelper.getDataByDomain(selectedDomain);
        while (resultat.moveToNext()) {
            domainWords.add(resultat.getString(0));
        }
    }

    private void populateworddomain() {
        loadWordsByDomain();
        Cursor resultat = mDatabaseHelper.getData();
        while (resultat.moveToNext()) {
            String word = resultat.getString(0);
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(word);
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    if (cb.isChecked()) {
                        mDatabaseHelper.addToDomain(selectedDomain, cb.getText().toString());
                    } else {
                        mDatabaseHelper.removeFromDomain(selectedDomain, cb.getText().toString());
                    }
                }
            });
            cb.setChecked(domainWords.contains(word));
            domainWord.addView(cb);
        }

    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
