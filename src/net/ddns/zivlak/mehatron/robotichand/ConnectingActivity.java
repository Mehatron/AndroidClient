package net.ddns.zivlak.mehatron.robotichand;

import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ConnectingActivity extends Activity implements Runnable {

	public static final int ACTIVITY_REQUEST						=  1;

	public static final int ACTIVITY_RESULT_CONNECTED				=  0;
	public static final int ACTIVITY_RESULT_FAILED					= -1;
	public static final int aCTIVITY_RESULT_WRONG_ADDRESS			= -2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connecting);

		setResult(ACTIVITY_RESULT_FAILED);
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			String address = getIntent().getExtras().getString("address");
			URI url = new URI("ws://" + address + ":8272/");

			WSClient wsClient = WSClient.createInstance(url);
			wsClient.addOnConnectHandler(new IConnectHandler() {

				@Override
				public void onConnect() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							close(ACTIVITY_RESULT_CONNECTED);
						}
					});
				}
			});
			wsClient.addOnErrorHandler(new IErrorHandler() {

				@Override
				public void onError(final Exception ex) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							close(ACTIVITY_RESULT_FAILED);
						}
					});
				}
			});
			wsClient.open();
		} catch(Exception ex) {
			close(aCTIVITY_RESULT_WRONG_ADDRESS);
		}
	}

	public void close(int resultCode) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}

		setResult(resultCode);
		finish();
	}
}
