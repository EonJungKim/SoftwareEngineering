package com.example.user.softwareengineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 *  어떤 검색을 할 것인지 선택하는 액티비티
 */

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView btnBugImageSearch = (TextView) findViewById(R.id.btnImageSearch);
        btnBugImageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), VisualSearchActivity.class);
                startActivity(myIntent);
            }
        });

        TextView btnKeySearch = (TextView) findViewById(R.id.btnKeySearch);
        btnKeySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), KeySearchActivity.class);
                myIntent.putExtra("REQUEST_CODE", "key");
                startActivity(myIntent);
            }
        });

        TextView btnSymptomSearch = (TextView) findViewById(R.id.btnSymptomSearch);
        btnSymptomSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), KeySearchActivity.class);
                myIntent.putExtra("REQUEST_CODE", "symptom");
                startActivity(myIntent);
            }
        });
    }
}
