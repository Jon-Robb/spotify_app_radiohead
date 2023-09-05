package com.example.tp1clonespotify;

//classe utilitaire à la rétention de l'information et pour séparer la vue
public class Chanson {

    private String nom;
    private Artiste artiste;
    private String album;

    public Chanson(String nom, Artiste artiste, String album) {
        this.nom = nom;
        this.artiste = artiste;
        this.album = album;
    }

    public String getNom() {
        return nom;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public String getAlbum() {
        return album;
    }

}


