package IfThenLearning;

public class Population {
    private Player[] population;

    public Population(int size) {
        if (size % 2 != 0)
            throw new IllegalArgumentException("Parameter size must be an even number. Given: " + size);
        population = new Player[size];
        for (int i = 0; i < population.length; i++)
            population[i] = Main.newPlayer();
    }

    public Player getBest() {
        return population[0];
    }

    public void iterate() throws InterruptedException {
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
        Game[] games = new Game[size / 2];
        int j = 0;
        for (int i = top; j < games.length && top + i + games.length < btm; i++) {
            games[j] = Main.newGame(population[top + i], population[top + games.length + i]);
            games[j].play();
            j++;
        }
        j = 0;
        for (int i = top; j < games.length && i + games.length < btm; i++) {
            try {
//                games[j].join();
                population[top + i] = games[j].getWinner();
                population[top + i + games.length] = games[j].getLoser();
            } catch (Game.GameOverException e) {
                catchGameOver(games[i], top + i, top + (i * 2));
            } catch (NullPointerException e) {
//                System.out.println(e.getMessage());
            } finally {
                j++;
            }
        }
        if (size > 2) {
            sort(top, top + (games.length / 2) - 1);
            sort(btm - (games.length / 2), btm);
        }
    }

    private void catchGameOver(Game g, int i, int j) throws InterruptedException {
        try {
            population[i] = g.getWinner();
            population[j] = g.getLoser();
        } catch (InterruptedException e) {
            System.out.println("Game " + g.getId() + " interrupted. Waiting .5 seconds.");
            Thread.sleep(500);
            catchGameOver(g, i, j);
        } catch (Game.GameOverException e) {
            if (Math.random() < 0.5) {
                Player p = population[i];
                population[i] = population[j];
                population[j] = p;
            }
        }
    }
    
    private void replaceBottom() {
        int half = population.length / 2;
        for (int i = 0; half + i < population.length; i++)
            population[half + i] = Main.newPlayer(population[i * 2], population[(i * 2) + 1]);
    }
}
