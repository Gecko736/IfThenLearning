package IfThenLearning;

import java.util.HashMap;

public class Move {
    private static HashMap<Integer, ByteArray> pool = new HashMap<>();
    private final int hash;

    public Move(byte... move) {
        hash = Game.hasher.getMoveHash(move);
        if (!pool.containsKey(hash))
            pool.put(hash, new ByteArray(hash, move));
    }

    public byte get(int index) {
        return pool.get(hash).array[index];
    }

    private class ByteArray {
        private final byte[] array;
        private int hashCode;

        private ByteArray(int hashCode, byte[] array) {
            this.hashCode = hashCode;
            this.array = new byte[array.length];
            System.arraycopy(array, 0, this.array, 0, array.length);
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
