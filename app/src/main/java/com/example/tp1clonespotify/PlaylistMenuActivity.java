package com.example.tp1clonespotify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.Hashtable;
import java.util.Vector;

public class PlaylistMenuActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_menu);


        ListView liste = findViewById(R.id.liste);
//        On créer un nouvelle liste de playlist

        ListePlaylist listePlaylist = new ListePlaylist();

        Vector<Hashtable<String, String>> vec;
//       On va chercher les infos de la liste de playlist
        vec = listePlaylist.getVecHash();

//        On construit la liste  complexe
        String[] strings = {"nom", "nbChansons", "duree"};
        int[] integers = {R.id.plName, R.id.nbChansons, R.id.duree};

        SimpleAdapter simpleAdapter = new SimpleAdapter(PlaylistMenuActivity.this, vec, R.layout.liste_enfant, strings, integers);

        liste.setAdapter(simpleAdapter);

        Ecouteur ec = new Ecouteur();

        liste.setOnItemClickListener(ec);


    }

//    Écouteur classique style 3 étapes du vénérable Éric Labonté
    public class Ecouteur implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent retour = new Intent();
            if (position == 0){
                retour.putExtra("uri", "spotify:playlist:2I9t0VoXbhjgCwlQ4LasO9");
            }
            else if (position == 1){
                retour.putExtra("uri", "spotify:playlist:1bu27chTNb5Jj4XzSEhf1z");
            }
            else{
                retour.putExtra("uri", "spotify:playlist:7GIc01qZ7ruStQCG2ZTdUM");
            }
            setResult(24, retour);
            finish();
        }
    }
}