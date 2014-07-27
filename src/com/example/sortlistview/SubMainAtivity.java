package com.example.sortlistview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.R.integer;
import android.R.string;
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

public class SubMainAtivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private Map<String, Integer> server;
	
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;
	private List<Address> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainActivity mainActivity = new MainActivity();
		//server = mainActivity.server;
		
		Intent itIntent = getIntent();
		String area = itIntent.getStringExtra("area");
		Log.i("subarea", area);
		 String path = "http://192.168.228.216:8080/python/qnaddress.py";  
         String jsonString = HttpUtils.getJsonContent(path);  
         Log.i("json", "The jsonString:" + jsonString);  

         list=JSONTools.getAddresslists(jsonString);
         //server = list.get(position).getServer();
         Log.i("address","address : "+list.size());
         server = getsevername(area);
		initViews();
	}

	private Map<String, Integer> getsevername (String area) {
		// TODO Auto-generated method stub
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		int i=0;
		int n=list.size();
		for(i=0;i<list.size();++i){
			String string =list.get(i).getArea();
			//�ַ������Ҫ��equals������ʼ�
			if(area.equals(list.get(i).getArea())) break;
		}
		if(i>=list.size()) i=0;
		serverMap = list.get(i).getServer();
		return serverMap;
	}

	private void initViews() {
		//ʵ��������תƴ����
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
            //�����������
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				//Toast.makeText(getApplication(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
				
			}
		});
		
		//SourceDateList = filledData(getResources().getStringArray(R.array.date));
		SourceDateList = filledData2(server);
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//�������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//������������ֵΪ�գ�����Ϊԭ�����б�������Ϊ���������б�
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
	 * ΪListView�������
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
//			// �������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	private List<SortModel> filledData2(Map<String, Integer> server){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		//Set<String> keys=server.keySet();
		for(Map.Entry<String, Integer> entry: server.entrySet()){
			SortModel sortModel = new SortModel();
			sortModel.setName(entry.getKey());
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(entry.getKey());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// �������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	 * ����������е�ֵ���������ݲ�����ListView
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
		
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
}