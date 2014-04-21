package com.example.androidvoicecommand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.TextView;

import java.util.Locale;

public class AndroidActivity extends Activity implements OnInitListener {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	// Speech Recognizer instance (analyzes user voice)
	private SpeechRecognizer speech;

	// Text To Speech instance (android Voice)
	private TextToSpeech repeatTTS;
	
	TextView tvCommand;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tvCommand = (TextView) findViewById(R.id.tvCommandInfo);
		
		tvCommand.setText("Say any of the following commmands: \n\n'enable' / 'on' / 'left' / " +
				"'right' / 'back' / 'front' / 'forward' / 'backward' / 'disable' / 'off'" + 
				"\n\nNote: 'disable' or 'off' command shuts off speech listener for at least 30 seconds" +
				"\n\n\nIf you see error message that only means that speech wasn't recognized. Try to say command again.");
		
		// Create speech recognizer
		speech = SpeechRecognizer.createSpeechRecognizer(this);

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// Analyze English language
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				this.getPackageName());
		// After analyzing user voice return top 5 best results
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
		// Keep listener on for 1 second as soon as a voice is recognized
		// This helps speed up the voice analyze process
		intent.putExtra(
				RecognizerIntent. EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
				1000);

		// Set up Text to Speech
		startRepeat();
		repeatTTS = new TextToSpeech(this, this);

		MySpeechListener speechListener = new MySpeechListener(this, speech,
				intent, repeatTTS);

		speech.setRecognitionListener(speechListener);
		
		// Start Listener
		speech.startListening(intent);
	}

	void startRepeat() {
		// prepare the TTS to repeat chosen words
		Intent checkTTSIntent = new Intent();
		// check TTS data
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		// start the checking Intent - will retrieve result in onActivityResult
		startActivityForResult(checkTTSIntent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	/**
	 * onInit fires when TTS initializes
	 */
	public void onInit(int initStatus) {
		// if successful, set locale
		if (initStatus == TextToSpeech.SUCCESS)
			repeatTTS.setLanguage(Locale.ENGLISH);// ***choose your own locale
													// here***
	}

}