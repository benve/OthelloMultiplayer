package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.g_Player;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.19
 * To change this template use File | Settings | File Templates.
 */
public interface g_Registration_Remote extends Remote {
    List<g_Player> register(g_Player pplay) throws MaxPlayerException,RemoteException;

    List<g_Player> getPlayerList() throws RemoteException;

    void instaceRegistration() throws RemoteException, AlreadyBoundException;
}
