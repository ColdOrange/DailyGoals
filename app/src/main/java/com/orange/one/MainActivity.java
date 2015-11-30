package com.orange.one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private List<Item> itemList = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initItemList();
        ItemAdapter itemAdapter = new ItemAdapter(MainActivity.this, R.layout.list_item, itemList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = itemList.get(position);
                String activityName = item.getActivityName();
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                intent.putExtra("ActivityName", activityName);
                startActivity(intent);
            }
        });

        Button button_new = (Button) findViewById(R.id.new_button_main);
        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(intent, 1);
                // 根据返回码判断是新建活动后返回还是直接返回,以确定是否销毁当前MainActivity
            }
        });

        Button button_delete = (Button) findViewById(R.id.delete_button_main);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                startActivityForResult(intent, 2);
                // 根据返回码判断是新建活动后返回还是直接返回,以确定是否销毁当前MainActivity
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
            default:
        } }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void initItemList() {
        boolean fileExist = false;
        String activities = "";

        try {
            FileInputStream in = openFileInput("Activities");
            fileExist = true;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            activities = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            fileExist = false;
        }

        if (!fileExist) {
            Item item = new Item("背单词");
            itemList.add(0, item);
        } else {
            String[] itemName;
            itemName = activities.split("-");
            for (int i = 0; i < itemName.length; i++) {
                Item item = new Item(itemName[i]);
                if (!item.getActivityName().isEmpty()) {
                    itemList.add(i, item);
                }
            }
        }
    }
}
