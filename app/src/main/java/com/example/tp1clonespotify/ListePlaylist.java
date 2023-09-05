package com.example.tp1clonespotify;

import java.util.Hashtable;
import java.util.Vector;

public class ListePlaylist {

//    Classe qui créé une liste de playlist et qui remplit un vector de hashtable
//    qui sera utilisé pour créer de toute piece les éléments de la liste complexe

    private Vector<Hashtable<String, String>> vecHash;
    private Vector<Playlist> vecPlaylist;

    public ListePlaylist() {

        Playlist bestOfRadiohead = new Playlist("Best of Radiohead", 42, "3h11m");
        Playlist OkComputer = new Playlist("Ok Computer", 23, "1h32m");
        Playlist KidA = new Playlist("Kid A", 34, "2h5m");

        this.vecPlaylist = new Vector<>();
        this.vecPlaylist.add(bestOfRadiohead);
        this.vecPlaylist.add(OkComputer);
        this.vecPlaylist.add(KidA);

        this.vecHash = new Vector<>();
//        On ajoute les playlist dans une hashtable et ensuite on ajoute cette hashtable a notre vecteur de hashtable
        for (Playlist playlist : this.vecPlaylist){
            Hashtable hashtable = new Hashtable<>();
            hashtable.put("nom", playlist.getNom());
            hashtable.put("nbChansons", playlist.getNbChansons() + " chansons");
            hashtable.put("duree", "Durée : "+ playlist.getDuree());
            this.vecHash.add(hashtable);
        }

    }

    public Vector<Hashtable<String, String>> getVecHash() {
        return vecHash;
    }
}
