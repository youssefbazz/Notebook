package com.youssefbazzaoui.notebookapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.youssefbazzaoui.notebookapp.R;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class PagePrincipale extends AppCompatActivity {
    private static final java.lang.String SEPARATOR = ";";
    private static final int IMPORT_FILE = 0;
    private static final int EXPORT_FILE = 1;
    DatabaseHelper mDatabaseHelper;
    private Button btnAddword;
    private Button btnViewword;
    private Button btnExam;
    private Button btnDomain;


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_principale);
        btnAddword = (Button) findViewById(R.id.btn_addword);
        btnViewword = (Button) findViewById(R.id.btn_viewword);
        btnDomain = (Button) findViewById(R.id.btnDomain);
        btnExam = (Button) findViewById(R.id.btn_exam);

        mDatabaseHelper = new DatabaseHelper(this);

        btnAddword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AddWordActivity.this, com.youssefbazzaoui.notebookapp.ListDataActivity.class);
                Intent intent = new Intent(PagePrincipale.this, AddWordActivity.class);
                startActivity(intent);

            }
        });
        btnViewword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PagePrincipale.this, ListDataActivity.class);
                startActivity(intent1);


            }
        });

        btnDomain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AddWordActivity.this, com.youssefbazzaoui.notebookapp.ListDataActivity.class);
                Intent intent = new Intent(PagePrincipale.this, MainDomainActivity.class);
                startActivity(intent);

            }
        });
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PagePrincipale.this, Exam.class);
                startActivity(intent);

            }
        });

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import:

                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("plain/text");
                try {
                    startActivityForResult(fileintent, IMPORT_FILE);
                } catch (ActivityNotFoundException e) {
                    Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                }
                toastMessage("Option select " + item.getItemId());
                break;
            case R.id.action_export:
                Intent fileExportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                fileExportIntent.addCategory(Intent.CATEGORY_OPENABLE);
                fileExportIntent.setType("plain/text");
                fileExportIntent.putExtra(Intent.EXTRA_TITLE, "dictionnary.txt");
                try {
                    startActivityForResult(fileExportIntent, EXPORT_FILE);
                } catch (ActivityNotFoundException e) {
                    Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                }


        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMPORT_FILE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                importFile(fileUri);
            }
        } else if (requestCode == EXPORT_FILE) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                exportFile(fileUri);
            }
        }
    }

    public boolean exportFile(Uri file) {
        OutputStream out = null;
        //String filename= file.getPath()+".txt";

        BufferedOutputStream bos = null;

        try {
            out = this.getContentResolver().openOutputStream(file);

            bos = new BufferedOutputStream(out);
            Cursor result = mDatabaseHelper.getData();

            byte[] buf = new byte[1024];
            while (result.moveToNext()) {
                bos.write(result.getString(0).getBytes());
                bos.write(SEPARATOR.getBytes());
                bos.write(result.getString(1).getBytes());
                bos.write(System.getProperty("line.separator").getBytes());
            }
            toastMessage("Successs");
        } catch (IOException e) {

            toastMessage(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean importFile(Uri uri) {
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;
        try {

            InputStream is = this.getContentResolver().openInputStream(uri);

            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(SEPARATOR);
                mDatabaseHelper.addData(fields[0], fields[1]);
                text.append(line);
                Log.i("Test", "text : " + text + " : end");
                text.append('\n');
            }
            br.close();
            toastMessage("Successfully imported");
            return true;
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (Exception E) {

            }
        }
        return false;
    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
