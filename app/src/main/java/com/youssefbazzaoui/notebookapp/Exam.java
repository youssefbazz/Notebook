package com.youssefbazzaoui.notebookapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.youssefbazzaoui.notebookapp.R;

import java.util.HashMap;
import java.util.Random;

public class Exam extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    String currentwordeng;
    HashMap<String, String> hashMap;
    private Button btnVerify;
    private EditText editTextword;
    private TextView textViewword;
    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        btnVerify = (Button) findViewById(R.id.btn_verify);
        editTextword = (EditText) findViewById(R.id.editTextword);
        textViewword = (TextView) findViewById(R.id.textView6);
        mDatabaseHelper = new DatabaseHelper(this);


        mDatabaseHelper.getData();
        hashMap = new HashMap<String, String>();
        Cursor resultat = mDatabaseHelper.getData();

        while (resultat.moveToNext()) {
            String wordengl = resultat.getString(0);
            String wordfr = resultat.getString(1);
            hashMap.put(wordengl, wordfr);
        }
        rand = new Random();
        //currentwordeng;
        getNextWord();


        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentwordfre = hashMap.get(currentwordeng);
                if (currentwordfre.equals(editTextword.getText().toString())) {//get data from db))*/ {
                    toastMessage("well done");
                } else {
                    toastMessage("false, the correct answer is:" + currentwordfre);//getdata);
                }
                getNextWord();
            }
        });


    }

    private void getNextWord() {
        int i = rand.nextInt(hashMap.size());
        //long i = Math.round(Math.random() *(hashMap.size()-1));
        currentwordeng = (String) (hashMap.keySet().toArray())[i];
        textViewword.setText(currentwordeng);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
