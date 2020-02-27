package com.example.smedicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.smedicine.bean.Medicine;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SetMedicinePlanActivity extends BaseActivity {
    public static final String MEDICINE_OBJECT_FOR_PLAN = "medicine_object_for_plan";
    Medicine medicine;

    final String[] medicinetime = {""};

    EditText editText = null;

    String zhoushu;

    int timess;

    class SaveMedicinePlanTask extends AsyncTask<String,Integer,Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {


            //删除药物计划的的AsycTask
            //                        //删除药物计划的AsycTask
            //                        //删除药物的AsycTask
            //                        //删除药物的AsycTask
            //                        //删除药物的AsycTask
            //                        //删除药物的AsycTask

            String url = strings[0];


            // http://39.105.11.138:8881/saier/plan/m/save/{medlicense}/{medbox}/{times}/{medicinetime}/{num}

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
            if (aBoolean == true) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SetMedicinePlanActivity.this);
                //    设置Title的内容
                builder.setTitle("保存服药计划成功");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_medicine_plan);
        //获取传过来的Medicine对象
        Intent intent = getIntent();
        medicine = (Medicine) intent.getSerializableExtra(MEDICINE_OBJECT_FOR_PLAN);



        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_medicine_plan);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_medicine_plan);
        ImageView medicineImage = (ImageView) findViewById(R.id.medicine_image_view_medicine_plan);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置药物详情的页面信息，应放在AsyncTask中
        //此句以后应放置在AsyncTask中
        collapsingToolbarLayout.setTitle(medicine.getMedicinename());
        //Glide.with(this).load(medicine_url).into(medicineImage);
        editText = (EditText) findViewById(R.id.edit_medicine_jiliang_for_plan);
        Glide.with(SetMedicinePlanActivity.this).load(medicine.getImg_url()).into(medicineImage);
        EditText editText1 = (EditText) findViewById(R.id.edit_medicine_zhoushu_for_plan);
        Button btn_makesure = (Button) findViewById(R.id.btn_makesure);
        btn_makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.edit_medicine_time_a_day);
                String timestring = editText.getText().toString();
                String zhoushunum = editText1.getText().toString();
                zhoushu = zhoushunum;
                int times = Integer.parseInt(timestring);
                timess = times;
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lay);
                while(times > 0){
                    TextView tv = new TextView(SetMedicinePlanActivity.this);
                    //2.把信息设置为文本框的内容
                    tv.setText("服药时间");
                    tv.setTextSize(30);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TimePickerView pvTime = new TimePickerBuilder(SetMedicinePlanActivity.this, new OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date, View v) {//选中事件回调
                                    //Log.d("time",""+date.getHours()+date.getMinutes());
                                    tv.setText(date.getHours()+":"+date.getMinutes());
                                    medicinetime[0] = medicinetime[0] + date.getHours()+":"+date.getMinutes()+",";
                                    Log.d("time的值",medicinetime[0]);
                                }
                            })
                                    .setType(new boolean[]{false, false, false, true, true, false})// 默认全部显示
                                    .setCancelText("取消")//取消按钮文字
                                    .setSubmitText("确定")//确认按钮文字
                                    .setContentTextSize(18)//滚轮文字大小
                                    .setTitleSize(20)//标题文字大小
                                    .setTitleText("服药时间")//标题文字
                                    .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                                    .isCyclic(true)//是否循环滚动
                                    .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                                    .isDialog(false)//是否显示为对话框样式
                                    .build();
                            pvTime.show();
                        }
                    });
                    //3.把textView设置为线性布局的子节点
                    linearLayout.addView(tv);
                    times --;
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_plan,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
            case R.id.save_plan:{
                if(editText.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetMedicinePlanActivity.this);
                    //    设置Title的内容
                    builder.setTitle("请输入每次服药剂量");
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
                else{
                    if(medicinetime[0].equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SetMedicinePlanActivity.this);
                        //    设置Title的内容
                        builder.setTitle("请选择服药时间");
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
                        //操作数据库取出mcid（grid）
                        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "medicine.db", null, MyDatabaseHelper.DB_VERSION);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String  mcid = MyDatabaseHelper.getMcid(db);
                        // http://39.105.11.138:8881/saier/plan/m/save/{medlicense}/{medbox}/{times}/{medicinetime}/{num}
                        new SaveMedicinePlanTask().execute("http://39.105.11.138:8881/saier/plan/m/save/"+zhoushu+"/"+medicine.getLicensenumber()+"/"+mcid+"/"+timess+"/"+medicinetime[0]+"/"+Integer.parseInt(editText.getText().toString()));
                        String[] strings = medicinetime[0].split(",");
                        for(int i = 0;i<=(strings.length-1);i++){
                            String[] splitimes = strings[i].split(":");
                            int hour = 0;
                            int minute = 0;
                            hour = Integer.valueOf(splitimes[0]);
                            minute = Integer.valueOf(splitimes[1]);
                            addRemind(hour,minute,i,medicine.getMedicinename(),timess);

                        }
                        Toast.makeText(SetMedicinePlanActivity.this,"保存服药计划成功",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }



                break;
            }

            default:
                break;
        }
        return true;
    }


    private void addRemind(int hour,int minute,int requestCode,String name,int num){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar1 = Calendar.getInstance();
        mCalendar1.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar1.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为13点
        mCalendar1.set(Calendar.HOUR_OF_DAY, hour);
        //设置在几分提醒  设置的为25分
        mCalendar1.set(Calendar.MINUTE, minute);
        //下面这两个看字面意思也知道
        mCalendar1.set(Calendar.SECOND, 0);
        mCalendar1.set(Calendar.MILLISECOND, 0);


        /**
         * 若感觉上面的设置过于繁琐，则可如此设置
         * mCalendar1.setTimeInMillis(System.currentTimeMillis()+60*1000);
         * 表示于当前时间1分钟后提醒
         */
        //sdf sdf.format(calendar.getTime())
        Log.i("MainActivity","提醒时间1-->"+sdf.format(mCalendar1.getTimeInMillis()));
        //上面设置的就是15点21分0秒的时间点

        //获取上面设置的15点21分0秒的时间点
        long selectTime = mCalendar1.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            mCalendar1.add(Calendar.DAY_OF_MONTH, 1);
        }

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(SetMedicinePlanActivity.this, AlarmReceiver.class);
        intent.putExtra("name",name);
        intent.putExtra("num",num);
        PendingIntent pi = PendingIntent.getBroadcast(SetMedicinePlanActivity.this, requestCode, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);

        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的15点21分0秒的时间点
         */
        am.set(AlarmManager.RTC_WAKEUP, mCalendar1.getTimeInMillis(), pi);

        /**
         * 重复提醒
         * 第一个参数是警报类型；下面有介绍
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
        //am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar1.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }
}
