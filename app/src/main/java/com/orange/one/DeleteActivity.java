package com.orange.one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

public class DeleteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Button button = (Button) findViewById(R.id.button_delete_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.text_delete_activity);
                String deleteActivity = editText.getText().toString();
                updateActivities(deleteActivity);
                Intent intent = new Intent(DeleteActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
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

    private void updateActivities(String deleteActivity) {
        FileInputStream in = null;
        BufferedReader reader = null;
        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            in = openFileInput("Activities");
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            String allActivities = content.toString();
            if (allActivities.contains(deleteActivity)) {
                allActivities = allActivities.replace(deleteActivity + "-", "");
                try {
                    out = openFileOutput("Activities", Context.MODE_PRIVATE);
                    writer = new BufferedWriter(new OutputStreamWriter(out));
                    writer.write(allActivities);
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
            } else {
                Toast.makeText(this, "删除失败,当前没有该活动!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            if (deleteActivity.equals("背单词")) {
                try {
                    out = openFileOutput("Activities", Context.MODE_PRIVATE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                Toast.makeText(this, "删除失败,当前没有该活动!", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
