package com.example.smedicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.smedicine.bean.Medicine;
import com.example.smedicine.bean.Plan;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowMedicinePlanActivity extends AppCompatActivity {

    public static final String MEDICINE_OBJECT_SHOW_PLAN = "medicine_object_show_plan";
    Medicine medicine;
    String mcid;

    class ShowMedicinePlan extends AsyncTask<String,Integer, Plan>{

        @Override
        protected Plan doInBackground(String... strings) {
            String url = strings[0];

            Plan plan = new Plan();
            // http://39.105.11.138:8881/saier/plan/m/checkPlan/{boxid}/{medlicense}

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                String resopnseData = response.body().string();
                JSONObject jsonObject = new JSONObject(resopnseData);
                JSONObject jsonObject1 = jsonObject.getJSONObject("plan");
                plan.setMedbox(jsonObject1.getInt("medbox"));
                plan.setMedicinetime(jsonObject1.getString("medlicense"));
                plan.setTimes(jsonObject1.getString("times"));
                plan.setNum(jsonObject1.getString("num"));
                plan.setMedicinetime(jsonObject1.getString("medicinetime"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return plan;
        }

        @Override
        protected void onPostExecute(Plan plan) {
            super.onPostExecute(plan);
            TextView tv_medicine_time_a_day = (TextView)findViewById(R.id.tv_medicine_time_a_day);
            tv_medicine_time_a_day.setText(plan.getTimes());
            TextView tv_medicine_jiliang_for_plan = (TextView)findViewById(R.id.tv_medicine_jiliang_for_plan);
            tv_medicine_jiliang_for_plan.setText(plan.getNum());
            TextView textView = (TextView)findViewById(R.id.txt_show_plan);
            textView.setText("时间："+plan.getMedicinetime());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicine_plan);
        //获取传过来的Medicine对象
        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra(MEDICINE_OBJECT_SHOW_PLAN);


        //操作数据库取出mcid（grid）
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mcid = MyDatabaseHelper.getMcid(db);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_medicine_show_plan);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_medicine_show_plan);
        ImageView medicineImage = (ImageView) findViewById(R.id.medicine_image_view_medicine_show_plan);
        Glide.with(ShowMedicinePlanActivity.this).load(medicine.getImg_url()).into(medicineImage);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置药物详情的页面信息，应放在AsyncTask中
        //此句以后应放置在AsyncTask中
        collapsingToolbarLayout.setTitle(medicine.getMedicinename());
        //Glide.with(this).load(medicine_url).into(medicineImage);

        new ShowMedicinePlan().execute("http://39.105.11.138:8881/saier/plan/m/checkPlan/"+mcid+"/"+medicine.getLicensenumber());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men_show_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.delete_plan: {
                class DeleteMedicinePlanTask extends AsyncTask<String, Integer, Boolean> {
                    @Override
                    protected Boolean doInBackground(String... strings) {


                        //删除药计划的AsycTask



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
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowMedicinePlanActivity.this);
                            //    设置Title的内容
                            builder.setTitle("删除服药计划成功");
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShowMedicinePlanActivity.this);
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
                new DeleteMedicinePlanTask().execute("http://39.105.11.138:8881/saier/plan/m/remove/"+medicine.getLicensenumber()+"/"+mcid);
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
