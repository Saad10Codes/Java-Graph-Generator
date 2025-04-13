package com.sample.calclate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateAppActivity extends Activity {
	private EditText xEditForm; // x入力フォーム
	private EditText yEditForm; // y入力フォーム
	private Button execBtn; // 計算を実行するボタン
	private TextView resultText; // 結果を表示
	private Spinner calculateCodeSelectBox; //計算の符号のセレクトボックス
	private int calculateCode; //計算の符号

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	public void execCalculte(View view) {
		
		try {
			int x  = Integer.parseInt(xEditForm.getText().toString());
			int y  = Integer.parseInt(yEditForm.getText().toString());
			Calculate calc = new Calculate(x, y, calculateCode);
			printResult(calc.exec()+"");

		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public void printResult(CharSequence text) {
		resultText.setText(text);
	}

	protected void init() {
		xEditForm = (EditText) findViewById(R.id.xEditForm);
		yEditForm = (EditText) findViewById(R.id.yEditForm);
		resultText = (TextView) findViewById(R.id.resultText);
		execBtn = (Button) findViewById(R.id.execBtn);
		calculateCodeSelectBox=(Spinner)findViewById(R.id.calculateCodeSelectBox);
		calculateCode = Calculate.CODE_ADD;
		
		calculateCodeSelectBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				Spinner spinner = (Spinner) parent;
				String item = (String) spinner.getSelectedItem();
				calculateCode = position;
				
				Toast.makeText(CalculateAppActivity.this,
						String.format("%sが選択されました。", item),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(CalculateAppActivity.this,
						"onNothingSelected", Toast.LENGTH_SHORT).show();
			}
		});
	}
}