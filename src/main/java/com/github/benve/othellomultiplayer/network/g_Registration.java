package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.g_Player;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.14
 * To change this template use File | Settings | File Templates.
 */
public class g_Registration extends UnicastRemoteObject implements g_Registration_Remote {
    /**
     * Contiene i giocatori registrati
     */
    private final List<g_Player> plist;
    private int regPort;
    /**
     * Numero di giocatori richiesti per la partita
     */
    private int maxplayer = 4;

    private Registry registry;

    public g_Registration(int r_port) throws RemoteException, AlreadyBoundException {
        super();
        this.regPort = r_port;
        this.plist = new ArrayList();
    }

    public g_Registration(int r_port,int maxplayer) throws RemoteException {
        super();
        this.plist = new ArrayList();
        this.maxplayer = maxplayer;
        this.regPort = r_port;
    }

    /**
     * Registra un giocatore alla partita, rimane in arresa fino a che il numero di giocatori richiesto
     * per la partita si è regiatrato al gioco
     * @param pplay
     * @return la lista con i giocatori
     * @throws java.rmi.RemoteException
     * @throws MaxPlayerException quando ho già raggiunto il numero massimo di giocatori
     */

    public List<g_Player> register(g_Player pplay) throws MaxPlayerException,RemoteException {

        if (plist.size() >= maxplayer) throw new MaxPlayerException("Numero massimo di giocatori raggiunto");

        plist.add(pplay);

        while(plist.size() != maxplayer) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return plist;
    }


    public List<g_Player> getPlayerList() throws RemoteException{
        return plist;
    }

    public void instaceRegistration() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.getRegistry(this.regPort);
        this.registry = registry;
        registry.bind("g_Reg",this);
    }
}
