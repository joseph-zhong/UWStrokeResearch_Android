package com.stroke_trial_research.str;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEST OF PARSER CLASS
        JSONParser jsonParser= new JSONParser();
        long startTime = System.nanoTime();
        jsonParser.getNodeList(getApplicationContext(), R.raw.stroke_demo);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        Log.d("V", ("List: " + duration));
        //Takes about 70 - 90 milliseconds. Absolutely baffling.

        startTime = System.nanoTime();
        Node n = jsonParser.getNodeTree(getApplicationContext(), R.raw.stroke_demo_new);
        endTime = System.nanoTime();

        duration = (endTime - startTime);
        String dur = "Tree: " + duration;
        Log.d("V", dur);
        //Takes about 37 milliseconds. Hella slow right now.

        //Eventual test of tree connections.
        /*
        String tip = "";

        while (!n.type.equals("RESULT") || !n.type.equals("UNKNOWN")) {
            tip = n.type;

            switch (tip) {
                case "NUMBER":
                    break;
                case "BUTTON":
                    break;
                case ""
            }
        }
        */


    }

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
