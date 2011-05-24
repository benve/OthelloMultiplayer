package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;

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
public interface RegistrationRemote extends Remote {
    List<Player> register(Player pplay) throws MaxPlayerException,RemoteException;

    List<Player> getPlayerList() throws RemoteException;

    void instaceRegistration() throws RemoteException, AlreadyBoundException;
}
