package com.example.smedicine;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "闹钟响了", Toast.LENGTH_SHORT).show();
        Log.i("AlarmReceiver","闹钟响了");
        Bundle bundle=intent.getExtras();
        Log.i("AlarmReceiver","接收传值"+bundle.getString("name"));


        //从系统服务中获得通知管理器
//        Intent it=new Intent(context,MainActivity.class);
//        PendingIntent pi=PendingIntent.getActivity(context,0,it, PendingIntent.FLAG_CANCEL_CURRENT);


//        Resources res=context.getResources();
//        Bitmap bmp= BitmapFactory.decodeResource(res,R.drawable.notice);
//
//
//        NotificationManager manager_notifica=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        //定义notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle("日程通知")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(bmp)
//                .setContentText("这是通知的内容")
//                .setTicker("日程通知")
//                // 单机 面板自动取消通知
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_SOUND);// 设置通知响应方式
//        // .setContentIntent(pi);
//
//        manager_notifica.notify(1, builder.build());// 通过管理器发送通知（通知的id，Notification对象）

        String id = "channel_001";
        String name = "name";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(context)
                    .setChannelId(id)
                    .setContentTitle("您该吃药了")
                    .setContentText("请打开app查看您的服药计划")
                    .setSmallIcon(R.drawable.valet_img).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle("您该吃药了")
                    .setContentText("请打开app查看您的服药计划")
                    .setSmallIcon(R.drawable.valet_img)
                    .setOngoing(true)
                    .setChannelId(id);//无效
            notification = notificationBuilder.build();
        }
        notificationManager.notify(1, notification);





    }
}


