package com.github.benve.othellomultiplayer.old.network;

import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.old.game.Player;

import java.rmi.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ledonne
 */
public class Registration {

    /**
     * Contiene i giocatori registrati
     */
    private final List<Player> plist;

    /**
     * Numero di giocatori richiesti per la partita
     */
    private int maxplayer = 4;

    public Registration() {
        plist = new ArrayList();
    }

    public Registration(int maxplayer) {
        plist = new ArrayList();
        this.maxplayer = maxplayer;
    }

    /**
     * Registra un giocatore alla partita, rimane in arresa fino a che il numero di giocatori richiesto
     * per la partita si è regiatrato al gioco
     * @param pplay
     * @return la lista con i giocatori
     * @throws RemoteException
     * @throws com.github.benve.othellomultiplayer.network.MaxPlayerException quando ho già raggiunto il numero massimo di giocatori
     */
    public List<Player> register(Player pplay) throws MaxPlayerException {

        if (plist.size() >= maxplayer) throw new MaxPlayerException("Numero massimo di giocatori raggiunto");

        assert(plist.add(pplay));

        while(plist.size() != 4) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return plist;
    }

    public List<Player> getPlayerList() {
        return plist;
    }

}