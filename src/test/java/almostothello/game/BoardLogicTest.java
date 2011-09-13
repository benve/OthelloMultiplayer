package almostothello.game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: giacomo
 * Date: 6/3/11
 * Time: 11:15 AM
 */
public class BoardLogicTest {

    Board board;

    BoardLogic boardLogic;

    private static final int P1 = 1;
    private static final int P2 = 2;
    private static final int P3 = 3;

    @Before
    public void setUp() throws Exception {
        board = new Board(4);

        board.setBoard(new int[][]{
                {-1, -1, -1, -1},
                {-1,  P3,  P1, -1},
                {-1,  P1,  -1, -1},
                {-1, -1,  -1, P2}
        });

        boardLogic = BoardLogic.getInstance();
    }

    @Test
    public void testCanColonize() throws Exception {
        assertTrue(
                boardLogic.canColonize(board, 0, 1, P3)
        );
        //Casella non adiacente
        assertFalse(
                boardLogic.canColonize(board, 0, 1, P2)
        );
        //Casella già occupata
        assertFalse(
                boardLogic.canColonize(board, 2, 1, P3)
        );
        //Casella già occupata da me
        assertFalse(
                boardLogic.canColonize(board, 1, 1, P3)
        );
    }

    @Test
    public void testGetSingleReversi() throws Exception {
        boolean[][] res = boardLogic.getSingleReversi(board, 0, 1, P1);

        boolean[][] exp = new boolean[][]{
                {false, true, false, false},
                {false, true, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };

        testMatrix(exp, res);

        res = boardLogic.getSingleReversi(board, 0, 3, P3);

        exp = new boolean[][]{
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };

        testMatrix(exp, res);

        res = boardLogic.getSingleReversi(board, 2, 2, P2);

                exp = new boolean[][]{
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

    private void testMatrix(int [][] exp, int [][] act) {
        for (int i = 0; i < act.length; i++) {
            for (int j = 0; j < act[i].length; j++) {
                assertEquals(i+" "+j, act[i][j], exp[i][j]);
            }

        }
    }


    @Test
    public void testHasReversi() throws Exception {
        //assertTrue(boardLogic.hasReversi(board, P3));
        assertFalse(
                boardLogic.hasReversi(board, P2)
        );
    }

    @Test
    public void testGetAllReversi() throws Exception {

        boolean[][] exp = new boolean[][]{
                {false, false, false, false},
                {false, false, false, true},
                {false, false, false, false},
                {false, true, false, false}
        };

        boolean[][] act = boardLogic.getAllReversi(board, P3);

        testMatrix(exp, act);
    }

    @Test
    public void testCanReversi() throws Exception {

        boolean[][] exp = new boolean[][]{
                {false, true, false, false},
                {false, true, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };

        assertTrue(boardLogic.canReversi(4,4,exp));

        exp = new boolean[][]{
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false},
                {false, false, false, false}
        };

        assertFalse(boardLogic.canReversi(4,4,exp));

    }

    @Test
    public void testMoveReversi() throws Exception {
        Board board2 = new Board(4);

        board2.setBoard(new int[][]{
                {P2, P2,   P1, -1},
                {P1,  P3,  P1, -1},
                {P1,  P1,  P1, -1},
                {-1, -1,  -1, P2}
        });

        boolean[][] move = new boolean[][]{
                {false, false, false, false},
                {false, false, false, true},
                {false, false, false, false},
                {false, true, false, false}
        };


        boardLogic.moveReversi(board2, move, P3);



        testMatrix(new int[][]{
                {P2, P2,   P1, -1},
                {P1,  P3,  P1, P3},
                {P1,  P1,  P1, -1},
                {-1, P3,  -1, P2}
        }, board2.board);
    }

    @Test
    public void testHasColonize() throws Exception {
        assertTrue(boardLogic.hasColonize(board, P1));

        Board board2 = new Board(4);

        board2.setBoard(new int[][]{
                {P2, P2, P1, -1},
                {P1,  P3,  P1, -1},
                {P1,  P1,  P1, -1},
                {-1, -1,  -1, P2}
        });


        assertFalse(boardLogic.hasColonize(board2,P3));
    }

    @Test
    public void testGetAllColonize() throws Exception {
        boolean[][] res = boardLogic.getAllColonize(board, P1);

        boolean[][] exp = new boolean[][]{
                {false, true, true, true},
                {true, false, false, true},
                {true, false, true, true},
                {true, true, true, false}
        };

        testMatrix(exp, res);


    }

    @Test
    public void testGetWinner() {

        assertEquals(-1, boardLogic.getWinner(board));

        board.setBoard(new int[][]{
                {P1, P1, P1, P3},
                {P1,  P3,  P1, P3},
                {P1,  P1,  P1, P1},
                {P1, P1,  P1, P2}
        });


        assertEquals(P1, boardLogic.getWinner(board));

    }

    /*@After
    public void tearDown() throws Exception {

    }  */
}
