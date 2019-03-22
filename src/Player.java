public interface Player {
    void initiate();
    double getWinRate();
    int move(State state, int numOfLegalMoves);
    void won();
    void lost();
}
