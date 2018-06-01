package com.dlvn.mcustomerportal;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.adapter.NewsListAdapter;
import com.dlvn.mcustomerportal.adapter.model.NewsModel;
import com.dlvn.mcustomerportal.base.BaseActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NewsActivity extends BaseActivity {

	ListView lvData;

	NewsListAdapter adapter;
	List<NewsModel> lstData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initData();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		lvData = (ListView) findViewById(R.id.lvData);
	}

	private void initData() {
		// TODO Auto-generated method stub
		lstData = new ArrayList<>();
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 3 năm 2017",
				"https://kh.dai-ichi-life.com.vn/documents/10156/fc1cfe99-f1e5-4273-940b-5e37490a7667", "26/09/2017"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 2 năm 2017",
				"https://kh.dai-ichi-life.com.vn/documents/10156/cf551b01-0e4c-4085-ad9f-94b708149859", "28/06/2017"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số Đặc biệt năm 2017",
				"https://kh.dai-ichi-life.com.vn/documents/10156/3212a61c-b815-44a4-a1c2-17da3b98114d", "27/03/2017"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 4 năm 2016",
				"https://kh.dai-ichi-life.com.vn/documents/10156/3cacaa63-ea76-4ec1-8204-d054cf531ec8", "30/12/2016"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 3 năm 2016",
				"https://kh.dai-ichi-life.com.vn/documents/10156/812bb5ab-4534-4e9d-bc7f-a8c453b050fa", "28/09/2016"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 2 năm 2016",
				"https://kh.dai-ichi-life.com.vn/documents/10156/6ae3ad70-3c24-4bdf-b89e-f5a1da69d900", "24/05/2016"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 1 năm 2016",
				"https://kh.dai-ichi-life.com.vn/documents/10156/784565aa-45f9-4e5a-afbf-f96a7057183c", "25/03/2016"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 3 năm 2015",
				"https://kh.dai-ichi-life.com.vn/documents/10156/de4038a8-9d38-47bb-bcd6-7d0aad69f6fa", "22/11/2015"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 2 năm 2015",
				"https://khuat.dai-ichi-life.com.vn/documents/10156/14af5b7f-1a87-40f2-af81-1237a4a62a61",
				"23/06/2015"));
		lstData.add(new NewsModel("Bản tin Dai-Ichi-Life Việt Nam số 1 năm 2015",
				"https://khuat.dai-ichi-life.com.vn/documents/10156/a680ac1f-7ba4-407b-abd9-9839abdad764",
				"29/04/2015"));

		adapter = new NewsListAdapter(this, lstData);
		lvData.setAdapter(adapter);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				NewsModel item = (NewsModel) parent.getAdapter().getItem(position);
				if (item != null) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
					startActivity(browserIntent);
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
