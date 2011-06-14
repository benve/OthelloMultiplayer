package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.PlayerList;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 11.24
 */
public interface NodeRemote extends Remote {

    void initializeNode(boolean server) throws RemoteException, AlreadyBoundException;

    public void registerToGame(boolean server,int rPort) throws MaxPlayerException, RemoteException, NotBoundException, AlreadyBoundException;

    public void broadcast(Message msg) throws RemoteException, NotBoundException;

    //public void RebuildPlayerList(int toDel, int pUUID) throws RemoteException, NotBoundException;

    public void receive(Object msg) throws RemoteException;

    public void updatePlayerList(int delIndex) throws RemoteException;


}
