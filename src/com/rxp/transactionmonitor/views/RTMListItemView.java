package com.rxp.transactionmonitor.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.rxp.transactionmonitor.R;

public class RTMListItemView extends View {

	private Paint mPaint = new Paint();
	private TextPaint tPaint = new TextPaint();
	Button testButton;
	private String topLine = ""; 
	private String subLine = ""; 


	public String getTopLine() {
		return topLine;
	}

	public void setTopLine(String topLine) {
		this.topLine = topLine;
	}


	public String getSubLine() {
		return subLine;
	}

	public void setSubLine(String subLine) {
		this.subLine = subLine;
	}

	public RTMListItemView(Context context) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	public RTMListItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
		
	}

	public RTMListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.RTMListItem);
		topLine = attributes.getString(R.styleable.RTMListItem_topLine);
		subLine = attributes.getString(R.styleable.RTMListItem_subLine);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// get the size of your control based on last call to onMeasure

		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		canvas.drawColor(Color.parseColor("#e8e3dc"));

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.parseColor("#c5c0ba"));
		canvas.drawRect(15, 14, width - 15, height - 14, mPaint);

		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.parseColor("#ffffff"));
		canvas.drawRect(16, 15, width - 15, height - 14, mPaint);

		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.parseColor("#a3f35b"));
		canvas.drawRect(16, 15, 20, height - 14, mPaint);
		
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.parseColor("#dad6cf"));
		canvas.drawRect(15, height - 13, width - 15, height - 11, mPaint);

		tPaint.setColor(Color.parseColor("#000000"));
		//tPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
		//		"fonts/Roboto-Thin.ttf"));
		tPaint.setFakeBoldText(false);
		tPaint.setTextSize(36);

		StaticLayout textLayout = new StaticLayout(
				getTopLine(),
				tPaint, width - 60, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

		canvas.save();
		canvas.translate(30, 22);
		textLayout.draw(canvas);
		canvas.restore(); // back to where we were at save()
		
		tPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Roboto-Thin.ttf"));
		tPaint.setFakeBoldText(true);
		tPaint.setTextSize(24);

		textLayout = new StaticLayout(
				getSubLine(),
				tPaint, width - 60, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);

		canvas.save();
		canvas.translate(30, 64);
		textLayout.draw(canvas);
		canvas.restore(); // back to where we were at save()

		
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.parseColor("#f4f0ed"));
		canvas.drawRect(16, height - 60, width - 15, height - 14, mPaint);

		mPaint.setColor(Color.parseColor("#e5e1dd"));
		canvas.drawLine(16, height - 60, width - 16, height - 60, mPaint);

		mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
				"fonts/Roboto-Thin.ttf"));
		mPaint.setFakeBoldText(true);
		mPaint.setTextSize(20);
		mPaint.setColor(Color.parseColor("#ffffff"));
		canvas.drawText("2 days ago", 30, height - 29, mPaint);
		mPaint.setColor(Color.parseColor("#928a81"));
		canvas.drawText("2 days ago", 30, height - 30, mPaint);

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.e("RTM", "HIGHT CLICk!!" + event.getY()+
						"-----------" + event.getX());

				if ((26 < event.getX() && event.getX() < 120)
						&& (25 < event.getY() && event.getY() < 120)) {

					// do your actions here
					Log.e("RTM", "Button pressed!");

				}
				return true;
			}
		});

	}

	@Override
	protected void onMeasure(int widthSpec, int heightSpec) {
		int measuredWidth = MeasureSpec.getSize(widthSpec);
		int measuredHeight = MeasureSpec.getSize(heightSpec);

		setMeasuredDimension(measuredWidth, 350);// measuredHeight);

	}
}
