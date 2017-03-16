package com.ax.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout{

	private int num = 0;
	private TextView lable;
	public Card(Context context) {
		super(context);
		lable = new TextView(getContext());
		lable.setTextSize(32);
		lable.setBackgroundColor(0x33ffffff);
		lable.setGravity(Gravity.CENTER);
		
		LayoutParams lp = new LayoutParams(-1, -1);
		lp.setMargins(10, 10, 0, 0);
		addView(lable, lp);
		setNum(0);
	}

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
		if (num<=0) {
			lable.setText("");
			lable.setBackgroundColor(0x33ffffff);
		}else{
			lable.setText(num+"");
			//设置不同的颜色
			int n = (int)(Math.log(num)/Math.log(2));
				lable.setBackgroundColor(0x99ffffff - 5958*n);
		}
	}
	
	public boolean equals(Card c){
		return getNum() == c.getNum();
	}
}
