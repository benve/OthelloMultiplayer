package com.github.benve.othellomultiplayer.utils;

import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.PlayerList;
import com.github.benve.othellomultiplayer.network.NodeRemote;

import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: Giacomo Benvenuti
 * Date: 4/30/11
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
    public class NetUtils {

        private static NetUtils instance;

        private NetUtils(){};

        public static NetUtils getInstance(){
            if(instance == null)
                instance = new NetUtils();
            return instance;
        }

        /**
         * Ritorna un indirizzo ip pubblico della macchina
         * @return un indirizzo pubblico della macchina, null ne la macchina non ne ha
         * @throws UnknownHostException
         */
        public InetAddress getPublicIP() throws UnknownHostException {

            //Ottengo tutti gli IP della macchina
            String hostName = InetAddress.getLocalHost().getHostName();
            InetAddress addrs[] = InetAddress.getAllByName(hostName);

            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()) {
                    return addr;
                }
            }

            return null;
        }

        /**
         * Ritorna un indirizzo ip pubblico della macchina
         * @return un indirizzo pubblico della macchina, null ne la macchina non ne ha
         * @throws UnknownHostException
         */
        public String getHostAddress() throws UnknownHostException, SocketException {
            NetworkInterface iface;
            iface = NetworkInterface.getByName("wlan0");
            for(Enumeration<InetAddress> addresses = iface.getInetAddresses(); addresses.hasMoreElements();){
                InetAddress address = addresses.nextElement();
                if(address instanceof Inet4Address)
                    return address.getHostAddress();
            }

            return null;
        }

}