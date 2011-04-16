package com.github.benve.othellomultiplayer.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Giacomo Benvenuti
 */
public interface RemoteNode extends Remote {

    public void msg(String s) throws RemoteException;

    
}
