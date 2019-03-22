import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Phenotype implements Player {
    /* static variables ******************************************************/

    // limit of the randomness variable of all Phenotypes without parents
    private static final double initialRandomnessLimit = 0.05;

    // the chance that a new Phenotype will be born with a randomness not informed
    // by the randomness of either parent
    private static final double randomRandomnessRate = 0.05;

    // the static variable from which all new Players get their ID
    private static int nextID = 1;
    private static synchronized int getID() {
        return nextID++;
    }

    /* instance variables ****************************************************/

    // this int by which this Phenotype can be found in the HashSet<Phenotype> in the
    // Main class
    private final int ID;

    // the set of If-Then statements used by this Phenotype
    private HashMap<State, Integer> brain = new HashMap<>();

    // the set of new If-Then statements used by this Phenotype in its current game
    private HashMap<State, Integer> tempBrain = new HashMap<>();

    // because of the tempBrain, a Phenotype can only play one game at a time
    private Semaphore attention = new Semaphore(1);

    // the number of games played by this Phenotype
    private int gamesPlayed = 0;

    // the number of games won by this Phenotype
    private int gamesWon = 0;

    // the chance that this Phenotype will disobey its If-Then statements and make
    // a random move on any given turn
    private final double randomness;

    /* constructors **********************************************************/

    public Phenotype() {
        ID = getID();
        randomness = Math.random() * initialRandomnessLimit;
    }

    public Phenotype(Phenotype mother, Phenotype father) {
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

    /* inherited methods *****************************************************/

    @Override
    public void initiate() {
        try {
            attention.acquire();
        } catch (InterruptedException e) {}
    }

    @Override
    public int move(State state, int numOfLegalMoves) {
        if (brain.containsKey(state))
            return brain.get(state);
        int move = (int) (Math.random() * numOfLegalMoves);
        tempBrain.put(state, move);
        return move;
    }

    @Override
    public double getWinRate() {
        if (gamesPlayed == 0)
            return 0.5;
        return (gamesWon + 0.0) / gamesPlayed;
    }

    @Override
    public synchronized void won() {
        gamesPlayed++;
        gamesWon++;
        brain.putAll(tempBrain);
        tempBrain.clear();
        attention.release();
    }

    @Override
    public synchronized void lost() {
        gamesPlayed++;
        tempBrain.clear();
        attention.release();
    }
}
