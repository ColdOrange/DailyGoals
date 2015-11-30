package com.orange.one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CalendarActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Intent intent = getIntent();
        String activityName = intent.getStringExtra("ActivityName");
        TextView textView = (TextView) findViewById(R.id.text_calendar);
        textView.setText("今天你" + activityName + "了吗");
        loadData(activityName);
        buttonClick(activityName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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


    private void loadData(String activityName) {
        FileInputStream in = null;
        BufferedReader reader = null;
        FileOutputStream out = null;
        BufferedWriter writer = null;

        TextView textView = (TextView) findViewById(R.id.text_days);

        try {
            in = openFileInput(activityName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder content = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            String[] allDays = content.toString().split("-");
            if (allDays.length == 1 && allDays[0].equals("")) {
                textView.setText("0");
            } else {
                textView.setText(Integer.toString(allDays.length));
            }

            MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
            materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
            for (int i = 0; i < allDays.length; i++) {
                if (!allDays[i].isEmpty()) {
                    String[] date = allDays[i].split("#");
                    int year = Integer.parseInt(date[0]);
                    int month = Integer.parseInt(date[1]);
                    int day = Integer.parseInt(date[2]);
                    CalendarDay calendarDay = CalendarDay.from(year, month, day);
                    materialCalendarView.setDateSelected(calendarDay, true);
                }
            }
            //materialCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        } catch (IOException e) {
            try {
                out = openFileOutput(activityName, Context.MODE_PRIVATE);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            textView.setText("0");
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

    private void buttonClick(final String activityName) {
        Button button = (Button) findViewById(R.id.button_calendar);
        final String mActivityName = activityName;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDay today = CalendarDay.today();
                String year = Integer.toString(today.getYear());
                String month = Integer.toString(today.getMonth());
                String day = Integer.toString(today.getDay());
                String date = year + "#" + month + "#" + day;

                FileInputStream in = null;
                BufferedReader reader = null;
                FileOutputStream out = null;
                BufferedWriter writer = null;

                try {
                    in = openFileInput(mActivityName);
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder content = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    String[] allDays = content.toString().split("-");
                    if (!allDays[allDays.length-1].isEmpty() && allDays[allDays.length-1].equals(date)) {
                        Toast.makeText(CalendarActivity.this, "今天已经打过卡了,不能再打!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CalendarActivity.this, "打卡成功!", Toast.LENGTH_SHORT).show();
                        try {
                            out = openFileOutput(mActivityName, MODE_APPEND);
                            writer = new BufferedWriter(new OutputStreamWriter(out));
                            writer.write(date + "-");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } finally {
                            try {
                                if (writer != null) {
                                    writer.close();
                                    loadData(mActivityName);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
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
        });
    }
}
