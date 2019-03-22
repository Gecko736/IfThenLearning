public class Dummy implements Player {
    private static Dummy ourInstance = new Dummy();

    public static Dummy getInstance() {
        return ourInstance;
    }

    private Dummy() {}

    @Override
    public void initiate() {}

    @Override
    public double getWinRate() {
        return 0;
    }

    @Override
    public int move(State state, int numOfLegalMoves) {
        return (int) (Math.random() * numOfLegalMoves);
    }

    @Override
    public void won() {}

    @Override
    public void lost() {}
}
