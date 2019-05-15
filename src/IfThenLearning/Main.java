package IfThenLearning;

import java.util.Scanner;

public class Main {
    public static Game newGame(Player p1, Player p2) {
        return new Hexapawn(p1, p2);
    }

    public static Player newPlayer() {
        return new Phenotype();
    }

    public static Player newPlayer(Player mother, Player father) {
        if (!(mother instanceof Phenotype) && !(father instanceof Phenotype))
            return new Phenotype((Phenotype) mother, (Phenotype) father);
        return newPlayer();
    }

    public static void main(String[] args) throws InterruptedException {
        Hexapawn.setUp();
        System.out.println("Playing " + Game.name);
        System.out.print("What size population would you like to start with?>> ");
        int size = askUserFor("Population size");
        System.out.print("How many generations would you like to run?>> ");
        int generations = askUserFor("Generation count");

        Population population = new Population(size);
        boolean keepGoing = true;
        Player user = User.getInstance();
        int total = 0;
        while (keepGoing) {
            total += generations;
            for (int i = 0; i < generations; i++)
                population.iterate();
            System.out.println("iterated population " + generations + " times for a total of " + total + " generations");
            System.out.print("Would you like to go first? (y/n)>> ");
            String input = s.next();
            while (input.isEmpty() || (input.charAt(0) != 'y' && input.charAt(0) != 'n')) {
                System.out.print("Invlaid input. Try again>> ");
                input = s.next();
            }
            Game.view.instructions();
            Game g = newGame(user, population.getBest());
            if (input.charAt(0) == 'n') {
                g = newGame(population.getBest(), user);
            }
            g.run();
            g.join();
            int response = getResponse();
            if (response == -1)
                keepGoing = false;
            else
                generations = response;
        }
        System.out.println("Thank you for playing.");
    }

    private static final Scanner s = new Scanner(System.in);
    private static int askUserFor(String thing) {
        int output = s.nextInt();
        while (output <= 0) {
            System.out.print(thing + " must be greater than 0. Enter again>> ");
            try {
                output = s.nextInt();
            } catch (Exception e) {
                output = -1;
            }
        }
        return output;
    }

    private static int getResponse() {
        System.out.print("Would you like to play again? (y/n)>> ");
        String playAgain = s.next();
        while (playAgain.isEmpty() || (playAgain.charAt(0) != 'y' && playAgain.charAt(0) != 'n')) {
            System.out.print("Invalid input. Try again>> ");
            playAgain = s.next();
        }
        if (playAgain.charAt(0) == 'n')
            return -1;
        int generations;
        do {
            System.out.print("How many more generations would you like to run first?>> ");
            generations = s.nextInt();
        } while (generations < 0);
        return generations;
    }
}
