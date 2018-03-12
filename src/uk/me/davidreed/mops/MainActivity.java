package uk.me.davidreed.mops;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import android.widget.TextView.*;
import android.view.inputmethod.*;

public class MainActivity extends Activity
{
	private Mops M;
	private TextView mainTextViewOutput;
	private EditText mainEditTextInput;
	private ScrollView mainScrollViewOutput;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		// Grab references to all UI controls
		mainTextViewOutput = (TextView)findViewById(R.id.mainTextViewOutput);
		mainEditTextInput = (EditText)findViewById(R.id.mainEditTextInput);
		mainScrollViewOutput = (ScrollView)findViewById(R.id.mainScrollViewOutput);
		
		// Create new MOPS engine but don't start it until we have hooked up all listeners
		M = new Mops();
		
		// Listen for responses to commands and display them
		M.setOnMopsOutputListener(new Mops.OnMopsOutputListener() {
				@Override
				public void OnMopsOutput(String output) {
					displayText(output);
				}
			}
		);
		
		// Listen for the enter key and send the command
		mainEditTextInput.setOnEditorActionListener(new OnEditorActionListener()
			{
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
				{
					boolean handled = false;
					
					/*if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)*/
					switch (actionId)
					{
						case EditorInfo.IME_ACTION_DONE:
							displayText(mainEditTextInput.getText().toString());
							sendText(mainEditTextInput.getText().toString());
							handled = true;
							break;
					}
					return handled;
				}
			});
		
		// We're all hooked up so start the MOPS engine
		M.Start();
	}
	
	private void displayText(String text) {
		// Add the text response to the text view and scroll to bottom so it is visible
		mainTextViewOutput.setText(mainTextViewOutput.getText().toString() + "\n" + text);
		// fullScroll doesn't work unless posted in to the UI message queue via a runnable!!!
		mainScrollViewOutput.post(new Runnable() {
				@Override
				public void run() {
					mainScrollViewOutput.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
	}
	
	private void sendText(String text)
	{
		// Send the text command to the MOPS engine and clear the command text
		M.Send(text);
		mainEditTextInput.setText("");
		// requestFocus doesn't work unless posted in to the UI message queue via a runnable!!!
		mainEditTextInput.post(new Runnable() {
				@Override
				public void run() {
					mainEditTextInput.requestFocus();
				}
			});
	}
}

