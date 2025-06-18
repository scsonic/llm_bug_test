package com.nexretail.javatest;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mediapipe.tasks.genai.llminference.GraphOptions;
import com.google.mediapipe.tasks.genai.llminference.LlmInference;
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession;
import com.google.mediapipe.tasks.genai.llminference.ProgressListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LLMTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            @Override
            public void run() {
                testLLM() ;
            }
        }.start();
    }

    public void testLLM() {
        ArrayList<String> maths = new ArrayList<>();
        maths.add("10*10*10 = ?");
        maths.add("10000 / 10 = ?");
        maths.add("500 + 500 = ?");
        maths.add("1100 - 100 = ?");
        maths.add("2000 / 2 = ?");
        maths.add("10*5*2*5*2 = ?");
        maths.add("1000+100-100 = ?");
        maths.add("can you help me to get 100+200+300+400=?");
        maths.add("200*5=?");

        LlmInference.LlmInferenceOptions options = LlmInference.LlmInferenceOptions.builder()
                //.setModelPath("/sdcard/Download/gemma3-1b-it-int4.task")
                .setModelPath("/sdcard/Download/gemma-3n-E4B-it-int4.task")
                .setMaxTokens(4096)
                .setPreferredBackend(LlmInference.Backend.GPU)
                .build();


        LlmInference llmInference = LlmInference.createFromOptions(this, options);

        LlmInferenceSession.LlmInferenceSessionOptions sessionOptions = LlmInferenceSession.LlmInferenceSessionOptions.builder()
                .setTopK(40)
                .setTopP(0.9f)
                .setTemperature(1.0f)
                //.setGraphOptions(GraphOptions.builder().setEnableVisionModality(true).build())
                .build();

        LlmInferenceSession session = LlmInferenceSession.createFromOptions(llmInference, sessionOptions);
        session.addQueryChunk("you are a helpful assistant");

        for ( int i = 0 ; i < 300 ; i++){
            String question = maths.get((int)(Math.random() * maths.size()));
            session.addQueryChunk(question);
            String res = session.generateResponse();
            Log.i(TAG, "user:  " + question) ;
            if ( res.contains("1000")){
                Log.i(TAG,  "model:" + res);
            }
            else {
                Log.e(TAG, "model:" + res) ;
            }
        }
    }
}