package com.ax.game2048;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static MainActivity mainActivity = null;
	private int score = 0;
	private int maxScore = 0;
	private int maxNum = 0;
	private int finishCount = 0;
	TextView tvScore, tvMaxScore, tvMaxNum;
	
	public static MainActivity getMainActivity(){
		return mainActivity;
	}
	
	public MainActivity() {
		mainActivity = this;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvScore = (TextView) findViewById(R.id.tv_score);
		tvMaxScore = (TextView) findViewById(R.id.tv_maxscore);
		tvMaxNum = (TextView) findViewById(R.id.tv_maxnum);
		
		getRecord();
		
	}

	public void clearScore() {
		score = 0;
		showScore();
	}
	public void addScore(int score){
		this.score+=score;
		showScore();
	}
	public void showScore(){
		tvScore.setText(score+"");
	}
	public int getScore() {
		return score;
	}
	
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public int getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}
	
	public void saveRecord(int maxScore, int maxNum){
		SharedPreferences sharedPreferences = getSharedPreferences("Game2048", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("maxScore", maxScore);
		editor.putInt("maxNum", maxNum);
		editor.commit();
	}
	
	public void getRecord(){
		SharedPreferences sharedPreferences = getSharedPreferences("Game2048", MODE_PRIVATE);
		maxScore = sharedPreferences.getInt("maxScore", 0);
		maxNum = sharedPreferences.getInt("maxNum", 0);
		tvMaxScore.setText(maxScore+"");
		tvMaxNum.setText(maxNum+"");
	}
	
	public void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("游戏提示");
		builder.setMessage("确认退出？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finishCount = 0;
			}
		});
		builder.create().show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
		}
		return false;
	}

}
