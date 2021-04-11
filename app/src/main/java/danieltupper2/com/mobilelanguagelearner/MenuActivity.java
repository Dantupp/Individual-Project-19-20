package danieltupper2.com.mobilelanguagelearner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

public class MenuActivity extends AppCompatActivity {
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        settings = AppSettings.getInstance();

        ImageButton closeImageButton = findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        settings.setDifficulty(1);
                        break;
                    case 1:
                        settings.setDifficulty(2);
                        break;
                    case 2:
                        settings.setDifficulty(3);
                        break;
                    case 3:
                        settings.setDifficulty(4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
