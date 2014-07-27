package com.example.sortlistview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;

public class MainActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	//public Map<String, Integer> server;
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * ���ƴ��������ListView����������
	 */
	private PinyinComparator pinyinComparator;
	private List<Address> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 String path = "http://192.168.228.216:8080/python/qnaddress.py";  
         String jsonString = HttpUtils.getJsonContent(path);  
         Log.i("json", "The jsonString:" + jsonString);  

         list=JSONTools.getAddresslists(jsonString);
         Log.i("address","address : "+list.size());
         
         HttpAsync
         
		initViews();
	}

	private void initViews() {
		//ʵ����תƴ����
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
            //����������
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition���Ӧ�Ķ���
				//Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
				// server = list.get(position).getServer();
				Log.i("mainposition", position+"");
				Intent intent = new Intent(MainActivity.this, SubMainAtivity.class);
				intent.putExtra("area", SourceDateList.get(position).getName());
				startActivity(intent);
			}
		});
		
		//SourceDateList = filledData(getResources().getStringArray(R.array.date));
		SourceDateList = filledData(list);
		// ���a-z��������Դ���
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б?����Ϊ��������б�
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * ΪListView������
	 * @param date
	 * @return
	 */
//	private List<SortModel> filledData(String [] date){
//		List<SortModel> mSortList = new ArrayList<SortModel>();
//		
//		for(int i=0; i<date.length; i++){
//			SortModel sortModel = new SortModel();
//			sortModel.setName(date[i]);
//			//����ת����ƴ��
//			String pinyin = characterParser.getSelling(date[i]);
//			String sortString = pinyin.substring(0, 1).toUpperCase();
//			
//			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
//			if(sortString.matches("[A-Z]")){
//				sortModel.setSortLetters(sortString.toUpperCase());
//			}else{
//				sortModel.setSortLetters("#");
//			}
//			
//			mSortList.add(sortModel);
//		}
//		return mSortList;
//		
//	}
//	
	private List<SortModel> filledData(List<Address> addresses){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<addresses.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(addresses.get(i).getArea());
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(addresses.get(i).getArea());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	
	
	/**
	 * ���������е�ֵ��������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// ���a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}
