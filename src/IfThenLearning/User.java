package IfThenLearning;

import java.util.HashMap;

public class User implements Player {
    private static User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    @Override
    public String getID() {
        return "User";
    }

    @Override
    public void initiate() {}

    @Override
    public double getWinRate() {
        return 0;
    }

    @Override
    public int move(State state, int numOfLegalMoves) {
        return Game.view.getUserMove(state);
    }

    @Override
    public void won(HashMap<State, Integer> moves) {
        System.out.println("You win!");
    }

    @Override
    public void lost() {
        System.out.println("You lose");
    }

    @Override
    public void tied() {
        System.out.println("Stalemate");
    }
}
