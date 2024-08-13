package com.example.myxogame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class AddPlayersActivity extends AppCompatActivity {
    EditText playerOne, playerTwo;
    Button startGameButton;
    private Chip chipPlayWithComputer;
    private Chip chipPlayWithFriend;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_players);
        initView();

         // Default selection setup
//        chipPlayWithComputer.setChecked(true); // Default the "Play with Player" option
//
//        // Handle ChipGroup selection changes
//        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
//            if (checkedId == R.id.PlayWithComputer) {
//                // Handle selection for "Play with Player"
//                chipPlayWithFriend.setChecked(false);
//            } else if (checkedId == R.id.PlayWithFriend) {
//                // Handle selection for "Play with Friend"
//                chipPlayWithComputer.setChecked(false);
//            }
//        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerOneName = playerOne.getText().toString();
                String playerTwoName = playerTwo.getText().toString();

                if (playerOneName.isEmpty() || playerTwoName.isEmpty()) {
                    // Show an AlertDialog if either player name is empty
                    new AlertDialog.Builder(AddPlayersActivity.this)
                            .setTitle("Missing Information")
                            .setMessage("Please enter both player names to start the game.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Close the dialog
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }

                int selectedChipId = chipGroup.getCheckedChipId();
                if (selectedChipId == -1) {
                    // Show an AlertDialog if no game mode is selected
                    new AlertDialog.Builder(AddPlayersActivity.this)
                            .setTitle("Missing Selection")
                            .setMessage("Please select a game mode to start the game.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Close the dialog
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }

                // Determine game mode based on selected chip
                String gameMode;
                if (selectedChipId == R.id.PlayWithComputer) {
                    gameMode = "computer";
                    playerTwoName = "Computer"; // Set player two name as "Computer" if playing with the computer
                } else {
                    gameMode = "friend";
                }

                // Start the main activity with the selected game mode
                Intent intent = new Intent(AddPlayersActivity.this, MainActivity.class);
                intent.putExtra("playerOne", playerOneName);
                intent.putExtra("playerTwo", playerTwoName);
                intent.putExtra("gameMode", gameMode);
                startActivity(intent);
            }
        });



    }

    public void initView() {
        playerOne = findViewById(R.id.playerOne);
        playerTwo = findViewById(R.id.playerTwo);
        startGameButton = findViewById(R.id.startGameButton);
        chipPlayWithComputer = findViewById(R.id.PlayWithComputer);
        chipPlayWithFriend = findViewById(R.id.PlayWithFriend);
        chipGroup = findViewById(R.id.chipGroupMode);
    }
}