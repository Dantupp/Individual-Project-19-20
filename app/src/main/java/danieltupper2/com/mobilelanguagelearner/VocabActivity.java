package danieltupper2.com.mobilelanguagelearner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VocabActivity extends AppCompatActivity {
    private int ID;
    private AppDatabase db;
    private final int RECORD_AUDIO_REQUEST_CODE = 101;
    private TextToSpeech mTTS;
    private String chosenWord="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == RECORD_AUDIO_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                init();
            } else{
                Toast audioNeedsAcceptingMessage = Toast.makeText(getApplicationContext(),"Recording audio must be enabled",Toast.LENGTH_LONG);
                audioNeedsAcceptingMessage.show();
            }
        }
    }

    private void init(){
        setContentView(R.layout.activity_vocab);
        db = AppDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();
        ID = intent.getIntExtra("ID",0);

        ImageButton closeImageButton = findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText(db.MarkerDao().getMarkerFromID(ID).getPlaceName());

        List<Vocab> vocabs = db.VocabDao().getVocabFromID(ID);
        TextView EnglishTextView = findViewById(R.id.EnglishTextView);
        final TextView TranslationTextView = findViewById(R.id.TranslationTextView);
        if(!vocabs.isEmpty()) {
            int randword = new Random().nextInt(vocabs.size());
            String EnglishText = "ENGLISH: " + vocabs.get(randword).getEnglish();
            EnglishTextView.setText(EnglishText);
            chosenWord = vocabs.get(randword).getHungarian();
            String TranslationText = "HUNGARIAN: " + chosenWord;
            TranslationTextView.setText(TranslationText);
        } else {
            EnglishTextView.setText("No Vocabulary Found!");
        }

        final ImageButton playSoundImageButton = findViewById(R.id.playSoundImageButton);
        playSoundImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTTS.speak(chosenWord,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = mTTS.setLanguage(new Locale("hu","HU"));
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TextToSpeech","Language not supported");
                    } else {
                        playSoundImageButton.setEnabled(true);
                    }
                } else {
                    Log.e("TextToSpeech","Initialization Failed");
                }
            }
        });

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hu_HU");
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float rmsdB) { }

            @Override
            public void onBufferReceived(byte[] buffer) { }

            @Override
            public void onEndOfSpeech() { }

            @Override
            public void onError(int error) { }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                boolean matched = false;
                if(matches != null){
                    for(String s : matches){
                        if(s.toUpperCase().equals(chosenWord.toUpperCase())){
                            matched = true;
                        }
                    }
                    if(matched){
                        Toast matchedMessage = Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_LONG);
                        matchedMessage.show();
                    } else{
                        Toast unmatchedMessage = Toast.makeText(getApplicationContext(),"Try again!",Toast.LENGTH_LONG);
                        unmatchedMessage.show();
                    }
                } else {
                    Toast noSpeechResults = Toast.makeText(getApplicationContext(),"Cannot understand speech",Toast.LENGTH_LONG);
                    noSpeechResults.show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) { }

            @Override
            public void onEvent(int eventType, Bundle params) { }
        });

        ImageButton recordImageButton = findViewById(R.id.recordImageButton);
        recordImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                findViewById(R.id.stopRecordImageButton).setVisibility(View.VISIBLE);
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
        });

        ImageButton stopRecordImageButton = findViewById(R.id.stopRecordImageButton);
        stopRecordImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                findViewById(R.id.recordImageButton).setVisibility(View.VISIBLE);
                mSpeechRecognizer.stopListening();
            }
        });
    }
}
