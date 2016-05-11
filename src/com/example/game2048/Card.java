package com.example.game2048;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public class Card extends FrameLayout {
    private int num = 0;
    private TextView label;//呈现文字用
    
    public Card(Context context) {
        super(context);
		LayoutParams lp = null;
		
		lp = new LayoutParams(-1, -1);//-1代表填充满整个父级容器
		lp.setMargins(10, 10, 0, 0);//设置间隔，左边和上边都是10，如果设置为0，这里就会看不到边界，每个card都是连成一片，这个间隔是card的内间隔吧
		
		background = new View(getContext());//这里和上次不同了，多了一个background的View
		background.setBackgroundColor(0x33ffffff);//这层背景起到了蒙版的效果，调节alpha的值，实际上看到的是底部的颜色
		addView(background, lp);
        label = new TextView(getContext());//新建了一个TextView
        label.setTextSize(32);
//        label.setTextColor(0xffffffff);
//        label.setBackgroundColor(0xff464848);//设置文字所处区域的背景色ff2C3135
        label.setGravity(Gravity.CENTER);//将内容设置居中
        
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
			switch(num){
			case 2:
				label.setText("工兵");
				break;
			case 4:
				label.setText("排长");
				break;
			case 8:
				label.setText("连长");
				break;
			case 16:
				label.setText("营长");
				break;
			case 32:
				label.setText("团长");
				break;
			case 64:
				label.setText("旅长");
				break;
			case 128:
				label.setText("师长");
				break;
			case 256:
				label.setText("军长");
				break;
			case 512:
				label.setText("司令");
				break;
			case 1024:
				label.setText("元帅");
				break;
			case 2048:
				label.setText("主席");
				break;
			default:
				label.setText("传说");
				break;
			}
//			label.setText(num+"");//如果这里直接传入num，它会把这个整形值当做某个资源的id，不能直接传入整形num
		}

		switch (num) {
		case 0:
			label.setBackgroundColor(0x00000000);
			break;
		case 2:
			label.setBackgroundColor(0xffeee4da);
			break;
		case 4:
			label.setBackgroundColor(0xffede0c8);
			break;
		case 8:
			label.setBackgroundColor(0xfff2b179);
			break;
		case 16:
			label.setBackgroundColor(0xfff59563);
			break;
		case 32:
			label.setBackgroundColor(0xfff67c5f);
			break;
		case 64:
			label.setBackgroundColor(0xfff65e3b);
			break;
		case 128:
			label.setBackgroundColor(0xffedcf72);
			break;
		case 256:
			label.setBackgroundColor(0xffedcc61);
			break;
		case 512:
			label.setBackgroundColor(0xffedc850);
			break;
		case 1024:
			label.setBackgroundColor(0xffedc53f);
			break;
		case 2048:
			label.setBackgroundColor(0xffedc22e);
			break;
		default:
			label.setBackgroundColor(0xff3c3a32);
			break;
		}
	}

	public boolean equals(Card o) {
		return getNum()==o.getNum();
	}
	
	protected Card clone(){//这个clone应该是把当前的card复制一个返回去
		Card c= new Card(getContext());
		c.setNum(getNum());
		return c;
	}

	public TextView getLabel() {
		return label;//label就是TextView
	}
	
	private View background;
}
