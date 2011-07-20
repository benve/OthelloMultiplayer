package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.utils.NetUtils;
import sun.rmi.runtime.NewThreadAction;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.14
 * To change this template use File | Settings | File Templates.
 */
public class Registration extends UnicastRemoteObject implements RegistrationRemote {
    /**
     * Contiene i giocatori registrati
     */
    private final LinkedList plist;
    //private PlayerList plist;
    private int regPort;
    /**
     * Numero di giocatori richiesti per la partita
     */
    private int maxplayer = 4;

    private Registry registry;

    public Registration(int r_port) throws RemoteException, AlreadyBoundException {
        super();
        this.regPort = r_port;
        this.plist = new LinkedList();//PlayerList.getInstance();
    }

    public Registration(int r_port, int maxplayer) throws RemoteException {
        super();
        this.plist = new LinkedList();//PlayerList.getInstance();
        this.maxplayer = maxplayer;
        this.regPort = r_port;
    }

    /**
     * Registra un giocatore alla partita, rimane in arresa fino a che il numero di giocatori richiesto
     * per la partita si è regiatrato al gioco
     *
     * @param pplay
     * @return la lista con i giocatori
     * @throws java.rmi.RemoteException
     * @throws MaxPlayerException quando ho già raggiunto il numero massimo di giocatori
     */

    public List<Player> register(Player pplay) throws MaxPlayerException, RemoteException, CantAddPlayerException {

        if (plist.size() >= maxplayer) throw new MaxPlayerException("Numero massimo di giocatori raggiunto");

        if(plist.add(pplay))
            System.out.println("Aggiunto "+pplay.getUuid());
        else
            throw new CantAddPlayerException("Impossibile caricare il giocatore "+pplay.getUuid());

        while(plist.size() != maxplayer) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return plist;
    }

    public void instaceRegistration() throws RemoteException, AlreadyBoundException, UnknownHostException, SocketException {
        Registry registry = LocateRegistry.getRegistry(this.regPort);
        this.registry = registry;
        registry.bind("Reg",this);
    }
}
