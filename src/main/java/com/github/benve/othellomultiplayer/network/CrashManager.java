package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.game.Player;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

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


    public CrashManager(Player me) throws RemoteException {
        super();
        this.allPlayer = PlayerList.getInstance();
        this.me = me;

    }

    public void initializeCrashManager() throws RemoteException, AlreadyBoundException {
        Registry register;
        register = LocateRegistry.getRegistry(this.me.getPort());

        this.registry = register;
        this.registry.bind("CrashManager",this);
    }

    public void startTimedController(final Node node) {
        (new Timer()).schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    node.sendNext("OK");
                    return;
                } catch (NotBoundException e) {

                }
            }
        }, 0, 2000);//Secondi ogni quanto fa il controllo del crash
    }

    public void repairAndBroadcastPlayerList() throws NotBoundException {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        System.out.println(stackTraceElements[stackTraceElements.length-1].getMethodName());



        int delIndex = allPlayer.getPosition(allPlayer.getNext(me));

        allPlayer.removeElementByPosition(delIndex);
        startRebuildPlayerList(delIndex);
    }

    public void startRebuildPlayerList(int toDel) throws NotBoundException {
        try {
            this.getNext().RebuildPlayerList(toDel, this.me.getUuid());
        } catch (RemoteException e) {
            repairAndBroadcastPlayerList();
            this.startRebuildPlayerList(toDel);
        }
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
            allPlayer.removeElementByPosition(toDel);
            try {
                getNext().RebuildPlayerList(toDel, pUUID);
            } catch (RemoteException e) {
                repairAndBroadcastPlayerList();
                this.RebuildPlayerList(toDel,pUUID);
            }
        }
    }

    public void ping() throws NotBoundException {
        try{
            getNext().pong();
        } catch (RemoteException e) {
                this.repairAndBroadcastPlayerList();
                //this.ping();
        }
    }

    public boolean pong() throws RemoteException {
        //System.out.println(this.allPlayer.toString());
        return true;
    }

}
