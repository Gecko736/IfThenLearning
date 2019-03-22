public abstract class Game implements Runnable {
    public static int boardWidth;
    public static int boardHeight;
    public static int moveSize;
    public Status status = Status.UNPLAYED;

    final Player p1;
    final Player p2;

    public Game(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Player getWinner() throws GameOverException {
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }

    public Player getLoser() throws GameOverException {
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }

    public static Hasher hasher;

    public interface Hasher {
        int getStateHash(byte[][] state);
        int getMoveHash(byte[] move);
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
