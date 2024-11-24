package GameSearch;

import java.io.Serializable;

public class GameSave implements Serializable {
    private static final long serialVersionUID = 1L; // Requis pour la sérialisation
    public MancalaPosition position;
    public boolean joueur1; // Indique si c'est le tour du joueur 1

    public GameSave(MancalaPosition position, boolean joueur1) {
        this.position = position;
        this.joueur1 = joueur1;
    }
}
