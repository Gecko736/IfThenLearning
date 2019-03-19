public interface Game extends Runnable {
    Player getWinner() throws GameOverException;

    Player getLoser() throws GameOverException;

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
