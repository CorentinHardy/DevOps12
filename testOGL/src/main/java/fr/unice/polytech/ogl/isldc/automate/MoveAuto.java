package fr.unice.polytech.ogl.isldc.automate;

import java.util.Stack;

import org.json.JSONException;

import fr.unice.polytech.ogl.isldc.map.IslandTile;

/**
 * Moving action : used to move between glimpsed/scouted tiles
 * 
 * @author user
 * 
 */
public class MoveAuto extends ActionAuto {
    public static final String MOVE = "move_to";
    private Stack<Character> toGoto = new Stack<>(); // contain the next direction we have to go to.
    private IslandTile[] previousTiles; // useful for detect a while between 2 tile, for the automate.
    private int numPreviousTile; // it is where we are in "previousTiles".
    private static final int NUM_PREVIOUS = 3, SIZE = Auto.ALL_DIRECTION.length,
            STOP = 6, WANTED_SCORE = 10;

    public MoveAuto(Auto auto) {
        super(auto);
        previousTiles = new IslandTile[NUM_PREVIOUS];
    }

    /**
     * Move a group to another tile, for this IA. Need direction. Change values
     * of x, y, and prevAction.
     * 
     * @return message for Explorer, in JSon.
     */
    public String actionMove(char dir) {
        getAI().setDirection(dir);
        getAI().setX(Auto.switchX(getAI().getX(), dir, 1));
        getAI().setY(Auto.switchY(getAI().getY(), dir, 1));
        getAI().setPrevAction(MOVE);
        return "{ \"action\": \"" + MOVE
                + "\", \"parameters\": {\"direction\": \"" + dir + "\" } }";
    }

    /**
     * If the previous action the automate performed is moving
     * 
     * @throws JSONException
     */
    public void resultsMove() throws JSONException {
        getAI().addMovePoint(getAI().getJson().getInt("cost"));
        getAI().updateAverageMoveCost(getAI().getJson().getInt("cost"));
    }

    /**
     * Go to the best tile calculate by findBestTile. If the previous tile are
     * the same, we find a better tile near the current tile (with bestMapTile).
     * 
     * @return the move we have to do.
     */
    public String bestMove() {
        if (!toGoto.empty())
            return actionMove(toGoto.pop());
        char next = findBestTile();
        previousTiles[numPreviousTile++] = getAI().getMap().getCase(
                Auto.switchX(getAI().getX(), next),
                Auto.switchY(getAI().getY(), next));
        numPreviousTile = numPreviousTile % NUM_PREVIOUS;

        if (previousTiles[numPreviousTile] == previousTiles[(numPreviousTile + 2)
                % NUM_PREVIOUS]) {
            int[] tile = bestMapTile(getAI().getX(), getAI().getY(), WANTED_SCORE);
            gotoTile(tile[0], tile[1]);
            return actionMove(toGoto.pop());
        } else {
            return actionMove(next);
        }
    }

    /**
     * We look at the best tile near us. We stop when the distance is greater
     * than STOP, or when we have a score greater than wantedScore.
     * 
     * @param beginX
     *            the x coordinate where we begin.
     * @param beginY
     *            the y coordinate where we begin.
     * @param wantedScore
     *            best score we prefer.
     * @return the coordinate x, y.
     */
    private int[] bestMapTile(int beginX, int beginY, int wantedScore) {
        return bestMapTile(beginX,beginY, wantedScore, STOP);
    }

    private int[] bestMapTile(int beginX, int beginY, int wantedScore, int searchStop) {
        // you cannot use the method findBestTile with this method.
        int x, y, currentScore = Integer.MIN_VALUE, bestScore = currentScore, level = 1, stop, iteratX, iteratY;
        int[] bestCoordinate = new int[2];
        bestCoordinate[0] = beginX;
        bestCoordinate[1] = beginY;

        // we want to go around the actual tile, to find the best one
        while (level < searchStop) {
            // number of current tile
            stop = level * SIZE;
            iteratX = -1;
            iteratY = -1;
            // we begin again perhaps
            x = --beginX;
            y = beginY;
            for (int i = 0; i < stop; i++) {
                // we find the direction we want to go next
                if ((i % level) == 0) {
                    if ((i % (level * 2)) == 0) {
                        if (iteratX == 1)
                            iteratX = -1;
                        else
                            iteratX = 1;
                    } else {
                        if (iteratY == 1)
                            iteratY = -1;
                        else
                            iteratY = 1;
                    }
                }
                // then we calculate the score of the actual tile.
                try {
                    currentScore = getAI().calculateScore(
                            getAI().getMap().getCase(x, y));
                    if (currentScore >= bestScore) {
                        bestCoordinate[0] = x;
                        bestCoordinate[1] = y;
                        if (currentScore >= wantedScore)
                            return bestCoordinate;
                        bestScore = currentScore;
                    }
                } catch (Exception e) {
                    // if there is a problem, we stop this search.
                    return bestCoordinate;
                } finally {
                    x += iteratX;
                    y += iteratY;
                }
            }
            level++;
        }
        return bestCoordinate;
    }

    /**
     * Search the best tile near us.
     * 
     * @return the best direction to choose in order to move
     */
    public char findBestTile() {
        try {
            return findBestTile(Auto.ALL_DIRECTION);
        }catch(Where_to_go_Exception w){
            return idMax(Auto.ALL_DIRECTION, w);
        }
    }

    /**
     * Search the best tile in tabDirection.
     *
     * @param tabDirection a list of direction, like Auto.ALL_DIRECTION
     * @return the best direction to choose in order to move
     * @throws Where_to_go_Exception
     */
    private char findBestTile(char[] tabDirection) throws Where_to_go_Exception{
        int sizeMax = tabDirection.length;
        int[] currentScore = new int[sizeMax];
        boolean[] isSafe = new boolean[sizeMax];
        IslandTile currentCase;
        // we see the four cases near us:
        for (int i = 0; i < sizeMax; i++) {
            // we control that this case already exist:
            currentCase = getAI().getMap().getCase(
                    Auto.switchX(getAI().getX(), tabDirection[i], 1),
                    Auto.switchY(getAI().getY(), tabDirection[i], 1));
            if (currentCase != null && currentCase.isReachable()) {
                // so we calculate their score and see if they are safe.
                currentScore[i] = getAI().calculateScore(currentCase);
                isSafe[i] = !currentCase.isOcean();
            } else {
                // so we tell it is the worst place to go
                currentScore[i] = Integer.MIN_VALUE;
                isSafe[i] = false;
            }
            /*
             * we don't sort scores right now, because we want to know all the
             * tile's state for ocean.
             */
        }
        // we initialize scoreMax a the minimum value. that way he have to be
        // replaced by the best case.
        int scoreMax = 0;
        // if we are already in a unsafe place, we want to go to a safe place:
        boolean safe = ! getAI().getMap().getCase(getAI().getX(), getAI()
                    .getY()).isOcean();
        // after that, we search the best:
        if (safe) {
            for (int i = 0; i < sizeMax; i++) {
                // we want the highest score
                if (currentScore[i] > currentScore[scoreMax])
                    scoreMax = i;
            }
        } else {
            // we want to go to a safe place, whatever the score is.
            boolean nowSafe = false;
            for (int i = 0; i < sizeMax; i++)
                if (isSafe[i]) {
                    if (nowSafe) {
                        // if we now we have a case of security, we can look at
                        // the scores.
                        if (currentScore[i] > currentScore[scoreMax])
                            scoreMax = i;
                    } else {
                        nowSafe = true;
                        scoreMax = i;
                    }
                }
        }
        // then we see if there was a problem
        if (currentScore[scoreMax] < 0){
            // we throw a Exception, for telling to other method
            //  that we don't know exactly where to go.
            for(int i = 0; i < sizeMax; i++)
                if(currentScore[i] == currentScore[scoreMax])
                    throw new Where_to_go_Exception(currentScore, scoreMax);
        }
        return tabDirection[scoreMax];
    }

    /**
     * useful if you have a Where_to_go_Exception. We take near tile, and we tell what is the good direction for going here.
     *
     * @param directions the list of direction
     * @param w a Where_to_go_Exception, produce by findBestTile
     * @return a tile (perhaps bad)  where you should have next to it a better tile.
     */
    private char idMax(char[] directions, Where_to_go_Exception w){
        int[] best = bestMapTile(this.getAI().getX(), this.getAI().getY(), WANTED_SCORE, 2);
        int coordX = this.getAI().getX() - best[0], coordY = this.getAI().getY() - best[1];
        for(char di : directions) {
            if ((Auto.switchX(0, di) == coordX) && (Auto.switchY(0, di) == coordY))
                return di;
        }
        return directions[w.getScoreMax()];
    }

    /**
     * define a way to go to the tile in x and y in toGoto.
     * 
     * @param x
     *            the position we want to go for axe x
     * @param y
     *            the position we want to go for axe y
     */
    public void gotoTile(int x, int y) {
        int actX = getAI().getX(), // we want our current position
        actY = getAI().getY(), //
        directX = ((actX - x) > 0) ? 1 : -1, // we want the good direction
        directY = ((actY - y) > 0) ? 1 : -1, // in int to find it in char
        sizeMax = Auto.ALL_DIRECTION.length;  // size of all the directions
        final int sizeGood = 2; // dimension of the map

        // always goodDirec[0] -> x and goodDirec[1] -> y, and it is important
        char[] goodDirec = new char[sizeGood];
        char currentDirec;

        // first, we identify the good direction
        for (int i = 0; i < sizeMax; i++) {
            if (Auto.switchX(0, Auto.ALL_DIRECTION[i]) == directX)
                goodDirec[0] = Auto.ALL_DIRECTION[i];
            if (Auto.switchY(0, Auto.ALL_DIRECTION[i]) == directY)
                goodDirec[1] = Auto.ALL_DIRECTION[i];
        }

        try {
            // secondly we try to find the best way between the 2 choices we
            // have
            while (x != actX && y != actY) {
                try {
                    currentDirec = findBestTile(goodDirec);
                }catch(Where_to_go_Exception w){
                    currentDirec = idMax(goodDirec, w);
                }
                x = Auto.switchX(x, currentDirec);
                y = Auto.switchY(y, currentDirec);
                toGoto.push(Auto.invertDirection(currentDirec));
            }

            // and third, we go in straight line
            while (x != actX) {
                toGoto.push(Auto.invertDirection(goodDirec[0]));
                x += directX;
            }
            while (y != actY) {
                toGoto.push(Auto.invertDirection(goodDirec[1]));
                y += directY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // there is a unknow bug or there is a modification in ALL_DIRECTION
            // without modification of invertDirection.
            throw new RuntimeException(e.getMessage());
        }
    }

    public Stack cloneToGoto() {
        return (Stack) toGoto.clone();
    }

    /**
     * a Exception produce by findBestTile. Contain all the scoreof the nearest tile, and one of the best score.
     */
    private class Where_to_go_Exception extends java.lang.Exception{
        private int[] scores;
        private int scoreMax;

        /**
         * scores is a list of score, and scoreMax is the number of a highest score in this list
         *
         * @param scores list of score
         * @param scoreMax number of a highest score
         */
        public Where_to_go_Exception(int[] scores, int scoreMax){
            this.scores = scores;
            this.scoreMax = scoreMax;
        }

        public int[] getScores(){
            return scores;
        }

        public int getScoreMax(){
            return scoreMax;
        }

        @Override
        public boolean equals(Object obj){
            if (this == obj)
                return true;
            if(!(obj instanceof Where_to_go_Exception))
                return false;
            Where_to_go_Exception ex = (Where_to_go_Exception)obj;
            if (this.scores.length != ex.getScores().length)
                return false;
            for(int i = 0; i < scores.length; i++)
                if (this.scores[i] != ex.getScores()[i])
                    return false;
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = hash * 13 + numPreviousTile;
            hash = hash * 27 + previousTiles.hashCode();
            hash = hash * 35 + toGoto.hashCode();
            return hash;
        }
    }
}
