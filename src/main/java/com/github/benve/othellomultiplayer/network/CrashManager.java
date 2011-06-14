package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.game.Player;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 14/06/11
 * Time: 10.44
 * To change this template use File | Settings | File Templates.
 */
public class CrashManager extends UnicastRemoteObject implements CrashManagerRemote {

    private PlayerList allPlayer;
    private Player me;
    private Registry registry;


    public CrashManager(PlayerList allPlayer, Player me) throws RemoteException {
        super();
        this.allPlayer = (PlayerList) allPlayer.clone();
        this.me = me;

    }

    public void initializeCrashManager() throws RemoteException, AlreadyBoundException {
        Registry register;
        register = LocateRegistry.getRegistry(this.me.getPort());


        this.registry = register;
        this.registry.bind("CrashManager",this);
    }

    public PlayerList repairAndBroadcastPlayerList() throws NotBoundException {
        int delIndex = allPlayer.getPosition(allPlayer.getNext(me));

        allPlayer.remove(delIndex);
        startRebuildPlayerList(delIndex);

        return allPlayer;
    }

    public void startRebuildPlayerList(int toDel) throws NotBoundException {
        try {
            this.getNext().RebuildPlayerList(toDel, this.me.getUuid());
        } catch (RemoteException e) {
            this.allPlayer = repairAndBroadcastPlayerList();
            this.startRebuildPlayerList(toDel);
        }
    }

    public NodeRemote getMyNode() throws NotBoundException, RemoteException {
        Registry register = null;

        register = LocateRegistry.getRegistry(me.getPort());
        NodeRemote rem = (NodeRemote) register.lookup("Node");

        return rem;
    }

    public CrashManagerRemote getNext() throws NotBoundException, RemoteException {
        Player nextPlayer = allPlayer.getNext(me);
        Registry register = null;

        register = LocateRegistry.getRegistry(nextPlayer.getPort());
        CrashManagerRemote rem = (CrashManagerRemote) register.lookup("CrashManager");

        return rem;
    }

    public void RebuildPlayerList(int toDel, int pUUID) throws NotBoundException, RemoteException {
        if (pUUID != this.me.getUuid()) {
            getMyNode().updatePlayerList(toDel);
            try {
                getNext().RebuildPlayerList(toDel, pUUID);
            } catch (RemoteException e) {
                this.allPlayer = repairAndBroadcastPlayerList();
                this.RebuildPlayerList(toDel,pUUID);
            }
        }
    }

}
