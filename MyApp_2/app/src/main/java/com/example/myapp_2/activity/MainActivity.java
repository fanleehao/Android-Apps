package com.example.myapp_2.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapp_2.database.MyAdapter;
import com.example.myapp_2.R;
import com.example.myapp_2.database.MyAdapterGuru;
import com.example.myapp_2.media.SoundPlayer;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends Activity {

    private static MainActivity mainActivity = null;
    public MainActivity(){
        mainActivity=this;
    }
    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    private int level;
    private Dialog dialog;
    private Bitmap bitmap;
    private GridView gridView;
    private Button buttonGuru;
    public int getLevel(){
        return level;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        String username = MenuActivity.getMenuActivity().getUser().getUsername();
        level = MenuActivity.getMenuActivity().getMyDB().selectP(username).getLevel();
        buttonGuru = (Button)findViewById(R.id.bt_guru);
        //GridView
        gridView = (GridView)findViewById(R.id.gv);
        switch (MenuActivity.getMenuActivity().getGuru()){
            case 0:
                MyAdapter myAdapter = new MyAdapter(this);
                gridView.setAdapter(myAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                        if (position > level){
                            Toast.makeText(MainActivity.this,"请先通关上一关卡",Toast.LENGTH_SHORT).show();
                        }else {
                            //bitmap = BitmapFactory.decodeResource(getResources(),pictures[position]);
                            MenuActivity.getMenuActivity().setPos(position);
                            Intent intent = new Intent(MainActivity.this,GameActivity.class);
                            startActivityForResult(intent,0);
                        }
                    }
                });
                buttonGuru.setText("进入骨灰级难度");
                break;
            case 1:
                MyAdapterGuru myAdapterGuru = new MyAdapterGuru(this);
                gridView.setAdapter(myAdapterGuru);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                        if (position + 10 > level){
                            Toast.makeText(MainActivity.this,"请先通关上一关卡",Toast.LENGTH_SHORT).show();
                        }else {
                            //bitmap = BitmapFactory.decodeResource(getResources(),pictures[position]);
                            MenuActivity.getMenuActivity().setPos(position + 10);
                            Intent intent = new Intent(MainActivity.this,GameActivity.class);
                            startActivityForResult(intent,0);
                        }
                    }
                });
                buttonGuru.setText("返回正常难度");
                break;
        }
    }
    public void menuOnClick(View view){
        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
        //MENU对话框
        dialog = new Dialog(this,R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_hint,null);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        Button button = (Button)dialogView.findViewById(R.id.bt_dialog_menu_confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                dialog.dismiss();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }
    private static final int IMAGE = 1;
    public void selectOnClick(View view){
        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    IMAGE);

        }else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == IMAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            } else
            {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            String imagePath = cursor.getString(columnIndex);
            if( imagePath != null) {
                showImage(imagePath);
            }
            cursor.close();
        }
        if (requestCode == 0){
            init();
        }
    }
    private void showImage(String imagePath){
        Bitmap bitmaptmp = BitmapFactory.decodeFile(imagePath);
        final Bitmap bm = zoomBitmap(bitmaptmp,500,500);
        ImageView imageView = (ImageView)findViewById(R.id.iv_selected);
        imageView.setImageBitmap(bm);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                bitmap = bm;
                MenuActivity.getMenuActivity().setPos(-1);
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });
    }
    private Bitmap zoomBitmap(Bitmap bitmap,int w,int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return newBitmap;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }


    public void onClick(View view){
        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
        switch (MenuActivity.getMenuActivity().getGuru()){
            case 0:
                if (level >= 10){
                    MyAdapterGuru myAdapterGuru = new MyAdapterGuru(this);
                    gridView.setAdapter(myAdapterGuru);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                            if (position + 10 > level){
                                Toast.makeText(MainActivity.this,"请先通关上一关卡",Toast.LENGTH_SHORT).show();
                            }else {
                                //bitmap = BitmapFactory.decodeResource(getResources(),pictures[position]);
                                MenuActivity.getMenuActivity().setPos(position + 10);
                                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                                startActivityForResult(intent,0);
                            }
                        }
                    });
                    buttonGuru.setText("返回正常难度");
                    MenuActivity.getMenuActivity().setGuru(1);
                } else {
                    Toast.makeText(this,"请通关全部普通关卡",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                MyAdapter myAdapter = new MyAdapter(this);
                gridView.setAdapter(myAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SoundPlayer.move2(MenuActivity.getMenuActivity().getSoundVolume());
                        if (position > level){
                            Toast.makeText(MainActivity.this,"请先通关上一关卡",Toast.LENGTH_SHORT).show();
                        }else {
                            //bitmap = BitmapFactory.decodeResource(getResources(),pictures[position]);
                            MenuActivity.getMenuActivity().setPos(position);
                            Intent intent = new Intent(MainActivity.this,GameActivity.class);
                            startActivityForResult(intent,0);
                        }
                    }
                });
                buttonGuru.setText("进入骨灰级难度");
                MenuActivity.getMenuActivity().setGuru(0);
                break;
        }
    }
}
