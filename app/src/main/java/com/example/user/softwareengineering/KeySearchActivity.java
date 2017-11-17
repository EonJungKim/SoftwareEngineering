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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *  병충해를 직접적으로 검색하는 액티비티
 *  UI구성해야함.
 *  현재 구현되어 있는 버튼으로 인해 ListViewActivity로 화면을 전환하게 된다.
 */

public class KeySearchActivity extends AppCompatActivity {

    Button btnSearch;
    ListView listView;
    TextView txtBug;
    Spinner spnBug;

    SQLiteDatabase db;

    DiagnosisItem[] diagnosisItems;
    DiagnosisAdapter adapter;

    String bugName_ko = "전체보기";
    int itemNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_search);

        btnSearch  = (Button) findViewById(R.id.btnSearch);
        spnBug = (Spinner) findViewById(R.id.spnBug);
        listView = (ListView) findViewById(R.id.listView);
        txtBug = (TextView) findViewById(R.id.txtBug);

        findDatabase(bugName_ko);

        ArrayAdapter spnAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.data_spinner, android.R.layout.simple_spinner_item);
        spnBug.setAdapter(spnAdapter);

        spnBug.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bugName_ko = String.valueOf(parent.getItemAtPosition(position));
                txtBug.setText(bugName_ko);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findDatabase(bugName_ko);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = diagnosisItems[position].getName();

                        Intent myIntent = new Intent(getApplicationContext(), DiseaseActivity.class);
                        myIntent.putExtra("BUG_NAME", name);
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
            // 현재 액티비티를 프래그먼트로 바꾸게 되면 getApplicationContext()부분에서 에러가 발생
            // 아직까지 처리하기 어려워 그냥 액티비티 상태로 놔둠

            DiagnosisItem item = items.get(position);
            view.setName(item.getName());
            view.setSymptom(item.getSymptom());
            view.setImage(item.getResId());

            return view;
        }
    }

    private void findDatabase(String bugName_ko) {
        db = openOrCreateDatabase("bugs.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql;

            if(!bugName_ko.equals("전체보기")) {
                Toast.makeText(this, bugName_ko, Toast.LENGTH_SHORT).show();
                sql = "select bugNum, bugName_ko, symptom from bug where bugName_ko = \"" + bugName_ko + "\";";
            }
            else {
                sql = "select bugNum, bugName_ko, symptom from bug;";
            }

            Cursor cursor = db.rawQuery(sql, null);

            setListView(cursor);
        }

    }

    private void setListView(Cursor cursor) {
        itemNum = cursor.getCount();

        adapter = new DiagnosisAdapter();
        diagnosisItems = new DiagnosisItem[itemNum];

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();

            int resId = findResID(cursor.getInt(0));

            diagnosisItems[i] = new DiagnosisItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), resId);

            adapter.addItem(diagnosisItems[i]);
        }
    }

    private int findResID(int id) {
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
}