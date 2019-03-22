import java.util.HashMap;

public class Move {
    private static HashMap<Integer, ByteArray> pool = new HashMap<>();
    private final int hash;

    public Move(byte[] move) {
        hash = Game.hasher.getMoveHash(move);
        pool.putIfAbsent(hash, new ByteArray(hash, move));
    }

    public byte get(int index) {
        return pool.get(hash).array[index];
    }

    private class ByteArray {
        private final byte[] array;
        private int hashCode;

        private ByteArray(int hashCode, byte[] array) {
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
