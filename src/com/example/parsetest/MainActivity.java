package com.example.parsetest;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R.id;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private TextView stateText;
	private ListView listView;
	private ArrayAdapter arrayAdapter;
	private ParseItem parseItem;
	private EditText editText;
	private Button creatBtn, updateBtn, deleteBtn;
	private ParseQuery<ParseItem> parseItemQuery;
	private List<String> itemStringList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialObject(); //初始化所有之後要用的全域物件
		
		ParseObject.registerSubclass(ParseItem.class); //要在 Parse Initialize之前處理
		Parse.initialize(this, "PARSE_APPLICATION_ID",
				"PARSE_CLIENT_KEY"); //這裏要換成自己Parse專案的key值
		parseItem = new ParseItem(); //塞入資料庫後才會有 Object ID，因此剛new完是沒有的。
		parseItemQuery = ParseQuery.getQuery(ParseItem.class);
		
		updataParseQuery();
		setStateView();
	}

	private void initialObject(){
		stateText = (TextView) findViewById(R.id.stateText); //對應UI變數與畫面上UI元件的關係
		listView = (ListView) findViewById(R.id.listView);
		editText = (EditText) findViewById(R.id.editText);
		creatBtn = (Button) findViewById(R.id.creatButton);
		creatBtn.setOnClickListener(this); //註冊監聽器 按鈕才會有反應
		updateBtn = (Button) findViewById(R.id.updateButton);
		updateBtn.setOnClickListener(this);
		deleteBtn = (Button) findViewById(R.id.deleteButton);
		deleteBtn.setOnClickListener(this);
		arrayAdapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, itemStringList); //使用範本省得自己刻ListView的子物件
	}
	
	private void setStateView() {
		stateText.setText("資料庫實體：" + parseItem.getClassName() + "\n當前物件ID："
				+ parseItem.getObjectId());
	}

	private void updataParseQuery() {
		parseItemQuery.findInBackground(new FindCallback<ParseItem>() {

			@Override
			public void done(List<ParseItem> itemList, ParseException e) {
				if (e == null) {
					itemStringList.clear(); //要清掉，否則下次會看到舊的
					for (ParseItem i : itemList) {
						itemStringList.add(i.getString("foo"));
						listView.setAdapter(arrayAdapter);
					}// end for
				}//end if
			}//end done
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.creatButton) {
			parseItem = new ParseItem(); //創立一個新項目即是新增
			parseItem.setKey(editText.getText().toString());
			Toast.makeText(this, "新增內容："+editText.getText().toString(), Toast.LENGTH_LONG).show();
		
		} else if (arg0.getId() == R.id.updateButton) {
			parseItem.setKey(editText.getText().toString()); //直接改值就是修改了
			Toast.makeText(this, "修改內容："+editText.getText().toString(), Toast.LENGTH_LONG).show();
		
		} else if (arg0.getId() == R.id.deleteButton) {
			try {
				parseItem.delete();//刪除當前物件
				Toast.makeText(this, "刪除物件："+ parseItem.getObjectId(), Toast.LENGTH_LONG).show();
			
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		setStateView();
		updataParseQuery();
	}
}
