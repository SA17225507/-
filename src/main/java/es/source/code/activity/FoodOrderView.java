package es.source.code.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.source.code.model.User;

import static java.lang.Thread.sleep;

//import android.support.design.widget.Tablayout;


public class FoodOrderView extends AppCompatActivity {
    private ViewPager vp;
    private NoOrderFragment oneFragment;
    private UnderOrderFragment twoFragment;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentAdapter mFragmentAdapter;
    private android.support.design.widget.TabLayout mTableLayout;//是一个可切换的布局
    private LocalReceiver localReceiver;/////广播用的代码
    private LocalBroadcastManager localBroadcastManager;/////广播用的代码

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//加载menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除工具栏
        ////getSupportActionBar().hide();
        setContentView(R.layout.activity_food_order_view);
        User user1=(User)getIntent().getSerializableExtra("toFoodOrderView");///////////获得从MainScreen传递过来的参数user
        final User user2=user1;
        initViews();

        localBroadcastManager=LocalBroadcastManager.getInstance(this);/////////广播用的代码
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("From FoodView OneFragment to FoodOrderView");
        localReceiver=new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);/////////广播用的代码

        mFragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), mFragmentList);
        vp.setOffscreenPageLimit(4);//ViewPager的缓存为4帧
        vp.setAdapter(mFragmentAdapter);
        //vp.setCurrentItem(0);//初始设置ViewPager选中第一帧

        mTableLayout = (TabLayout) findViewById(R.id.main_tab);
        mTableLayout.setupWithViewPager(vp);// tab_layout.setupWithViewPager(viewPager);//这两句是将ViewPager和TabLayout联系起来
        mTableLayout.setTabMode(android.support.design.widget.TabLayout.MODE_FIXED);// tab_layout.setTabsFromPagerAdapter(mAdapter); 这三个方法。
        Intent intent=getIntent();
        int page = intent.getIntExtra("page",0);
        vp.setCurrentItem(page);
    }


    /**
     * FragmentOne 按钮点击时的回调
     */
    public void onFOneBtnClick()///////////////////////////////////////////////点击结账
    {

        new CheckoutTask().execute();
        FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction tx = fm.beginTransaction();

        tx.addToBackStack(null);
        tx.commit();
    }                             ///////////////////////////////////////////////点击结账

    protected void onDestory(){/////广播用的代码
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){

            Toast.makeText(context,"received local broadcast",Toast.LENGTH_SHORT).show();
        }
    }//广播用的代码

    /**
     * 初始化布局View
     */
    private void initViews() {
        vp = (ViewPager) findViewById(R.id.mainViewPager);
        oneFragment = new NoOrderFragment();
        twoFragment = new UnderOrderFragment();

        //给FragmentList添加数据
        mFragmentList.add(oneFragment);
        mFragmentList.add(twoFragment);

    }

    /**
     * 点击底部Text 动态修改ViewPager的内容
     */



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
                    return "未下单菜";
                default:
                    return "已下单菜";

            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////模拟完成结账功能


    class CheckoutTask extends AsyncTask<Void,Integer,Boolean> {
        @Override
        protected void onPreExecute(){
            ProgressBar progressBar=twoFragment.progressBar_myProssBarhandle_under_order;//进度条
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(10);
        }
        @Override
        protected Boolean doInBackground(Void... params){
            try{
                int downloadPercent=0;
                while(downloadPercent<10){
                    downloadPercent=downloadPercent+1;
                    sleep(600);
                    // int downloadPercent=doDownload();
                    publishProgress(downloadPercent);
                    if(downloadPercent>=10){
                        break;
                    }
                }
            }catch (Exception e){
                return false;
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            if (values[0]<10) {
                ProgressBar progressBar=twoFragment.progressBar_myProssBarhandle_under_order;//进度条
                progressBar.setProgress(values[0]);
            }
        }
        @Override
        protected void onPostExecute(Boolean result){
            if(result){
                twoFragment.button_checkout.setEnabled(false);
                ProgressBar progressBar=twoFragment.progressBar_myProssBarhandle_under_order;//进度条
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FoodOrderView.this,"尊敬的顾客，本次结账成功，结账金额为100元，您的积分增加10分",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FoodOrderView.this,"Checkout failed",Toast.LENGTH_SHORT).show();
            }
        }
    }////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////模拟完成结账功能


    /*
     *由ViewPager的滑动修改底部导航Text的颜色
     */

}
