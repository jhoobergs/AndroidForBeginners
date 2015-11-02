package be.jhoobergs.mathgame.Classes;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import be.jhoobergs.mathgame.R;

/**
 * Created by jesse on 31/10/2015.
 */
public class Game{
    private Context context;

    private int[] MINVALUES = {0, -10,-15,-20,-25,-30,-35,-40,-45,-50,-55,-60};
    private int[] MAXVALUES = {10, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};

    private List<Button> pointsButtonList = new ArrayList<Button>();
    private Button buttonAnswer1, buttonAnswer2, buttonAnswer3;

    private TextView textViewQuestion, textViewTimer;
    private int answeredRight = 0;
    private boolean gameBuzy = true;
    private boolean questionOld = true;
    private Question questionNow;
    private onWinOrLoseInterface winListener;

    private int level = 0;
    private Handler customHandler = new Handler();
    private long endTime;
    private long updatedTime = 0L;

    public void setOnWinListener(onWinOrLoseInterface listener){
        this.winListener = listener;
    }

    public Game(Context context){
        this.context = context;
    }

    public void restart(){
        answeredRight = 0;
        level = 0;
        gameBuzy = true;
        questionOld = true;
        setUpNewLevel();
    }

    public void clearPointsButtons(){
        pointsButtonList.clear();
    }

    public void addPointsButton(Button button){
        pointsButtonList.add(button);
    }

    public void setTimerTextView(TextView textView){
        textViewTimer = textView;
    }

    public void setQuestionTextView(TextView textView){
        textViewQuestion = textView;
    }

    public void setAnswerButtons(Button button1, Button button2, Button button3){
        buttonAnswer1 = button1;
        buttonAnswer2 = button2;
        buttonAnswer3 = button3;
    }

    public void start(){
        if(pointsButtonList.size() != 10 || textViewTimer == null || buttonAnswer1 == null || buttonAnswer2 == null
                || buttonAnswer3 == null || textViewQuestion==null){
            //error
        }
        else {
            setUpNewLevel();
            //setOnAnswerButtonClicks();
        }
    }

    private void setUpNewLevel(){
        changeAnswerButtonsState(true);
        setUpTimer();
        setTotalView();
    }

    private void setUpTimer(){
        if(questionOld)
            endTime = SystemClock.uptimeMillis() + 30*1000;
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private void setAllQuestionViews() {
        buttonAnswer1.setText(questionNow.answers.get(0));
        buttonAnswer2.setText(questionNow.answers.get(1));
        buttonAnswer3.setText(questionNow.answers.get(2));
        textViewQuestion.setText(questionNow.question);
    }
    private void setOnAnswerButtonClicks() {
        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfRightAnswered(buttonAnswer1.getText().toString());
            }
        });
        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfRightAnswered(buttonAnswer2.getText().toString());
            }
        });
        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfRightAnswered(buttonAnswer3.getText().toString());
            }
        });
    }


    private void setTotalView(){
        setAllPointsButtonsToState();
        if(gameBuzy) {
            if(questionOld) {
                questionNow = generateQuestion();
                questionOld = false;
            }
            setAllQuestionViews();
        }
        else{
            if(updatedTime > 0){
                if(winListener!=null)
                    winListener.onWin();
                else{
                    Toast.makeText(context, "Winning not implemented", Toast.LENGTH_LONG).show();
                }
                customHandler.postDelayed(waitForNewLevel, 2000); //Wacht 2 seconden om waitforNewLevel() uit te voeren.
            }
            changeAnswerButtonsState(false);
        }
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            updatedTime = endTime - SystemClock.uptimeMillis();
            if (updatedTime < 0)
                updatedTime = -1;
            int secs = (int) (updatedTime / 1000);
            int milliseconds = (int) (updatedTime % 1000);
            textViewTimer.setText(String.format("%02d", secs) + ":" + String.format("%02d", milliseconds / 10));
            if(updatedTime !=-1 && answeredRight < 10)
                customHandler.postDelayed(this, 0);
            else {
                changeAnswerButtonsState(false);
                gameBuzy = false;
                if(answeredRight < 10) {
                    if(winListener!=null)
                        winListener.onLose();
                    else{
                        Toast.makeText(context, "Losing not implemented", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void changeAnswerButtonsState(boolean enabled) {
        buttonAnswer1.setEnabled(enabled);
        buttonAnswer2.setEnabled(enabled);
        buttonAnswer3.setEnabled(enabled);
    }

    private void setAllPointsButtonsToState() {
        int bottomButtons = 0;
        for (Button button : pointsButtonList){
            if(bottomButtons == answeredRight) {
                button.setBackgroundResource(android.R.color.holo_orange_light);
                button.setAlpha(1);
            }
            else if(bottomButtons < answeredRight){
                button.setBackgroundResource(android.R.color.holo_green_dark);
                button.setAlpha(1);
            }
            else {
                button.setBackgroundResource(android.R.color.white);
                button.setAlpha(0.2F);
            }
            bottomButtons +=1;
        }
    }

    private Question generateQuestion(){
        Question question = new Question();
        int max = MAXVALUES[level];
        int min = MINVALUES[level];

        Random random = new Random();
        int first = random.nextInt(max+Math.abs(min)) - Math.abs(min);
        int second = random.nextInt(max+Math.abs(min)) - Math.abs(min);
        int randomAddFirst = random.nextInt(3) + 1;
        int randomSubstractFirst = random.nextInt(3) + 1;
        int randomAddSecond = random.nextInt(3) + 1;
        int randomSubstractSecond = random.nextInt(3) + 1;
        int solution = first*second;
        String operator = "*";
        question.question = String.format("%s %s %s", first, operator, second);
        question.answers = new ArrayList<String>();
        question.answers.add(0, String.valueOf(solution));
        question.answers.add(1, String.valueOf((first + randomAddFirst) * (second - randomSubstractFirst)));
        question.answers.add(2, String.valueOf((first + randomAddSecond) * (second - randomSubstractSecond)));
        Collections.shuffle(question.answers);
        question.rightAnswer = solution;

        return question;
    }

    public void checkIfRightAnswered(String string){
        int value = Integer.parseInt(string);
        if(updatedTime !=0) {
            if (value == questionNow.rightAnswer) {
                answeredRight += 1;
                if (answeredRight == 10)
                    gameBuzy = false;
            } else {
                answeredRight -= 1;
                if (answeredRight < 0)
                    answeredRight = 0;
            }
        }
        else{
            gameBuzy = false;
        }
        questionOld = true;
        setTotalView();
    }

    private Runnable waitForNewLevel = new Runnable() {
        public void run() {
            level+=1;
            gameBuzy = true;
            answeredRight = 0;
            setUpNewLevel();
        }
    };

    public void destroy(){
        customHandler.removeCallbacks(updateTimerThread);
        customHandler.removeCallbacks(waitForNewLevel);
    }

    public int getLevel(){
        return level;
    }
}

