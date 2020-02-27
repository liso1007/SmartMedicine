package com.example.smedicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.smedicine.bean.Medicine;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MedicineAcivity extends BaseActivity {


    public static final String MEDICINE_OBJECT = "medicine_object";
    Medicine medicine;

    String mcid;

    class CheckPlanTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {


            String url = strings[0];


            // http://39.105.11.138:8881/saier/plan/m/checkPlan/{boxid}/{medlicense}

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String resopnseData = response.body().string();
                JSONObject jsonObject = new JSONObject(resopnseData);
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean == false) {
                Intent intent = new Intent(MedicineAcivity.this, SetMedicinePlanActivity.class);
                intent.putExtra(SetMedicinePlanActivity.MEDICINE_OBJECT_FOR_PLAN, medicine);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MedicineAcivity.this, ShowMedicinePlanActivity.class);
                intent.putExtra(ShowMedicinePlanActivity.MEDICINE_OBJECT_SHOW_PLAN, medicine);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_acivity);
        //获取传过来的Medicine对象
        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra(MEDICINE_OBJECT);

        //操作数据库取出mcid（grid）
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mcid = MyDatabaseHelper.getMcid(db);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView medicineImage = (ImageView) findViewById(R.id.medicine_image_view);
        TextView medicineText = (TextView) findViewById(R.id.medicine_context_text);
        Glide.with(MedicineAcivity.this).load(medicine.getImg_url()).into(medicineImage);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置药物详情的页面信息，应放在AsyncTask中
        //此句以后应放置在AsyncTask中
        collapsingToolbarLayout.setTitle(medicine.getMedicinename());
        //Glide.with(this).load(medicine_url).into(medicineImage);


//        /**国药准字号： 国药准字Z45021599*/
//        private String licensenumber;
//        /**药品名： 桂林西瓜霜*/
//        private String medicinename;
//        /**有效成分： 西瓜霜、锻硼砂、黄柏、黄连、山豆根、射干、浙贝母、青黛、冰片、大黄、木汉果（炭）黄芩、甘草、薄荷脑*/
//        private String activeingredient;
//        /**药物特征： 本品为灰黄绿色的粉末；气香，味咸、微苦而辛凉*/
//        private String medcharacter;
//        /**剂量 ： 每瓶装2.5克*/
//        private String dose;
//        /** 药量(医嘱)： 喷（吹）敷患处，一次适量，一日数次。
//         重症者兼服，一次1～2g，次数视情况而定。*/
//        private String dosage;
//        /**禁忌： 忌烟酒、辛辣、鱼腥食物。*/
//        private String contraindication;
//        /** 迹象： 口腔溃疡*/
//        private String indication;
//        /** 药量（说明书说明）：喷（吹）敷患处，一次适量，一日数次。*/
//        private String dosagefromdoc;
//        /**副作用： 尚不明确*/
//        private String untowardeffect;
//        /**药物相互影响： 与其他药物可能发生相互作用,请询问医师 */
//        private String druginteraction;
//        /**有效期： 36个月*/
//        private String periodvalidity;
//        /** 生产厂家 : 桂林三金药业股份有限公司 */
//        private String manufacturer;
//        /** 储藏条件: 密闭，防潮。*/
//        private String storageconditions;
        medicineText.append(medicine.toString());


        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_medicine);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置服药计划
                // http://39.105.11.138:8881/saier/plan/m/checkPlan/{boxid}/{medlicense}
                new CheckPlanTask().execute("http://39.105.11.138:8881/saier/plan/m/checkPlan/"+mcid+"/"+medicine.getLicensenumber());
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.delete_cloth: {
                class DeleteMedicineTask extends AsyncTask<String, Integer, Boolean> {
                    @Override
                    protected Boolean doInBackground(String... strings) {


                        //删除药物的AsycTask
                        //                        //删除药物的AsycTask
                        //                        //删除药物的AsycTask
                        //                        //删除药物的AsycTask
                        //                        //删除药物的AsycTask
                        //                        //删除药物的AsycTask

                        String url = strings[0];


                        // http://39.105.11.138:8881/saier/plan/m/checkPlan/{boxid}/{medlicense}

                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).build();
                        try {
                            Response response = client.newCall(request).execute();
                            String resopnseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(resopnseData);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                return true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return false;


                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if(aBoolean ==true){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MedicineAcivity.this);
                            //    设置Title的内容
                            builder.setTitle("删除药物成功");
                            //    设置Content来显示一个信息
                            builder.setMessage("");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                            //    显示出该对话框
                            builder.show();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MedicineAcivity.this);
                            //    设置Title的内容
                            builder.setTitle("错误，请稍后再试");
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
                        }
                    }
                }
                ///saier/box/m/remove/{gridId}/{boxId}/{mboxid}
                new DeleteMedicineTask().execute("http://39.105.11.138:8881/saier/box/m/removemed/"+mcid+"/"+medicine.getLicensenumber());

            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}