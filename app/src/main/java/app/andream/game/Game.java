package app.andream.game;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andream on 2019/3/4.
 * Email: andreamapp@qq.com
 * Website: http://andreamapp.com
 */
public class Game {

    public static final String TAG = "Game";

    // the total round times of game, default is 10
    private int round;
    // specify current round
    private int currRound;
    private int score;

    // provided options of certain round
    private Animal[] animalOptions;
    // provided answer of certain round
    private Animal animalTarget;

    // the limit time of each round, in milliseconds
    private long roundDuration;
    private long roundStartTime;
    private Listener listener;
    private Handler handler = new Handler();
    private Runnable progressCallback = new Runnable() {
        @Override
        public void run() {
            long remained = roundDuration - (System.currentTimeMillis() - roundStartTime);
            setRoundRemain((int) remained);
            handler.postDelayed(progressCallback, 20);
        }
    };

    private boolean cancled;

    public static final int GAME_RESULT_GREAT = 1;
    public static final int GAME_RESULT_OVER = 2;


    public Game(int round, long roundDuration) {
        this.round = round;
        this.roundDuration = roundDuration;
    }

    public Game() {
        this(10, 3000);
    }

    public void start() {
        currRound = 0;
        score = 0;
        Log.i(TAG, "game started");
        nextRound();
    }

    public void stop() {
        // stop round timer
        Log.i(TAG, "game stoped. score = " + score);
        handler.removeCallbacks(progressCallback);
        if(listener != null) {
            int threshScore = round * 3;
            listener.onGameEnded(score >= threshScore ? GAME_RESULT_GREAT : GAME_RESULT_OVER);
        }
    }

    public void cancle() {
        // stop round timer
        Log.i(TAG, "game cancled");
        cancled = true;
        handler.removeCallbacks(progressCallback);
        listener = null;
    }

    public boolean selectAnimal(int index) {
        Log.i(TAG, "game selected " + index);
        boolean correct = index == animalTarget.index;
        if(correct) {
            score += 5;
            if(listener != null)
                listener.onScoreChanged(score - 5, score);
        }
        // stop round timer
        handler.removeCallbacks(progressCallback);
        return correct;
    }

    public boolean nextRound() {
        if(currRound >= round) {
            stop();
            cancle();
            return false;
        }
        Random random = new Random(System.currentTimeMillis());
        Animal[] options = new Animal[4];
        int seed = random.nextInt(1000);
        for(int i = 0; i < options.length; i++) {
            options[i] = Animal.generate(seed + i);
            options[i].index = i;
        }
        animalOptions = options;
        animalTarget = options[random.nextInt(4)];

        currRound++;
        if(listener != null) {
            listener.onOptionsChanged(animalOptions, animalTarget, currRound, round);
        }
        Log.i(TAG, "game round " + currRound);

//        timer
        roundStartTime = System.currentTimeMillis();
        handler.removeCallbacks(progressCallback);
        handler.post(progressCallback);
        return true;
    }

    public void setRoundRemain(int remained) {
        if(remained > 0) {
            if(listener != null) {
                listener.onRoundRemainTimeChanged(remained, roundDuration);
            }
        }
        else if(!cancled){
            Log.i(TAG, "game round expired");
            nextRound();
        }
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Animal[] getAnimalOptions() {
        return animalOptions;
    }

    public interface Listener {
        void onOptionsChanged(Animal[] options, Animal answer, int currRound, int totalRound);
        void onScoreChanged(int oldScore, int newScore);
        void onRoundRemainTimeChanged(long remained, long total);
        void onGameEnded(int result);
    }

    public static class Animal {
        public String name;
        public int resId;
        public int resIdActive;
        public int resIdInactive;
        public int index;

        // TODO: Add animals
        public static final String[] ANIMAL_NAMES = {
                "bird", "cat", "fish", "honey", "house", "pig", "sun"
        };

        /**
         * get resource id by string name
         * @param resName resource name
         * @param c class of resource, eg. R.drawable.class
         * @return res id
         */
        private static int getResId(String resName, Class<?> c) {
            try {
                Field idField = c.getDeclaredField(resName);
                return idField.getInt(idField);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }

        public static Animal generate(int seed) {
            Animal animal = new Animal();
            animal.name = ANIMAL_NAMES[seed % ANIMAL_NAMES.length];
            animal.resId = getResId("animal_" + animal.name, R.mipmap.class);
            animal.resIdActive = getResId("animal_" + animal.name + "_active", R.mipmap.class);
            animal.resIdInactive = getResId("animal_" + animal.name + "_inactive", R.mipmap.class);
            return animal;
        }
    }
}
