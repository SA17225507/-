package es.source.code.activity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThreeFragment extends Fragment {


    @BindView(R.id.lv)//绑定View
            ListView lv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSwipeRefreshLayout;//刷新的布局

    private List<String> stringList;
    private ArrayAdapter lvAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment（为这个fragment增加布局）
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);//绑定fragment
        initData();
        return view;
    }

    private void initData() {
        stringList = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            stringList.add("龙虾");
            stringList.add("大闸蟹");

        }
        lvAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, stringList);
        lv.setAdapter(lvAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {///////////当点击时，显示内容
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), stringList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {/////////////长时间点击时，显示不同的内容
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "long click:" + stringList.get(i).toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        //初始化下拉控件颜色
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        SystemClock.sleep(2000);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(getActivity(), "下拉刷新成功", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }.execute();
            }
        });
    }
}
