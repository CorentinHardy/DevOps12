package fr.unice.polytech.ogl.isldc.testAuto;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Stack;

import org.junit.Test;

import fr.unice.polytech.ogl.isldc.automate.Auto;
import fr.unice.polytech.ogl.isldc.automate.MoveAuto;

public class TestMoveAuto {
    // TT is half of the size of the test's map
    public final static int TT = 5;
    public static final String[] BIOME_OCEAN = {"OCEAN"},
            BIOME_FOREST = {"FOREST"};

    private Auto generalAuto = new Auto();
    private MoveAuto moveAI;
    private int initX = -3, initY = -3;

    // if altitude >= -1, all is right.
    // else if altitude == -2, it is lost in the ocean,
    // if altitude == -3 it is in a unreachable, and
    // if altitude == -4, something really bad happened, it teleport perhaps ?
    public static final int[][] ALTITUDE = {
            {-2, -1, -2, -4, -2, -2, -2, -4, -3, -3, -3},
            {-1, 1, -1, -2, -1, -1, -1, -2, -3},
            {-1, 1, -1, -1, 1, 2, 3, -1, -3},
            {-1, 1, 2, 4, 2, -1, -1, -2, -3},
            {-2, -1, -1, -1, -1, -1, 2, -1, -3},
            {-4, -2, -2, -2, -1, -1, 1, -1, -3},
            {-4, -4, -2, -1, 0, 1, 2, -1, -3},
            {-4, -4, -4, -2, 1, -1, -1, -2, -3},
            {-4, -4, -4, -4, -4, -2, -2, -4, -3}, {-3}, {-3}};

    private int[][] orderWent = new int[11][11];

    /**
     * we initialize a short map. This map 's tile 's altitude is describe by the int[][] altitude. Tile with a altitude > 0 have WOOD inside, and tile  < 0 have FISH.
     *
     * @param resourceObjective we tell to generalAuto that this is a objective of 600.
     * @throws ParseException if their are a problem in altitude.
     */
    public void init(String resourceObjective, int[][] altitude) throws ParseException {
        generalAuto
                .start("{\"creek\": \"creekId\", \"budget\": 600, \"men\": 50, \"objective\": [ { \"resource\": \""
                        + resourceObjective + "\", \"amount\": 600 } ] }");
        moveAI = new MoveAuto(generalAuto);
        int curAlt;
        // we initialize a map of TT x TT
        for (int x = generalAuto.getX() - TT; x < TT; x++) {
            for (int y = generalAuto.getY() - TT; y < TT; y++) {
                if (x < -4 || x > 4 || y < -4 || y > 4) {
                    generalAuto.getMap().addCase(x, y, -3, false);
                } else {
                    curAlt = altitude[x + TT - 1][y + TT - 1];
                    generalAuto.getMap().addCase(x, y, curAlt, true);
                    if (curAlt < 0) {
                        generalAuto.getMap().getCase(x, y)
                                .addScoutedResource("FISH");
                        generalAuto.getMap().getCase(x, y).addBiome(BIOME_OCEAN);
                    } else if (curAlt > 0) {
                        generalAuto.getMap().getCase(x, y).addBiome(BIOME_FOREST);
                        generalAuto.getMap().getCase(x, y)
                                .addScoutedResource("WOOD");
                    }
                }
            }
        }
        for (int k = 0; k < 11; k++) {
            for (int m = 0; m < 11; m++) {
                orderWent[k][m] = 0;
            }
        }
    }

    /**
     * Test the altitude security of FindBestTile. We have to avoid the sea. It is a unique case of FindBestTile and this can do a 'while true' if we change the automate.ALL_DIRECTION, or the ALTITTUDE. Work with wood only.
     *
     * @throws ParseException
     */
    @Test
    public void testFindBestTile() throws ParseException {
        // this test havn't to work with FISH, only WOOD.
        init("WOOD", ALTITUDE);
        int stop = TT * TT * TT, i = 0, x = initX, y = initY;
        char direction;
        boolean notFinish = true;
        while (notFinish) {
            if (i >= stop) {
                // we have to stop we have fail.
                printTab(ALTITUDE);
                fail("too many times");
            }
            direction = moveAI.findBestTile();
            x = Auto.switchX(x, direction);
            y = Auto.switchY(y, direction);
            orderWent[x + TT][y + TT] = i + 1;
            if (generalAuto.getMap().getCase(x, y).getAltitude() <= -2) {
                // we fail, so we try to print why
                printTab(ALTITUDE);
                switch (generalAuto.getMap().getCase(x, y).getAltitude()) {
                    case -2:
                        fail("lost in the ocean");
                    case -3:
                        fail("lost in a ureacheable place");
                    case -4:
                        fail("lost in a impossible place");
                    default:
                        fail("it is not possible, lost in no place known");
                }
            }
            generalAuto.setX(x);
            generalAuto.setY(y);
            generalAuto.getMap().getCase(x, y).setExploited(true);
            i++;
            if (x == 3 && y == 0) {
                notFinish = false;
            }
        }
    }

    /**
     * Useful for print orderWent, if we fail the test.
     */
    private void printTab(int[][] altitude) {
        for (int k = 0; k < 11; k++) {
            for (int m = 0; m < 11; m++) {
                if (k >= TT)
                    System.out.print(" [ " + (k - TT) + "]");
                else
                    System.out.print(" [" + (k - TT) + "]");
                if (m >= TT)
                    System.out.print("[" + (m - TT) + "] ");
                else
                    System.out.print("[" + (m - TT) + "] ");
                if (orderWent[k][m] < 10)
                    System.out.print(" ");
                if (orderWent[k][m] < 100)
                    System.out.print(" ");
                try {
                    if (altitude[k - 1][m - 1] >= 0)
                        System.out.print("| " + altitude[k - 1][m - 1]);
                    else
                        System.out.print("|" + altitude[k - 1][m - 1]);
                } catch (Exception e) {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }

    /**
     * simply test that we can go to a position, without any problems.
     *
     * @throws ParseException
     */
    @Test
    public void testGotoTile() throws ParseException {
        init("WOOD", ALTITUDE);
        final int DEST_X = 4, DEST_Y = 3;
        int x = initX, y = initY, total = Math.abs(x - DEST_X) + Math.abs(y - DEST_Y), precTotal = total;
        char direction;
        generalAuto.setX(x);
        generalAuto.setY(y);
        assertEquals(generalAuto.getX(), x);
        assertEquals(generalAuto.getY(), y);
        // we want to go to DEST X/Y with gotoTile, we load a stack.
        try {
            generalAuto.getMoveAi().gotoTile(DEST_X, DEST_Y);
        } catch (Exception e) {
            fail("exception catch: " + e.getMessage());
        }
        Stack stack = generalAuto.getMoveAi().cloneToGoto();
        // now the stack isn't empty:
        assertTrue(!stack.empty());
        // and we do the movement.
        int i = 1;
        orderWent[x + TT][y + TT] = i++;
        while (!stack.empty()) {
            direction = (char) stack.pop();
            x = Auto.switchX(x, direction);
            y = Auto.switchY(y, direction);
            // we look at the distance we haven't go yet.
            total = Math.abs(x - DEST_X) + Math.abs(y - DEST_Y);
            if (total > precTotal)
                fail("we don't move in a good way.");
            precTotal = total;
//            System.out.println("x:" + x + " y:" + y + " t:" + total + " p:" + precTotal + " a:" + generalAuto.getMap().getCase(x, y).getAltitude());
            orderWent[x + TT][y + TT] = i++;
        }
        // if it work, we test that we are arrived
        assertEquals(DEST_X, x);
        assertEquals(DEST_Y, y);
    }

    /**
     * We begin in a "while true" spot a we have to go to the tile which have a altitude of 2.
     *
     * @throws ParseException
     */
    @Test
    public void testMove() throws ParseException {
        // we have to go to the tile which have a altitude of 2.
        final int[][] ALTITUDE_MOVE = {
                {-2, -1, -2, -4, -2, -2, -2, -4, -3, -3, -3},
                {-1, 9, -1, -2, -1, -1, 7, 7, -3},
                {-1, 7, 7, -1, 6, 7, 7, 7, -3},
                {-1, 7, 7, 7, 6, -1, -1, -2, -3},
                {-2, -1, -1, -1, -1, -1, 7, 7, -3},
                {-4, 6, 6, -1, 1, -1, 6, 6, -3},
                {-4, -4, -2, -1, -1, -1, 6, 6, -3},
                {-4, -4, -4, -2, 6, -1, 6, 2, -3},
                {-4, -4, -4, -4, -4, -1, -2, -4, -3},
                {-3},
                {-3}};
        init("WOOD", ALTITUDE_MOVE);
        // We begin at the tile which have a altitude of 1.
        generalAuto.setX(1);
        assertEquals(1, generalAuto.getMap().getCase(generalAuto.getX(), generalAuto.getY()).getAltitude());
        // normally, we identify the "boucle" in 2 move,
        // and we find the best tile in 4, 5 or 6 move.

        // So we begin with 6 move,
        for (int i = 0; i < 6; i++) {
            moveAI.bestMove();
        }
        if (generalAuto.getMap().getCase(generalAuto.getX(), generalAuto.getY()).getAltitude() != 2) {
            // there are 2 move max left.
            for (int i = 0; i < 2; i++) {
                moveAI.bestMove();
                if (generalAuto.getMap().getCase(generalAuto.getX(), generalAuto.getY()).getAltitude() == 2)
                    break;
            }
        }
        // We have to be at the tile of a altitude of 2.
        assertEquals(2, generalAuto.getMap().getCase(generalAuto.getX(), generalAuto.getY()).getAltitude());
    }
}
