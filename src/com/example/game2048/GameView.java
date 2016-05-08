package com.example.game2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {
//让这个类绑定xml，拷贝它的全路径com.example.game2048.GameView
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		Log.i("mDebug", "GameView context, attr, defStyle"+attrs.getAttributeValue(0));
		initGameView();
		
	}

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Log.i("mDebug", "GameView context");
		initGameView();
		
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i("mDebug", "GameView context, attr"+attrs.getAttributeValue(0));
		// TODO Auto-generated constructor stub
		initGameView();
	}
	
	private void initGameView(){
		setColumnCount(4);//设置GridLayout为4列 
		setBackgroundColor(0xff2C3135);//设置背景色
		
		setOnTouchListener(new View.OnTouchListener() {
			
			private float startX, startY, offsetX, offsetY;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						startX = event.getX();
						startY = event.getY();
						break;
					case MotionEvent.ACTION_UP:
						offsetX = event.getX() - startX;
						offsetY = event.getY() - startY;
						
						if(Math.abs(offsetX) > Math.abs(offsetY)){
							//区分出水平距离大还是 垂直距离大
							if(offsetX < -5){//代表向左移动
								swipeLeft();
							}else if(offsetX > 5){//代表向右移动
								swipeRight();
							}
						}else{
							if(offsetY < -5){//代表向上移动
								swipeUp();
							}else if(offsetY > 5){//代表向下移动
								swipeDown();
							}
						}						
						
						break;
					
				}
				return true;
				//因为这整个事件是连续的，ACTION_MOVE要在ACTION_DOWN之后才行，如果这里返回false，那么move就不会进行了
			}
		});		
	}
	
	//动态计算手机屏幕的宽高
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i("mDebug", "onSizeChanged");
		
		int cardWidth = (Math.min(w, h)-10)/4;//减10是为了给右边留下10像素的空隙

		
		addCards(cardWidth, cardWidth);
		//手机直立和平放的时候，布局会发生变化，为了不让它变化，需要配置一下AndroidManifest文件
		
		startGame();
	}
	
	private void addCards(int cardWidth, int cardHeight){
		Card c;
		
		for(int y = 0; y < 4; y++){//一行一行添加
			for(int x = 0; x < 4; x++){
				c = new Card(getContext());
				c.setNum(0);
				addView(c, cardWidth, cardHeight);
				
				cardsMap[x][y] = c;//用二位数组来记录这个方阵
			}
		}
	}
	
	private void startGame(){
		MainActivity.getMainActivity().clearScore();//这里是把主活动的数据清除了
		
		for(int y = 0; y < 4; y++){
			for(int x = 0; x < 4; x++){
				cardsMap[x][y].setNum(0);
			}
		}

		addRandowNum();
		addRandowNum();
		
	}
	
	private void addRandowNum(){
		
		emptyPoints.clear();//这里的清除无关于cardsMap，所以不会影响到游戏开始时的随机数字
		//遍历所有卡片
		for(int y = 0; y < 4; y++){
			for(int x = 0; x < 4; x++){
				if(cardsMap[x][y].getNum() <= 0){//只有方格内为空才能添加值
					emptyPoints.add(new Point(x, y));//这个Point里的x和y应该只是一种标记，对应到以后使用
				}
			}
		}
		
		Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));//随机的移除某个点，移除了有什么用呢，移除了才能给这个点设置数字么
		
		cardsMap[p.x][p.y].setNum(Math.random() > 0.1? 2 : 4);//这样就添加一个随机的数值到p所处的位置，2的概率远远大于4
	}
	
	private void swipeLeft(){
		
		boolean merge = false;//
		for(int y = 0; y < 4; y++){
			for(int x = 0; x < 4; x++){
				for(int x1 = x+1; x1 < 4; x1++){//遍历当前位置右边的值，这里的循环是x与右边的三个值得一一比较
					if(cardsMap[x1][y].getNum() > 0){//如果大于0，则说明不为空
						if(cardsMap[x][y].getNum() <= 0){//如果当前位置是空的
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());//就把右边有效的值放进来，并且清掉右边的值
							cardsMap[x1][y].setNum(0);
							
							x--;//减减是为了让一个位置的右边值都充分比较
							//这个只有当x的位置为空的时候才会减减一次，
							merge = true;
						}else if(cardsMap[x][y].equals(cardsMap[x1][y])){//如果两个值相同，就可以合并了
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							
							merge = true;
						}
						break;
					}
				}
			}
		}
		
		if(merge == true){
			addRandowNum();
			checkComplete();
		}
	}
	
	private void swipeRight(){
		boolean merge = false;
		
		for(int y = 0; y < 4; y++){
			for(int x = 3; x >= 0; x--){
				for(int x1 = x-1; x1 >= 0; x1--){//遍历当前位置右边的值
					if(cardsMap[x1][y].getNum() > 0){//如果大于0，则说明不为空
						if(cardsMap[x][y].getNum() <= 0){//如果当前位置是空的
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());//就把右边有效的值放进来，并且清掉右边的值
							cardsMap[x1][y].setNum(0);
							
							x++;//在这种情况下多遍历一次，确保如果右边有两个相同的值，就可以将其合并，这里要对应到之后的自减，为了回到原位，所以自增一次
							
							merge = true;
						}else if(cardsMap[x][y].equals(cardsMap[x1][y])){//如果两个值相同，就可以合并了
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							
							merge = true;
						}
						break;
					}
				}
			}
		}
		
		if(merge == true){
			addRandowNum();
			checkComplete();
		}
	}
	
	private void swipeUp(){
		boolean merge = false;
		
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				for(int y1 = y+1; y1 < 4; y1++){//遍历当前位置下边的值
					if(cardsMap[x][y1].getNum() > 0){//如果大于0，则说明下侧不为空
						if(cardsMap[x][y].getNum() <= 0){//如果当前位置是空的
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());//就把下边有效的值放进来，并且清掉下边的值
							cardsMap[x][y1].setNum(0);
							
							y--;//如果右边有两个相同的值，多遍历一次，就可以将其合并
							
							merge = true;
						}else if(cardsMap[x][y].equals(cardsMap[x][y1])){//如果两个值相同，就可以合并了
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							
							merge = true;
						}
						break;
					}
				}
			}
		}
		
		if(merge == true){
			addRandowNum();
			checkComplete();
		}
	}
	
	private void swipeDown(){
		boolean merge = false;
		
		for(int x = 0; x < 4; x++){
			for(int y = 3; y >= 0; y--){
				for(int y1 = y-1; y1 >= 0; y1--){//遍历当前位置下边的值
					if(cardsMap[x][y1].getNum() > 0){//如果大于0，则说明下侧不为空
						if(cardsMap[x][y].getNum() <= 0){//如果当前位置是空的
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());//就把下边有效的值放进来，并且清掉下边的值
							cardsMap[x][y1].setNum(0);
							
							y++;//如果右边有两个相同的值，多遍历一次，就可以将其合并
							
							merge = true;
						}else if(cardsMap[x][y].equals(cardsMap[x][y1])){//如果两个值相同，就可以合并了
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							
							merge = true;
						}
						break;
					}
				}
			}
		}
		
		if(merge == true){
			addRandowNum();
			checkComplete();
		}
	}
	
	private void checkComplete(){
		
		boolean complete = true;
		
		ALL:
		for(int y = 0; y < 4; y++){
			for(int x = 0; x < 4; x++){
				if((cardsMap[x][y].getNum() == 0) || //说明有空位，游戏没结束
						(x > 0 && cardsMap[x][y].equals(cardsMap[x-1][y])) ||//当与左边比较相等
						(x < 3 && cardsMap[x][y].equals(cardsMap[x+1][y])) ||//当与右边比较相等
						(y > 0 && cardsMap[x][y].equals(cardsMap[x][y-1])) ||//当与下边比较相等
						(y < 3 && cardsMap[x][y].equals(cardsMap[x][y+1]))){//当与上边比较相等，我晕，就是这里越界了，这里写错了，写成+3了

					complete = false;
					break ALL;//break只能跳出一层循环，所以加all
				}
			}
		}
		
		if(complete){
			new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startGame();
				}
			}).show();
		}
	}
	
	private Card[][] cardsMap = new Card[4][4];
	private List<Point> emptyPoints = new ArrayList<Point>();
}
