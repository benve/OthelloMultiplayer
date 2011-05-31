package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Player;
import com.github.benve.othellomultiplayer.game.PlayerList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: lemad85
 * Date: 20/05/11
 * Time: 10.51
 * To change this template use File | Settings | File Templates.
 */
public class Node extends UnicastRemoteObject implements NodeRemote {

    public Player me;
    private Registry registry;
    public PlayerList allPlayer;
    private int maxplayer;
    private Registration reg1;

    public Node(int n_port) throws RemoteException, AlreadyBoundException, UnknownHostException, SocketException {
        super();
        me = new Player(n_port);
        maxplayer = 3;
    }

    public Node(String Name) throws IOException {
        super();
        ServerSocket ss = new ServerSocket(0);
        int freeport = ss.getLocalPort();
        ss.close();

        System.out.println("porta:"+freeport);

        me = new Player(Name,freeport);
        maxplayer = 4;
    }

    public Node(int n_port, int n_player) throws RemoteException, AlreadyBoundException, UnknownHostException, SocketException {
        super();
        me = new Player(n_port);
        maxplayer = n_player;
    }

    public Node(String Name, int port, int n_player) throws IOException {
        super();
        me = new Player(Name, port);
        maxplayer = n_player;
    }

    /**
     * Crea il registro RMI e si registra
     * se il nodo è il server di registrazione al gioco istanzia anche Registration
     * @param server true se il nodo deve istanziare anche il Registration
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    public void initializeNode(boolean server) throws RemoteException, AlreadyBoundException {
        Registry register;
        register = LocateRegistry.createRegistry(this.me.getPort());
        this.registry = register;
        this.registry.bind("Node",this);

        if(server)
            this.setRegister();

    }

    private void setRegister() throws RemoteException, AlreadyBoundException {
        reg1 = new Registration(me.getPort(),this.maxplayer);
        reg1.instaceRegistration();
    }

    /**
     * Il nodo si registra al gioco
     * @param server se true io ho il Registration altrimenti lo prendo dal nodo remoto
     * @param rPort porta del registro remoto
     * @throws MaxPlayerException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void registerToGame(boolean server,int rPort) throws MaxPlayerException, RemoteException, NotBoundException {
        int regPort;
        if(server)
            regPort = this.me.getPort();
        else
            regPort = rPort;

        Registry register = LocateRegistry.getRegistry(regPort);
        this.registry = register;
        RegistrationRemote r_reg =  (RegistrationRemote) this.registry.lookup("Reg");

        this.allPlayer = r_reg.register(this.me);

    }

    @Override
    public void broadcast(Message msg) throws NotBoundException {
        if (msg.uuid == this.me.getUuid()) {
            System.out.println(msg.content);
        } else {
            System.out.println(msg.content+"|"+me.getPort());
            try {
                getNext().broadcast(msg);
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void startBroadcast(Message msg) throws NotBoundException {
        msg.uuid = me.getUuid();
        try {
            this.getNext().broadcast(msg);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public NodeRemote getNext() throws RemoteException, NotBoundException {
        Player nextPlayer = this.allPlayer.getNext(this.me);
        Registry register = LocateRegistry.getRegistry(nextPlayer.getPort());

        NodeRemote rem = (NodeRemote) register.lookup("Node");
        return rem;
        }



    /**
     * Prendo il nodo next e gli chiedo l'ip porta (chiamo replyForIp)
     * @return
     * @throws RemoteException
     * @throws NotBoundException
     */
    public String askForIp() throws RemoteException, NotBoundException {
        //Trovo me stesso
        int position = 0;
        for(int i=0;i<this.allPlayer.size();i++){
            if(me.getUuid() == (allPlayer.get(i).getUuid())){
                System.out.println("Trovato!"+i);
                position = i;
                break;
            }
        }

        Registry register = LocateRegistry.getRegistry(allPlayer.get((position+1)%maxplayer).getPort());

        NodeRemote rem = (NodeRemote) register.lookup("Node");
        return rem.replyForIp();


    }

    /**
     * Restituisco una Stringa con ip:porta
     * @return
     * @throws RemoteException
     */
    public String replyForIp() throws RemoteException{
        return (me.getIpAddress()+":"+me.getPort()).toString();//me.getUuid().toString();
    }



}
