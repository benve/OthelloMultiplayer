package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;

import java.lang.reflect.Array;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ledonne
 */
public class Registration extends UnicastRemoteObject implements RemoteRegistration {

    /**
     * Contiene i giocatori registrati
     */
    private final List<Player> plist;

    /**
     * Numero di giocatori richiesti per la partita
     */
    private int maxplayer = 4;

    public Registration() throws RemoteException {
        plist = new ArrayList();
    }

    public Registration(int maxplayer) throws RemoteException {
        plist = new ArrayList();
        this.maxplayer = maxplayer;
    }

    /**
     * Registra un giocatore alla partita, rimane in arresa fino a che il numero di giocatori richiesto
     * per la partita si è regiatrato al gioco
     * @param pplay
     * @return la lista con i giocatori
     * @throws RemoteException
     * @throws MaxPlayerException quando ho già raggiunto il numero massimo di giocatori
     */
    public List<Player> register(Player pplay) throws RemoteException, MaxPlayerException {

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

    public List<Player> getPlayerList() throws RemoteException {
        return plist;
    }

}