package com.youssefbazzaoui.notebookapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.youssefbazzaoui.notebookapp.R;

import java.util.Locale;


public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";
    DatabaseHelper mDatabaseHelper;
    private Button btnSave, btnDelete, btnSpeech;
    private EditText editableEng;
    private EditText editableFr;
    private TextToSpeech t1_fr, t2_eng;
    private String selectedFr;
    private String selectedEng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnSpeech = (Button) findViewById(R.id.btn_speech);
        editableEng = (EditText) findViewById(R.id.editableEng);
        editableFr = (EditText) findViewById(R.id.editableFr);
        mDatabaseHelper = new DatabaseHelper(this);

        t1_fr = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1_fr.setLanguage(Locale.FRANCE);
                }
            }
        }
        );

        t2_eng = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2_eng.setLanguage(Locale.ENGLISH);
                }
            }
        }
        );

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedEng = receivedIntent.getStringExtra("eng"); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        selectedFr = receivedIntent.getStringExtra("fr");

        //set the text to show the current selected name
        editableEng.setText(selectedEng);
        editableFr.setText(selectedFr);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemEng = editableEng.getText().toString();
                String itemFr = editableFr.getText().toString();
                if (!itemEng.equals("") && !itemFr.equals("")) {
                    mDatabaseHelper.updateEntry(selectedEng, itemEng, itemFr);
                    toastMessage("Entry updated");
                } else {
                    toastMessage("You must enter a name");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteName(selectedEng, selectedFr);
                editableEng.setText("");
                editableFr.setText("");

                toastMessage("removed from database");
            }
        });

        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tospeak2 = editableFr.getText().toString();
                String tospeak1 = editableEng.getText().toString();
                Toast.makeText(getApplicationContext(), tospeak1, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), tospeak2, Toast.LENGTH_SHORT).show();

                t2_eng.speak(tospeak1, TextToSpeech.QUEUE_FLUSH, null);
                t1_fr.speak(tospeak2, TextToSpeech.QUEUE_FLUSH, null);


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






