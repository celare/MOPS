package uk.me.davidreed.mops;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;

public class MainActivity extends Activity
{
	private Mops M;
	private TextView mainTextViewOutput;
	private EditText mainEditTextInput;
	private Button mainButtonSend;
	private ScrollView mainScrollViewOutput;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		mainTextViewOutput = (TextView)findViewById(R.id.mainTextViewOutput);
		mainEditTextInput = (EditText)findViewById(R.id.mainEditTextInput);
		mainButtonSend = (Button)findViewById(R.id.mainButtonSend);
		mainScrollViewOutput = (ScrollView)findViewById(R.id.mainScrollViewOutput);
		
		M = new Mops();
		M.setOnMopsOutputListener(new Mops.OnMopsOutputListener() {
				@Override
				public void OnMopsOutput(String output) {
					mainTextViewOutput.setText(mainTextViewOutput.getText().toString() + "\n" + output);
					mainScrollViewOutput.fullScroll(View.FOCUS_DOWN);
				}
			}
		);
		M.Start();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mainButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View p1) {
				M.Send(mainEditTextInput.getText().toString());
				mainEditTextInput.setText("");
			}
		});		
	}
}

