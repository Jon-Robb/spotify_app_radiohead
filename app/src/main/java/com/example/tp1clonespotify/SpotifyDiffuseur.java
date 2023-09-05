package com.example.tp1clonespotify;

import android.content.Context;


import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.Vector;

//SpotifyDiffuseur classe de style Singleton, pour gérer les actions qui portent sur la lecture des chansons
// et la sélection des playlists.

public class SpotifyDiffuseur {

    private Context context;
    private static SpotifyDiffuseur instance;
    private static final String CLIENT_ID = "b2b20661ad71475b81e4b19305de50e6";
    private static final String REDIRECT_URI = "com.example.tp1clonespotify://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerState vPlayerState;


//    getinstance classique
    public static SpotifyDiffuseur getInstance(Context context){
        if(instance == null){
            instance = new SpotifyDiffuseur(context);
        }
        return  instance;
    }


    public PlayerState getvPlayerState() {
        return vPlayerState;
    }

    public void setvPlayerState(PlayerState vPlayerState) {
        this.vPlayerState = vPlayerState;
    }

    private SpotifyDiffuseur (Context context){
        this.context = context;
    }

//    Connection à l'Api
    public void seConnecter(){

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        ((PlayerActivity) context).startChronos();
                        rafraichir();

                        mSpotifyAppRemote.getPlayerApi()
                                .subscribeToPlayerState()
                                .setEventCallback(playerState ->{
                                    final Track track = playerState.track;
                                    if (track != null){
                                        setvPlayerState(playerState);
                                    }
                                });
                    }
                    public void onFailure(Throwable throwable) { }

                });
    }


    public void seDeconnecter(){
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }


//    Fonctions utilitaires pour la lecture des playlists/chansons
    public void play(String playlist) {
        mSpotifyAppRemote.getPlayerApi().play(playlist);
    }

    public void pause(){
        mSpotifyAppRemote.getPlayerApi().pause();
    }

    public void resume(){
        mSpotifyAppRemote.getPlayerApi().resume();
    }

    public void next(){
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }

    public void back(){
        mSpotifyAppRemote.getPlayerApi().skipPrevious();
    }


//    Fonction qui rafraichit l'état de la Vue en se servant des informations du playerState
//      et en se servant des fonctions de refresh dans la vue
    public void rafraichir(){
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState ->{
                    final Track track = playerState.track;
                    if (track != null){
                        Chanson chanson = new Chanson(track.name, new Artiste(track.artist.name), track.album.name);
                        mSpotifyAppRemote.getImagesApi().getImage(track.imageUri).setResultCallback(imgChanson -> {
                            setvPlayerState(playerState);
//                            On refresh la Vue (text, image et seekbar) en envoyant les informations de la track
                            ((PlayerActivity) context).setSeekBarMax((int)track.duration / 1000, (int) playerState.playbackPosition / 1000);
                            ((PlayerActivity) context).rafraichir(chanson, imgChanson);
                        });
                        if (playerState.isPaused) {
                            ((PlayerActivity) context).stopChronos();
                        }
                        else{
                            ((PlayerActivity) context).startChronos();
                        }

                    }
                });
    }



}
