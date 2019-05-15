package IfThenLearning;

import java.util.HashMap;
import java.util.Iterator;

import static IfThenLearning.Game.boardWidth;
import static IfThenLearning.Game.boardHeight;

public class State implements Iterable<Byte[]> {
    private static HashMap<Integer, ByteArray> pool = new HashMap<>();
    private final int hash;

    private State(int hash) {
        this.hash = hash;
    }

    public State(byte[][] board) {
        if (board.length != boardWidth)
            throw new IllegalArgumentException(
                    "Illegal board width, expected: " + boardWidth + ", got: " + board.length);
        if (board[0].length != boardHeight)
            throw new IllegalArgumentException(
                    "Illegal board height, expected: " + boardHeight + ", got: " + board[0].length);
        hash = Game.hasher.getStateHash(board);
        if (!pool.containsKey(hash))
            pool.put(hash, new ByteArray(hash, board));
    }

    public byte get(int x, int y) {
        return pool.get(hash).array[x][y];
    }

    public State clone() {
        return new State(hash);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    private class ByteArray {
        private final byte[][] array;
        private int hashCode;

        private ByteArray(int hashCode, byte[][] array) {
            this.hashCode = hashCode;
            this.array = array;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof ByteArray)
                return hashCode == ((ByteArray) o).hashCode;
            return false;
        }
    }

    @Override
    public Iterator<Byte[]> iterator() {
        return new Iterator<Byte[]>() {
            private byte x = 0;
            private byte y = 0;

            @Override
            public boolean hasNext() {
                try {
                    byte _ = pool.get(hash).array[x][y];
                    return true;
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }

            @Override
            public Byte[] next() {
                Byte[] output;
                try {
                    output = new Byte[]{pool.get(hash).array[x][y], x, y};
                    y++;
                    if (y >= boardHeight && x < boardWidth) {
                        y = 0;
                        x++;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return null;
                }
                return output;
            }
        };
    }
}
