package GameSearch;

import java.util.*;

import static java.lang.System.exit;

public abstract class GameSearch {

    public static final boolean DEBUG = false;
    /*
     * Note: the abstract Position also needs to be
     *       subclassed to write a new game program.
     */
    /*
     * Note: the abstract class Move also needs to be subclassed.
     *
     */

    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    /**
     *  Notes:  PROGRAM false -1,  HUMAN true 1
     */

    /*
     * Abstract methods:
     */

    public abstract boolean drawnPosition(Position p);
    public abstract boolean wonPosition(Position p, boolean player);
    public abstract float positionEvaluation(Position p, boolean player);
    public abstract void printPosition(Position p);
    public abstract Position [] possibleMoves(Position p, boolean player);
    public abstract Position makeMove(Position p, boolean player, Move move,boolean isSumulation);
    public abstract boolean reachedMaxDepth(Position p, int depth);
    public abstract Move createMove(boolean player,Position pos);
    public abstract Position makemovemulitjoueur(Position p, boolean joueur1, Move move);
    /*
     * Search utility methods:20
     */
    protected Vector alphaBeta(int depth, Position p, boolean player) {
        Vector v = alphaBetaHelper(depth, p, player, 1000000.0f, -1000000.0f);
        //System.out.println("^^ v(0): " + v.elementAt(0) + ", v(1): " + v.elementAt(1));
        return v;
    }

    protected Vector alphaBetaHelper(int depth, Position p, boolean player, float alpha, float beta) {

        if (GameSearch.DEBUG) System.out.println("alphaBetaHelper("+depth+","+p+","+alpha+","+beta+")");
        if (reachedMaxDepth(p, depth)){
            Vector v = new Vector(2);
            float value = positionEvaluation(p, player);
            v.addElement(new Float(value));
            v.addElement(null);
            if(GameSearch.DEBUG){
                System.out.println(" alphaBetaHelper: mx depth at " + depth+
                                   ", value="+value);
            }
            return v;
        }

        Vector best = new Vector();
        Position[] moves = possibleMoves(p, player);

        if (moves.length == 0) { // Aucun mouvement possible
            Vector v = new Vector(2);
            v.addElement(positionEvaluation(p, player)); // Ajouter une évaluation de position
            v.addElement(null); // Pas de position suivante
            return v;
        }
        for (int i=0; i<moves.length; i++) {
            Vector v2 = alphaBetaHelper(depth + 1, moves[i], !player, -beta, -alpha);
            //  if (v2 == null || v2.size() < 1) continue;
            float value = -((Float)v2.elementAt(0)).floatValue();
            if (value > beta) {
                if(GameSearch.DEBUG) System.out.println(" ! ! ! value="+value+", beta="+beta);
                beta = value;
                best = new Vector();
                best.addElement(moves[i]);
                Enumeration enum2 = v2.elements();
                enum2.nextElement(); // skip previous value
                while (enum2.hasMoreElements()) {
                    Object o = enum2.nextElement();
                    if (o != null) best.addElement(o);
                }
            }
            /**
             * Use the alpha-beta cutoff test to abort search if we
             * found a move that proves that the previous move in the
             * move chain was dubious
             */
            if (beta >= alpha) {
                break;
            }
        }
        Vector v3 = new Vector();
        v3.addElement(new Float(beta));
        Enumeration enum2 = best.elements();
        while (enum2.hasMoreElements()) {
            v3.addElement(enum2.nextElement());
        }
        return v3;
    }

    public void playMultiplayerGame(Position startingPosition, boolean joueur1) {
        MancalaGame gameLogic = new MancalaGame();
        boolean gameOver = false; // Drapeau pour arrêter le jeu
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            gameLogic.printPosition(startingPosition); // Affiche la position actuelle au début du tour

            MancalaPosition pos = (MancalaPosition) startingPosition;

            // Vérifiez si la partie est terminée
            if (gameLogic.isGameOver(pos)) {
                gameLogic.finDePartie(pos);
                gameOver = true;
                break;
            }

            // Demande au joueur de jouer
            boolean mouvementValide = false;
            while (!mouvementValide) {
                System.out.print((joueur1 ? "Joueur 1" : "Joueur 2")
                        + ", choisissez votre case (ou tapez 13 pour afficher le menu) : ");
                int selectedPit = scanner.nextInt();

                // Si l'utilisateur choisit 13, afficher le menu
                if (selectedPit == 13) {
                    boolean returnToGame = false; // Indique si le joueur souhaite retourner au jeu

                    while (!returnToGame) {
                        System.out.println("\nMenu principal :");
                        System.out.println("1. Sauvegarder la partie");
                        System.out.println("2. Charger une partie");
                        System.out.println("3. Quitter");
                        System.out.println("4. Continuer à jouer");
                        System.out.print("Choisissez une option : ");
                        int menuChoice = scanner.nextInt();

                        if (menuChoice == 1) {
                            gameLogic.saveGame(new GameSave((MancalaPosition) startingPosition, joueur1));
                            System.out.println("Partie sauvegardée !");
                        } else if (menuChoice == 2) {
                            GameSave loadedGame = gameLogic.loadGame();
                            if (loadedGame != null) {
                                startingPosition = loadedGame.position;
                                joueur1 = loadedGame.joueur1;
                                System.out.println("Partie chargée !");
                                gameLogic.printPosition(startingPosition); // Affiche la position après chargement
                            } else {
                                System.out.println("Aucune partie sauvegardée trouvée !");
                            }
                        } else if (menuChoice == 3) {
                            System.out.println("Fin de la partie !");
                            gameOver = true;
                            returnToGame = true; // Quitte la boucle et arrête le jeu
                        } else if (menuChoice == 4) {
                            System.out.println("Retour au jeu...");
                            gameLogic.printPosition(startingPosition); // Affiche la position actuelle avant de continuer
                            returnToGame = true; // Quitte le menu et retourne au jeu
                        } else {
                            System.out.println("Option invalide. Réessayez.");
                        }
                    }
                    continue; // Revenir à la demande de sélection de case
                }

                // Vérifiez que le joueur joue sur ses propres cases
                if (joueur1 && (selectedPit < 0 || selectedPit > 5)) {
                    System.out.println("Choisissez une case entre 0 et 5.");
                    continue;
                }
                if (!joueur1 && (selectedPit < 6 || selectedPit > 11)) {
                    System.out.println("Choisissez une case entre 6 et 11.");
                    continue;
                }

                // Effectuez le mouvement
                Move move = new MancalaMove(selectedPit);
                Position nouvellePosition = gameLogic.makemovemulitjoueur(startingPosition, joueur1, move);
                if (nouvellePosition != null) {
                    startingPosition = nouvellePosition;
                    gameLogic.printPosition(startingPosition); // Affiche la position après un mouvement valide
                    mouvementValide = true;
                } else {
                    System.out.println("Mouvement non valide. Réessayez.");
                }
            }

            // Alternez le tour
            joueur1 = !joueur1;
        }
    }




    public void playGame(Position startingPosition, boolean humanPlayFirst) {
         MancalaGame mancalaGame = new MancalaGame();
        if (humanPlayFirst == false){
            Vector v = alphaBeta(0, startingPosition, PROGRAM);
            startingPosition = (Position)v.elementAt(1);
        }
        while (true) {
            printPosition(startingPosition);
            if (mancalaGame.finDePartie(startingPosition)) {
                printPosition(startingPosition);
                break;
            }

            System.out.print("\nYour move:");
            Move move = createMove(humanPlayFirst ,startingPosition);
            startingPosition = makeMove(startingPosition, HUMAN, move,false);
            printPosition(startingPosition);
            boolean extraTurn;
            do {
                System.out.print(" \n Computer Move : \n ------------------------------------------------------- \n");
                Vector v = alphaBeta(0, startingPosition, PROGRAM);
                if (v.size() < 2 || !(v.elementAt(1) instanceof Position)) {
                    System.out.println("No valid moves available for the computer. Game over!");
                    break;
                }
                startingPosition = (Position) v.elementAt(1);

                // Vérifier si l'ordinateur a un tour supplémentaire
                extraTurn = ((MancalaPosition) startingPosition).extraTurn;

                if (extraTurn) {
                    printPosition(startingPosition);
                    System.out.println("Computer gets an extra turn!");
                }
            } while (extraTurn);
        }
    }
}
