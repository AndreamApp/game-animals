package app.andream.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Andream on 2019/3/4.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int result = getIntent().getIntExtra("result", Game.GAME_RESULT_GREAT);
        if (Game.GAME_RESULT_GREAT == result) {
            setContentView(R.layout.activity_result_great);
        } else {
            setContentView(R.layout.activity_result_over);
        }
    }

}
