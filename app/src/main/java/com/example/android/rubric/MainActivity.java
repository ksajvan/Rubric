package com.example.android.rubric;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction().add(R.id.fragment, new MovieFragment())
//                    .commit();
//        }
    }

    // TODO: Promeniti App Name tako da se dinamicki menja u zavisnosti od odabira filmova
    // main ekran trenutno ima naslov Rubric, koji je definisan u strings.xml fajlu, key je app_name
    // Proveriti da li je moguce menjati app_name u zavisnosti od odabira kriterijuma (Pop Movies stoji u mockup-u) a trebalo bi da stoji
    // Top Rated Movies ako je odabrana lista sa Top Rated filmovima

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
}
