package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import es.source.code.service.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {
    public DeviceStartedListener() {
    }

    //接收到广播后自动调用
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "广播已经启动服务了", Toast.LENGTH_SHORT).show();
        if (intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent service = new Intent(context, UpdateService.class);
            //service.setAction(TestActivity.ACTION_Service);
            //在广播中启动服务必须加上startService(intent)的修饰语,Context是对象
            context.startService(service);
        }
    }
}

