package GameSearch;

import java.util.*;
public class MancalaPosition extends Position {
        public int[] board; // Les cases du plateau
        public int playerStore1; // Mancala du joueur 1
        public int playerStore2; // Mancala du joueur 2
        public boolean currentPlayer;
        public boolean extraTurn;
        // Joueur en cours (true = joueur humain)

        public MancalaPosition() {
            // Initialisation classique d'un plateau Mancala
            this.board = new int[12];
            Arrays.fill(this.board, 4); // Chaque case commence avec 4 graines
            this.playerStore1 = 0;
            this.playerStore2 = 0;
            this.currentPlayer = true; // Par défaut, le joueur humain commence
        }

        public MancalaPosition copy() {
            MancalaPosition copy = new MancalaPosition();
            copy.board = Arrays.copyOf(this.board, this.board.length);
            copy.playerStore1 = this.playerStore1;
            copy.playerStore2 = this.playerStore2;
            copy.currentPlayer = this.currentPlayer;
            return copy;
        }

}
