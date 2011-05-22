package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.g_Player;
import com.sun.corba.se.spi.activation.Server;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParameterList;

import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 13.25
 * To change this template use File | Settings | File Templates.
 */
public class gMain {

    public gMain(){}

    public static void main(String[] args) throws IOException, AlreadyBoundException, MaxPlayerException, NotBoundException {
        g_Node mySelf;
        boolean isServer = false;
        List<g_Player> plist = new ArrayList();
        if(args.length >= 3){
            mySelf = new g_Node(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            if(Integer.parseInt(args[2]) == 1){
                isServer = true;
            }

            mySelf.initializeNode(isServer);

            if(isServer)
                mySelf.registerToGame(isServer,0);
            else
                mySelf.registerToGame(isServer,1755);

            for(int i=0;i<255; i++)
                System.out.println(mySelf.askForIp());
        }
    }
}
