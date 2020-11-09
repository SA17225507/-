package es.source.code.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwoFragment extends Fragment implements MyRecyclerViewOnclickInterface {


    @BindView(R.id.id_recyclerview)
    RecyclerView mRecyclerview;

    private MyRecyclerViewAdapter mAdapter;
    private List<String> stringList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        stringList = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            stringList.add("小炒肉");
            stringList.add("糖醋里脊");

        }
        mAdapter = new MyRecyclerViewAdapter(getActivity(), stringList);
        //设置布局管理器
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置adapter
        mRecyclerview.setAdapter(mAdapter);
        //添加分割线
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter.setOnItemClickLitener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), stringList.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(getActivity(), "onItemLongClick" + stringList.get(position), Toast.LENGTH_SHORT).show();
    }
}
