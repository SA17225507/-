package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.source.code.model.User;

public class LoginOrRegister extends AppCompatActivity {
    private ProgressBar progressBar;
    private SharedPreferences pref;                 ///////////////////////记住密码的代码;/////////记住密码的代码
    private SharedPreferences.Editor editor;
    //private static  int flag=0;
    User user = new User();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        pref= PreferenceManager.getDefaultSharedPreferences(this);///////////////////////记住密码的
        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        Button ReturnButton = (Button) findViewById(R.id.return_button);
        Button RegisterButton = (Button) findViewById(R.id.register_button);
        Intent intent3=getIntent();
        if(intent3.getStringExtra("from_MainScreen_to_LoginOrRegister").equals("0")){///////////////////////记住密码的代码
            SignInButton.setVisibility(View.INVISIBLE);
            RegisterButton.setVisibility(View.VISIBLE);
        }else{
            SignInButton.setVisibility(View.VISIBLE);
            RegisterButton.setVisibility(View.INVISIBLE);
            pref= PreferenceManager.getDefaultSharedPreferences(this);////记住密码的代码
            String userName=pref.getString("userName","");  //////////读取用户名
            EditText myUsername=(EditText) findViewById(R.id.users);//登录名
            myUsername.setText(userName);

        }
        //进度条
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        //SignInButton
        SignInButton.setOnClickListener(new ButtonListener());
        //ReturnButton
        ReturnButton.setOnClickListener(new View.OnClickListener() {//按返回按钮时回到MainScreen
            @Override
            public void onClick(View v) {//返回到MainScreen，并返回"Return"
                String userName = pref.getString("userName", "");
                if (!userName.equals("")) {
                    editor = pref.edit();/////////若点击返回，判断是否有用户名信息，无 ， 则 保 持 原 功 能 不 变 ； 否 则 在
                    // SharedPreferences 写入 loginState 值为 0
                    editor.putInt("loginState", 0);
                    editor.apply();
                } else {
                    Intent intent2 = new Intent(LoginOrRegister.this, MainScreen.class);
                    intent2.putExtra("FromLoginOrRegister", "Return");
                    startActivity(intent2);
                }
            }
        });

        //RegisterButton
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//向MainScreen传递字符串"RegisterSuccess"
                Intent intent2 = new Intent(LoginOrRegister.this, MainScreen.class);
                EditText myUsername = (EditText) findViewById(R.id.users);
                EditText myPassword = (EditText) findViewById(R.id.password);
                myPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                Username username = new Username();
                if(!username.checkUsername(myPassword.getText().toString())) {//当登录名和登录密码不符合规则时，显示错误
                    myPassword.setError("输入内容不符合规则");
                    if(!username.checkUsername(myUsername.getText().toString())) {
                        myUsername.setError("输入内容不符合规则");
                    }
                }
                if(username.checkUsername(myPassword.getText().toString())
                        &&username.checkUsername(myUsername.getText().toString())){
                    editor=pref.edit();/////////若点击注册，则记录登录名,loginState值为1
                    editor.putString("userName",myUsername.getText().toString());
                    editor.putInt("loginState",1);
                    editor.apply();


                    User loginUser=new User();//如果正确，则新建 User 对象 loginUser，将用户输入框输入的用
                    //户名和密码值通过 Setter 方法赋值到 loginUser 中,并对其 oldUser 域赋值为 False，
                    loginUser.setterUserName(myUsername.getText().toString());
                    loginUser.setterPassWord(myPassword.getText().toString());
                    loginUser.setterOldUser(false);
                    intent2.putExtra("user",loginUser);//向MainScreen传递类loginUser
                    intent2.putExtra("FromLoginOrRegister","RegisterSuccess");

                    startActivity(intent2);
                }
            }
        });

    }


    public class Username {//正则表达式验证
        public boolean checkUsername(String username) {
            String check = "(^[a-zA-Z0-9]{1,100})";
            java.util.regex.Pattern p = Pattern.compile(check);
            Matcher m = p.matcher(username);
            return m.matches();
        }

    }


    class ButtonListener implements View.OnClickListener { //使用 ProgressBar 显示登录进度，2 秒钟后进度条消失；
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(1000);
            new Thread() {
                public void run() {
                    for (int i = 0; i <= 1000; ) {
                        try {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.getData().putInt("size", i);
                            handler.sendMessage(msg);
                            sleep(200);
                            i += 100;
                        } catch (Exception e) {
                            handler.obtainMessage(-1).sendToTarget();
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            EditText myUsername = (EditText) findViewById(R.id.users);//登录名
            EditText myPassword = (EditText) findViewById(R.id.password);//登录密码
            Username username = new Username();
            if (!username.checkUsername(myPassword.getText().toString())) {//当登录名和登录密码不符合规则时，显示错误
                myPassword.setError("输入内容不符合规则");
            } else if (!username.checkUsername(myUsername.getText().toString())) {
                myUsername.setError("输入内容不符合规则");
            } else {
                switch (msg.what) {
                    case 1:
                        progressBar.setProgress(msg.getData().getInt("size"));

                        if (progressBar.getProgress() == progressBar.getMax()) {//如果记到2秒，则显示登录成功
                            editor=pref.edit();//若登录成功，则记录登录名,loginState值为1
                            editor.putString("userName",myUsername.getText().toString());
                            editor.putInt("loginState",1);
                            editor.apply();
                            Toast.makeText(LoginOrRegister.this, "登陆成功", 1).show();
                            progressBar.setVisibility(View.GONE);

                            User loginUser = new User();//如果正确，则新建 User 对象 loginUser，将用户输入框输入的用
                            //户名和密码值通过 Setter 方法赋值到 loginUser 中,并对其 oldUser 域赋值为 True，
                            loginUser.setterUserName(myUsername.getText().toString());
                            loginUser.setterPassWord(myPassword.getText().toString());
                            loginUser.setterOldUser(true);

                            Intent i = new Intent(LoginOrRegister.this, MainScreen.class);//当登录成功时，转跳到MainScreen

                            i.putExtra("user", loginUser);//向MainScreen传递类loginUser

                            i.putExtra("FromLoginOrRegister", "LoginSuccess");
                            startActivity(i);


                        }

                        break;

                    case -1:
                        Toast.makeText(LoginOrRegister.this, "登录失败", 1).show();
                        break;
                }
            }
        }
    };

}


