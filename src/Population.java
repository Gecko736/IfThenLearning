public class Population {
    private final Player[] population;

    public Population(int size) {
        if (size % 2 != 0)
            throw new IllegalArgumentException("Parameter size must be an even number. Given: " + size);
        population = new Player[size];
        for (int i = 0; i < population.length; i++)
            population[i] = Main.newPlayer();
    }

    public void play() {

    }
}
