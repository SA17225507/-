package es.source.code.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.service.ServerObserverService;

import static es.source.code.activity.R.id.menu_update;


public class FoodView extends AppCompatActivity {
    private ViewPager vp;
    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private android.support.design.widget.TabLayout mTableLayout;//是一个可切换的布局
    private Toolbar mToolbar;
    private MenuItem mainMenuItem;

    private int flag=0;//标志1：启动或者0：停止实时更新
    private boolean mBound;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private Messenger mMessengerFromService;
    private Message msg;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//加载menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mainMenuItem = menu.findItem(R.id.menu_update);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
        setContentView(R.layout.activity_food_view);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);


        initViews();
        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        vp.setCurrentItem(0);//初始设置ViewPager选中第一帧

        mTableLayout = (TabLayout) findViewById(R.id.main_tab_foodview);
        mTableLayout.setupWithViewPager(vp);// tab_layout.setupWithViewPager(viewPager);//这两句是将ViewPager和TabLayout联系起来
        mTableLayout.setTabMode(android.support.design.widget.TabLayout.MODE_FIXED);// tab_layout.setTabsFromPagerAdapter(mAdapter); 这三个方法。

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// TODO Auto-generated method stub
        if(item.getItemId() == R.id.menu_under_order){//已下单菜
            Intent intent1=new Intent(FoodView.this,FoodOrderView.class);
            intent1.putExtra("page",1);
            startActivity(intent1);
        }
        else if(item.getItemId() == R.id.menu_view_order){//未下单菜
            Intent intent2=new Intent(FoodView.this,FoodOrderView.class);
            intent2.putExtra("page",0);
            startActivity(intent2);
        }
        else if(item.getItemId()== menu_update){//切换更新状态
            if(flag==0){
                flag=1;
                mainMenuItem.setTitle("停止实时更新");
            }else{
                flag=0;
                mainMenuItem.setTitle("启动实时更新");
            }
            Intent intent = new Intent(this, ServerObserverService.class);//第一步新建Intent，并绑定服务
            //myConnection = new MyServiceConnection();
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
            //unbindService(serviceConnection); // 解绑服务
        }
        return true;
    }



    /**
     * 初始化布局View
     */
    private void initViews() {

        vp = (ViewPager) findViewById(R.id.mainViewPager_foodview);
        oneFragment = new OneFragment();
        twoFragment = new TwoFragment();
        threeFragment = new ThreeFragment();
        fourFragment = new FourFragment();
        //给FragmentList添加数据
        mFragmentList.add(oneFragment);
        mFragmentList.add(twoFragment);
        mFragmentList.add(threeFragment);
        mFragmentList.add(fourFragment);
    }


    public class FragmentAdapter extends FragmentPagerAdapter {//配适器算法

        List<Fragment> fragmentList = new ArrayList<Fragment>();

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }//返回当前Fragment的位置

        @Override
        public int getCount() {
            return fragmentList.size();
        }//返回fragment的大小

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "冷菜";
                case 1:
                    return "热菜";
                case 2:
                    return "海鲜";
                default:
                    return "酒水";
            }
        }
    }



    //新建 sMessageHandler 对象，重写方法handleMessage();
    private Handler sMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                Toast.makeText(FoodView.this, "更新菜品信息", Toast.LENGTH_SHORT).show();
                //更新菜品信息
            }
        }
    };

    //第三步：创建一个客户端的信使ClientMessenger
    private Messenger ClientMessenger = new Messenger(sMessageHandler);

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //第二步：获得服务那边返回出来的Messenger
            mMessengerFromService = new Messenger(binder);
            try {
                mMessengerFromService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mBound = true;
            msg = new Message();
            //第四步：创建一个消息对象
            //Message message = Message.obtain(null, 1);
            //Bundle data = new Bundle();
            //data.putString("msg", "0");
            //message.setData(data);
            if(flag==1){
                msg.what = 1;
            }else{
                msg.what = 0;
            }
            //第五步：将客户端的信使放到消息上//通过replyTo传递给service
            msg.replyTo = ClientMessenger;
            Toast.makeText(FoodView.this, msg.replyTo.toString(), Toast.LENGTH_SHORT).show();
            try {
                //第六步：通过服务端的信使将消息发送给服务端
                Toast.makeText(FoodView.this, "向服务器发送消息", Toast.LENGTH_SHORT).show();
                mMessengerFromService.send(msg);
            } catch (RemoteException e)  {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        //如果不加这一步，就会报异常。
    }

    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     */
    public int getAppSatus(Context context, String pageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);
        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }
}






