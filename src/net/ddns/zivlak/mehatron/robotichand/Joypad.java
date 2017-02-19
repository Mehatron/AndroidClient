package net.ddns.zivlak.mehatron.robotichand;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class Joypad extends SurfaceView implements Runnable, OnTouchListener {

	static final int DIRECTION_CENTER			= 0;
	static final int DIRECTION_RIGHT			= 1;
	static final int DIRECTION_UP				= 2;
	static final int DIRECTION_LEFT				= 3;
	static final int DIRECTION_DOWN				= 4;

	Thread m_thread = null;
	boolean m_running = false;
	SurfaceHolder m_holder;

	float m_width = 0.0f;
	float m_height = 0.0f;
	float m_stickX = 0.0f;
	float m_stickY = 0.0f;

	int m_direction;

	public Joypad(Context context) {
		super(context);

		m_holder = getHolder();
		setOnTouchListener(this);
	}

	public void resume() {
		m_running = true;
		m_thread = new Thread(this);
		m_thread.start();
	}

	public void pause() {
		m_running = false;
		try {
			m_thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m_thread = null;
	}

	@Override
	public void run() {
		while(m_running) {
			if(!m_holder.getSurface().isValid())
				continue;

			Canvas canvas = m_holder.lockCanvas();
			draw(canvas);
			m_holder.unlockCanvasAndPost(canvas);
		}
	}

	public void draw(Canvas canvas) {

		m_width = canvas.getWidth();
		m_height = canvas.getHeight();

		canvas.drawARGB(255, 255, 255, 255);

		drawBackgroundCircle(canvas);
		drawStick(canvas);
	}

	private void drawBackgroundCircle(Canvas canvas) {

		Paint pCircle = new Paint();
		pCircle.setColor(Color.rgb(65, 67, 71));

		float x = canvas.getWidth() / 2;
		float y = canvas.getHeight() / 2;
		float r = Math.min(canvas.getWidth(), canvas.getHeight()) / 3;
		canvas.drawCircle(x, y, r, pCircle);

		Paint pLine = new Paint();
		pLine.setColor(Color.WHITE);

		float x1 = (float)(x + Math.cos(Math.PI / 4) * r);
		float y1 = (float)(y - Math.sin(Math.PI / 4) * r);
		float x2 = (float)(x + Math.cos(-3 * Math.PI / 4) * r);
		float y2 = (float)(y - Math.sin(-3 * Math.PI / 4) * r);
		canvas.drawLine(x1, y1, x2, y2, pLine);

		x1 = (float)(x + Math.cos(3 * Math.PI / 4) * r);
		y1 = (float)(y - Math.sin(3 * Math.PI / 4) * r);
		x2 = (float)(x + Math.cos(-Math.PI / 4) * r);
		y2 = (float)(y - Math.sin(-Math.PI / 4) * r);
		canvas.drawLine(x1, y1, x2, y2, pLine);
	}

	private void drawStick(Canvas canvas) {

		Paint pCircle = new Paint();
		pCircle.setColor(Color.rgb(75, 77, 81));

		float cx = canvas.getWidth() / 2;
		float cy = canvas.getHeight() / 2;

		float x = cx + m_stickX;
		float y = cy + m_stickY;

		float r = Math.min(canvas.getWidth(), canvas.getHeight()) / 8;
		canvas.drawCircle(x, y, r, pCircle);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		switch(event.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			m_stickX = event.getX() - m_width / 2;
			m_stickY = event.getY() - m_height / 2;
			break;
		case MotionEvent.ACTION_UP:
			m_stickX = 0.0f;
			m_stickY = 0.0f;
			break;
		}

		int direction = coordsToDirection(m_stickX, m_stickY);
		if(direction != m_direction) {
			m_direction = direction;
		}

		return true;
	}

	private int coordsToDirection(float x, float y) {
		y = -y;

		double angle = Math.atan(y / x);
		if(angle < 0)
			angle += Math.PI;
		if(y < 0)
			angle += Math.PI;

		if(angle >= Math.PI * 5.0 / 3.0 || angle <= Math.PI / 3.0)
			return DIRECTION_RIGHT;
		if(angle >= Math.PI / 3.0 && angle <= Math.PI * 2.0 / 3.0)
			return DIRECTION_UP;
		if(angle >= Math.PI * 2.0 / 3.0 && angle <= Math.PI * 4.0 / 3.0)
			return DIRECTION_LEFT;
		if(angle >= Math.PI * 4.0 / 3.0 && angle <= Math.PI * 5.0 / 3.0)
			return DIRECTION_DOWN;

		return DIRECTION_CENTER;
	}
}
