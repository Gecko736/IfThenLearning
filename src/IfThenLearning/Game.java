package IfThenLearning;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public abstract class Game extends Thread {
    public static int boardWidth;
    public static int boardHeight;
    public static int moveSize;
    public static Hasher hasher;

    public interface Hasher {
        int getStateHash(byte[][] state);
        int getMoveHash(byte[] move);
    }

    private final Player p1;
    private final Player p2;

    private HashMap<IfThenLearning.State, Integer> p1Moves = new HashMap<>();
    private HashMap<IfThenLearning.State, Integer> p2Moves = new HashMap<>();

    public Game(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public abstract IfThenLearning.State getInitialState();

    private Status status = Status.UNPLAYED;
    public IfThenLearning.State currentState = getInitialState();
    private byte currentPlayer;

    private final Semaphore guard = new Semaphore(1);

    @Override
    public void run() {
        try {
            guard.acquire();
            p1.initiate();
            p2.initiate();
            status = Status.IN_PROGRESS;

            Move[] legalMoves = getLegalMoves();
            int move;
            while (status == Status.IN_PROGRESS) {
                currentPlayer = 1;
                move = p1.move(currentState, legalMoves.length);
                legalMoves = applyFlipCheck(legalMoves[move]);
                if (status == Status.IN_PROGRESS) {
                    currentPlayer = 2;
                    move = p2.move(currentState, legalMoves.length);
                    legalMoves = applyFlipCheck(legalMoves[move]);
                }
            }
            switch (status) {
                case P1WON:
                    p1.won(p1Moves);
                    p2.lost();
                    break;
                case P2WON:
                    p1.lost();
                    p2.won(p2Moves);
                    break;
                case TIE:
                    p1.lost();
                    p2.lost();
                    break;
            }
            guard.release();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private Move[] applyFlipCheck(Move move) {
        newState(move);
        flipPerspective();
        Move[] legalMoves = getLegalMoves();
        if (legalMoves.length == 0) {
            currentPlayerWon();
            return legalMoves;
        }
        checkGameOver();
        return legalMoves;
    }

    public abstract void newState(Move move);

    public abstract void flipPerspective();

    public abstract Move[] getLegalMoves();

    public abstract void checkGameOver();

    public void currentPlayerWon() {
        switch (currentPlayer) {
            case 1: status = Status.P1WON; break;
            case 2: status = Status.P2WON; break;
        }
    }

    @SuppressWarnings("Duplicates")
    public Player getWinner() throws GameOverException, InterruptedException {
        guard.acquire();
        guard.release();
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }

    @SuppressWarnings("Duplicates")
    public Player getLoser() throws GameOverException, InterruptedException {
        guard.acquire();
        guard.release();
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }

    public class GameOverException extends Throwable {
        private final Status status;

        public GameOverException(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }
    }

    public enum Status { UNPLAYED, IN_PROGRESS, P1WON, P2WON, TIE }
}
