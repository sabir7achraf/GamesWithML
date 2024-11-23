package GameSearch;

public class MancalaMove extends  Move {
    public int selectedPit; // Le numéro de la case choisie

    public MancalaMove(int selectedPit) {
        this.selectedPit = selectedPit;
    }
}
