package IfThenLearning;

import java.util.HashMap;

public interface Player {
    String getID();
    void initiate();
    double getWinRate();
    int move(State state, int numOfLegalMoves);
    void won(HashMap<State, Integer> moves);
    void lost();
    void tied();
}
