package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import es.source.code.activity.MainScreen;
import es.source.code.activity.R;

public class UpdateService extends IntentService {
    final String STATUS_BAR_COVER_CLICK_ACTION="download";
    private static Context context;
    private String  result = null;

    private static MediaPlayer player ;

    public UpdateService() {
        super("UpdateService");
    }

    //注意,如果不想覆盖前一个通知,需设置不同的ID
    private static final int NORMAL_NOTIFY_ID = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent)
    {
        new Thread(postThread).start();
        MediaPlayer player = new MediaPlayer();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        //NotificationManager 是一个系统Service，必须通过 getSystemService()方法来获取。
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews rv = new RemoteViews(getPackageName(),R.layout.acctivity_notice);

        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
        rv.setTextViewText(R.id.tv,"新品上架");
        rv.setTextViewText(R.id.tv0,"菜名;价格;类型");

        Intent nextIntent = new Intent(STATUS_BAR_COVER_CLICK_ACTION);
        nextIntent.putExtra("key",NORMAL_NOTIFY_ID);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
        rv.setOnClickPendingIntent(R.id.switch1, nextPendingIntent);

        //如果需要跳转到指定的Activity，则需要设置PendingIntent
        Intent notificationIntent = new Intent(this, MainScreen.class);
        PendingIntent contentItent = PendingIntent.getActivity(this, 0, notificationIntent,0);
        mBuilder.setContent(rv);
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setOngoing(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentIntent(contentItent);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL; //通知清除


        nm.notify(NORMAL_NOTIFY_ID, notification); //将通知加入状态栏
        try {
            if (player.isPlaying()) {
                player.reset();
            } else if (!player.isPlaying()) {
                ring(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Thread postThread = new Thread(){
        public void run() {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://10.0.2.2:8080/scosserver/FoodUpdateService");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST"); // 设置请求方式
                connection.setRequestProperty("Charset", "UTF-8"); // 设置编码格式
                connection.setRequestProperty("MyProperty", "test");// 传递自定义参数
                connection.setDoOutput(true);// 设置容许输出

                // 获取返回数据
                if(connection.getResponseCode() == 200){
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer();
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    result = sb.toString();
                    Message msg = Message.obtain();
                    msg.what = 0;
                    postHandler.sendMessage(msg);
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if(connection!=null){
                    connection.disconnect();
                }
            }
        };
    };

    private Handler postHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0 && result!=null){
                Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
            }
        };
    };

    //响铃
    private static MediaPlayer ring(MediaPlayer player) throws Exception, IOException {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        player.setDataSource(context, alert);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
            player.setLooping(true);
            player.prepare();
            player.start();
        }
        return player;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("TAG","onCreate");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }


}
