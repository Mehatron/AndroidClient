package net.ddns.zivlak.mehatron.robotichand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText m_txtAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		m_txtAddress = (EditText)findViewById(R.id.txtAddress);

		Button btnConnect = (Button)findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent("net.ddns.zivlak.mehatron.robotichand.CONNECTING");
				intent.putExtra("address", m_txtAddress.getText().toString());
				startActivityForResult(intent, ConnectingActivity.ACTIVITY_REQUEST);
			}
		});

		Button btnTest = (Button)findViewById(R.id.btnTest);
		btnTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent("net.ddns.zivlak.mehatron.robotichand.CONTROL");
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == ConnectingActivity.ACTIVITY_REQUEST)
		{
			switch(resultCode)
			{
			case ConnectingActivity.ACTIVITY_RESULT_CONNECTED:
				Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
				Intent intent = new Intent("net.ddns.zivlak.mehatron.robotichand.CONTROL");
				startActivity(intent);
				break;
			case ConnectingActivity.ACTIVITY_RESULT_FAILED:
				Toast.makeText(this,
							   "Can't connect to server",
							   Toast.LENGTH_LONG).show();
				break;
			case ConnectingActivity.aCTIVITY_RESULT_WRONG_ADDRESS:
				Toast.makeText(this,
							   "Wrong format of server address",
							   Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
}
