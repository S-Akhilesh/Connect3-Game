package com.akhilesh.connect3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    //0=x 1=o 2=empty
    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winningPlace = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    int activePlayer = 0;
    AdView adView;
    Button restartButton;
    Boolean gameActive = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        restartButton = findViewById(R.id.restartButton);
    }




    public void Show(View view) {

        ImageView counter = (ImageView) view;

        int tapped = Integer.parseInt(counter.getTag().toString());
        if(gameState[tapped] == 2 && gameActive) {

            gameState[tapped] = activePlayer;
            counter.setAlpha(0.0f);

            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.ex);
                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.circ);
                activePlayer = 0;
            }

            counter.animate().alpha(1.0f);

            for (int[] win : winningPlace) {
                if (gameState[win[0]] == gameState[win[1]] && gameState[win[1]] == gameState[win[2]] && gameState[win[0]] != 2) {
                    gameActive = false;
                    restartButton.setVisibility(View.VISIBLE);
                    String winner;
                    if (activePlayer == 1) {
                        winner = "X";
                    } else {
                        winner = "O";
                    }
                    Toast.makeText(this, winner + " has won!", Toast.LENGTH_SHORT).show();
                    play();
                }
            }
        }
    }

    private void play() {
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GridLayout gridLayout = findViewById(R.id.gridLayout);
                for(int i=0;i<gridLayout.getChildCount();i++){
                    ImageView counter = (ImageView) gridLayout.getChildAt(i);
                    counter.setImageDrawable(null);
                }

                for(int i=0;i<gameState.length;i++){
                    gameState[i]=2;
                }
                gameActive = true;
                activePlayer = 0;
            }
        });
    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}
