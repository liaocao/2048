package com.example.game2048;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView tvScore;
	private static MainActivity mainActivity = null;
	private int score = 0;
	
	
	public MainActivity() {
		mainActivity = this;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvScore = (TextView) findViewById(R.id.tvScore);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static MainActivity getMainActivity() {
		return mainActivity;
	}
	
	public void clearScore(){
		score = 0;
		showScore();
	}
	
	public void showScore(){
		tvScore.setText(score+"");//这里不加上空字符，就会报错
	}
	
	public void addScore(int s){
		score += s;
		showScore();
	}
	
}
