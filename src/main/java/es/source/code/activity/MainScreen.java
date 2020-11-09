package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.model.User;


public class MainScreen extends Activity {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private SharedPreferences pref;////////////记住密码的代码
    private SharedPreferences.Editor editor;////记住密码的代码

    // 图片封装为一个数组
    private int[] icon = {R.drawable.user, R.drawable.help,
            R.drawable.menu, R.drawable.order};
    private String[] iconName = {"登录／注册", "系统帮助", "点菜", "查看订单"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        pref= PreferenceManager.getDefaultSharedPreferences(this);///////////////////////记住密码的代码

        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();

        Intent intent = getIntent();
        User user=new User();
        if (intent.getExtras().containsKey("key")) {
            String data = intent.getStringExtra("key");
            if (!data.equals("FromEntry")) {
                getDataNoLogin();
            } else {
                getData();
            }
        } else if (intent.getExtras().containsKey("FromLoginOrRegister")) {
            String data1 = intent.getStringExtra("FromLoginOrRegister");
            if (data1.equals("LoginSuccess")) {
                getData();
                user = (User) intent.getSerializableExtra("user");
            } else if (data1.equals("RegisterSuccess")) {
                getData();
                user = (User) intent.getSerializableExtra("user");
                Toast.makeText(MainScreen.this, "欢迎您成为SCOS新用户", 1).show();

            } else {
                getDataNoLogin();
                user = null;//当传入 MainScreen 的 String 值为其他时，将 user 赋值为 NULL
            }
        }
        final User user1=user;/////y用于传给FoodView

    //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};

        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item_mainscreen, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){//当点击登录/注册按钮时跳到LoginOrRegister界面
                    String userName=pref.getString("userName","");///////////////判断是否有userName的信息///////////////////////记住密码的代码
                    Intent intent2=new Intent(MainScreen.this,LoginOrRegister.class);
                    if(userName.equals("")){////////////当无userNmae信息时，传给一个参数0
                        intent2.putExtra("from_MainScreen_to_LoginOrRegister","0");
                        editor=pref.edit();/////////////修改 MainScreen  代码，参照以上任务，将用户是否 ////录的判断逻辑改成使用 SharedPreferences 的 loginState 值进行功能实现
                        editor.putInt("loginState",0);
                        editor.apply();
                    }else{////////////当有userNmae信息时，传给一个参数1
                        intent2.putExtra("from_MainScreen_to_LoginOrRegister","1");
                    }
                    startActivity(intent2);
                }else if(position==2){////////////////////////////当点击点菜时跳到"FoodView界面
                    Intent intent3=new Intent(MainScreen.this,FoodView.class);
                    intent3.putExtra("user_toFoodView",user1);
                    startActivity(intent3);
                }else if(position==3){
                    Intent intent4=new Intent(MainScreen.this,FoodOrderView.class);
                    intent4.putExtra("toFoodOrderView",user1);
                    startActivity(intent4);

                }else{
                    Intent intent5=new Intent(MainScreen.this,SCOSHelper.class);
                    startActivity(intent5);
                }
               /*// User user = new User();
                switch (position) {
                    case 0:
                        //跳转登录/注册页面
                        *//**显式页面跳转*//*
                        Intent intent1 = new Intent(MainScreen.this, LoginOrRegister.class);
                        Toast.makeText(MainScreen.this, "欢迎您成为SCOS新用户", 1).show();
                        startActivity(intent1);
                        break;
                    case 1:
                        //跳转系统帮助页面
                        Intent intent4 = new Intent(MainScreen.this, SCOSHelper.class);
                        startActivity(intent4);
                        break;
                    case 2:
                        //跳转点菜页面
                        Intent intent2 = new Intent(MainScreen.this, FoodView.class);
                        startActivity(intent2);
                        //getData();
                        //user = (User) intent2.getSerializableExtra("user");
                        break;
                    case 3:
                        //跳转查看订单页面
                        Intent intent3 = new Intent(MainScreen.this, FoodOrderView.class);
                        startActivity(intent3);
                        //getData();
                        //user = (User) intent3.getSerializableExtra("user");
                        break;
                }*/
            }

        });
    }
    public List<Map<String, Object>> getDataNoLogin() {
        //icon和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
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
}
