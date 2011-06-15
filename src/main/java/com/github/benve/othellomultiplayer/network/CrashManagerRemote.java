package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.PlayerList;

import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 14/06/11
 * Time: 11.23
 * To change this template use File | Settings | File Templates.
 */
public interface CrashManagerRemote extends Remote {

    public void initializeCrashManager() throws RemoteException, AlreadyBoundException;

    public CrashManagerRemote getNext() throws NotBoundException, RemoteException;

    public void RebuildPlayerList(int toDel, int pUUID) throws NotBoundException, RemoteException;
}
