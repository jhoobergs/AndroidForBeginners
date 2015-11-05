package be.jhoobergs.mathgame;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.jhoobergs.mathgame.Classes.Game;
import be.jhoobergs.mathgame.Classes.onWinOrLoseInterface;

/**
 * Created by jesse on 4/11/2015.
 */
public class GameActivity extends AppCompatActivity {
    public Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        game = new Game(this);

        game.addPointsButton((Button) findViewById(R.id.buttonPoints0));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints1));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints2));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints3));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints4));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints5));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints6));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints7));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints8));
        game.addPointsButton((Button) findViewById(R.id.buttonPoints9));

        final Button buttonAnswer1 = (Button) findViewById(R.id.buttonAnswer1);
        final Button buttonAnswer2 = (Button) findViewById(R.id.buttonAnswer2);
        final Button buttonAnswer3 = (Button) findViewById(R.id.buttonAnswer3);
        game.setAnswerButtons(buttonAnswer1, buttonAnswer2, buttonAnswer3);

        game.setTimerTextView((TextView) findViewById(R.id.textViewTimer));

        game.setQuestionTextView((TextView) findViewById(R.id.textViewQuestion));

        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.checkIfRightAnswered(buttonAnswer1.getText().toString());
            }
        });
        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.checkIfRightAnswered(buttonAnswer2.getText().toString());
            }
        });

        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.checkIfRightAnswered(buttonAnswer3.getText().toString());
            }
        });

        game.setOnWinListener(new onWinOrLoseInterface() {
            @Override
            public void onWin() {
                //User wins
                Toast.makeText(getApplicationContext(), "Next level!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLose() {
                //User loses
                Toast.makeText(getApplicationContext(), String.format("You lose, you reached level %d", game.getLevel()+1), Toast.LENGTH_LONG).show();
            }
        });

        game.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                game.restart();
                Toast.makeText(this, "Game restarted!", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
