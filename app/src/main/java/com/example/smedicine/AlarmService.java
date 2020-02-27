package com.example.smedicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.example.smedicine.bean.Plan;
import com.example.smedicine.bean.PlanDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.AlarmManager.INTERVAL_DAY;

public class AlarmService extends Service {
    public AlarmService() {
    }

    public void setAlarm(int hour,int minute,int requestCode){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //处理具体逻辑
        Intent intent = new Intent(AlarmService.this, RemindingActivity.class);
//AlarmActivity就是当闹钟提醒的时候打开的activity,你也可以发送广播
        //传递参数的一种方式
        intent.setAction("nzy");
// 创建PendingIntent对象
        PendingIntent pi = PendingIntent.getActivity(AlarmService.this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        Log.d("lisooo",sdf.format(calendar.getTime()));
        // 根据用户选择时间来设置Calendar对象
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 54);
// 设置AlarmManager将在Calendar对应的时间启动指定组件


        Log.d("lisooo",sdf.format(calendar.getTime()));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),INTERVAL_DAY,pi);
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
        Intent intent = new Intent(AlarmService.this, AlarmReceiver.class);
        intent.putExtra("name",name);
        intent.putExtra("num",num);
        PendingIntent pi = PendingIntent.getBroadcast(AlarmService.this, requestCode, intent, 0);
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





    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //

                String url = "http://39.105.11.138:8881/saier/plan/m/plan/201512312";
                // http://39.105.11.138:8881/saier/plan/m/checkPlan/{boxid}/{medlicense}
                // http://39.105.11.138:8881/saier/plan/m/plan/201512312
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = client.newCall(request).execute();
                    String resopnseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(resopnseData);
                    JSONArray jsonArray = jsonObject.getJSONArray("plan");


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                Log.d("lisoooo","服务启动");
                //setAlarm(20,38,1);
                stopSelf();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anMinute = 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anMinute;
        Intent i = new Intent(this,AlarmService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


//    public List<PlanDetail> getPlan(String medicineName,String timeString){
//        List<PlanDetail> planDetails = new ArrayList<>();
//        String [] times = timeString.split(",");
//        for(String time:times){
//            String [] hourandminute = time.split(":");
//            int hour = Integer.valueOf(hourandminute[0]);
//            int minute = Integer.valueOf(hourandminute[1]);
//            PlanDetail planDetail = new PlanDetail();
//            planDetail.setMedicineName(medicineName);
//            planDetail.setHour(hour);
//            planDetail.setMinute(minute);
//            planDetails.add(planDetail);
//        }
//        return planDetails;
//    }

}
