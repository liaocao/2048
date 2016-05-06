package com.example.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
    private int num = 0;
    private TextView label;//呈现文字用
    public Card(Context context) {
        super(context);
        label = new TextView(getContext());
        label.setTextSize(32);
        label.setBackgroundColor(0x33ffffff);//设置文字所处区域的背景色
        label.setGravity(Gravity.CENTER);//将内容设置居中
        
        LayoutParams lp = new LayoutParams(-1, -1);//-1代表填充满整个父级容器
        lp.setMargins(5, 5, 0, 0);//设置间隔，左边和上边都是10
        addView(label, lp);
        
        setNum(0);
        // TODO Auto-generated constructor stub
    }
	
    public int getNum(){
        return num;
    }
    
    public void setNum(int num){
        this.num = num;
        
		if(num <= 0){
			label.setText("");
		}else{
			label.setText(num+"");//如果这里直接传入num，它会把这个整形值当做某个资源的id，不能直接传入整形num
		}
	}
	
	public boolean equals(Card o){
		return getNum() == o.getNum();
	}//只需要判断两张卡片的数字是否相同就可以了
}
