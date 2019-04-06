package IfThenLearning;
// obligatory change

public class Population {
    private Player[] population;

    public Population(int size) {
        if (size % 2 != 0)
            throw new IllegalArgumentException("Parameter size must be an even number. Given: " + size);
        population = new Player[size];
        for (int i = 0; i < population.length; i++)
            population[i] = Main.newPlayer();
    }

    public void interate() throws InterruptedException {
        sort(0, population.length - 1);
        replaceBottom();
    }

    private void playDummy() {
        for (Player player : population)
            Main.newGame(player, Dummy.getInstance()).run();
    }

    private void sort(int top, int btm) throws InterruptedException {
        int size = btm - top;
        if (size == 1)
            return;
        Game[] games = new Game[(btm - top) / 2];
        for (int i = 0; i < games.length; i++) {
            games[i] = Main.newGame(population[top + i], population[top + (i * 2)]);
            games[i].run();
        }
        for (int i = 0; i < games.length; i++) {
            games[i].join();
            try {
                population[top + i] = games[i].getWinner();
                population[top + (i * 2)] = games[i].getLoser();
            } catch (Game.GameOverException e) {
                catchGameOver(games[i], top + i, top + (i * 2));
            }
        }
        if (size > 2) {
            sort(top, (games.length / 2) - 1);
            sort(games.length / 2, btm);
        }
    }

    private void catchGameOver(Game g, int i, int j) throws InterruptedException {
        try {
            population[i] = g.getWinner();
            population[j] = g.getLoser();
        } catch (Game.GameOverException e) {
            System.out.println("Game " + g.getId() + " interrupted. Waiting .5 seconds.");
            Thread.sleep(500);
            catchGameOver(g, i, j);
        }
    }
    
    private void replaceBottom() {
        int half = population.length / 2;
        for (int i = 0; i < half; i++)
            population[half + i] = Main.newPlayer(population[i * 2], population[(i * 2) + 1]);
    }
}
