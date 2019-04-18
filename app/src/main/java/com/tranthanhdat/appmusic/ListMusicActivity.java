package com.tranthanhdat.appmusic;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ListMusicActivity extends AppCompatActivity {

    private static final int STORAGE_REQUEST_CODE = 400;

    ArrayList<HashMap<String, String>> lsSong;
    String storagePermission[];

    private Handler threadHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_mucsic);

        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        loadLsSong(Environment.getExternalStorageDirectory().getAbsolutePath());

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void loadLsSong(String rootPath) {
        if(!checkStoragePermisstion()){
            //storge permisstion not allowed, request it
            requestStoragePermission();
        }
        else
        {

        lsSong = getPlayList(rootPath);
        if (lsSong != null) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.parent_list);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < lsSong.size(); i++) {
                try {
                    Button btnTmp = new Button(this);
                    btnTmp.setId(i);
                    btnTmp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    btnTmp.setText(lsSong.get(i).get("file_name") + lsSong.get(i).get("file_path"));
                    btnTmp.setOnClickListener(new Button.OnClickListener(){
                        public void onClick(View v) {
                            openMainMusic(v.getId());
                        }
                    });
                    linearLayout.addView(btnTmp, lp);

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

    private void openMainMusic(int id) {
        // Chuẩn bị dữ liệu Intent.
        Intent data = new Intent(this,MainActivity.class);
        data.putExtra("URI", lsSong.get(id).get("file_path"));
        // Activity đã hoàn thành OK, trả về dữ liệu.
        this.startActivity(data);

        onBackPressed();
    }


    private ArrayList<HashMap<String, String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        try {

            File file = new File(rootPath);
            File[] lsFile = file.listFiles();

            for (File fileTmp : lsFile) {
                if (fileTmp.isDirectory()) {
                    if (getPlayList(fileTmp.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(fileTmp.getAbsolutePath()));
                    }
                } else if (fileTmp.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", fileTmp.getAbsolutePath());
                    song.put("file_name", fileTmp.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    private boolean checkStoragePermisstion() {
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }



    class UpdateSeekBarThread implements Runnable {
        public void run()  {

            Toast.makeText(ListMusicActivity.this, "Đang quét...", Toast.LENGTH_SHORT).show();
            // Ngừng thread 50 mili giây.
            threadHandler.postDelayed(this, 50);
        }
    }
}
