package com.github.benve.othellomultiplayer.network;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 11.24
 * To change this template use File | Settings | File Templates.
 */
public interface NodeRemote extends Remote {

    void initializeNode(boolean server) throws RemoteException, AlreadyBoundException;

    public String replyForIp() throws RemoteException;

    public String askForIp() throws RemoteException, NotBoundException;

    public void registerToGame(boolean server,int rPort) throws MaxPlayerException, RemoteException, NotBoundException;
}
