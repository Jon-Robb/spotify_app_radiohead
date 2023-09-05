package com.example.tp1clonespotify;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class PlayerActivity extends AppCompatActivity {

    private ImageView imgStartPause, imgSkipBack, imgSkipNext;
    private SpotifyDiffuseur instance;
    private TextView playlistName, songTitle, albumName, lien;
    private boolean isPlayBtn = true;
    private ImageView songImage, playlistMenu;
    private SeekBar seekBar;
    private Chronometer timeElapsed, timeLeft;
    private ActivityResultLauncher<Intent> lanceur;
    private int seekBarProgress = 0;
    private String playlist;

    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (playlist == null){
            playlist = "spotify:playlist:2I9t0VoXbhjgCwlQ4LasO9";

        }

        lien = findViewById(R.id.lien);
        imgStartPause = findViewById(R.id.imgStartPause);
        imgSkipBack = findViewById(R.id.imgSkipBack);
        imgSkipNext  = findViewById(R.id.imgSkipNext);
        playlistName = findViewById(R.id.playlistName);
        songTitle = findViewById(R.id.songTitle);
        albumName = findViewById(R.id.songArtist);
        songImage = findViewById(R.id.songImg);
        seekBar = findViewById(R.id.seekBar);
        instance = SpotifyDiffuseur.getInstance(PlayerActivity.this);
        imgStartPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
        timeElapsed = findViewById(R.id.timeElapsed);
        timeLeft = findViewById(R.id.timeLeft);
        playlistMenu = findViewById(R.id.playlistMenu);



//        Le boomerang est lancé lorsque l'usager appuie sur le hamburgermenu qui le mène vers la PlaylistMenuActivity,
//          il peut sélectionner la playlist de son choix.
        lanceur = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if (result.getResultCode() == 24){
                assert result.getData() != null;
                playlist = result.getData().getStringExtra("uri");
                instance.play(playlist);
                isPlayBtn = true;
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onStart() {
        super.onStart();


        instance.seConnecter();

//        bool pour la logique du bouton play/pause
        if (instance.getvPlayerState() != null){
            if (instance.getvPlayerState().isPaused){
                isPlayBtn = false;
            }
        }

//        logique pour le bouton play/pause
        imgStartPause.setOnClickListener(source -> {
            if (!isPlayBtn){
                instance.resume();
                imgStartPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                isPlayBtn = true;
            }
            else{
                instance.pause();
                stopChronos();
                imgStartPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                isPlayBtn = false;
            }
            instance.rafraichir();
        });

//        C'est direct
        imgSkipNext.setOnClickListener(source -> instance.next());
        imgSkipBack.setOnClickListener(source -> instance.back());


//        A chaque tick du chrono on incrémente la seekbar
        timeElapsed.setOnChronometerTickListener(chronometer ->{
            ++seekBarProgress;
            seekBar.setProgress(seekBarProgress);
        });

//        Logique du hamburgerMenu
        playlistMenu.setOnClickListener(source ->{
            Intent i = new Intent(PlayerActivity.this, PlaylistMenuActivity.class);
            lanceur.launch(i);
        });

        lien.setOnClickListener(source ->{
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.radiohead.com"));
            lanceur.launch(i);
        });

    }


//    Fonctions utilitaires pour la Vue (qui sont utilisées dans le SpotifyDiffuseur
    public void rafraichir(Chanson chanson, Bitmap imgChanson){
        playlistName.setText(chanson.getArtiste().getNom());
        songTitle.setText(chanson.getNom());
        albumName.setText(chanson.getAlbum());
        songImage.setImageBitmap(imgChanson);
    }


    public void setSeekBarMax(int value, int progress){
        seekBar.setMax(value);
        seekBar.setProgress(progress);
        seekBarProgress = progress;
//        Petit calcul pour ajuster le temps en fonction de la position de la chanson
        timeElapsed.setBase(SystemClock.elapsedRealtime() - seekBarProgress * 1000L);
        timeLeft.setBase(SystemClock.elapsedRealtime() -(progress - value) * 1000L);
    }


    public void startChronos(){
        timeElapsed.start();
        timeLeft.start();
    }

    public void stopChronos(){
        timeLeft.stop();
        timeElapsed.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.pause();
        instance.seDeconnecter();
    }
}


