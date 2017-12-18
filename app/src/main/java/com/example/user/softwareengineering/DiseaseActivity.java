package com.example.user.softwareengineering;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 *  ListViewActivity에서 선택된 진단에 대한 정보를 보여주는 액티비티
 *  UI 구현해야 함.
 *  (일단 어떤 데이터를 보여줄지 정해져야 UI 구성이 편할듯함)
 *
 *  대처방안 버튼을 만들고, 클릭하면 ManagementActivity로 화면을 전환한다.
 */

public class DiseaseActivity extends AppCompatActivity {

    int num;
    String name_ko;
    String name_eg;
    String symptom;
    String content;
    String management;
    ScrollView scrollView;

    TextView txtDiseaseNameKorean, txtDiseaseNameEnglish, txtSymptom, txtContent, txtManagement;
    ImageView imageView;

    SQLiteDatabase db;

    String searchTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        txtSymptom = (TextView) findViewById(R.id.txtSymptom);
        txtContent = (TextView) findViewById(R.id.txtExplanation);
        txtManagement = (TextView) findViewById(R.id.txtManagement);

        imageView = (ImageView) findViewById(R.id.imageView2);

        txtDiseaseNameKorean = (TextView) findViewById(R.id.txtDiseaseNameKorean);
        txtDiseaseNameEnglish = (TextView) findViewById(R.id.txtDiseaseNameEnglish);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        Intent intent = getIntent();
        name_ko = intent.getStringExtra("NAME");
        searchTable = intent.getStringExtra("SEARCH_TABLE");

        setTextView();
    }

    private void setTextView() {
        db = openOrCreateDatabase("mandarin.db", MODE_PRIVATE, null);

        if(db != null) {

            String sql1 = null;

            Cursor cursor1 = null, cursor2;

            if(searchTable.equals("all")) {
                sql1 = "select num, name_eg, name_ko, symptom, content, management from bug where name_ko = \"" + name_ko + "\";";
                String sql2 = "select num, name_eg, name_ko, symptom, content, management from disease where name_ko = \"" + name_ko + "\";";

                cursor1 = db.rawQuery(sql1, null);
                cursor2 = db.rawQuery(sql2, null);

                if(cursor1.getCount() == 1) {
                    searchTable = "bug";

                    for(int i = 0; i < cursor1.getCount(); i++) {
                        cursor1.moveToNext();
                        num = cursor1.getInt(0);
                        name_eg = cursor1.getString(1);
                        name_ko = cursor1.getString(2);
                        symptom = cursor1.getString(3);
                        content = cursor1.getString(4);
                        management = cursor1.getString(5);
                    }
                }
                else if(cursor2.getCount() == 1){
                    searchTable = "disease";

                    for(int i = 0; i < cursor2.getCount(); i++) {
                        cursor2.moveToNext();
                        num = cursor2.getInt(0);
                        name_ko = cursor2.getString(2);
                        name_eg = cursor2.getString(1);
                        symptom = cursor2.getString(3);
                        content = cursor2.getString(4);
                        management = cursor2.getString(5);
                    }
                }

            }
            else {
                if(searchTable.equals("bug")) {
                    sql1 = "select num, name_eg, name_ko, symptom, content, management from bug where name_ko = \"" + name_ko + "\";";
                    cursor1 = db.rawQuery(sql1, null);

                }
                else if(searchTable.equals("disease")) {
                    sql1 = "select num, name_eg, name_ko, symptom, content, management from disease where name_ko = \"" + name_ko + "\";";
                    cursor1 = db.rawQuery(sql1, null);

                }

                for(int i = 0; i < cursor1.getCount(); i++) {
                    cursor1.moveToNext();
                    num = cursor1.getInt(0);
                    name_ko = cursor1.getString(2);
                    name_eg = cursor1.getString(1);
                    symptom = cursor1.getString(3);
                    content = cursor1.getString(4);
                    management = cursor1.getString(5);
                }
            }

            txtDiseaseNameKorean.setText(name_ko);
            txtDiseaseNameEnglish.setText(name_eg);
            txtSymptom.setText(symptom);
            txtContent.setText(content);
            txtManagement.setText(management);

            if(searchTable.equals("bug")) {
                imageView.setImageResource(findBugResID(num));
            }
            else {
                imageView.setImageResource(findDiseaseResID(num));
            }
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

        return R.mipmap.ic_launcher;
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

        return R.mipmap.ic_launcher;
    }

}