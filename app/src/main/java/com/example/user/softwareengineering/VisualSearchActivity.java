package com.example.user.softwareengineering;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.io.File;
import java.io.IOException;

/**
 *
 *  카메라 애플리케이션으로 사진을 찍고 그 사진을 받아오거나, 갤러리에서 이미지를 찾을 수 있음.
 *
 *  imageLoad 변수를 사용해서 이미지가 업로드 되지 않은 채로 검색 버튼을 누르면 경고 다이얼로그가 실행됨
 */

public class VisualSearchActivity extends AppCompatActivity {

    ImageView imageView;

    File file;
    boolean imageLoad;  // 이미지를 업로드 했는지 확인하는 변수

    RadioButton rdDisease, rdBug;

    private static int CAMERA_REQUEST_CODE = 101;   // 카메라 요청 코드
    private static int GALLERY_REQUEST_CODE = 201;    // 갤러리 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_search);

        File sdcard = Environment.getExternalStorageDirectory();
        file = new File(sdcard, "capture.jpg");

        imageView = (ImageView) findViewById(R.id.imageView);

        rdDisease = (RadioButton) findViewById(R.id.rdDiseaseImage);
        rdBug = (RadioButton) findViewById(R.id.rdBugImage);

        rdDisease.setChecked(true);

        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });

        Button btnGallery = (Button) findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageLoad == true) {
                    // 사진을 업로드 한 경우 결과를 ListViewActivity에서 확인한다.

                    // 사진을 ListViewActivity에서 보낼지, VisualSearchActivity에서 보낼지 결정한 후 다음을 구현한다.

                    // ListViewActivity에서 보내면 결과를 ListViewActivity에서 받아서 myIntent에 추가하고,
                    // startActivity(myIntent);를 startActivityForResult(myIntent, 요청코드); 로 바꾼다.

                    // VisualSearchActivity에서 보내면 사진을 myIntent에 추가하고,
                    // startActivity(myIntent);를 startActivityForResult(myIntent, 요청코드); 로 바꾼다.
                    // VisualSearchActivity에서 사진을 보내므로 결과 또한 해당 액티비티에서 받아서 사용한다.

                    Intent myIntent = new Intent(getApplicationContext(), DiseaseActivity.class);
                    myIntent.putExtra("NAME", "달팽이");

                    if(rdBug.isChecked())
                        myIntent.putExtra("SEARCH_TABLE", "bug");
                    else if(rdDisease.isChecked())
                        myIntent.putExtra("SEARCH_TABLE", "disease");

                    startActivity(myIntent);
                }
                else    // 사진을 업로드 하지 않은 경우 경고 메시지 출력
                    showMessage();
            }
        });

    }

    private void showMessage() {    // 이미지를 업로드하지 않은 경우 경고 메시지 출력
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("안내");
        builder.setIcon(android.R.drawable.ic_dialog_alert);    // 경고 이미지 설정

        // 확인버튼 설정
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        builder.setMessage("이미지가 업로드되지 않았습니다.");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void load() {   // 갤러리에서 사진을 불러오는 경우
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    private void capture() {    // 카메라 애플리케이션으로 사진을 찍는 경우
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 카메라 애플리케이션으로 사진을 찍고 결과를 받는 경우

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            imageView.setImageBitmap(bitmap);
            imageLoad = true;
        }
        else if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // 갤러리에서 사진을 선택하고 결과를 받는 경우

            Uri uri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                imageView.setImageBitmap(scaled);
                imageLoad = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
