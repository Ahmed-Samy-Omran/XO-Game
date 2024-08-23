package com.example.myxogame;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myxogame.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;
    private boolean playWithComputer = false;
    private String difficultyLevel;
    MediaPlayer mediaPlayerWon, mediaPlayerDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeGameSetup();
        setupClickListeners();
    }

    private void initializeGameSetup() {
        updateScores();

        combinationList.add(new int[]{0, 1, 2});
        combinationList.add(new int[]{3, 4, 5});
        combinationList.add(new int[]{6, 7, 8});
        combinationList.add(new int[]{0, 3, 6});
        combinationList.add(new int[]{1, 4, 7});
        combinationList.add(new int[]{2, 5, 8});
        combinationList.add(new int[]{2, 4, 6});
        combinationList.add(new int[]{0, 4, 8});

        String getPlayerOneName = getIntent().getStringExtra("playerOne");
        String getPlayerTwoName = getIntent().getStringExtra("playerTwo");
        String gameMode = getIntent().getStringExtra("gameMode");
        difficultyLevel = getIntent().getStringExtra("difficultyLevel");

        mediaPlayerWon = MediaPlayer.create(this, R.raw.adelshakel);
        mediaPlayerDraw = MediaPlayer.create(this, R.raw.draw);

        binding.playerOneName.setText(getPlayerOneName != null ? getPlayerOneName : getString(R.string.player_one));
        binding.playerTwoName.setText(getPlayerTwoName != null ? getPlayerTwoName : getString(R.string.player_two));

        if ("computer".equals(gameMode)) {
            playWithComputer = true;
            binding.playerTwoName.setText(R.string.computer);
        }
    }

    private void setupClickListeners() {
        View.OnClickListener boxClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getPositionForView(view);
                if (position != -1 && isBoxSelectable(position)) {
                    performAction((ImageView) view, position);
                }
            }
        };

        binding.image1.setOnClickListener(boxClickListener);
        binding.image2.setOnClickListener(boxClickListener);
        binding.image3.setOnClickListener(boxClickListener);
        binding.image4.setOnClickListener(boxClickListener);
        binding.image5.setOnClickListener(boxClickListener);
        binding.image6.setOnClickListener(boxClickListener);
        binding.image7.setOnClickListener(boxClickListener);
        binding.image8.setOnClickListener(boxClickListener);
        binding.image9.setOnClickListener(boxClickListener);
    }

    private int getPositionForView(View view) {
        int id = view.getId();

        if (id == R.id.image1) return 0;
        else if (id == R.id.image2) return 1;
        else if (id == R.id.image3) return 2;
        else if (id == R.id.image4) return 3;
        else if (id == R.id.image5) return 4;
        else if (id == R.id.image6) return 5;
        else if (id == R.id.image7) return 6;
        else if (id == R.id.image8) return 7;
        else if (id == R.id.image9) return 8;
        else return -1;
    }

    private boolean checkResults() {
        for (int[] combination : combinationList) {
            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                return true;
            }
        }
        return false;
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.ximage);
            handleGameEndScenario(binding.playerOneName.getText().toString(), R.string.is_a_winner, 1);
        } else {
            imageView.setImageResource(R.drawable.oimage);
            handleGameEndScenario(binding.playerTwoName.getText().toString(), R.string.is_a_winner, 2);
        }
    }

    private void handleGameEndScenario(String playerName, int winMessageResId, int currentPlayer) {
        if (checkResults()) {
            if (mediaPlayerWon != null) mediaPlayerWon.start();
            saveScore(playerName, 1);
            updateScores();
            showResultDialog(playerName + " " + getString(winMessageResId));
        } else if (totalSelectedBoxes == 9) {
            if (mediaPlayerDraw != null) mediaPlayerDraw.start();
            showResultDialog(getString(R.string.match_draw));
        } else {
            totalSelectedBoxes++;
            changePlayerTurn(currentPlayer == 1 ? 2 : 1);
            if (playWithComputer && playerTurn == 2) computerPlay();
        }
    }

    private void showResultDialog(String message) {
        ResultDialogActivity resultDialog = new ResultDialogActivity(MainActivity.this, message, MainActivity.this);
        resultDialog.setCancelable(false);
        resultDialog.show();
    }

    private void updateScores() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        int playerOneScore = databaseHelper.getScore(binding.playerOneName.getText().toString());
        int playerTwoScore = databaseHelper.getScore(binding.playerTwoName.getText().toString());

        binding.playerOneScore.setText( " Score: " + playerOneScore);
        binding.playerTwoScore.setText( " Score: " + playerTwoScore);
    }

    private void saveScore(String playerName, int score) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.saveScore(playerName, score);    }

    private void computerPlay() {
        int selectedBoxPosition = -1;

        switch (difficultyLevel) {
            case "easy":
                selectedBoxPosition = getRandomMove();
                break;
            case "medium":
                selectedBoxPosition = getMediumMove();
                break;
            case "hard":
                selectedBoxPosition = getHardMove();
                break;
        }

        if (selectedBoxPosition != -1) {
            ImageView imageView = getImageViewByPosition(selectedBoxPosition);
            if (imageView != null) {
                int finalSelectedBoxPosition = selectedBoxPosition;
                imageView.postDelayed(() -> performAction(imageView, finalSelectedBoxPosition), 500);
            }
        }
    }

    private int getRandomMove() {
        Random random = new Random();
        int selectedBoxPosition;

        do {
            selectedBoxPosition = random.nextInt(9);
        } while (!isBoxSelectable(selectedBoxPosition));

        return selectedBoxPosition;
    }

    private int getMediumMove() {
        int winningMove = getWinningMove(2); // Check if the computer has a winning move
        return winningMove != -1 ? winningMove : getRandomMove();
    }

    private int getHardMove() {
        int winningMove = getWinningMove(1); // Computer's winning move
        if (winningMove != -1) return winningMove;

        int blockingMove = getWinningMove(2); // Block player's winning move
        return blockingMove != -1 ? blockingMove : getRandomMove();
    }

    private int getWinningMove(int player) {
        for (int[] combination : combinationList) {
            int count = 0;
            int emptyPosition = -1;

            for (int pos : combination) {
                if (boxPositions[pos] == player) {
                    count++;
                } else if (boxPositions[pos] == 0) {
                    emptyPosition = pos;
                }
            }

            if (count == 2 && emptyPosition != -1) {
                return emptyPosition;
            }
        }
        return -1;
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;

        if (playerTurn == 1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);
        } else {
            binding.playerOneLayout.setBackgroundResource(R.drawable.white_box);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.black_border);
        }
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    private ImageView getImageViewByPosition(int position) {
        switch (position) {
            case 0: return binding.image1;
            case 1: return binding.image2;
            case 2: return binding.image3;
            case 3: return binding.image4;
            case 4: return binding.image5;
            case 5: return binding.image6;
            case 6: return binding.image7;
            case 7: return binding.image8;
            case 8: return binding.image9;
            default: return null;
        }
    }

    public void restartMatch() {
        boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        playerTurn = 1;
        totalSelectedBoxes = 1;

        binding.image1.setImageResource(R.drawable.white_box);
        binding.image2.setImageResource(R.drawable.white_box);
        binding.image3.setImageResource(R.drawable.white_box);
        binding.image4.setImageResource(R.drawable.white_box);
        binding.image5.setImageResource(R.drawable.white_box);
        binding.image6.setImageResource(R.drawable.white_box);
        binding.image7.setImageResource(R.drawable.white_box);
        binding.image8.setImageResource(R.drawable.white_box);
        binding.image9.setImageResource(R.drawable.white_box);

        changePlayerTurn(1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
