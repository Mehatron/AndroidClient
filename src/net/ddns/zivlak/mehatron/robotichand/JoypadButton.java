package net.ddns.zivlak.mehatron.robotichand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class JoypadButton extends SurfaceView implements Runnable, Callback, OnTouchListener {

	private Thread m_thread = null;
	private boolean m_running = false;
	private SurfaceHolder m_holder;
	private boolean m_clicked = false;

	IClickHandler m_clickHandler = null;

	String m_text = "";

	public JoypadButton(Context context) {
		super(context);

		m_holder = getHolder();
		m_holder.setFormat(PixelFormat.TRANSPARENT);

		m_holder.addCallback(this);
		setOnTouchListener(this);
	}

	void resume() {
	}

	void pause() {
		m_running = false;
		try {
			m_thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		m_thread = null;
	}

	String getText() {
		return m_text;
	}

	void setText(String value) {
		m_text = value;
	}

	void setOnClickHandler(IClickHandler handler) {
		m_clickHandler = handler;
	}

	@Override
	public void run() {
		while(m_running) {
			if(!m_holder.getSurface().isValid())
				continue;

			Canvas canvas = m_holder.lockCanvas();
			draw(canvas);
			//canvas.drawARGB(255, 255, 0, 0);
			m_holder.unlockCanvasAndPost(canvas);
		}
	}

	public void draw(Canvas canvas) {

		canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);

		Paint pCircle = new Paint();
		float x = canvas.getWidth() / 2.0f;
		float y = canvas.getHeight() / 2.0f;
		float r = Math.min(canvas.getWidth(), canvas.getHeight()) / 2.0f;

		pCircle.setStyle(Style.FILL_AND_STROKE);
		int pColor = !m_clicked ? Color.rgb(9, 98, 124): Color.rgb(0, 125, 125);
		pCircle.setShader(new RadialGradient(x,
										   	 y,
										   	 r,
										   	 Color.TRANSPARENT,
										   	 pColor,
										   	 TileMode.MIRROR));
		canvas.drawCircle(x, y, r, pCircle);

		Paint pText = new Paint();
		pText.setColor(Color.BLACK);
		pText.setTextSize(50);
		Rect textBounds = new Rect();
		pText.getTextBounds(m_text, 0, m_text.length(), textBounds);
		canvas.drawText(m_text,
						canvas.getWidth() / 2 - textBounds.width() / 2.0f,
						canvas.getHeight() / 2 + textBounds.height() / 2.0f,
						pText);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		m_running = true;
		m_thread = new Thread(this);
		m_thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(!m_clicked) {
				m_clicked = true;
				if(m_clickHandler != null)
					m_clickHandler.onClick();
			}
			break;
		case MotionEvent.ACTION_UP:
			m_clicked = false;
			break;
		}

		return true;
	}
}
