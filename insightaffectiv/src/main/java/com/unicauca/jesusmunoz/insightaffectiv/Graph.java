package com.unicauca.jesusmunoz.insightaffectiv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class Graph extends View {
	Context mContext;

	ArrayList<Float> graphValues;

	float realWidth = 0;
	float realHeight = 0;
	float w2hScale = 0.25f;
	float frameSize = 600.0f;

	Paint areaPaint;

	Paint borderPaint;

	int offsetX = 0;

	Boolean isStop = false;

	public Graph(Context context, int width, int color) {
		super(context);
		this.mContext = context;

		realWidth = width;

		this.graphValues = new ArrayList<Float>();
		graphValues.clear();
		areaPaint = new Paint();
		areaPaint.setColor(Color.rgb(146, 211, 238));
		areaPaint.setFakeBoldText(true);
		areaPaint.setTextAlign(Align.LEFT);
		areaPaint.setTextSize(10);
		areaPaint.setStrokeWidth(5);

		borderPaint = new Paint();
		borderPaint.setColor(color);
		borderPaint.setFakeBoldText(true);
		borderPaint.setTextAlign(Align.LEFT);
		borderPaint.setTextSize(10);
		borderPaint.setStrokeWidth(5);

		realHeight = (realWidth * w2hScale);
		this.setLayoutParams(new LayoutParams((int) realWidth, (int) realHeight));
	}

	public void setScale(float scale) {
		this.w2hScale = scale;
		realHeight = (realWidth * w2hScale);
		this.setLayoutParams(new LayoutParams((int) realWidth, (int) realHeight));
	}

	public void setHeight(float newHeight) {
		realHeight = newHeight;
		this.setLayoutParams(new LayoutParams((int) realWidth, (int) realHeight));
	}

	public void reset() {
		this.graphValues.clear();
	}

	public void addData(Float x) {
		if (!isStop) {
			float addvalue = x;
			if (this.graphValues.size() <= this.frameSize) {
				this.graphValues.add(0, addvalue);
			}

			else {
				this.graphValues.remove(this.graphValues.size() - 1);
				this.graphValues.add(0, addvalue);
			}
			invalidate();
		}
	}

	public void setStop() {

	}

	@Override
	protected void onDraw(Canvas canvas) {

		final float engagementAxis = realHeight;

		float preX = 0;
		float preY = 0;
 
		float distance = this.realWidth / (this.frameSize - 1); 

		if (this.graphValues.size() > 0) {
			preX = this.realWidth;
			preY = this.graphValues.get(0) * this.realHeight;

			int distanceCounter = 0;
			for (int i = 0; i < this.graphValues.size(); i++) {
				float curX = this.realWidth - distance * distanceCounter;
				float curY;
				if (this.graphValues.get(i) == 0) {
					curY = 1.0f;
					this.w2hScale = 0.555f;
					realHeight = (realWidth * w2hScale);
					LayoutParams lp = this.getLayoutParams();
					lp.width = (int) realWidth;
					lp.height = (int) realHeight;
					this.requestLayout();
				} else {
					curY = this.graphValues.get(i) * this.realHeight;
				}
				canvas.drawLine(offsetX + preX, engagementAxis - preY, offsetX+ curX, engagementAxis - curY, borderPaint);
				try {

					preX = curX + 0.5f;
					preY = curY + 0.5f;
					distanceCounter++;
					//
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}

	}

}