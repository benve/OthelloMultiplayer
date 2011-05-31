package com.github.benve.othellomultiplayer;

import com.github.benve.othellomultiplayer.game.Board;
import com.github.benve.othellomultiplayer.game.BoardLogic;
import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.network.MaxPlayerException;
import com.github.benve.othellomultiplayer.network.Message;
import com.github.benve.othellomultiplayer.network.Node;
import com.github.benve.othellomultiplayer.utils.NetUtils;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.sql.SQLOutput;
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
        Node mySelf;
        boolean isServer = false;
        BoardLogic bl1 = BoardLogic.getInstance();
        Board b1;
        List<Player> plist = new ArrayList();
        Message msg = new Message();

        NetUtils n = NetUtils.getInstance();

        System.out.println(n.getHostAddress());

        //n.getPublicIP().toString());
        if(args.length >= 3){
            mySelf = new Node(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
            if(Integer.parseInt(args[2]) == 1){
                isServer = true;
            }

            mySelf.initializeNode(isServer);

            System.out.println(mySelf.me.getUuid()+"|"+mySelf.me.getPort());

            if(isServer) {
                mySelf.registerToGame(isServer,0);
                b1 = new Board(10,10);

                msg.content = "Server";
                mySelf.startBroadcast(msg);
            } else {
                mySelf.registerToGame(isServer,1234);
                //mySelf.allPlayer.remove(0);

            }


            /*for(int i=0;i<255; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                System.out.println(mySelf.allPlayer);

            }*/


        } else {
            System.out.println("Servono 3 parametri: porta numerogiocatori 1\n" +
                    "con 1 viene istanziato il registro dei giocatori");

        }






















    }
}
