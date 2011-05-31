package com.github.benve.othellomultiplayer.network;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: giacomo
 * Date: 5/31/11
 * Time: 9:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Message implements Serializable {

    public int uuid;

    public Object content;

}
