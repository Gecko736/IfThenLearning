public class Move {
    private final byte[] move;

    public Move(byte[] move) {
        this.move = move;
    }

    public byte get(int index) {
        return move[index];
    }
}
