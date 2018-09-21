package com.dlvn.mcustomerportal.activity.prototype;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.QuaTrinhTichLuyAdapter;
import com.dlvn.mcustomerportal.adapter.model.QuaTrinhTichLuyModel;

import java.util.ArrayList;
import java.util.List;

public class PointListActivity extends AppCompatActivity {
    List<QuaTrinhTichLuyModel> listData;
    QuaTrinhTichLuyAdapter adapter;
    ListView lvGeneratingHistory;
    SwipeRefreshLayout swipeContainer;
    LinearLayout lloBack;
    Button leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        initData();
        setListener();
    }

    private void initData(){
        lloBack = (LinearLayout) this.findViewById(R.id.lloBack);
        swipeContainer = (SwipeRefreshLayout) this.findViewById(R.id.swipeContainer);
        lvGeneratingHistory = (ListView) this.findViewById(R.id.lvGeneratingHistory);
        leftButton = (Button) this.findViewById(R.id.btnQuaTrinhTichLuy);
        rightButton = (Button) this.findViewById(R.id.btnQuaTrinhSuDung);

        if (listData == null)
            listData = new ArrayList<>();

        listData.add(new QuaTrinhTichLuyModel("01/01/2018", "1256987", "Hoàn Tất", "120"));
        listData.add(new QuaTrinhTichLuyModel("02/01/2018", "1544897", "Hoàn Tất", "120"));
        listData.add(new QuaTrinhTichLuyModel("03/01/2018", "5646987", "Hoàn Tất", "120"));
        listData.add(new QuaTrinhTichLuyModel("04/01/2018", "8956987", "Hoàn Tất", "120"));
        listData.add(new QuaTrinhTichLuyModel("05/01/2018", "1541567", "Hoàn Tất", "120"));


        if (adapter == null)
            adapter = new QuaTrinhTichLuyAdapter(this, R.layout.item_qua_trinh_tich_luy, listData);
        lvGeneratingHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeContainer.setRefreshing(false);
    }

    private void setListener() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                initData();
            }
        });

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftButton.setSelected(true);
                rightButton.setSelected(false);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightButton.setSelected(true);
                leftButton.setSelected(false);
            }
        });
    }
}
