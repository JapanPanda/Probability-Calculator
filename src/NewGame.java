package example.firstprogram;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Character.isDigit;

public class NewGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showDialog(final int desiredTurns, final double probabilityofDrawing,
                           final int deckSize, final int cardCopies) {
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        final Intent realtime = new Intent(this, InAGameProbability.class);
        final Bundle bundle = new Bundle();
        bundle.putDouble("probabilityofDrawing", probabilityofDrawing * 100);
        bundle.putInt("cardsLeft", deckSize);
        bundle.putInt("desiredTurns", 1);
        bundle.putInt("currentTurn", desiredTurns);
        bundle.putInt("cardCopies", cardCopies);
        bundle.putBoolean("fromNewGame", true);
        prompt.setMessage("How many copies did you draw throughout your turns?");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        prompt.setView(input);
        prompt.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String copiesDrawn = "";
                Boolean validInput = true;
                copiesDrawn = input.getText().toString();
                if(Integer.parseInt(copiesDrawn) > cardCopies) {
                    showToast("You can't draw more copies than you have.");
                    validInput = false;
                    showDialog(desiredTurns, probabilityofDrawing, deckSize, cardCopies);
                }
                if(validInput) {
                    bundle.putInt("copiesDrawn", Integer.parseInt(copiesDrawn));
                    realtime.putExtras(bundle);
                    startActivity(realtime);
                    dialog.dismiss();
                }
            }
        });
        prompt.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        prompt.setTitle("Copies Drawn");
        prompt.create();
        prompt.show();
    }

    public void printResults(final int desiredTurns, final int cardsMulliganned, final double probabilityofDrawing,
                             final int deckSize, final int cardCopies) {
        AlertDialog.Builder results = new AlertDialog.Builder(this);
        String displayProbability = Double.toString(probabilityofDrawing * 100);
        final Intent mainmenu = new Intent(this, MainActivity.class);
        results.setMessage("The probability of drawing at least one of the cards by turn " +
                desiredTurns + " given you mulliganned " + cardsMulliganned + " cards is " +
                displayProbability.substring(0,5) + "%");
        results.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        results.setNeutralButton("Start Realtime", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog(desiredTurns, probabilityofDrawing, deckSize, cardCopies);
                dialog.dismiss();
            }
        });
        results.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(mainmenu);
            }
        })
        .setTitle("Finished Calculating")
        .create();
        results.show();
    }

    public void calculateProbability(int cardCopies, int deckSize, int desiredTurns,
                                     int cardsMulliganned, int initialHandSize) {
        double probabilityOfNotDrawing = 0;
        double probabilityOfDrawing = 0;
        double probabilityInitialDraw = 1;
        double probabilityMulligan = 1;
        double probabilityDrawPhase = 1;
        int cardsLeftAfterMulligan = deckSize - initialHandSize;
        for(int i = 0; i < initialHandSize; i++) {  // Uses a for loop for the first initial draw phase
            if(i != 0) {
                deckSize--;  // First iteration should not have a card taken out
            }
            probabilityInitialDraw *= (double)(deckSize - cardCopies) / deckSize;  // Calculates probability of not drawing
        }
        for(int j = 0; j < cardsMulliganned; j++) {  // Uses a for loop for mulligan phase
            deckSize--;
            probabilityMulligan *= (double)(deckSize - cardCopies) / deckSize;
        }
        deckSize = cardsLeftAfterMulligan;  // Resets number of cards after mulliganed cards are shuffled back
        for(int z = 0; z < desiredTurns; z++) {  // Uses a for loop for the drawing phase
            if(z != 0) {
                deckSize--;  // First iteration should not have a card taken out
            }
            probabilityDrawPhase *= (double)(deckSize - cardCopies) / deckSize;
        }
        // Multiplies all the probabilities to get the total probability of not drawing
        probabilityOfNotDrawing = probabilityInitialDraw * probabilityMulligan * probabilityDrawPhase;
        probabilityOfDrawing = 1 - probabilityOfNotDrawing;  // Subtracts from 1 to get probability of drawing
        printResults(desiredTurns, cardsMulliganned, probabilityOfDrawing, deckSize, cardCopies);  // Calls upon function to print results
    }

    public void parseInput(View view) {
        int cardCopies = 0;
        int deckSize = 0;
        int cardsMulliganned = 0;
        int initialHandSize = 0;
        int desiredTurns = 0;
        boolean inputValid = true;
        EditText edit = (EditText) findViewById(R.id.CardCopies);
        if(edit.getText().toString().equals("")) {
            cardCopies = -99;
        } else {
            if(edit.getText().toString().length() > 9) {
                showError("Too large of a value!", "Bigger Than Max Value");
            } else {
                cardCopies = Integer.parseInt(edit.getText().toString());
            }
        }
        edit = (EditText) findViewById(R.id.DeckSize);
        if(edit.getText().toString().equals("")) {
            deckSize = -99;
        } else {
            if(edit.getText().toString().length() > 9) {
                showError("Too large of a value!", "Bigger Than Max Value");
            } else {
                deckSize = Integer.parseInt(edit.getText().toString());
            }
        }
        edit = (EditText) findViewById(R.id.CardsMulliganned);
        if(edit.getText().toString().equals("")) {
            cardsMulliganned = -99;
        } else {
            if(edit.getText().toString().length() > 9) {
                showError("Too large of a value!", "Bigger Than Max Value");
            } else {
                cardsMulliganned = Integer.parseInt(edit.getText().toString());
            }
        }
        edit = (EditText) findViewById(R.id.DesiredTurns);
        if(edit.getText().toString().equals("")) {
            desiredTurns = -99;
        } else {
            if(edit.getText().toString().length() > 9) {
                showError("Too large of a value!", "Bigger Than Max Value");
            } else {
                desiredTurns = Integer.parseInt(edit.getText().toString());
            }
        }
        edit = (EditText) findViewById(R.id.InitialHandSize);
        if(edit.getText().toString().equals("")) {
            initialHandSize = -99;
        } else {
            if(edit.getText().toString().length() > 9) {
                showError("Too large of a value!", "Bigger Than Max Value");
            } else {
                initialHandSize = Integer.parseInt(edit.getText().toString());
            }
        }
        if(cardCopies <= 0 || deckSize <= 0 || desiredTurns <= 0 || cardsMulliganned < 0 ||
                initialHandSize == 0) {
            inputValid = false;
            if(cardCopies == 0 || deckSize == 0 || desiredTurns == 0 || initialHandSize == 0) {
                showError("You can't enter in 0 for those values!", "Invalid Input");
            }
            else {
                showError("You haven't filled out all the info yet!", "No Input");
            }
        }
        else if(cardCopies > deckSize) {
            inputValid = false;
            showError("You have more card copies than your deck size!", "Invalid Input");
        }
        else if(desiredTurns >= deckSize) {
            inputValid = false;
            showError("You would run out of cards by the desired turn!", "Invalid Input");
        }
        else if(cardCopies == deckSize) {
            inputValid = false;
            showError("Well obviously your probability is 100% with that deck...", "What...");
        }
        else if(cardsMulliganned >= initialHandSize) {
            inputValid = false;
            showError("You can't mulligan more cards than your initial hand size would have.",
                    "Invalid Input");
        }
        else if(initialHandSize >= deckSize) {
            inputValid = false;
            showError("You can't draw more cards than you have.", "Invalid Input");
        }
        else if(cardsMulliganned > deckSize) {
            inputValid = false;
            showError("You can't mulligan more cards than your deck size.", "Invalid Input");
        }
        if (inputValid) {
            calculateProbability(cardCopies, deckSize, desiredTurns, cardsMulliganned, initialHandSize);
        }
    }
}
