package com.example.myxogame;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class AddPlayersActivity extends AppCompatActivity {
    EditText playerOne, playerTwo;
    Button startGameButton;
    private Switch soundSwitch;
    private SharedPreferences preferences;
    private Chip chipPlayWithComputer;
    private Chip chipPlayWithFriend;
    private ChipGroup chipGroup, chipGroupDifficulty;

    ImageButton musicBackgroundBtn, languagesBtn,highScoreBtn;
    boolean isSoundOn,isClickSoundOn;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_players);
        initView();
        setupSoundSwitch();



        highScoreBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddPlayersActivity.this, HighScoreActivityActivity.class);
                        startActivity(intent);
                    }
                }
        );

        mediaPlayer = MediaPlayer.create(this, R.raw.game_start);

        isSoundOn = false;
        musicBackgroundBtn.setImageResource(R.drawable.volume_off); // Default icon (sound off)

        musicBackgroundBtn.setOnClickListener(v -> {
            if (isSoundOn) {
                musicBackgroundBtn.setImageResource(R.drawable.volume_off);
                MusicManager.stopMusic();
            } else {
                musicBackgroundBtn.setImageResource(R.drawable.sound_on);
                MusicManager.playMusic(AddPlayersActivity.this);
            }
            isSoundOn = !isSoundOn;
        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerOneName = playerOne.getText().toString();
                String playerTwoName = playerTwo.getText().toString();

                if (playerOneName.isEmpty() || playerTwoName.isEmpty()) {
                    new AlertDialog.Builder(AddPlayersActivity.this)
                            .setTitle(R.string.missing_information)
                            .setMessage(R.string.please_enter_both_player_names_to_start_the_game +"")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }

                int selectedChipId = chipGroup.getCheckedChipId();
                if (selectedChipId == -1) {
                    new AlertDialog.Builder(AddPlayersActivity.this)
                            .setTitle(R.string.missing_selection)
                            .setMessage(R.string.please_select_a_game_mode_to_start_the_game)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }
                // Start playing music when the game starts
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }




                String gameMode;
                if (selectedChipId == R.id.PlayWithComputer) {
                    gameMode = "computer";
                    playerTwoName = "Computer";
                } else {
                    gameMode = "friend";
                }

                String difficultyLevel = null;
                if (gameMode.equals("computer")) {
                    int selectedDifficultyId = chipGroupDifficulty.getCheckedChipId();
                    if (selectedDifficultyId == -1) {
                        new AlertDialog.Builder(AddPlayersActivity.this)
                                .setTitle(R.string.missing_selection)
                                .setMessage(R.string.please_select_a_difficulty_level_to_start_the_game)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return;
                    }
                    if (selectedDifficultyId == R.id.easy) {
                        difficultyLevel = "easy";
                    } else if (selectedDifficultyId == R.id.medium) {
                        difficultyLevel = "medium";
                    } else if (selectedDifficultyId == R.id.hard) {
                        difficultyLevel = "hard";
                    }
                }

                Intent intent = new Intent(AddPlayersActivity.this, MainActivity.class);
                intent.putExtra("playerOne", playerOneName);
                intent.putExtra("playerTwo", playerTwoName);
                intent.putExtra("gameMode", gameMode);
                intent.putExtra("difficultyLevel", difficultyLevel);
                startActivity(intent);
            }
        });

        languagesBtn.setOnClickListener(v -> showLanguageSelectionDialog());



    }

    private void setupSoundSwitch() {
        soundSwitch = findViewById(R.id.soundSwitch);

        // Load the current sound preference
        SharedPreferences preferences = getSharedPreferences("game_settings", MODE_PRIVATE);
        isClickSoundOn = preferences.getBoolean("sound_on", true);

        soundSwitch.setChecked(isClickSoundOn);
        soundSwitch.setText(isClickSoundOn ? "Sound On" : "Sound Off");

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("sound_on", isChecked);
            editor.apply();
            soundSwitch.setText(isChecked ? "Sound On" : "Sound Off");
        });
    }


    private void showLanguageSelectionDialog() {
        final String[] languages = {"Egypt", "Español", "Français", "China", "Italiano"};
        final String[] languageCodes = {"ar", "es", "ca", "bo", "de"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_a_language);
        builder.setItems(languages, (dialog, which) -> {
            // Set the selected language
            setLocale(languageCodes[which]);
            // Restart activity to apply the language change
            recreate();
        });
        builder.show();
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Optionally, save the selected language in shared preferences for future use
    }

    public void initView() {
        playerOne = findViewById(R.id.playerOne);
        playerTwo = findViewById(R.id.playerTwo);
        startGameButton = findViewById(R.id.startGameButton);
        chipPlayWithComputer = findViewById(R.id.PlayWithComputer);
        chipPlayWithFriend = findViewById(R.id.PlayWithFriend);
        chipGroup = findViewById(R.id.chipGroupMode);
        chipGroupDifficulty = findViewById(R.id.chipGroupDiffcult);
        musicBackgroundBtn = findViewById(R.id.music_background);
        languagesBtn=findViewById(R.id.languages_btn);

        highScoreBtn=findViewById(R.id.high_score_btn);
        soundSwitch = findViewById(R.id.soundSwitch);
        preferences = getSharedPreferences("game_settings", MODE_PRIVATE);

//        musicSwitch = findViewById(R.id.musicSwitch);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        MusicManager.release();
    }
}