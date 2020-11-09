package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class SCOSEntry extends AppCompatActivity {
    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    public static String FROM_ENTRY = "FromEntry";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scosentry);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        /**通过手指所在坐标的改变判断移动方向*/
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候，记录初始坐标
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 150) {
                //向上滑,设置150是规避误操作
                Toast.makeText(this, "进入向左滑动", Toast.LENGTH_SHORT).show();
            } else if(y2 - y1 > 150) {
                //向下滑，设置150是规避误操作
                Toast.makeText(this, "进入向左滑动", Toast.LENGTH_SHORT).show();
            } else if(x1 - x2 > 50) {
                //向左滑
                setupNewActivity();
                Toast.makeText(this, "欢迎进入SCOS", Toast.LENGTH_SHORT).show();
                /**显式页面跳转*/
//                Intent it = new Intent(this,MainScreen.class);
                /**隐式页面跳转*/
                Intent it = new Intent();
                it.setAction("scos.intent.action.SCOSMAIN");
                it.addCategory("scos.intent.category.SCOSLAUNCHER");
                it.putExtra("key",FROM_ENTRY);
                startActivity(it);
            } else if(x2 - x1 > 50) {
                //向右滑
                Toast.makeText(this, "进入向左滑动", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTouchEvent(event);
    }

    private void setupNewActivity() {
        Intent MainScreenActivityIntent = new Intent (SCOSEntry.this,MainScreen.class);
        MainScreenActivityIntent.putExtra("name","FromEntry");
        Log.d("name",MainScreenActivityIntent.getStringExtra("name"));
        startActivity(MainScreenActivityIntent);
    }

}
