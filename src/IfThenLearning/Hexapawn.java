package IfThenLearning;

import java.util.ArrayList;
import java.util.Scanner;

public class Hexapawn extends Game {
    public static void setUp() {
        Game.name = "Hexapawn";
        Game.boardWidth = 3;
        Game.boardHeight = 3;
        Game.moveSize = 4;
        Game.hasher = new Hasher() {
            @Override
            public int getStateHash(byte[][] state) {
                char[] binaryHash = new char[boardWidth * boardHeight * 2];
                int half = binaryHash.length / 2;
                int i = 0;
                for (int x = 0; x < boardWidth; x++) {
                     for (int y = 0; y < boardHeight; y++) {
                         binaryHash[i] = '0';
                         binaryHash[half + i] = '0';
                         if (state[x][y] > 0)
                             binaryHash[i] = '1';
                         else if (state[x][y] < 0)
                             binaryHash[half + i] = '1';
                         i++;
                     }
                }
                return Integer.parseInt(new String(binaryHash), 2);
            }

            @Override
            public int getMoveHash(byte[] move) {
                return move[0] | (move[1] << 8) | (move[2] << 16) | (move[3] << 24);
            }
        };
        Game.view = new Viewer() {
            private String lineBreak = "+---+---+---+";
            private String header = "  0   1   2  x/y";
            private int piece = 1;
            private char[][] board = new char[boardWidth][boardHeight];

            @Override
            public void instructions() {
                System.out.println("HEXAPAWN");
                System.out.println("You and an opponent each have 3 pawns. Each turn, you can move one pawn up one space.");
                System.out.println("You can move the pawn diagonally if and only if that pawn is capturing one of your opponent's pawns.");
                System.out.println("You cannot use a pawn to capture an opponent pawn directly in front of it.");
                System.out.println("Your pawns will be numbered 1 to 3, and your opponent's will be shown as o's, as shown below.");
                System.out.println("At each turn, your available moves will be listed and numbered for you.");
                System.out.println("Enter the number of the move you want to make.");
            }

            @SuppressWarnings("Duplicates")
            @Override
            public synchronized void board(IfThenLearning.State state) {
                board = new char[boardWidth][boardHeight];
                analyzeBoard(state);
                System.out.println(header);
                System.out.println(lineBreak);
                for (int y = 0; y < boardHeight; y++) {
                    println(y);
                    System.out.println(lineBreak);
                }
            }

            private void println(int y) {
                for (int x = 0; x < boardWidth; x++)
                    System.out.print("| " + board[x][y] + " ");
                System.out.println("| " + y);
            }

            @Override
            public int getUserMove(IfThenLearning.State state) {
                Scanner s = new Scanner(System.in);
                board(state);
                Move[] legalMoves = getLegalMoves(state);
                System.out.println("Your possible moves are:");
                for (int i = 0; i < legalMoves.length; i++)
                    System.out.println("[" + i + "] " + descriptionOf(legalMoves[i]));
                System.out.print("Enter the number of the move you want to make>> ");
                String input = s.nextLine();
                int move = parse(input);
                while (move < 0 || move >= legalMoves.length) {
                    System.out.println("Invalid input. Try again>> ");
                    move = parse(s.nextLine());
                }
                return move;
            }

            private void analyzeBoard(IfThenLearning.State state) {
                piece = 1;
                char c;
                for (Byte[] arr : state) {
                    c = piece(arr[0]);
                    board[arr[1]][arr[2]] = c;
                }
                piece = 1;
            }

            private char piece(byte piece) {
                switch (piece) {
                    case 0:
                        return ' ';
                    case -1:
                        return 'O';
                    case 1:
                        return (char) (48 + this.piece++);
                }
                return 'E';
            }

            private String descriptionOf(Move move) {
                return "#" + board[move.get(0)][move.get(1)] + " to (" + move.get(2) + "," + move.get(3) + ")";
            }

            private int parse(String str) {
                try {
                    return Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        };
        initialState = new IfThenLearning.State(new byte[][]{
                {-1, 0, 1},
                {-1, 0, 1},
                {-1, 0, 1}
        });
    }

    private static IfThenLearning.State initialState;

    @Override
    public IfThenLearning.State getInitialState() {
        return initialState;
    }

    public Hexapawn(Player p1, Player p2) {
        super(p1, p2);
    }

    @Override
    public void newState(Move move) {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int x = 0; x < boardWidth; x++)
            for (int y = 0; y < boardHeight; y++)
                newState[x][y] = currentState.get(x, y);
        newState[move.get(2)][move.get(3)] = 1;
        newState[move.get(0)][move.get(1)] = 0;
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public void flipPerspective() {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int x = 0; x < boardWidth; x++)
            for (int y = 0; y < boardHeight; y++)
                newState[x][y] = (byte)(currentState.get(x, boardHeight - y - 1) * -1);
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public Move[] getLegalMoves() {
        return getLegalMoves(currentState);
    }
    private static Move[] getLegalMoves(IfThenLearning.State state) {
        int count = 0;
        ArrayList<Move> moves = new ArrayList<>();
        for (int x = 0; x < boardWidth && count < 3; x++) {
            for (int y = 0; y < boardHeight && count < 3; y++) {
                if (state.get(x, y) == 1) {
                    count++;
                    if (x - 1 >= 0 && y - 1 >= 0 && state.get(x - 1, y - 1) == -1)
                        moves.add(new Move((byte) x, (byte) y, (byte) (x - 1), (byte) (y - 1)));
                    if (y - 1 >= 0 && state.get(x, y - 1) == 0)
                        moves.add(new Move((byte) x, (byte) y, (byte) x, (byte) (y - 1)));
                    if (x + 1 < boardWidth && y - 1 >= 0 && state.get(x + 1, y - 1) == -1)
                        moves.add(new Move((byte) x, (byte) y, (byte) (x + 1) , (byte) (y - 1)));
                }
            }
        }
        return moves.toArray(new Move[0]);
    }

    @Override
    public void checkGameOver() {
        for (int i = 0; i < boardWidth; i++) {
            if (currentState.get(i, 0) == 1) {
                currentPlayerWon();
                return;
            }
        }
        boolean done = true;
        for (int x = 0; x < boardWidth; x++)
            for (int y = 0; y < boardHeight; y++)
                if (currentState.get(x, y) == -1)
                    done = false;
        if (done)
            currentPlayerWon();
    }
}
