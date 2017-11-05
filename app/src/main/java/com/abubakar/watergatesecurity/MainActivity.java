package com.abubakar.watergatesecurity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String savedString = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView textViewDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Display string
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        savedString = sharedPreferences.getString("patrolData",null);
        //Array List
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Check North Tower Foyer");
        arrayList.add("Check South Tower Foyer");
        arrayList.add("Check Podium Foyer");
        arrayList.add("Patrol Building Externally");
        arrayList.add("Patrol Building Square");
        arrayList.add("Patrol Pool & Gym Area");
        arrayList.add("Patrol Car-park");
        arrayList.add("Close Pool & Gym");
        arrayList.add("Patrol North and South Tower Levels");
        arrayList.add("Patrol Podium Levels");

        //Attach UI
        Button saveBtn = (Button) findViewById(R.id.btn_save);
        Button selectTimeBtn = (Button) findViewById(R.id.btn_selectTime);
        Button selectActivityButton = (Button) findViewById(R.id.btn_selectActivity);
        Button clearBtn = (Button) findViewById(R.id.btn_clear);
        ImageButton clearActBtn = (ImageButton) findViewById(R.id.btn_actClear);
        ImageButton clearTimeBtn = (ImageButton) findViewById(R.id.btn_timeClear);
        final TextView textViewActivity = (TextView) findViewById(R.id.tv_activitiLabel);
        final TextView textViewTime = (TextView) findViewById(R.id.tv_time);
        textViewDisplay = (TextView) findViewById(R.id.tv_display);
        textViewDisplay.setMovementMethod(new ScrollingMovementMethod());
        // If saved data
        if (savedString != null){
            textViewDisplay.append(savedString);
        }
        //Button Listeners
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time =  textViewTime.getText().toString();
                String activity = textViewActivity.getText().toString();
                if (!activity.equalsIgnoreCase("No Activity Selected") && !time.equalsIgnoreCase("No Time Selected")){
                    textViewDisplay.append("\n- " + activity + " | Time: "+ time);
                    Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG).show();
                    textViewActivity.setText("No Activity Selected");
                    textViewTime.setText("No Time Selected");
                }else {
                    Toast.makeText(getApplicationContext(),"Select Both Activity & Time",Toast.LENGTH_LONG).show();
                }
            }
        });
        clearActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewActivity.setText("No Activity Selected");
            }
        });
        clearTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewTime.setText("No Time Selected");
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewDisplay.setText("");
                textViewActivity.setText("No Activity Selected");
                textViewTime.setText("No Time Selected");
                editor.remove("patrolData");
                editor.apply();
            }
        });
        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.DialogFragment dialogFragment = new TimeDialog();
                dialogFragment.show(getFragmentManager(), "timePicker");
            }
        });
        //list data
        ListView listView = new ListView(this);
        AddActivityAdapter adapter = new AddActivityAdapter(getApplicationContext(),arrayList);
        listView.setAdapter(adapter);
        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("SELECT TASK TO ADD");
        builder.setView(listView);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alertDialog = builder.create();
        selectActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                textViewActivity.setText(arrayList.get(i));
                alertDialog.dismiss();
            }
        });

    }

    @Override
    protected void onPause() {
        savedString = textViewDisplay.getText().toString().trim();
        editor.putString("patrolData",savedString);
        editor.apply();
        super.onPause();
    }

    public class AddActivityAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<String> activitiesList;

        public AddActivityAdapter(Context context, ArrayList<String> activitiesList) {
            this.context = context;
            this.activitiesList = activitiesList;
        }

        @Override
        public int getCount() {
            return activitiesList.size();
        }

        @Override
        public Object getItem(int i) {
            return activitiesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = View.inflate(context, R.layout.add_activity_list_row,null);
            TextView activityTV = v.findViewById(R.id.tv_activityRow);
            activityTV.setText(activitiesList.get(i));
            return v;
        }
    }
}
