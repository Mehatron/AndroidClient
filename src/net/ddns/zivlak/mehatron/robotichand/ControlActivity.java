package net.ddns.zivlak.mehatron.robotichand;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

		final WSClient wsClient = WSClient.getInstance();

		m_joypadLeft = new Joypad(this);
		m_joypadLeft.setZOrderOnTop(true);
		LinearLayout jl = (LinearLayout)findViewById(R.id.joypadLeft);
		jl.addView(m_joypadLeft);
		m_joypadLeft.setOnDirectionChangeHandler(new IDirectionChangeHandler() {

			@Override
			public void onDirectionChanged(int direction) {
				if(wsClient == null)
					return;

				switch(direction) {
				case Joypad.DIRECTION_RIGHT:
					wsClient.send("move_right");
					break;
				case Joypad.DIRECTION_UP:
					wsClient.send("move_up");
					break;
				case Joypad.DIRECTION_LEFT:
					wsClient.send("move_left");
					break;
				case Joypad.DIRECTION_DOWN:
					wsClient.send("move_down");
					break;
				}
			}
		});

		m_joypadRight = new Joypad(this);
		m_joypadRight.setZOrderOnTop(true);
		LinearLayout jr = (LinearLayout)findViewById(R.id.joypadRight);
		jr.addView(m_joypadRight);
		m_joypadRight.setOnDirectionChangeHandler(new IDirectionChangeHandler() {

			@Override
			public void onDirectionChanged(int direction) {
				if(wsClient == null)
					return;

				switch(direction) {
				case Joypad.DIRECTION_RIGHT:
					wsClient.send("right_right");
					break;
				case Joypad.DIRECTION_UP:
					wsClient.send("right_up");
					break;
				case Joypad.DIRECTION_LEFT:
					wsClient.send("right_left");
					break;
				case Joypad.DIRECTION_DOWN:
					wsClient.send("right_down");
					break;
				}
			}
		});

		wsClient.addOnMessageHandler(new IMessageHandler() {

			@Override
			public void onMessageRecived(String message) {
			}
		});
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
