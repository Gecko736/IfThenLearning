public class TicTacToe implements Game {
    private Game.Status status = Status.UNPLAYED;
    private final Player p1;
    private final Player p2;

    private static State initialState = new State(new byte[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});

    public TicTacToe(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void run() {
        status = Status.IN_PROGRESS;

        State state = initialState;
    }

    @Override
    public Player getWinner() throws GameOverException {
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }

    @Override
    public Player getLoser() throws GameOverException {
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }
}
