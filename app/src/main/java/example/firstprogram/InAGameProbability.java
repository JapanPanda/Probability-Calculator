package example.firstprogram;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static java.lang.Character.isDigit;

public class InAGameProbability extends AppCompatActivity {
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_agame_probability);
        setTitle("In Game Probability Calculator");
        /*mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D0B62C9BE29FE8D450B1480C57431598")
                .build();
        mAdView.loadAd(adRequest);*/
    }

    protected void onResume() {
        super.onResume();
        initialcalculateProbability();
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

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();;
    }

    public void showApply(View view) {
        TextView probabilityWindow = (TextView) findViewById(R.id.Probability);
        TextView copiesLeftWindow = (TextView) findViewById(R.id.CopiesLeft);
        TextView modifiedturnsLeftWindow = (TextView) findViewById(R.id.NewTurn);
        TextView cardsLeftWindow = (TextView) findViewById(R.id.CardsLeft);
        TextView turnsLeftWindow = (TextView) findViewById(R.id.DesiredTurns);
        int cardCopies = Integer.parseInt(truncateString(copiesLeftWindow.getText().toString()));
        int turnsLeft = Integer.parseInt(truncateString(modifiedturnsLeftWindow.getText().toString()));
        int cardsLeft = Integer.parseInt(truncateString(cardsLeftWindow.getText().toString()));
        if(turnsLeft > cardsLeft) {
            showError("You would run out of cards by the desired turn!", "Insufficient cards");
        } else {
            Toast.makeText(this, "New Turn Applied", Toast.LENGTH_LONG).show();
            double probabilityofDrawingCard = 0;
            double probabilityofNotDrawingCard = 1;
            for (int i = 0; i < turnsLeft; i++) {
                cardsLeft--;
                probabilityofNotDrawingCard *= (double) (cardsLeft - cardCopies) / cardsLeft;
                if (Double.isNaN(probabilityofNotDrawingCard)) {
                    break;
                }
            }
            probabilityofDrawingCard = 1 - probabilityofNotDrawingCard;
            String finalProbability = Double.toString(100 * probabilityofDrawingCard);
            String displayfinalProbability = "Probability: " + finalProbability.substring(0, 5) + "%";
            String displayTurnsLeft = "Turns until desired turn: " + Integer.toString(turnsLeft);
            turnsLeftWindow.setText(displayTurnsLeft);
            probabilityWindow.setText(displayfinalProbability);
        }
    }

    public void showHelp(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("This is the in game probability calculator.\n\n" +
                "The program will display a realtime probability on how probable you drawing a card"
                + "by a certain turn will be.\n\nClick Copy if you drew one of the desired card's " +
                "copies or Not A Copy if you haven't.");
        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setTitle("Help")
        .create();
        alert.show();
    }

    public void startANewGame(int scenario) {
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        final Intent newGame = new Intent(this, NewGame.class);
        final Intent mainMenu = new Intent(this, MainActivity.class);
        if(scenario == 0) {
            prompt.setMessage("No turns left! Would you like to start a new game?");
        }
        else if(scenario == 1) {
            prompt.setMessage("No cards left! Would you like to start a new game?");
        }
        else if(scenario == 2) {
            prompt.setMessage("No copies left! Would you like to start a new game?");
        }
        prompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(newGame);
            }
        });
        prompt.setNeutralButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        prompt.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(mainMenu);
            }
        })
        .setTitle("New Game?")
        .create();
        prompt.show();

    }
    public void drawCopy(View view) {
        Toast.makeText(this, "Drew a copy", Toast.LENGTH_LONG);
        updateProbability(true);
    }

    public void didnotdrawCopy(View view) {
        Toast.makeText(this, "Did not draw a copy", Toast.LENGTH_LONG);
        updateProbability(false);
    }

    public String truncateString(String string) {
        String truncatedString = "";
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == '-') {
                truncatedString += string.charAt(i);
            }
            else if(isDigit(string.charAt(i))) {

                truncatedString += string.charAt(i);
            }
        }
        return truncatedString;
    }

    public void updateProbability(boolean drawACopy) {
        TextView probabilityWindow = (TextView) findViewById(R.id.Probability);
        TextView currentTurnWindow = (TextView) findViewById(R.id.CurrentTurn);
        TextView copiesLeftWindow = (TextView) findViewById(R.id.CopiesLeft);
        TextView turnsLeftWindow = (TextView) findViewById(R.id.DesiredTurns);
        TextView cardsLeftWindow = (TextView) findViewById(R.id.CardsLeft);
        int cardCopies = Integer.parseInt(truncateString(copiesLeftWindow.getText().toString()));
        int currentTurn = Integer.parseInt(truncateString(currentTurnWindow.getText().toString()));
        int turnsLeft = Integer.parseInt(truncateString(turnsLeftWindow.getText().toString()));
        int cardsLeft = Integer.parseInt(truncateString(cardsLeftWindow.getText().toString()));
        int copyofCardsLeft = Integer.parseInt(truncateString(cardsLeftWindow.getText().toString()));
        double probabilityofNotDrawing = 1;
        double probabilityofDrawing = 0;
        Boolean noProbability = false;
        if(turnsLeft == 2) {
            showToast("Last turn!");
        }
        else if(turnsLeft <= 0) {
            startANewGame(0);
        }
        if(cardsLeft <= 0) {
            startANewGame(1);
        }
        if(cardCopies <= 0) {
            startANewGame(2);
        }
        else if(cardCopies == 2) {
            if(drawACopy) {
                showToast("Last copy!");
            }
        }
        else if(cardCopies == 1) {
            if (drawACopy) {
                turnsLeft--;
                cardCopies = 0;
                probabilityWindow.setText("Probability: 0.00%");
            }
        }
        if(turnsLeft != 0 && cardsLeft != 0 && cardCopies > 1) {
            if (drawACopy) {
                cardCopies--;
            }
            cardsLeft--;
            copyofCardsLeft--;
            turnsLeft--;
            currentTurn++;
            for (int i = 0; i < turnsLeft; i++) {
                cardsLeft--;
                probabilityofNotDrawing *= (double) (cardsLeft - cardCopies) / cardsLeft;
            }
            probabilityofDrawing = 1 - probabilityofNotDrawing;
        }
        String finaldisplayProbability = Double.toString(probabilityofDrawing * 100);
        String displayCurrentTurn = "Current Turn: " + Integer.toString(currentTurn);
        String displayCopiesLeft = "Copies Left: " + Integer.toString(cardCopies);
        String displayTurnsLeft = "Turns until desired turn: " + Integer.toString(turnsLeft);
        String displayCardsLeft = "Cards Left: " + Integer.toString(copyofCardsLeft);
        if(turnsLeft != 0) {
            probabilityWindow.setText("Probability: " + finaldisplayProbability.substring(0, 5) + "%");
        }
        if(cardCopies == 0) {
            probabilityWindow.setText("Probability: 0%");
        }
        copiesLeftWindow.setText(displayCopiesLeft);
        currentTurnWindow.setText(displayCurrentTurn);
        turnsLeftWindow.setText(displayTurnsLeft);
        cardsLeftWindow.setText(displayCardsLeft);
        }




    public void initialcalculateProbability() {
        Bundle bundle = getIntent().getExtras();
        TextView probabilityWindow = (TextView) findViewById(R.id.Probability);
        TextView currentTurnWindow = (TextView) findViewById(R.id.CurrentTurn);
        TextView copiesLeftWindow = (TextView) findViewById(R.id.CopiesLeft);
        TextView turnsLeftWindow = (TextView) findViewById(R.id.DesiredTurns);
        TextView cardsLeftWindow = (TextView) findViewById(R.id.CardsLeft);
        int cardCopies = bundle.getInt("cardCopies");
        int cardsLeft = bundle.getInt("cardsLeft");
        int desiredTurns = bundle.getInt("desiredTurns");
        int currentTurn = bundle.getInt("currentTurn");
        double probabilityofDrawing = 0;
        double probabilityofNotDrawing = 1;
        int copyofCardsLeft = cardsLeft;
        boolean newGame = bundle.getBoolean("fromNewGame");
        if(newGame) {
            int copiesDrawn = bundle.getInt("copiesDrawn");
            cardCopies -= copiesDrawn;
            String displayProbability = "Probability: 0% (Enter new desired turn)";
            probabilityWindow.setText(displayProbability);
            String displayCurrentTurn = "Current Turn: " + Integer.toString(currentTurn);
            String displayCopiesLeft = "Copies Left: " + Integer.toString(cardCopies);
            String displayCardsLeft = "Cards Left: " + Integer.toString(cardsLeft);
            String displayTurnsLeft = "Turns until desired turn: " + Integer.toString(desiredTurns);
            currentTurnWindow.setText(displayCurrentTurn);
            copiesLeftWindow.setText(displayCopiesLeft);
            turnsLeftWindow.setText(displayTurnsLeft);
            cardsLeftWindow.setText(displayCardsLeft);
            newGame = false;
        }
        else if(!newGame) {
            String displayProbability = "";
            String displayCurrentTurn = "Current Turn: " + Integer.toString(currentTurn);
            String displayCopiesLeft = "Copies Left: " + Integer.toString(cardCopies);
            String displayDesiredTurns = "Turns until desired turn: " + Integer.toString(desiredTurns);
            String displayCardsLeft = "Cards Left: " + Integer.toString(copyofCardsLeft);
            // Calculate initial probability
            for (int i = 0; i < desiredTurns; i++) {
                cardsLeft--;
                probabilityofNotDrawing *= (double) (cardsLeft - cardCopies) / cardsLeft;
                if (Double.isNaN(probabilityofNotDrawing)) {
                    break;
                }
            }
            probabilityofDrawing = 1 - probabilityofNotDrawing;
            displayProbability = String.valueOf(probabilityofDrawing * 100);
            String finaldisplayProbability = "Probability: " + displayProbability.substring(0, 5) + "%";
            probabilityWindow.setText(finaldisplayProbability);
            currentTurnWindow.setText(displayCurrentTurn);
            copiesLeftWindow.setText(displayCopiesLeft);
            turnsLeftWindow.setText(displayDesiredTurns);
            cardsLeftWindow.setText(displayCardsLeft);
        }
    }
}
