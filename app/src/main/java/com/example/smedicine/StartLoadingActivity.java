package com.example.smedicine;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class StartLoadingActivity extends BaseActivity {
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_loading);
        ImageView imageView = (ImageView)findViewById(R.id.img_start_loading);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(MyDatabaseHelper.checkLogin(db)){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),IndexActivity.class);
                    startActivity(intent);
                }
            }, 2000);
        }
        else{
            //两秒后启动Index
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),ReallyLogin.class);
                    startActivity(intent);
                }
            }, 2000);
        }

    }


}

