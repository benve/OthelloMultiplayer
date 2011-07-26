package com.github.benve.othellomultiplayer.network;

import com.github.benve.othellomultiplayer.game.Board;
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
    private CrashManager cm;
    public Board b;
    private String address;

    public Node(String name) throws IOException {
        super();
        ServerSocket ss = new ServerSocket(0);
        int freeport = ss.getLocalPort();
        ss.close();

        System.out.println("porta:"+freeport);

        me = new Player(name,freeport);

        allPlayer = PlayerList.getInstance();
    }


    public Node(String name, int n_player) throws IOException {
        super();
        ServerSocket ss = new ServerSocket(0);
        int freeport = ss.getLocalPort();
        ss.close();

        System.out.println("porta:"+freeport);

        me = new Player(name,freeport);
        maxplayer = n_player;
        allPlayer = PlayerList.getInstance();
    }

    public Node(String name, int port, int n_player) throws IOException {
        super();
        me = new Player(name, port);
        maxplayer = n_player;
        allPlayer = PlayerList.getInstance();
    }

    /**
     * Crea il registro RMI e si registra
     * se il nodo è il server di registrazione al gioco istanzia anche Registration
     * @param server true se il nodo deve istanziare anche il Registration
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    public void initializeNode(boolean server) throws RemoteException, AlreadyBoundException, UnknownHostException, SocketException {
        Registry register;
        register = LocateRegistry.createRegistry(this.me.getPort());

        System.setProperty("java.rmi.server.hostname",me.getIpAddress());
        System.setProperty("java.rmi.disableHttp","true");

        this.registry = register;
        this.registry.bind("Node",this);

        if(server)
            this.setRegister();

        //Thread.currentThread().setDaemon(true);

    }

    private void setRegister() throws RemoteException, AlreadyBoundException, UnknownHostException, SocketException {
        reg1 = new Registration(me.getPort(),this.maxplayer);
        reg1.instaceRegistration();
    }

    /**
     * Il nodo si registra al gioco
     * @param server se true io ho il Registration altrimenti lo prendo dal nodo remoto
     * @throws MaxPlayerException
     * @throws RemoteException
     * @throws NotBoundException
     */
    public void registerToGame(boolean server,String address) throws MaxPlayerException, RemoteException, NotBoundException, AlreadyBoundException {
        String rAddress;

        Registry register = LocateRegistry.getRegistry(address,1234);//(regPort);
        this.registry = register;
        RegistrationRemote r_reg =  (RegistrationRemote) this.registry.lookup("Reg");

        try {
            this.allPlayer.addAll(r_reg.register(this.me));
        } catch (CantAddPlayerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        maxplayer = this.allPlayer.size();



        cm = new CrashManager(this.me);
        cm.initializeCrashManager();
        cm.startTimedController();
    }

    @Override
    public void broadcast(Message msg) throws NotBoundException {
        //System.out.println(msg.uuid+" Sta facendo Broadcast");
        if (msg.uuid != this.me.getUuid()) {
            try {

                if (msg.content instanceof String) //Debug
                    System.out.println(msg.content.toString());

                if (msg.content instanceof Board) {//StartGame
                    this.b = (Board) msg.content;
                } else if (msg.content instanceof int[]) {//mangio pedina
                    int[] m = (int[]) msg.content;
                    b.setStatus(m[0], m[1], m[2]);
                } if (msg.content instanceof Integer) {//mangio pedina
                    int m = (Integer) msg.content;
                    b.currP = m;
                }

                getNext().broadcast(msg);
            } catch (RemoteException e) {
                cm.repairAndBroadcastPlayerList();
                this.broadcast(msg);
            }
        }
    }

    public void startBroadcast(Message msg) throws NotBoundException {
        msg.uuid = me.getUuid();
        try {
            this.getNext().broadcast(msg);
        } catch (RemoteException e) {
            cm.repairAndBroadcastPlayerList();
            this.startBroadcast(msg);
        }
    }

    public NodeRemote getNext() throws NotBoundException, RemoteException {
        Player nextPlayer = this.allPlayer.getNext(this.me);
        Registry register = null;

        register = LocateRegistry.getRegistry(nextPlayer.getIpAddress(),nextPlayer.getPort());
        NodeRemote rem = (NodeRemote) register.lookup("Node");

        return rem;
    }

    public void sendNext(Object msg) throws NotBoundException {
        try{
            getNext().receive(msg);
        } catch (RemoteException e) {
                cm.repairAndBroadcastPlayerList();
                this.sendNext(msg);
        }
    }

    public void receive(Object msg) throws RemoteException {
        int i = 1;
    }

    /**
     * Inizia il gioco
     * Se il nodo è il primo della lista crea la board
     * e la passa agli altri
     */
    public void startGame() {
        b = new Board();
        b.initRandomBoard(allPlayer);
        Message msg = new Message(me.getUuid());
        msg.content = b;
        try {
            startBroadcast(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendMove(int r, int c, int uuid) {
        Message msg = new Message(me.getUuid());
        msg.content = new int[] {r, c, uuid};
        try {
            startBroadcast(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void sendToken(Integer currP) {
        Message msg = new Message(me.getUuid());
        msg.content = currP;
        try {
            startBroadcast(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

