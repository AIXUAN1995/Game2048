package com.ax.game2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout{
	
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();
	private boolean flag = false;						//卡片位置是否有变化
	private int max = 0;								//储存最大数字

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		initGameView();
	}

	private void initGameView() {
		setColumnCount(4);							//列数为4
		setBackgroundColor(0xffbbada0);
		
		setOnTouchListener(new OnTouchListener() {
			private float startX, startY, offsetX, offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;
					
					//判断移动方向
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						//横向移动
						if (offsetX < -5) {
							//向左移动
							moveLeft();
						}else if (offsetX > 5) {
							//向右移动
							moveRight();
						}
					}else {
						//纵向移动
						if (offsetY < -5) {
							//向上移动
							moveUp();
						}else if (offsetY > 5) {
							//向下移动
							moveDown();
						}
					}
					break;
				default:
					break;
				}
				
				//是否添加随机数，并判断游戏是否结束
				if (flag) {
					addRandomNum();
					flag = false;
					if (max == 2048) {
						save();
						new AlertDialog.Builder(getContext()).setTitle("游戏提示").setMessage("恭喜通关！").setPositiveButton("再玩一次", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								startGame();
							}
						}).show();
					}
					else if (isOver()) {
						save();
						new AlertDialog.Builder(getContext()).setTitle("游戏提示").setMessage("Game Over").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startGame();
							}
						}).show();
					}
				}
				return true;
			}
		});
	}
	
	private void startGame(){
		//分数清零
		MainActivity.getMainActivity().clearScore();
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				cardsMap[i][j].setNum(0);
			}
		}
		
		MainActivity.getMainActivity().getRecord();
		addRandomNum();
		addRandomNum();
	}
	
	//当宽高发生改变时执行，如果Manifest设置只有竖屏，则初始化时执行
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		int cardWidth = (Math.min(w, h)-10)/4;
		addCard(cardWidth, cardWidth);
		startGame();
	}
	
	public void addCard(int cardWidth, int cardHeight){
		Card card;
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				card = new Card(getContext());
				card.setNum(2);
				addView(card, cardWidth, cardHeight);
				cardsMap[i][j] = card;
			}
		}
	}
	
	private void addRandomNum(){
		emptyPoints.clear();
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if (cardsMap[i][j].getNum()<=0) {
					emptyPoints.add(new Point(i, j));
				}
			}
		}
		
		Point point = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		cardsMap[point.x][point.y].setNum(Math.random()>0.1?2:4);
	}
	
	private void moveLeft() {
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				//从当前位置向右查询
				for(int k=j+1; k<4; k++){
					//找到有数字的卡片
					if (cardsMap[i][k].getNum()>0) {
						//判断左边的卡片是不是空
						if (cardsMap[i][j].getNum()<=0) {
							cardsMap[i][j].setNum(cardsMap[i][k].getNum());
							cardsMap[i][k].setNum(0);
							j--;
							flag = true;
						}else if (cardsMap[i][j].getNum() == cardsMap[i][k].getNum()) {
							cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
							cardsMap[i][k].setNum(0);
							//记录最大数字
							if (max < cardsMap[i][j].getNum()) {
								max = cardsMap[i][j].getNum();
							}
							//计分
							MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
							flag = true;
						}
						break;
					}
				}
			}
		}
	}
	private void moveRight() {
		for(int i=0; i<4; i++){
			for(int j=3; j>=0; j--){
				//从当前位置向左查询
				for(int k=j-1; k>=0; k--){
					//找到有数字的卡片
					if (cardsMap[i][k].getNum()>0) {
						//判断右边的卡片是不是空
						if (cardsMap[i][j].getNum()<=0) {
							cardsMap[i][j].setNum(cardsMap[i][k].getNum());
							cardsMap[i][k].setNum(0);
							j++;
							flag = true;
						}else if (cardsMap[i][j].getNum() == cardsMap[i][k].getNum()) {
							cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
							cardsMap[i][k].setNum(0);
							//记录最大数字
							if (max < cardsMap[i][j].getNum()) {
								max = cardsMap[i][j].getNum();
							}
							//计分
							MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
							flag = true;
						}
						break;
					}
				}
			}
		}
	}
	private void moveUp() {
		for(int j=0; j<4; j++){
			for(int i=0; i<4; i++){
				//从当前位置向下查询
				for(int k=i+1; k<4; k++){
					//找到有数字的卡片
					if (cardsMap[k][j].getNum()>0) {
						//判断上边的卡片是不是空
						if (cardsMap[i][j].getNum()<=0) {
							cardsMap[i][j].setNum(cardsMap[k][j].getNum());
							cardsMap[k][j].setNum(0);
							i--;
							flag = true;
						}else if (cardsMap[i][j].getNum() == cardsMap[k][j].getNum()) {
							cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
							cardsMap[k][j].setNum(0);
							//记录最大数字
							if (max < cardsMap[i][j].getNum()) {
								max = cardsMap[i][j].getNum();
							}
							//计分
							MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
							flag = true;
						}
						break;
					}
				}
			}
		}
	}
	private void moveDown() {
		for(int j=0; j<4; j++){
			for(int i=3; i>=0; i--){
				//从当前位置向下查询
				for(int k=i-1; k>=0; k--){
					//找到有数字的卡片
					if (cardsMap[k][j].getNum()>0) {
						//判断下边的卡片是不是空
						if (cardsMap[i][j].getNum()<=0) {
							cardsMap[i][j].setNum(cardsMap[k][j].getNum());
							cardsMap[k][j].setNum(0);
							i++;
							flag = true;
						}else if (cardsMap[i][j].getNum() == cardsMap[k][j].getNum()) {
							cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
							cardsMap[k][j].setNum(0);
							//记录最大数字
							if (max < cardsMap[i][j].getNum()) {
								max = cardsMap[i][j].getNum();
							}
							//计分
							MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
							flag = true;
						}
						break;
					}
				}
			}
		}
	}
	
	private boolean isOver(){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if (cardsMap[i][j].getNum() == 0 ||
						(j>0 && cardsMap[i][j].getNum() == cardsMap[i][j-1].getNum()) ||
						(j<3 && cardsMap[i][j].getNum() == cardsMap[i][j+1].getNum()) ||
						(i>0 && cardsMap[i][j].getNum() == cardsMap[i-1][j].getNum()) ||
						(i<3 && cardsMap[i][j].getNum() == cardsMap[i+1][j].getNum())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void save(){
		int maxNum = MainActivity.getMainActivity().getMaxNum();
		int maxScore = MainActivity.getMainActivity().getMaxScore();
		int score = MainActivity.getMainActivity().getScore();
		if (max>maxNum || score>maxScore) {
			maxNum = maxNum>max?maxNum:max;
			maxScore = maxScore>score?maxScore:score;
			MainActivity.getMainActivity().saveRecord(maxScore, maxNum);
		}
	}
	
}
