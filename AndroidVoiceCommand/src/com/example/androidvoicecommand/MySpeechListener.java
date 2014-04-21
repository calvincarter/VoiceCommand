package com.example.androidvoicecommand;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class MySpeechListener implements RecognitionListener {

	AndroidActivity context = null;
	private SpeechRecognizer speech;
	private Intent intent;
	private TextToSpeech repeatTTS;
	
	MySpeechListener(AndroidActivity c, SpeechRecognizer s, Intent i, TextToSpeech tts) {
		this.context = c;
		this.speech = s;
		this.intent = i;
		this.repeatTTS = tts;
	}
	
	@Override
    public void onBeginningOfSpeech() {
            Log.d("Speech", "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
            Log.d("Speech", "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
            Log.d("Speech", "onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
    	// If an error occurs notify user and reset listener
    	// This is usually triggered if no speech is recognized
    	Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
    	speech.startListening(intent);
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
            Log.d("Speech", "onEvent");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
            Log.d("Speech", "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
            Log.d("Speech", "onReadyForSpeech");
    }
    

    @Override
    public void onResults(Bundle results) {
            
    		// Gather top results and store in a array list
    		ArrayList<String> words = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            
    		// Run array for each top word
    		for (int i = 0; i < words.size();i++ ) {
    			// Normalize possible word and prepare to analyze
    			String possibleWord = words.get(i).toLowerCase().trim();
            
	            if(possibleWord.equals("enable") || possibleWord.equals("on")) {
	            	
	            	// Android echos the following:
	            	repeatTTS.speak("rover " + possibleWord, TextToSpeech.QUEUE_FLUSH, null);
	                            
	                try {
	    				Thread.sleep(500);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	                
	                break;	// terminate loop because possible word was correct
	            }
	            else if(possibleWord.equals("left") || possibleWord.equals("right") || possibleWord.equals("back") || possibleWord.equals("go") || possibleWord.equals("forward") || possibleWord.equals("backward") || possibleWord.equals("front")) {
	            	
	            	// Android echos the following:
	            	repeatTTS.speak(possibleWord, TextToSpeech.QUEUE_FLUSH, null);
	                
	                try {
	    				Thread.sleep(500);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}        
	                
	                break;	// terminate loop because possible word was correct
	            	
	            } else if(possibleWord.equals("disable") || possibleWord.equals("off")) {
	            	
	            	// Android echos the following:
	            	repeatTTS.speak("rover " + possibleWord, TextToSpeech.QUEUE_FLUSH, null);
	            	speech.stopListening();
	            	
	            	// disable speech listener for amount of time
	            	try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	break;	// terminate loop because possible word was correct
	            }
	        }
            
    		// Reset listener
            speech.startListening(intent);
                        
    }

    @Override
    public void onRmsChanged(float rmsdB) {
            Log.d("Speech", "onRmsChanged");
    }

}
