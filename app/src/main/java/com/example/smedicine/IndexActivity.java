package com.example.smedicine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smedicine.bean.PlanShow;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IndexActivity extends BaseActivity {

    String mcid;
    RecyclerView recyclerView;
    CircleImageView circleImageView;

    //list中获取药物列表的任务
    class GetMedicinePlanInfo extends AsyncTask<String, Integer, List<PlanShow>> {


        private List<PlanShow> getJsonData(String url) {
            List<PlanShow> planShowList = new ArrayList<>();
            PlanShow planShow;
            JSONObject jsonObject, jsonObjectfor;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                jsonObject = new JSONObject(responseData);
                JSONArray jsonArray = jsonObject.getJSONArray("plan");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String medicineName;
                    String num;
                    jsonObjectfor = jsonArray.getJSONObject(i);
                    JSONObject medicine = jsonObjectfor.getJSONObject("medicineDO");
                    medicineName = medicine.getString("medicinename");
                    num = jsonObjectfor.getString("num");
                    JSONArray jsonArrayfor = jsonObjectfor.getJSONArray("medTimeList");
                    for (int j = 0; j < jsonArrayfor.length(); j++) {
                        JSONObject time = jsonArrayfor.getJSONObject(j);
                        String hour = time.getString("hour");
                        String minute = time.getString("minute");
                        planShow = new PlanShow();
                        planShow.setMedicineName(medicineName);
                        planShow.setNum(num);
                        planShow.setHour(hour);
                        planShow.setMinute(minute);
                        planShowList.add(planShow);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return planShowList;
        }

        @SuppressLint("WrongThread")
        @Override
        protected List<PlanShow> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<PlanShow> planShows) {
            super.onPostExecute(planShows);
            PlanAdapter planAdapter = new PlanAdapter(planShows);
            recyclerView.setAdapter(planAdapter);
        }
    }

    //获取天气的任务
    class GetWeatherTask extends AsyncTask<Void, Integer, String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            //https://api.seniverse.com/v3/weather/now.json?key=x3owc7bndhbvi8oq&location=nanjing&language=zh-Hans&unit=c
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://39.105.11.138:8881/box/environment").build();
            try {
                Response response = client.newCall(request).execute();
                String resopnseData = response.body().string();
                JSONObject jsonObject = new JSONObject(resopnseData);
                JSONObject jsonObject1 = jsonObject.getJSONObject("environment");
                int tem = jsonObject1.getInt("tem");
                int hum = jsonObject1.getInt("hum");
                int pm = jsonObject1.getInt("pm");
                String[] result = new String[2];
                result[0] = "温度：" + tem + "  " + "湿度：" + hum;
                result[1] = "pm2.5浓度：" + pm;
                return result;
            } catch (IOException e) {
                String[] result = new String[2];
                result[0] = "温度：未知"  + "  " + "湿度：未知" ;
                result[1] = "pm2.5浓度：未知" ;
                e.printStackTrace();
                return result;
            } catch (JSONException e) {
                String[] result = new String[2];
                result[0] = "温度：未知"  + "  " + "湿度：未知" ;
                result[1] = "pm2.5浓度：未知" ;
                e.printStackTrace();
                return result;
            }
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            //Toast.makeText(IndexActivity.this,"获取天气信息成功",Toast.LENGTH_LONG).show();
            TextView tv_weather = (TextView) findViewById(R.id.temperature);
            TextView tv_location = (TextView) findViewById(R.id.pm25);
            tv_weather.setText(s[0]);
            tv_location.setText(s[1]);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void Request() {             //获取相机拍摄读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//版本判断
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Request();

        //操作数据库取出mcid（grid）
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (MyDatabaseHelper.checkMcid(db)) {
            new GetMedicinePlanInfo().execute("http://39.105.11.138:8881/saier/plan/m/list/plan");
            new GetWeatherTask().execute();
        }

        if (!MyDatabaseHelper.checkMcid(db)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
            //    设置Title的内容
            builder.setTitle("请绑定药箱");
            //    设置Content来显示一个信息
            builder.setMessage("");
            //    设置一个PositiveButton
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //掃描事件
                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                    Toast.makeText(IndexActivity.this, "scan event", Toast.LENGTH_LONG).show();
                    IntentIntegrator integrator = new IntentIntegrator(IndexActivity.this);
                    // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                    integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
                    integrator.setPrompt("请扫描条形码"); //底部的提示文字，设为""可以置空
                    integrator.setCameraId(0); //前置或者后置摄像头
                    integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                    integrator.initiateScan();
                }
            });
            //    显示出该对话框
            builder.show();
        }


        //RecycleView的初始化操作
        //药物List初始化
        //initMedicines();
        //绑定Fragment中的RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_plan);
        //加载GridLayoutManager布局管理器  第一个参数不知道是getActivity还是getContext，不知道有何区别
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //下面开始异步操作
//        new GetMedicinePlanInfo().execute("http://39.105.11.138:8881/saier/plan/m/list/plan");
//        new GetWeatherTask().execute();
//        new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/1/12312");

        circleImageView = (CircleImageView) findViewById(R.id.icon_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(IndexActivity.this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if (MyDatabaseHelper.checkMcid(db)){
                    Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult.getContents() != null) {
            String result = scanResult.getContents();
            Map<String, String> resultmap = getScanDataType(result);
            if (resultmap.get("MED") == null) {
                //这是一个药箱条码
                String mcid = resultmap.get("MCID");


                //数据库操作以及弹出提示框
                MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if (MyDatabaseHelper.updateMcid(db, mcid)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                    //    设置Title的内容
                    builder.setTitle("绑定药箱");
                    //    设置Content来显示一个信息
                    builder.setMessage("" + mcid);
                    //    设置一个PositiveButton
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //    显示出该对话框
                    builder.show();
                    this.mcid = mcid;
                }
            } else {
                Toast.makeText(IndexActivity.this, "请扫描药箱条码", Toast.LENGTH_LONG).show();
            }
        }
    }


    protected Map<String, String> getScanDataType(String msg) {
        if ((msg.length() > 4) && (msg.substring(0, 4).toUpperCase().equals("MCID"))) {
            String key = "MCID";
            String value = msg.substring(4, msg.length());
            Map<String, String> result = new HashMap<>();
            result.put(key, value);
            result.put("MED", null);
            return result;
        } else {
            String key = "MED";
            String value = msg;
            Map<String, String> result = new HashMap<>();
            result.put(key, value);
            result.put("MCID", null);
            return result;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            ActivityCollector.finishAll();
        }
        return true;
    }

}
