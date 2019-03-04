package app.andream.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Andream on 2019/3/4.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class PlayActivity extends AppCompatActivity implements Game.Listener{

    private ImageView[] animalViews;
    private TextView scoreText, remainTimeText;
    private Button hintBtn;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initViews();
        game = new Game(10, 3000);
        game.setListener(this);
        game.start();
    }

    private void initViews() {
        animalViews = new ImageView[4];
        animalViews[0] = findViewById(R.id.animal1);
        animalViews[1] = findViewById(R.id.animal2);
        animalViews[2] = findViewById(R.id.animal3);
        animalViews[3] = findViewById(R.id.animal4);
        hintBtn = findViewById(R.id.hint);
        scoreText = findViewById(R.id.score);
        remainTimeText = findViewById(R.id.remain_time);

        for(int i = 0; i < animalViews.length; i++) {
            final int finalI = i;
            animalViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAnimalClicked(finalI);
                }
            });
        }
    }

    protected void onAnimalClicked(final int pos) {
        boolean result = game.selectAnimal(pos);
        if(result) {
            animalViews[pos].setImageResource(game.getAnimalOptions()[pos].resIdActive);
            animalViews[pos].setScaleX(1.3f);
            animalViews[pos].setScaleY(1.3f);
        }
        else {
            animalViews[pos].setImageResource(game.getAnimalOptions()[pos].resIdInactive);
            animalViews[pos].setScaleX(1.3f);
            animalViews[pos].setScaleY(1.3f);
        }
        animalViews[pos].postDelayed(new Runnable() {
            @Override
            public void run() {
                animalViews[pos].setScaleX(1.0f);
                animalViews[pos].setScaleY(1.0f);
                game.nextRound();
            }
        }, 200);
    }

    public void skip(View view) {
        game.nextRound();
    }

    @Override
    public void onOptionsChanged(Game.Animal[] options, Game.Animal answer, int currRound, int totalRound) {
        for(int i = 0; i < options.length; i++) {
            animalViews[i].setImageResource(options[i].resId);
        }
        hintBtn.setText(answer.name);
    }

    @Override
    public void onScoreChanged(int oldScore, int newScore) {
        scoreText.setText("Score: " + newScore);
    }

    @Override
    public void onRoundRemainTimeChanged(long remained, long total) {
        remainTimeText.setText(String.valueOf(remained));
    }

    @Override
    public void onGameEnded(int result) {
        Intent intent = new Intent(PlayActivity.this, ResultActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        game.stop();
    }
}
