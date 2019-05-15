package IfThenLearning;

import java.util.HashMap;

public class Dummy implements Player {
    private static Dummy ourInstance = new Dummy();

    public static Dummy getInstance() {
        return ourInstance;
    }

    private Dummy() {}

    @Override
    public String getID() {
        return "Dummy";
    }

    @Override
    public void initiate() {}

    @Override
    public double getWinRate() {
        return 0;
    }

    @Override
    public synchronized int move(State state, int numOfLegalMoves) {
        return (int) (Math.random() * numOfLegalMoves);
    }

    @Override
    public void won(HashMap<State, Integer> moves) {}

    @Override
    public void lost() {}

    @Override
    public void tied() {}
}
