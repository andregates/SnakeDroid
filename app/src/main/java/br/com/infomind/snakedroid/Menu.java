package br.com.infomind.snakedroid;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //aqui a mágica
        setContentView(R.layout.activity_menu);

        MediaPlayer mpBG = MediaPlayer.create(this, R.raw.cobrabg);
        mpBG.setLooping(true);
        mpBG.start();



    }

    public void clickControl(View v){

        startActivity(new Intent(this,GameBoard.class));
        //finish();


    }


}
