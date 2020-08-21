package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> title = new ArrayList<>();
    static  ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
    ListView listView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(getApplicationContext(), AddNotesActivity.class);
                startActivity(intent);
                return  true;

            default: return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        title.clear();
        notes.clear();

        try {
            title = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("title", ObjectSerializer.serialize(new ArrayList<>())));
            notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<>())));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, title);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddNotesActivity.class);
                intent.putExtra("title", title.get(position));
                intent.putExtra("notes", notes.get(position));
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete this note!!!")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                title.remove(position);
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                try {
                                    sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.title)).apply();

                                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


                return true;
            }
        });

    }
}