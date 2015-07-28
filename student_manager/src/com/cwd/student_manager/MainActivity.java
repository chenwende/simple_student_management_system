package com.cwd.student_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.cwd.student_manager.R;
import com.cwd.students.students;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText ed_name;
	private EditText ed_num;
	private RadioGroup rg_sex;
	private ListView result;
	private List<students> studentslist = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity);
		ed_name = (EditText) findViewById(R.id.et_name);
		ed_num = (EditText) findViewById(R.id.et_num);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		result = (ListView) findViewById(R.id.ll_result);
		studentslist = new ArrayList<students>();
		// result.setAdapter(new SimpleAdapter());
		// refreshData();
	}

	public void save(View view) {
		String name = ed_name.getText().toString().trim();
		String num = ed_num.getText().toString().trim();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num)) {
			Toast.makeText(this, "name or munber is empty", Toast.LENGTH_SHORT)
					.show();
			;
			return;
		} else {
			try {
				File file = new File(getFilesDir(), num + ".xml");
				FileOutputStream oStream = new FileOutputStream(file);
				// 采用谷歌api 生成xml文件
				// 1.获取一个xml文件的生成器,序列化器
				XmlSerializer serializer = Xml.newSerializer();
				// init
				serializer.setOutput(oStream, "utf-8");
				// write data
				serializer.startDocument("utf-8", true);
				serializer.startTag(null, "student");
				serializer.startTag(null, "name");
				serializer.text(name);
				serializer.endTag(null, "name");
				serializer.startTag(null, "num");
				serializer.text(num);
				serializer.endTag(null, "num");
				serializer.startTag(null, "sex");
				if (rg_sex.getCheckedRadioButtonId() == R.id.rb_male) {
					serializer.text("male");
				} else {
					serializer.text("female");
				}
				serializer.endTag(null, "sex");
				serializer.endTag(null, "student");
				serializer.endDocument();
				oStream.close();
				Toast.makeText(this, "save data successful", 0).show();
			} catch (Exception e) {
				Toast.makeText(this, "save data fail", 0).show();
			}

		}
		refreshData();
		refreshview();
	}

	// 刷新数据
	private void refreshData() {
		// 清除旧的全部的数据
		studentslist = new ArrayList<students>();
		// 解析已经存在的xml文件，把里面的数据全部都获取出来，添加上到界面上
		File files = getFilesDir();
		for (File file : files.listFiles()) {
			readXmlInfo(file);
		}
	}

	private void refreshview() {
		//result.removeAllViews();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (studentslist != null) {
			for (Iterator iterator = studentslist.iterator(); iterator
					.hasNext();) {
				students student = (students) iterator.next();
				HashMap<String, String> map = new HashMap<String, String>();				
				map.put("name", student.getName());
				map.put("num", student.getNum());
				map.put("sex", student.getSex());
				list.add(map);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
					R.layout.student_item,
					new String[] { "name", "num", "sex" }, new int[] {
							R.id.name, R.id.num, R.id.sex });
			// 刷新界面
			result.setAdapter(simpleAdapter);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub		
		refreshData();
		refreshview();
		super.onResume();
	}

	private void readXmlInfo(File file) {
		//List<students> studentslist_temp = new ArrayList<students>();
		try {
			// init
			XmlPullParser parser = Xml.newPullParser();
			// param
			InputStream inputStream = new FileInputStream(file);
			parser.setInput(inputStream, "utf-8");
			int type = parser.getEventType();

			students student = new students();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("name".equals(parser.getName())) {
						// 这是name节点
						String name = parser.nextText();
						student.setName(name);
					} else if ("num".equals(parser.getName())) {
						// 这是num节点
						String num = parser.nextText();
						student.setNum(num);
					} else if ("sex".equals(parser.getName())) {
						// 这是sex节点
						String sex = parser.nextText();
						student.setSex(sex);
					}
					break;
				default:
					break;
				}
				type = parser.next();// parser next element
			}
			if (student.getName() != null) {
				studentslist.add(student);
			} else {
				Log.d("", "student is null");
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
