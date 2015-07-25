package com.cwd.student_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.cwd.student_manager.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText ed_name;
	private EditText ed_num;
	private RadioGroup rg_sex;
	private LinearLayout result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity);
		ed_name = (EditText) findViewById(R.id.et_name);
		ed_num = (EditText) findViewById(R.id.et_num);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		result = (LinearLayout) findViewById(R.id.ll_result);
		refreshData();
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
	}

	// 刷新数据
	private void refreshData() {
		// 清除旧的全部的数据
		result.removeAllViews();
		// 解析已经存在的xml文件，把里面的数据全部都获取出来，添加上到界面上
		File files = getFilesDir();
		for (File file : files.listFiles()) {
			readXmlInfo(file);
		}
	}

	private void readXmlInfo(File file) {
		try {
			// init
			XmlPullParser parser = Xml.newPullParser();
			// param
			InputStream inputStream = new FileInputStream(file);
			parser.setInput(inputStream, "utf-8");
			int type = parser.getEventType();
			StringBuilder sb = new StringBuilder();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
				case XmlPullParser.START_TAG:
					if ("name".equals(parser.getName())) {
						// 这是name节点
						String name = parser.nextText();
						sb.append("--name:" + name);
					} else if ("num".equals(parser.getName())) {
						// 这是name节点
						String num = parser.nextText();
						sb.append("--num:" + num);
					} else if ("sex".equals(parser.getName())) {
						// 这是name节点
						String sex = parser.nextText();
						sb.append("--sex:" + sex);
					}
					break;
				default:
					break;
				}
				type = parser.next();// parser next element
			}
			inputStream.close();
			String text = sb.toString();
			TextView tView = new TextView(this);
			tView.setText(text);
			this.result.addView(tView);
		} catch (Exception e) {
			TextView tV = new TextView(this);
			tV.setText("null");
			result.addView(tV);
		}

	}
}
