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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smedicine.bean.History;
import com.example.smedicine.bean.Medicine;
import com.google.android.material.navigation.NavigationView;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends BaseActivity {
    //药箱id
    String mcid;
    //药盒id
    String boxid = "1";

    RecyclerView recyclerView;

    //list中获取药物列表的任务
    class GetMedicineInfo extends AsyncTask<String, Integer, List<Medicine>> {


        private List<Medicine> getJsonData(String url) {
            List<Medicine> medicineList = new ArrayList<>();
            Medicine medicine;
            JSONObject jsonObject;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                jsonObject = new JSONObject(responseData);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("medicineDO");
                    JSONObject jsonObject11 = jsonObject.getJSONObject("box");
                    medicine = new Medicine();
//                    /**国药准字号： 国药准字Z45021599*/
//                    private String licensenumber;
//                    /**药品名： 桂林西瓜霜*/
//                    private String medicinename;
//                    /**有效成分： 西瓜霜、锻硼砂、黄柏、黄连、山豆根、射干、浙贝母、青黛、冰片、大黄、木汉果（炭）黄芩、甘草、薄荷脑*/
//                    private String activeingredient;
//                    /**药物特征： 本品为灰黄绿色的粉末；气香，味咸、微苦而辛凉*/
//                    private String medcharacter;
//                    /**剂量 ： 每瓶装2.5克*/
//                    private String dose;
//                    /** 药量(医嘱)： 喷（吹）敷患处，一次适量，一日数次。
//                     重症者兼服，一次1～2g，次数视情况而定。*/
//                    private String dosage;
//                    /**禁忌： 忌烟酒、辛辣、鱼腥食物。*/
//                    private String contraindication;
//                    /** 迹象： 口腔溃疡*/
//                    private String indication;
//                    /** 药量（说明书说明）：喷（吹）敷患处，一次适量，一日数次。*/
//                    private String dosagefromdoc;
//                    /**副作用： 尚不明确*/
//                    private String untowardeffect;
//                    /**药物相互影响： 与其他药物可能发生相互作用,请询问医师 */
//                    private String druginteraction;
//                    /**有效期： 36个月*/
//                    private String periodvalidity;
//                    /** 生产厂家 : 桂林三金药业股份有限公司 */
//                    private String manufacturer;
//                    /** 储藏条件: 密闭，防潮。*/
//                    private String storageconditions;
                    //获取准字号
                    medicine.setLicensenumber(jsonObject1.getString("licensenumber"));
                    //获取药品名
                    medicine.setMedicinename(jsonObject1.getString("medicinename"));
                    //获取有效成分
                    medicine.setActiveingredient(jsonObject1.getString("activeingredient"));
                    //获取药物特征
                    medicine.setMedcharacter(jsonObject1.getString("medcharacter"));
                    //获取用药剂量
                    medicine.setDose(jsonObject1.getString("dose"));
                    //获取用药剂量
                    medicine.setDosage(jsonObject1.getString("dosage"));
                    //获取禁忌
                    medicine.setContraindication(jsonObject1.getString("contraindication"));
                    //获取迹象
                    medicine.setIndication(jsonObject1.getString("indication"));
                    //获取药量说明书说明
                    medicine.setDosagefromdoc(jsonObject1.getString("dosagefromdoc"));
                    //获取副作用
                    medicine.setUntowardeffect(jsonObject1.getString("untowardeffect"));
                    //获取药物相互影响
                    medicine.setDruginteraction(jsonObject1.getString("druginteraction"));
                    //获取药物有效期
                    medicine.setPeriodvalidity(jsonObject1.getString("periodvalidity"));
                    //获取生产厂家
                    medicine.setManufacturer(jsonObject1.getString("manufacturer"));
                    //获取储藏条件
                    medicine.setStorageconditions(jsonObject1.getString("storageconditions"));
                    /**
                     *
                     * 此处后正式应修改成图片正确url
                     *
                     * */
                    String urlll = "http://39.105.11.138:8881/files/"+jsonObject1.getString("medicinename")+".jpg";
                    medicine.setImg_url(urlll);
                    medicine.setRest(jsonObject11.getInt("rest"));
                    medicineList.add(medicine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return medicineList;
        }

        @SuppressLint("WrongThread")
        @Override
        protected List<Medicine> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Medicine> medicineList) {
            super.onPostExecute(medicineList);
            MedicineAdapter medicineAdapter = new MedicineAdapter(medicineList);
            recyclerView.setAdapter(medicineAdapter);
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
    //获取扫描的药物的任务


    class GetMedicineScaned extends AsyncTask<String, Integer, Medicine> {
        //执行耗时任务的方法
        @Override
        protected Medicine doInBackground(String... strings) {
            String medicineid = strings[0];
            String url = "http://39.105.11.138:8881/saier/medicinemini/m/query/" + medicineid;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String resopnseData = response.body().string();
                JSONObject jsonObject = new JSONObject(resopnseData);
                if (jsonObject.isNull("medicine")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("medicinemini");
                    /*
                    * "medicinemini": {
"id": 1030319,
"pzwh": "国药准字Z20060158",
"format": "0.3g*30s",
"name": "前列舒乐胶囊",
"manufacturer": "通化斯威药业股份有限公司",
"barcode": "6932405720068",
"form": null,
"wholesalePrice": null,
"retailPrice": null,
"createTime": "2019-02-20 15:14:40.0"
}
                    * */
                    Medicine medicine = new Medicine();
//                    /**国药准字号： 国药准字Z45021599*/
//                    private String licensenumber;
//                    /**药品名： 桂林西瓜霜*/
//                    private String medicinename;
//                    /**有效成分： 西瓜霜、锻硼砂、黄柏、黄连、山豆根、射干、浙贝母、青黛、冰片、大黄、木汉果（炭）黄芩、甘草、薄荷脑*/
//                    private String activeingredient;
//                    /**药物特征： 本品为灰黄绿色的粉末；气香，味咸、微苦而辛凉*/
//                    private String medcharacter;
//                    /**剂量 ： 每瓶装2.5克*/
//                    private String dose;
//                    /** 药量(医嘱)： 喷（吹）敷患处，一次适量，一日数次。
//                     重症者兼服，一次1～2g，次数视情况而定。*/
//                    private String dosage;
//                    /**禁忌： 忌烟酒、辛辣、鱼腥食物。*/
//                    private String contraindication;
//                    /** 迹象： 口腔溃疡*/
//                    private String indication;
//                    /** 药量（说明书说明）：喷（吹）敷患处，一次适量，一日数次。*/
//                    private String dosagefromdoc;
//                    /**副作用： 尚不明确*/
//                    private String untowardeffect;
//                    /**药物相互影响： 与其他药物可能发生相互作用,请询问医师 */
//                    private String druginteraction;
//                    /**有效期： 36个月*/
//                    private String periodvalidity;
//                    /** 生产厂家 : 桂林三金药业股份有限公司 */
//                    private String manufacturer;
//                    /** 储藏条件: 密闭，防潮。*/
//                    private String storageconditions;
                    //获取准字号
                    medicine.setLicensenumber(jsonObject1.getString("pzwh"));
                    //获取药品名
                    medicine.setMedicinename(jsonObject1.getString("name"));
                    //获取用药剂量
                    medicine.setDose(jsonObject1.getString("format"));
                    //获取生产厂家
                    medicine.setManufacturer(jsonObject1.getString("manufacturer"));
                    String urlll = "http://39.105.11.138:8881/files/"+jsonObject1.getString("name")+".jpg";
                    medicine.setImg_url(urlll);
                    return medicine;
                } else {
                    jsonObject = jsonObject.getJSONObject("medicine");
                    Medicine medicine = new Medicine();
//                    /**国药准字号： 国药准字Z45021599*/
//                    private String licensenumber;
//                    /**药品名： 桂林西瓜霜*/
//                    private String medicinename;
//                    /**有效成分： 西瓜霜、锻硼砂、黄柏、黄连、山豆根、射干、浙贝母、青黛、冰片、大黄、木汉果（炭）黄芩、甘草、薄荷脑*/
//                    private String activeingredient;
//                    /**药物特征： 本品为灰黄绿色的粉末；气香，味咸、微苦而辛凉*/
//                    private String medcharacter;
//                    /**剂量 ： 每瓶装2.5克*/
//                    private String dose;
//                    /** 药量(医嘱)： 喷（吹）敷患处，一次适量，一日数次。
//                     重症者兼服，一次1～2g，次数视情况而定。*/
//                    private String dosage;
//                    /**禁忌： 忌烟酒、辛辣、鱼腥食物。*/
//                    private String contraindication;
//                    /** 迹象： 口腔溃疡*/
//                    private String indication;
//                    /** 药量（说明书说明）：喷（吹）敷患处，一次适量，一日数次。*/
//                    private String dosagefromdoc;
//                    /**副作用： 尚不明确*/
//                    private String untowardeffect;
//                    /**药物相互影响： 与其他药物可能发生相互作用,请询问医师 */
//                    private String druginteraction;
//                    /**有效期： 36个月*/
//                    private String periodvalidity;
//                    /** 生产厂家 : 桂林三金药业股份有限公司 */
//                    private String manufacturer;
//                    /** 储藏条件: 密闭，防潮。*/
//                    private String storageconditions;
                    //获取准字号
                    medicine.setLicensenumber(jsonObject.getString("licensenumber"));
                    //获取药品名
                    medicine.setMedicinename(jsonObject.getString("medicinename"));
                    //获取有效成分
                    medicine.setActiveingredient(jsonObject.getString("activeingredient"));
                    //获取药物特征
                    medicine.setMedcharacter(jsonObject.getString("medcharacter"));
                    //获取用药剂量
                    medicine.setDose(jsonObject.getString("dose"));
                    //获取用药剂量
                    medicine.setDosage(jsonObject.getString("dosage"));
                    //获取禁忌
                    medicine.setContraindication(jsonObject.getString("contraindication"));
                    //获取迹象
                    medicine.setIndication(jsonObject.getString("indication"));
                    //获取药量说明书说明
                    medicine.setDosagefromdoc(jsonObject.getString("dosagefromdoc"));
                    //获取副作用
                    medicine.setUntowardeffect(jsonObject.getString("untowardeffect"));
                    //获取药物相互影响
                    medicine.setDruginteraction(jsonObject.getString("druginteraction"));
                    //获取药物有效期
                    medicine.setPeriodvalidity(jsonObject.getString("periodvalidity"));
                    //获取生产厂家
                    medicine.setManufacturer(jsonObject.getString("manufacturer"));
                    //获取储藏条件
                    medicine.setStorageconditions(jsonObject.getString("storageconditions"));
                    String urlll = "http://39.105.11.138:8881/files/"+jsonObject.getString("medicinename")+".jpg";
                    medicine.setImg_url(urlll);
                    return medicine;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        //耗时任务执行结束之后
        @Override
        protected void onPostExecute(Medicine medicine) {
            super.onPostExecute(medicine);
            Intent intent = new Intent(MainActivity.this, MedicineScanedActivity.class);
            intent.putExtra(MedicineScanedActivity.MEDICINE_OBJECT, medicine);
            startActivity(intent);
        }
    }


    //侧滑菜单
    private DrawerLayout mDrawerLayout;

    //用于动态申请权限，先在androidmanifest中声明一遍
    @RequiresApi(api = Build.VERSION_CODES.M)
    void Request() {             //获取相机拍摄读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//版本判断
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            }
        }
    }

    //一个权限没有，就一次申请所有所需的权限，这样可以在打开应用的时候获得所有权限
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //操作数据库取出mcid（grid）
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mcid = MyDatabaseHelper.getMcid(db);

        //启动服务
//        Intent startIntent = new Intent(this, AlarmService.class);
//        startService(startIntent);
        //获取权限
        Request();
        //设置导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_index);
        setSupportActionBar(toolbar);

        //启动获取天气状况的任务
        new GetWeatherTask().execute();

        new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/1/" + mcid);

        //设置侧滑菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //菜单图标
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        navigationView.setCheckedItem(R.id.box1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.box1:
                        boxid = "1";
                        if (mcid.equals("null")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //    设置Title的内容
                            builder.setTitle("请绑定药箱");
                            //    设置Content来显示一个信息
                            builder.setMessage("");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //    显示出该对话框
                            builder.show();
                        } else {
                            new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/1/" + mcid);
                        }
                        break;
                    case R.id.box2:
                        boxid = "2";
                        if (mcid.equals("null")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //    设置Title的内容
                            builder.setTitle("请绑定药箱");
                            //    设置Content来显示一个信息
                            builder.setMessage("");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //    显示出该对话框
                            builder.show();
                        } else {
                            new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/2/" + mcid);
                        }
                        break;
                    case R.id.box3:
                        boxid = "3";
                        if (mcid.equals("null")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //    设置Title的内容
                            builder.setTitle("请绑定药箱");
                            //    设置Content来显示一个信息
                            builder.setMessage("");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //    显示出该对话框
                            builder.show();
                        } else {
                            new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/3/" + mcid);
                        }
                        break;
                    case R.id.box4:
                        boxid = "4";
                        if (mcid.equals("null")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //    设置Title的内容
                            builder.setTitle("请绑定药箱");
                            //    设置Content来显示一个信息
                            builder.setMessage("");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //    显示出该对话框
                            builder.show();
                        } else {
                            new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/4/" + mcid);
                        }
                        break;
                    case R.id.box5:
                        boxid = "5";
                        if (mcid.equals("null")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            //    设置Title的内容
                            builder.setTitle("请绑定药箱");
                            //    设置Content来显示一个信息
                            builder.setMessage("");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                }
                            });
                            //    显示出该对话框
                            builder.show();
                        } else {
                            new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/5/" + mcid);
                        }
                        break;

                }
                return true;
            }
        });


        //RecycleView的初始化操作
        //药物List初始化
        //initMedicines();
        //绑定Fragment中的RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //加载GridLayoutManager布局管理器  第一个参数不知道是getActivity还是getContext，不知道有何区别
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //下面开始异步操作

//        new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/1/12312");

        //new GetMedicineScaned().execute("6901339913419");

        if (mcid.equals("null")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //    设置Title的内容
            builder.setTitle("请绑定药箱");
            //    设置Content来显示一个信息
            builder.setMessage("");
            //    设置一个PositiveButton
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                }
            });
            //    显示出该对话框
            builder.show();
        } else {
            new GetMedicineInfo().execute("http://39.105.11.138:8881/saier/box/m/query/1/" + mcid);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_item:
                Toast.makeText(this, "scan event", Toast.LENGTH_LONG).show();
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setCaptureActivity(ScanActivity.class); //设置打开摄像头的Activity
                integrator.setPrompt("请扫描条形码"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                integrator.initiateScan();
                break;
            case R.id.history_item:
                Intent intent = new Intent(MainActivity.this, MedicineHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.analysis_item:
                Intent intent1 = new Intent(MainActivity.this, HealthAnalyseActivity.class);
                startActivity(intent1);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            } else if (resultmap.get("MCID") == null) {
                if (mcid == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    //    设置Title的内容
                    builder.setTitle("请先绑定药箱");
                    //    设置Content来显示一个信息
                    builder.setMessage("");
                    //    设置一个PositiveButton
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //    显示出该对话框
                    builder.show();

                } else {
                    //这是一个药物条码
                    String med = resultmap.get("MED");
                    new GetMedicineScaned().execute(med);
                }
            }
            Log.d("scanMsg", result);
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

}