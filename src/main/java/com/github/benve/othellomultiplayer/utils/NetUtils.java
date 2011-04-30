package com.github.benve.othellomultiplayer.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: Giacomo Benvenuti
 * Date: 4/30/11
 * Time: 9:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class NetUtils {

    /**
     * Ritorna un indirizzo ip pubblico della macchina
     * @return un indirizzo pubblico della macchina, null ne la macchina non ne ha
     * @throws UnknownHostException
     */
    public static InetAddress getPublicIP() throws UnknownHostException {

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
    public static String getHostAddress() throws UnknownHostException {
        return NetUtils.getPublicIP().getHostAddress();
    }


}
