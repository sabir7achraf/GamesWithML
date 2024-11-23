package GameSearch;

import java.util.*;


public class MancalaGame extends GameSearch {

    @Override
    public boolean drawnPosition(Position p) {
        MancalaPosition pos = (MancalaPosition) p;
        return Arrays.stream(pos.board).allMatch(pit -> pit == 0); // Toutes les cases vides
    }

    @Override
    public boolean wonPosition(Position p, boolean player) {
        MancalaPosition pos = (MancalaPosition) p;
        if (drawnPosition(p)) {
            return player ? pos.playerStore1 > pos.playerStore2 : pos.playerStore2 > pos.playerStore1;
        }
        return false;
    }

    @Override
    public float positionEvaluation(Position p, boolean player) {
        MancalaPosition pos = (MancalaPosition) p;
        return player ? pos.playerStore1 - pos.playerStore2 : pos.playerStore2 - pos.playerStore1;
    }

    @Override
    public void printPosition(Position p) {
        MancalaPosition pos = (MancalaPosition) p;

        System.out.println("Joueur 1 (Mancala): " + pos.playerStore1);

        // Première ligne : indices 0 à 5
        System.out.print("P1: ");
        for (int i = 0; i <= 5; i++) {
            System.out.print(pos.board[i] + " ");
        }
        System.out.println(); // Saut de ligne

        // Deuxième ligne : indices 6 à 11
        System.out.print("P2: ");
        for (int i = 6; i <= 11; i++) {
            System.out.print(pos.board[i] + " ");
        }
        System.out.println(); // Saut de ligne

        System.out.println("Joueur 2 (Mancala): " + pos.playerStore2);
    }


    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        MancalaPosition pos = (MancalaPosition) p;
        Vector<Position> moves = new Vector<>();
        int start = player ? 0 : 6;
        int end = player ? 5 : 11;

        for (int i = start; i <= end; i++){
            if (pos.board[i] > 0) { // Si la case contient des graines
                MancalaPosition newPos = pos.copy();
                makeMove(newPos, player, new MancalaMove(i));
                System.out.println("from possibleMoves");
                moves.add(newPos);
            }
        }
        return moves.toArray(new Position[0]);
    }

    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        MancalaPosition pos = (MancalaPosition) p;
        MancalaMove mancalaMove = (MancalaMove) move;
        int selectedPit = mancalaMove.selectedPit;
        int seeds = pos.board[selectedPit];
        pos.board[selectedPit] = 0;
        int index = selectedPit;
        while (seeds > 0) {
            index = (index + 1) % 12; // Incrémenter l'index, en bouclant dans [0, 11]
            // Si c'est le Mancala du joueur actuel
            if ((index == 6 && player) || (index == 0 && !player)) {
                if (player){
                    pos.playerStore1++;
                }else{
                    pos.playerStore2++;
                }
                seeds--;
                if(seeds!=0) {
                    pos.board[index]++;
                }
                seeds--;
                continue; // Passez à l'élément suivant
            }
            pos.board[index]++;
            seeds--;
        }
        // Retournez la position mise à jour
        return pos;
    }

    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        return depth >= 10; // Profondeur maximale pour l'algorithme
    }

    @Override
    public Move createMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the pit number (0-5 for Player 1, 6-11 for Player 2): ");
        int pit = scanner.nextInt();
        return new MancalaMove(pit);
    }

    public static void main(String[] args) {
        MancalaPosition startingPosition = new MancalaPosition();
        MancalaGame game = new MancalaGame();
        Scanner scanner = new Scanner(System.in);

        boolean playerStarts = false;
        while (true) {
            System.out.println("Entrez 1 pour que le joueur commence, ou 0 pour que l'ordinateur commence :");
            int choice = scanner.nextInt();
            if (choice == 1) {
                playerStarts = true;
                break;
            } else if (choice == 0) {
                playerStarts = false;
                break;
            } else {
                System.out.println("Entrée invalide. Veuillez entrer 1 ou 0.");
            }
        }

        game.playGame(startingPosition, playerStarts); // Le joueur humain ou l'ordinateur commence selon le choix
    }

}
