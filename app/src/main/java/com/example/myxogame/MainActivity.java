package com.example.myxogame;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myxogame.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ActivityMainBinding binding;
    private final List<int[]> combinationList = new ArrayList<>();
    private int[] boxPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // 9 zero
    private int playerTurn = 1;
    private int totalSelectedBoxes = 1;
    private boolean playWithComputer = false; // New variable to check game mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Define winning combinations
        combinationList.add(new int[]{0, 1, 2});
        combinationList.add(new int[]{3, 4, 5});
        combinationList.add(new int[]{6, 7, 8});
        combinationList.add(new int[]{0, 3, 6});
        combinationList.add(new int[]{1, 4, 7});
        combinationList.add(new int[]{2, 5, 8});
        combinationList.add(new int[]{2, 4, 6});
        combinationList.add(new int[]{0, 4, 8});

        // Retrieve player names and game mode
        String getPlayerOneName = getIntent().getStringExtra("playerOne");
        String getPlayerTwoName = getIntent().getStringExtra("playerTwo");
        String gameMode = getIntent().getStringExtra("gameMode");

        binding.playerOneName.setText(getPlayerOneName);
        binding.playerTwoName.setText(getPlayerTwoName);

        // Check if game mode is against computer
        if ("computer".equals(gameMode)) {
            playWithComputer = true;
            binding.playerTwoName.setText("Computer"); // Change player 2 name to "Computer"
        }

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(0)) {
                    performAction((ImageView) view, 0);
                }
            }
        });
        binding.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(1)) {
                    performAction((ImageView) view, 1);
                }
            }
        });
        binding.image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(2)) {
                    performAction((ImageView) view, 2);
                }
            }
        });
        binding.image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(3)) {
                    performAction((ImageView) view, 3);
                }
            }
        });
        binding.image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(4)) {
                    performAction((ImageView) view, 4);
                }
            }
        });
        binding.image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(5)) {
                    performAction((ImageView) view, 5);
                }
            }
        });
        binding.image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(6)) {
                    performAction((ImageView) view, 6);
                }
            }
        });
        binding.image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(7)) {
                    performAction((ImageView) view, 7);
                }
            }
        });
        binding.image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBoxSelectable(8)) {
                    performAction((ImageView) view, 8);
                }
            }
        });
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;

        if (playerTurn == 1) {
            imageView.setImageResource(R.drawable.ximage);

            if (checkResults()) {
                ResultDialogActivity resultDialog = new ResultDialogActivity(MainActivity.this,
                        binding.playerOneName.getText().toString() + " is a Winner!", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if (totalSelectedBoxes == 9) {
                ResultDialogActivity resultDialog = new ResultDialogActivity(MainActivity.this,
                        "Match Draw", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;

                // Trigger computer move if playing with computer
                if (playWithComputer) {
                    computerPlay();
                }
            }
        } else {
            imageView.setImageResource(R.drawable.oimage);

            if (checkResults()) {
                ResultDialogActivity resultDialog = new ResultDialogActivity(MainActivity.this,
                        binding.playerTwoName.getText().toString() + " is a Winner!", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else if (totalSelectedBoxes == 9) {
                ResultDialogActivity resultDialog = new ResultDialogActivity(MainActivity.this,
                        "Match Draw", MainActivity.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }

    private void computerPlay() {
        // Simple AI: Random move selection for computer
        Random random = new Random();
        int selectedBoxPosition;
        ImageView imageView;

        // Find a random selectable box
        do {
            selectedBoxPosition = random.nextInt(9);
            imageView = getImageViewByPosition(selectedBoxPosition);
        } while (!isBoxSelectable(selectedBoxPosition));

        // Delay for better UX to simulate computer thinking
        ImageView finalImageView = imageView;
        int finalSelectedBoxPosition = selectedBoxPosition;
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                performAction(finalImageView, finalSelectedBoxPosition);
            }
        }, 500); // 500ms delay
    }

    private ImageView getImageViewByPosition(int position) {
        switch (position) {
            case 0:
                return binding.image1;
            case 1:
                return binding.image2;
            case 2:
                return binding.image3;
            case 3:
                return binding.image4;
            case 4:
                return binding.image5;
            case 5:
                return binding.image6;
            case 6:
                return binding.image7;
            case 7:
                return binding.image8;
            case 8:
                return binding.image9;
            default:
                return null;
        }
    }

    private void changePlayerTurn(int currentPlayerTurn) {
        playerTurn = currentPlayerTurn;

        if (playerTurn == 1) {
            binding.playerOneLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerTwoLayout.setBackgroundResource(R.drawable.white_box);
        } else {
            binding.playerTwoLayout.setBackgroundResource(R.drawable.black_border);
            binding.playerOneLayout.setBackgroundResource(R.drawable.white_box);
        }
    }

    private boolean checkResults() {
        boolean response = false;

        for (int i = 0; i < combinationList.size(); i++) {
            final int[] combination = combinationList.get(i);

            if (boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn &&
                    boxPositions[combination[2]] == playerTurn) {
                response = true;
            }
        }
        return response;
    }

    private boolean isBoxSelectable(int boxPosition) {
        return boxPositions[boxPosition] == 0;
    }

    public void restartMatch() {
        boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0}; // 9 zero
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}