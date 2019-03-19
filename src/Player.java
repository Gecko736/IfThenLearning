import java.util.HashMap;
import java.util.HashSet;

public class Player {
    /* static variables ******************************************************/

    // limit of the randomness variable of all Players without parents
    private static final double initialRandomnessLimit = 0.05;

    // the chance that a new Player will be born with a randomness not informed
    // by the randomness of either parent
    private static final double randomRandomnessRate = 0.05;

    // the static variable from which all new Players get their ID
    private static int nextID = 1;
    private static synchronized int getID() {
        return nextID++;
    }

    /* instance variables ****************************************************/

    // this int by which this Player can be found in the HashSet<Player> in the
    // Main class
    private final int ID;

    // the set of If-Then statements used by this Player
    private HashMap<State, Move> brain = new HashMap<>();

    // the number of games played by this Player
    private int gamesPlayed = 0;

    // the number of games won by this Player
    private int gamesWon = 0;

    // the chance that this player will disobey its If-Then statements and make
    // a random move on any given turn
    private final double randomness;

    public Player() {
        ID = getID();
        randomness = Math.random() * initialRandomnessLimit;
    }

    public Player(Player mother, Player father) {
        ID = getID();

        if (Math.random() < randomRandomnessRate)
            randomness = Math.random();
        else if (Math.random() < mother.getWinRate() /(mother.getWinRate() + father.getWinRate()))
            randomness = mother.randomness + (
                    (Math.random() * mother.randomness * 0.05) - (mother.randomness * 0.025));
        else
            randomness = father.randomness + (
                    (Math.random() * father.randomness * 0.05) - (father.randomness * 0.025));


        HashSet<State> keys = new HashSet<>();
        keys.addAll(mother.brain.keySet());
        keys.addAll(father.brain.keySet());
        double getFromMother = mother.getWinRate() / (mother.getWinRate() + father.getWinRate());
        for (State state : keys) {
            if (Math.random() < getFromMother)
                brain.put(state, mother.brain.get(state));
            else
                brain.put(state, father.brain.get(state));
        }
    }

    public Move move(State state) {
        return brain.get(state);
    }

    public double getWinRate() {
        if (gamesPlayed == 0)
            return 0.5;
        return (gamesWon + 0.0) / gamesPlayed;
    }

    public void won() {
        gamesPlayed++;
        gamesWon++;
    }

    public void lost() {
        gamesPlayed++;
    }
}
