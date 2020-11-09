package es.source.code.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UnderOrderFragment extends Fragment {
    public Button button_checkout;
    public ProgressBar progressBar_myProssBarhandle_under_order;
    /////////////////////////////////////////////////////////////////////////////////////////////////
    //设置按钮点击的回调
    public interface FOneBtnClickListener
    {
        void onFOneBtnClick();
    }

    public class LengCaiAdapter extends BaseAdapter implements View.OnClickListener {
        //上下文
        private Context context;
        //数据项
        private List<String> data;
        public LengCaiAdapter(List<String> data){
            this.data = data;
        }
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if(context == null)
                context = viewGroup.getContext();
            if(view == null){
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lengcai_item,null);
                viewHolder = new ViewHolder();
                viewHolder.mTv = (TextView)view.findViewById(R.id.lengcai_name);
                viewHolder.mBtn = (Button)view.findViewById(R.id.DianCai_Button);
                view.setTag(viewHolder);
            }
            //获取viewHolder实例
            viewHolder = (ViewHolder)view.getTag();
            //设置数据
            viewHolder.mTv.setText(data.get(i));
            viewHolder.mTv.setTag(R.id.tv,i);
            //设置监听事件
            viewHolder.mTv.setOnClickListener(this);
            //设置数据
            viewHolder.mBtn.setTag(R.id.btn,i);
            viewHolder.mBtn.setText("点菜");
            viewHolder.mBtn.setOnClickListener(this);
            return view;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.DianCai_Button:
                    Log.d("tag", "Btn_onClick: " + "view = " + view);
                    Button mBtn=(Button)view.findViewById(R.id.DianCai_Button);
                    if(mBtn.getText()=="点菜"){
                        mBtn.setText("退点");
                        Toast.makeText(context,"点菜成功", Toast.LENGTH_SHORT).show();
                    }else{
                        mBtn.setText("点菜");
                        Toast.makeText(context,"退点成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.lengcai_name:
                    Log.d("tag", "Tv_onClick: " + "view = " + view);
                    Toast.makeText(context,"我是文本", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        class ViewHolder{
            TextView mTv;
            Button mBtn;
        }

    }


    class LengCai{
        private String name;
        private int imageId;
        private String price;
        public LengCai(String name, int imageId, String price){
            this.name=name;
            this.imageId=imageId;
            this.price=price;
        }
        public String getName(){
            return name;
        }
        public int getImageId(){
            return imageId;
        }
        public String getPrice(){
            return price;
        }
    }


    @BindView(R.id.lv)
    ListView lv;



    private LengCaiAdapter lvAdapter;
    private List<String> lengcaiList=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_under_order, container, false);//将自定义布局fragment_one加载进来,第二个参数是容器
        ButterKnife.bind(this, view);
        initData();
        progressBar_myProssBarhandle_under_order=(ProgressBar)view.findViewById(R.id.myProssBarhandle_under_order);
        button_checkout=(Button)view.findViewById(R.id.Button_Checkout);///////////////////////////若点击结账
        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof FOneBtnClickListener)
                {
                    ((FOneBtnClickListener) getActivity()).onFOneBtnClick();
                }
            }
        });//////////////////////////////////////////////////////////////////////////////////////////////////若点击结账
        return view;
    }

    private void initData() {
        lengcaiList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            lengcaiList.add("凉拌豌豆粉");
            lengcaiList.add("蜜汁山药");
            lengcaiList.add("口水杏鲍菇");
            lengcaiList.add("蒜蓉凤爪");
            lengcaiList.add("凉拌秋葵");
            lengcaiList.add("花生米");

        }
        lvAdapter = new LengCaiAdapter(lengcaiList);//第一个参数为上下文，
        // 第二个参数为一个包含TextView，用来填充ListView的每一行的布局资源ID，第三个参数为ListView的内容
        //simple_list_item_1最为简单，只有一个TextView
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), lengcaiList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
