package example.firstprogram;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.Toast;

public class InAGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_agame);
    }

    public void showError(String error, String errorName) {
        AlertDialog.Builder invalidInput = new AlertDialog.Builder(this);
        invalidInput.setMessage(error);
        invalidInput.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setTitle(errorName)
        .create();
        invalidInput.show();
    }

    public void parseInput(View view) {
        int cardCopies = 0;
        int cardsLeft = 0;
        int desiredTurns = 0;
        int currentTurn = 0;
        boolean inputValid = true;
        EditText edit = (EditText) findViewById(R.id.CardCopies);
        if(edit.getText().toString().equals("")) {
            cardCopies = 0;
        } else {
            cardCopies = Integer.parseInt(edit.getText().toString());
        }
        edit = (EditText) findViewById(R.id.CardsLeft);
        if(edit.getText().toString().equals("")) {
            cardsLeft = 0;
        } else {
            cardsLeft = Integer.parseInt(edit.getText().toString());
        }
        edit = (EditText) findViewById(R.id.DesiredTurns);
        if(edit.getText().toString().equals("")) {
            desiredTurns = 0;
        } else {
            desiredTurns = Integer.parseInt(edit.getText().toString());
        }
        edit = (EditText) findViewById(R.id.CurrentTurn);
        if(edit.getText().toString().equals("")) {
            currentTurn = 0;
        } else {
            currentTurn = Integer.parseInt(edit.getText().toString());
        }
        if(cardCopies <= 0 || cardsLeft <= 0 || desiredTurns <= 0 || currentTurn <= 0) {
            inputValid = false;
            showError("You haven't filled out all the info yet!", "No Input");
        }
        else if(cardCopies > cardsLeft) {
            inputValid = false;
            showError("You have more card copies than cards left!", "Invalid Input");
        }
        else if(desiredTurns > cardsLeft) {
            inputValid = false;
            showError("You would run out of cards by the desired turn!", "Invalid Input");
        }
        else if(cardCopies == cardsLeft) {
            inputValid = false;
            showError("Obviously your probability is 100%...", "What...");
        }
        if (inputValid) {
            Bundle bundle = new Bundle();
            bundle.putInt("cardCopies", cardCopies);
            bundle.putInt("cardsLeft", cardsLeft);
            bundle.putInt("desiredTurns", desiredTurns);
            bundle.putInt("currentTurn", currentTurn);
            Intent ingame = new Intent(this, InAGameProbability.class);
            ingame.putExtras(bundle);
            startActivity(ingame);
        }
    }
}
