package com.github.benve.othellomultiplayer.game;

import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: giacomo
 * Date: 6/3/11
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BoardLogicTest {

    Board board;

    BoardLogic boardLogic;

    private static final int P0 = 0;
    private static final int P1 = 1;
    private static final int P2 = 2;

    @Before
    public void setUp() throws Exception {
        board = new Board(4, 4);

        board.setBoard(new int[][]{
                {-1, -1, -1, -1},
                {-1,  P0,  P1, -1},
                {-1,  P1,  -1, -1},
                {-1, -1, -1, P2}
        });

        boardLogic = BoardLogic.getInstance();
    }

    @Test
    public void testCanColonize() throws Exception {
        assertTrue(
                boardLogic.canColonize(board,
                        0, 1, P0)
        );
        //Casella non adiacente
        assertFalse(
                boardLogic.canColonize(board,
                        0, 1, P2)
        );
        //Casella già occupata
        assertFalse(
                boardLogic.canColonize(board,
                        2, 1, P0)
        );
        //Casella già occupata da me
        assertFalse(
                boardLogic.canColonize(board,
                        1, 1, P0)
        );
    }

    @Test
    public void testGetSingleReversi() throws Exception {
        boolean[][] res = boardLogic.getSingleReversi(board,
                0, 1, P1);

        boolean[][] exp = new boolean[][]{
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };

        testMatrix(exp, res);

    }

    private void testMatrix(boolean [][] exp, boolean [][] act) {
        for (int i = 0; i < act.length; i++) {
            for (int j = 0; j < act[i].length; j++) {
                assertEquals(i+" "+j, act[i][j], exp[i][j]);
            }

        }
    }

    @Test
    public void testHasReversi() throws Exception {
        assertTrue(
                boardLogic.hasReversi(board,
                         P0)
        );
        assertFalse(
                boardLogic.hasReversi(board,
                         P2)
        );
    }

    @Test
    public void testGetAllReversi() throws Exception {

    }

    @Test
    public void testCanReversi() throws Exception {

    }

    @Test
    public void testMoveReversi() throws Exception {

    }

    @Test
    public void testColonizeField() throws Exception {

    }

    @Test
    public void testHasColonize() throws Exception {

    }

    @Test
    public void testGetAllColonize() throws Exception {
        boolean[][] res = boardLogic.getAllColonize(board, P1);

        boolean[][] exp = new boolean[][]{
                {false, true, true, true},
                {true, false, false, true},
                {true, false, true, true},
                {true, true, false, false}
        };

        testMatrix(exp, res);


    }

    @After
    public void tearDown() throws Exception {

    }
}
