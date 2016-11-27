package cn.lemon.recyclerview.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lemon.recyclerview.R;
import cn.lemon.recyclerview.app.QQZoomView;

/**
 * Created by HP on 2016/11/26.
 */

public class TTActivity extends AppCompatActivity {
    QQZoomView listView;
    private ImageView header_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qq_list);
        getHeaderView();
        initView();

    }


    private void initView() {
        // TODO Auto-generated method stub
        listView=(QQZoomView) findViewById(R.id.list);
        listView.addHeaderView(getHeaderView());
        listView.setZoomView(header_iv);
        listView.setAdapter(getSimpleAdapter());
    }

    public BaseAdapter getSimpleAdapter(){
        List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
        for(int i=0;i<15;i++){
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("name", "小马___"+i);
            map.put("describe", "asdfasdfasdfasdfasdfsadfsad");
            data.add(map);
        }

        SimpleAdapter simpleAdapter=new SimpleAdapter(this, data, R.layout.list_item, new String[]{"name","describe"},
                new int[]{R.id.tv_name,R.id.tv_describe});
        return simpleAdapter;
    }

    public View getHeaderView(){
        View view= getLayoutInflater().inflate(R.layout.qq_image, null);
        header_iv =(ImageView) view.findViewById(R.id.image);
        return view;
    }

}
