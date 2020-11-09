package es.source.code.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;


public class SCOSHelper extends AppCompatActivity {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;

    // 图片封装为一个数组
    private int[] icon = {R.drawable.contract, R.drawable.system,
            R.drawable.tel, R.drawable.msg, R.drawable.mail};
    private String[] iconName = {"用户使用协议", "关于系统", "电话人工帮助", "短信帮助", "邮件帮助"};

    PendingIntent paIntent;
    SmsManager smsManager = SmsManager.getDefault();//获得默认的消息管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        getData();

        //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};

        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_mainscreen, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.text);
                switch (position) {
                    case 0:
                        //用户使用协议
                        break;
                    case 1:
                       //关于系统
                        break;
                    case 2: startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:5554")));
                        break;
                    case 3:
                        //短信
                        /** Called when the activity is first created. */
                        paIntent = PendingIntent.getBroadcast(SCOSHelper.this, 0, new Intent(), 0);
                        smsManager.sendTextMessage ("5554", null, "test scos helper", paIntent, null);
                        Toast.makeText(SCOSHelper.this, "求助短信发送成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        //sendEmail();
                        //耗时操作要起线程
                        //Toast.makeText(SCOSHelper.this, "测试测试", Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MailSender sender = new MailSender("");
                                Message msg = Message.obtain();
                                try {
                                    sender.MailSender();
                                    msg.what = 1;
                                } catch (MessagingException e) {
                                    e.printStackTrace();
                                    msg.what = 0;
                                }
                                myHandler.handleMessage(msg);
                            }
                        }).start();
                        break;
                    default:
                        break;
                }
                Toast.makeText(SCOSHelper.this, textView.getText(), Toast.LENGTH_LONG).show();
            }

        });
    }
    public List<Map<String, Object>> getData(){
        //icon和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }

    final Handler myHandler = new Handler()//在主线程中，获取，处理消息，更新UI组件，可以修改UI组件
    {
        @Override
        public void handleMessage(Message msg)
        {
            // 如果该消息是本程序所发送的
            if (msg.what == 1)
            {
                Toast.makeText(SCOSHelper.this, "求助邮件已发送成功", Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 0) {
                Toast.makeText(SCOSHelper.this, "求助邮件发送失败", Toast.LENGTH_LONG).show();
            }
        }
    };

}