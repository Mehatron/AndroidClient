package net.ddns.zivlak.mehatron.robotichand;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class ControlActivity extends Activity {

	Joypad m_joypadLeft;
	Joypad m_joypadRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.control);

		m_joypadLeft = new Joypad(this);
		m_joypadLeft.setZOrderOnTop(true);
		LinearLayout jl = (LinearLayout)findViewById(R.id.joypadLeft);
		jl.addView(m_joypadLeft);

		m_joypadRight = new Joypad(this);
		m_joypadRight.setZOrderOnTop(true);
		LinearLayout jr = (LinearLayout)findViewById(R.id.joypadRight);
		jr.addView(m_joypadRight);

	}

	@Override
	protected void onPause() {
		super.onPause();
		m_joypadLeft.pause();
		m_joypadRight.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		m_joypadLeft.resume();
		m_joypadRight.resume();
	}
}
