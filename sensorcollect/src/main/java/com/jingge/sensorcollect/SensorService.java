package com.jingge.sensorcollect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class SensorService extends Service {
    public SensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * Oreo不用Priority了，用importance
         * IMPORTANCE_NONE 关闭通知
         * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
         * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
         * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
         * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
         */
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        String CHANNEL_ID = "my_channel_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {        //Android 8.0适配
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);//如果这里用IMPORTANCE_NONE就需要在系统的设置里面开启渠道， //通知才能正常弹出
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, String.valueOf(CHANNEL_ID));
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle("242Collection")            //指定通知栏的标题内容
                .setContentText("正在采集传感器数据")             //通知的正文内容
                .setWhen(System.currentTimeMillis())                //通知创建的时间
                .setSmallIcon(R.drawable.ic_launcher_background)    //通知显示的小图标，只能用alpha图层的图片进行设置
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));

        Notification notification = builder.build();
        startForeground(1, notification);
    }

}