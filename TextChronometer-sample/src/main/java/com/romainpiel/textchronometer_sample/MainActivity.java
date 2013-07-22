package com.romainpiel.textchronometer_sample;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long now = System.currentTimeMillis();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < 100; i++) {
            entries.add(new Entry("Entry "+i, now - i*1000));
        }

        MyAdapter adapter = new MyAdapter(this, android.R.id.list, entries);
        getListView().setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
