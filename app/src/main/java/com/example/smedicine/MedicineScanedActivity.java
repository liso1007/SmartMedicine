package com.example.smedicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.smedicine.bean.Medicine;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MedicineScanedActivity extends AppCompatActivity {

    //药箱id
    String mcid;

    //剂量
    String jiliang;

    //药准字号
    String mboxid;

    EditText editText = null;

    class SaveMedicineTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String url = strings[0];


            //保存药物的AsycTask
            //保存药物的AsycTask
            //保存药物的AsycTask
            //保存药物的AsycTask
            //保存药物的AsycTask
            //保存药物的AsycTask
            //保存药物的AsycTask
            //http://39.105.11.138:8881/saier/box/m/save/{grid}/{boxid}/{mboxid}/{rest}
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
            super.onCancelled(aBoolean);
            if (aBoolean == true) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicineScanedActivity.this);
                //    设置Title的内容
                builder.setTitle("保存药物成功");
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
        }
    }

    public static final String MEDICINE_OBJECT = "medicine_object_scaned";
    Medicine medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_scaned);

        //获取传过来的Medicine对象
        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra(MEDICINE_OBJECT);

        //操作数据库取出mcid（grid）
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        mcid = MyDatabaseHelper.getMcid(db);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scaned);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_scaned);
        ImageView medicineImage = (ImageView) findViewById(R.id.medicine_image_view_scaned);
        TextView medicineText = (TextView) findViewById(R.id.medicine_context_text_scaned);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置药物详情的页面信息，应放在AsyncTask中
        //此句以后应放置在AsyncTask中
        collapsingToolbarLayout.setTitle(medicine.getMedicinename());
        String imgurl = medicine.getImg_url();
        Log.d("looooooooo",imgurl);
        Glide.with(MedicineScanedActivity.this).load(medicine.getImg_url()).into(medicineImage);

        medicineText.append(medicine.toString());

        editText = (EditText) findViewById(R.id.edit_medicine_jiliang);
        mboxid = medicine.getLicensenumber();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scaned, menu);
        return true;
    }

    public boolean checkJiliang() {
        String txt = editText.getText().toString();

        if (txt.length() == 0) {
            Log.d("lisooo", "checkJiliang");
            AlertDialog.Builder builder = new AlertDialog.Builder(MedicineScanedActivity.this);
            //    设置Title的内容
            builder.setTitle("请输入药物剂量");
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
            return false;
        } else {
            jiliang = editText.getText().toString();
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }

            case R.id.save_scaned_1: {
                //保存计划的AsycTask
                Log.d("lisoooo", "检查");
                if (checkJiliang()) {
                    new SaveMedicineTask().execute("http://39.105.11.138:8881/saier/box/m/save/" + "1/" + mcid + "/" + medicine.getLicensenumber() + "/" + jiliang);
                }
                break;
            }
            case R.id.save_scaned_2: {
                //保存计划的AsycTask
                if (checkJiliang()) {
                    new SaveMedicineTask().execute("http://39.105.11.138:8881/saier/box/m/save/" + "2/" + mcid + "/" + medicine.getLicensenumber() + "/" + jiliang);
                }
            }
            case R.id.save_scaned_3: {
                //保存计划的AsycTask
                if (checkJiliang()) {
                    new SaveMedicineTask().execute("http://39.105.11.138:8881/saier/box/m/save/" + "3/" + mcid + "/" + medicine.getLicensenumber() + "/" + jiliang);
                }
            }
            case R.id.save_scaned_4: {
                //保存计划的AsycTask
                if (checkJiliang()) {
                    new SaveMedicineTask().execute("http://39.105.11.138:8881/saier/box/m/save/" + "4/" + mcid + "/" + medicine.getLicensenumber() + "/" + jiliang);
                }
            }
            case R.id.save_scaned_5: {
                //保存计划的AsycTask
                if (checkJiliang()) {
                    new SaveMedicineTask().execute("http://39.105.11.138:8881/saier/box/m/save/" + "5/" + mcid + "/" + medicine.getLicensenumber() + "/" + jiliang);
                }
            }
            case R.id.cancel_scaned: {
                //保存计划的AsycTask
                finish();
            }
            default:
                break;
        }
        return true;
    }
}
