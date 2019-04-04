package IfThenLearning;

import java.util.HashMap;

public class State {
    private static HashMap<Integer, ByteArray> pool = new HashMap<>();
    private final int hash;

    private State(int hash) {
        this.hash = hash;
    }

    public State(byte[][] board) {
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
}
