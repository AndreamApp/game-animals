package app.andream.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by Andream on 2019/3/4.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class SettingsActivity extends AppCompatActivity {

    private EditText roundEditor, durationEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        roundEditor = findViewById(R.id.et_round);
        durationEditor = findViewById(R.id.et_duration);
    }

}
