package com.orange.one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Button button = (Button) findViewById(R.id.button_new_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.text_new_activity);
                String newActivity = editText.getText().toString();
                saveActivities(newActivity);
                Intent intent = new Intent(NewActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveActivities(String newActivity) {
        FileInputStream in = null;
        BufferedReader reader = null;
        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            in = openFileInput("Activities");
            try {
                out = openFileOutput("Activities", Context.MODE_APPEND);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder content = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                if (!content.toString().contains(newActivity)) {
                    writer.write(newActivity+"-");
                } else {
                    Toast.makeText(this, "创建失败,该活动已存在!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                out = openFileOutput("Activities", Context.MODE_APPEND);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write("背单词-");
                writer.write(newActivity+"-");
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }


    }
}
