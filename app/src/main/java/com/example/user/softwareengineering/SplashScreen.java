package com.example.user.softwareengineering;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.Random;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class SplashScreen extends Activity {

    Thread splashTread;
    ImageView imageView;

    String name_eg, name_ko, symptom, content, management;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        imageView = (ImageView)findViewById(R.id.splash);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        int[] ids = new int[]{R.drawable.s_img};

        Random randomGenerator = new Random();
        int rg = randomGenerator.nextInt(ids.length);

        this.imageView.setImageDrawable(getResources().getDrawable(ids[rg]));

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time

                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }

                    Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                    SplashScreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashScreen.this.finish();
                }
            }
        };

        splashTread.start();

        try {
            createDataBase();

            int row1 = 0, column1 = 0;
            int row2 = 0, column2 = 0;

            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("mandarin.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);

            Sheet sh = workbook.getSheet("Sheet1");

            row1 = sh.getRows();
            column1 = sh.getColumns();

            for(int r = 1; r < row1; r++) {
                for(int c = 1; c < column1; c++) {
                    Cell sheetCell = sh.getCell(c,r);

                    switch(c) {
                        case 1:
                            name_eg = sheetCell.getContents();
                            break;
                        case 2:
                            name_ko = sheetCell.getContents();
                            break;
                        case 3:
                            symptom = sheetCell.getContents();
                            break;
                        case 4:
                            content = sheetCell.getContents();
                            break;
                        case 5:
                            management = sheetCell.getContents();
                            break;
                    }
                }

                insertData("bug", name_eg, name_ko, symptom, content, management);
/*
                    Cell sheetCell = sh.getCell(3, r);
                    symptom = sheetCell.getContents();

                    updateDate("bug", symptom, r);*/
            }

            Sheet sh2 = workbook.getSheet("Sheet2");

            row2 = sh2.getRows();
            column2 = sh2.getColumns();

            for(int r = 1; r < row2; r++) {
                for(int c = 1; c < column2; c++) {
                    Cell sheetCell = sh2.getCell(c,r);

                    switch(c) {
                        case 1:
                            name_eg = sheetCell.getContents();
                            break;
                        case 2:
                            name_ko = sheetCell.getContents();
                            break;
                        case 3:
                            symptom = sheetCell.getContents();
                            break;
                        case 4:
                            content = sheetCell.getContents();
                            break;
                        case 5:
                            management = sheetCell.getContents();
                            break;
                    }
                }

                insertData("disease", name_eg, name_ko, symptom, content, management);
/*
                    Cell sheetCell = sh2.getCell(3, r);
                    symptom = sheetCell.getContents();

                    updateDate("disease", symptom, r);*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDataBase() {
        db = openOrCreateDatabase("mandarin.db", MODE_PRIVATE, null);

        if(db != null) {
            String sql = "create table bug (num integer PRIMARY KEY autoincrement, name_eg text, name_ko text, symptom text, content text, management text);";
            db.execSQL(sql);

            sql = "create table disease (num integer PRIMARY KEY autoincrement, name_eg text, name_ko text, symptom text, content text, management text);";
            db.execSQL(sql);
        }
    }

    private void insertData(String tableName, String name_eg, String name_ko, String symptom, String content, String management) {
        if(tableName.equals("bug")) {
            if(db != null) {
                String sql = "insert into bug (name_eg, name_ko, symptom, content, management) values(?, ?, ?, ?, ?);";
                Object[] params = {name_eg, name_ko, symptom, content, management};

                db.execSQL(sql, params);
            }
        }
        else if(tableName.equals("disease")) {
            if(db != null) {
                String sql = "insert into disease (name_eg, name_ko, symptom, content, management) values(?, ?, ?, ?, ?);";
                Object[] params = {name_eg, name_ko, symptom, content, management};

                db.execSQL(sql, params);
            }
        }
    }

    private void updateTable(String tableName, String name_ko, int num) {

        if(tableName.equals("bug")) {
            if(db != null) {
                String sql = "update bug set name_ko = \"" + name_ko + "\" where num = " + num + ";";
                db.execSQL(sql);
            }
        }
        else if(tableName.equals("disease")) {
            if(db != null) {
                String sql = "update disease set name_ko = \"" + name_ko + "\" where num = " + num + ";";
                db.execSQL(sql);
            }
        }
    }

}