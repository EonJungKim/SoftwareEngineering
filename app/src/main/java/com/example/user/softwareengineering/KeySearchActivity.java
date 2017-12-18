package com.example.user.softwareengineering;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *  병충해를 직접적으로 검색하는 액티비티
 *  현재 구현되어 있는 버튼으로 인해 ListViewActivity로 화면을 전환하게 된다.
 */

public class KeySearchActivity extends AppCompatActivity {

    Button btnSearch;
    ListView listView;
    TextView txtKey;
    Spinner spnKey;

    RadioGroup container;
    RadioButton rdDisease, rdBug;

    ArrayAdapter spnAdapter;

    SQLiteDatabase db;

    DiagnosisItem[] diagnosisItems;
    DiagnosisAdapter adapter;

    String key = "전체보기";
    int itemNum;

    String REQUEST_CODE;

    String searchTable;

    private void initActivity() {
        btnSearch  = (Button) findViewById(R.id.btnSearch);
        spnKey = (Spinner) findViewById(R.id.spnBug);
        listView = (ListView) findViewById(R.id.listView);
        txtKey = (TextView) findViewById(R.id.txtBug);

        container = (RadioGroup) findViewById(R.id.container);
        rdDisease = (RadioButton) findViewById(R.id.rdDisease);
        rdBug = (RadioButton) findViewById(R.id.rdBug);

        Intent intent = getIntent();
        REQUEST_CODE = intent.getStringExtra("REQUEST_CODE");

        if(REQUEST_CODE.equals("key")) {
            rdBug.setVisibility(View.VISIBLE);
            rdDisease.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);

            spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.bug_spinner, android.R.layout.simple_spinner_item);
            rdBug.setChecked(true);
        }
        else if(REQUEST_CODE.equals("symptom")) {
            container.setVisibility(View.GONE);
            rdBug.setVisibility(View.GONE);
            rdDisease.setVisibility(View.GONE);

            spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.symptom_spinner, android.R.layout.simple_spinner_item);
        }

        spnKey.setAdapter(spnAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_search);

        initActivity();

        //findDatabase(key);

        rdDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.disease_spinner, android.R.layout.simple_spinner_item);
                spnKey.setAdapter(spnAdapter);
            }
        });

        rdBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.bug_spinner, android.R.layout.simple_spinner_item);
                spnKey.setAdapter(spnAdapter);
            }
        });

        spnKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                key = String.valueOf(parent.getItemAtPosition(position));
                txtKey.setText(key);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findDatabase(key);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = diagnosisItems[position].getName();

                        Intent myIntent = new Intent(getApplicationContext(), DiseaseActivity.class);
                        myIntent.putExtra("NAME", name);
                        myIntent.putExtra("SEARCH_TABLE", searchTable);
                        startActivity(myIntent);
                    }
                });
            }
        });
    }

    class DiagnosisAdapter extends BaseAdapter {
        ArrayList<DiagnosisItem> items = new ArrayList<DiagnosisItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        private void addItem(DiagnosisItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            DiagnosisItemView view = new DiagnosisItemView(getApplicationContext());

            DiagnosisItem item = items.get(position);
            view.setName(item.getName());
            view.setSymptom(item.getSymptom());
            view.setImage(item.getResId());

            return view;
        }
    }

    private void findDatabase(String key) {

        db = openOrCreateDatabase("mandarin.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql1 = null;

            if(REQUEST_CODE.equals("key")) {

                if(rdBug.isChecked() == true) {

                    if(!key.equals("전체보기")) {
                        sql1 = "select num, name_ko, symptom from bug where name_ko = \"" + key + "\";";
                    }
                    else {
                        sql1 = "select num, name_ko, symptom from bug;";
                    }

                    searchTable = "bug";
                }
                else if(rdDisease.isChecked()) {
                    if(!key.equals("전체보기")) {
                        sql1 = "select num, name_ko, symptom from disease where name_ko = \"" + key + "\";";
                    }
                    else {
                        sql1 = "select num, name_ko, symptom from disease;";
                    }

                    searchTable = "disease";
                }

                Cursor cursor1 = db.rawQuery(sql1, null);

                setListView(cursor1);
            }
            else if(REQUEST_CODE.equals("symptom")) {
                String sql2;

                if(!key.equals("전체보기")) {
                    sql1 = "select num, name_ko, symptom from bug where symptom like \'%" + key + "%\';";
                    sql2 = "select num, name_ko, symptom from disease where symptom like '%" + key + "%\';";
                }
                else {
                    sql1 = "select num, name_ko, symptom from bug;";
                    sql2 = "select num, name_ko, symptom from disease;";
                }

                Cursor cursor1 = db.rawQuery(sql1, null);
                Cursor cursor2 = db.rawQuery(sql2, null);

                setListView(cursor1, cursor2);
            }
        }
    }

    private void setListView(Cursor cursor1, Cursor cursor2) {
        itemNum = cursor1.getCount() + cursor2.getCount();

        adapter = new DiagnosisAdapter();
        diagnosisItems = new DiagnosisItem[itemNum];

        searchTable = "all";

        for(int i = 0; i < cursor1.getCount(); i++) {
            cursor1.moveToNext();

            int resId = findBugResID(cursor1.getInt(0));

            diagnosisItems[i] = new DiagnosisItem(cursor1.getInt(0), cursor1.getString(1), cursor1.getString(2), resId);

            adapter.addItem(diagnosisItems[i]);
        }

        for(int i = cursor1.getCount(); i < itemNum; i++) {
            cursor2.moveToNext();

            int resId = findDiseaseResID(cursor2.getInt(0));

            diagnosisItems[i] = new DiagnosisItem(cursor2.getInt(0), cursor2.getString(1), cursor2.getString(2), resId);

            adapter.addItem(diagnosisItems[i]);
        }

    }

    private void setListView(Cursor cursor) {
        itemNum = cursor.getCount();

        adapter = new DiagnosisAdapter();
        diagnosisItems = new DiagnosisItem[itemNum];

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            int resId = 0;

            if(searchTable.equals("bug")) {
                resId = findBugResID(cursor.getInt(0));
            }
            else if(searchTable.equals("disease")) {
                resId = findDiseaseResID(cursor.getInt(0));
            }

            diagnosisItems[i] = new DiagnosisItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), resId);

            adapter.addItem(diagnosisItems[i]);
        }
    }

    private int findBugResID(int id) {
        switch (id) {
            case 1:
                return R.drawable.bug1;
            case 2:
                return R.drawable.bug2;
            case 3:
                return R.drawable.bug3;
            case 4:
                return R.drawable.bug4;
            case 5:
                return R.drawable.bug5;
            case 6:
                return R.drawable.bug6;
            case 7:
                return R.drawable.bug7;
            case 8:
                return R.drawable.bug8;
            case 9:
                return R.drawable.bug9;
            case 10:
                return R.drawable.bug10;
            case 11:
                return R.drawable.bug11;
            case 12:
                return R.drawable.bug12;
            case 13:
                return R.drawable.bug13;
            case 14:
                return R.drawable.bug14;
            case 15:
                return R.drawable.bug15;
        }

        return R.drawable.bug1;
    }

    private int findDiseaseResID(int id) {
        switch (id) {
            case 1:
                return R.drawable.disease1;
            case 2:
                return R.drawable.disease2;
            case 3:
                return R.drawable.disease3;
        }

        return R.drawable.disease1;
    }
}