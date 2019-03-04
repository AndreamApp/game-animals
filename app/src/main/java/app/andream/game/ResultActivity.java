package app.andream.game;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;

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
        // Scale animation
        final View firework = findViewById(R.id.fireworks);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(2000);
        animator.setInterpolator(new OvershootInterpolator(2));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                float k = anim.getAnimatedFraction();
                firework.setScaleX(k);
                firework.setScaleY(k);
            }
        });
        animator.start();
    }

}
