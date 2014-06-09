package in.nrsh.augumentedreality.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SquareItem extends LinearLayout {

	
	public SquareItem(Context context) {
		super(context);
	}

	public SquareItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SquareItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

}
