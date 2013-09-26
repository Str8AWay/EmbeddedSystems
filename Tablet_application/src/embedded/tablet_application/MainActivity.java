package embedded.tablet_application;

import embedded.tablet_application.ControlActivity;
import embedded.tablet_application.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void sendMessage(View view) {
	    Intent intent = new Intent(this, ControlActivity.class);
	    EditText editText = (EditText) findViewById(R.id.ipTextBox);
	    String ip = editText.getText().toString();
	    editText = (EditText) findViewById(R.id.portTextBox);
	    String port = editText.getText().toString();
	    String[] connectionInfo = {ip, port};
	    intent.putExtra("connection", connectionInfo);
	    startActivity(intent);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
