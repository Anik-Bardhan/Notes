package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class AddNotesActivity extends AppCompatActivity {

    TextView title;
    TextView notes;
    int flag;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        title = findViewById(R.id.title);
        notes = findViewById(R.id.notes);

        intent = getIntent();

        if (intent.getStringExtra("title") != null) {
            title.setText(intent.getStringExtra("title"));
            notes.setText(intent.getStringExtra("notes"));
            flag = 1;
        }
        else {
            flag = 0;
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (flag == 0) {
            MainActivity.title.add(title.getText().toString());
            MainActivity.notes.add(notes.getText().toString());
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }
        else {
            MainActivity.title.set(intent.getIntExtra("position", 0), title.getText().toString());
            MainActivity.notes.set(intent.getIntExtra("position", 0), notes.getText().toString());
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        try {
            sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.title)).apply();
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}