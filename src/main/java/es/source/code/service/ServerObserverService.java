package es.source.code.service;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by zhangyajie on 2017/10/29.
 */


public class ServerObserverService extends Service {

    private Message toClientMsg;
    //private static Thread thread;
    private boolean running = true;
    private Messenger mClientMessenger;
    private String data = "这是默认信息";


    Handler cMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //第一步：获得客户端那边的信使clientMessenger；
            mClientMessenger = msg.replyTo;
            switch (msg.what) {
                //第二步：处理客户端发来的消息内容
                case 0://what 值为 0 时，则关闭模拟接收服务器传回菜品库存信息的多线程
                    //thread.interrupt();
                    //thread = null;
                    running = false;
                    Toast.makeText(ServerObserverService.this, "关闭线程", Toast.LENGTH_SHORT).show();
                    break;
                case 1:// what 值为 1 时:启动多线程模拟接收服务器传回菜品库存信息
                    toClientMsg = new Message();
                    //toClientMsg.setData(data);
                    toClientMsg.what = 10;
                    Message replyMsg = new Message();
                    Bundle data = new Bundle();
                    data.putString("reply", msg.getData().getString("send") + "--server msg");
                    replyMsg.setData(data);
                   /* //String str = (String)msg.obj
                    Bundle b = msg.getData();
                    int age = b.getInt("age");
                    String name = b.getString("name");
                    Toast toast = Toast.makeText(getApplicationContext(), "age="+age+"name="+name, Toast.LENGTH_LONG);
                    toast.show();*/
                    try {
                        Thread.sleep(300);
                        try {
                            //第三步：通过客户端的信使给客户端发一个消息
                            mClientMessenger.send(toClientMsg);
                            msg.replyTo.send(replyMsg);
                            Toast.makeText(ServerObserverService.this, "send", Toast.LENGTH_SHORT).show();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:super.handleMessage(msg);
            }
        }
    };

    //创建Messenger并传入Handler实例对象
    private Messenger mServerMessenger = new Messenger(cMessageHandler);

    //bindSerivce->onCreate->onBind->running->onUnbind->onDestroy.
    //只有在onBind方法里面返回一个IBinder对象的时候onServiceConnected才会被调用，说明服务成功绑定
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "OnBind(", Toast.LENGTH_SHORT).show();
        return mServerMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {//// 当调用者退出(即使没有调用unbindService)或者主动停止服务时会调用
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "OnCreate(", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        new Thread(new Runnable() {
            @Override
            public void run() { // 处理具体的逻辑
               // stopSelf();//自动停止
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 停止一个Service是调用stopService(Intent)方法，
     * 当调用该方法时，Service中的onDestory()方法被调用。
     */
    @Override
    public void onDestroy() {//// 当调用者退出(即使没有调用unbindService)或者主动停止服务时会调用
        Toast.makeText(this, "OnDestroy(", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

}



