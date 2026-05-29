package math130.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for creating and storing all of the current cards in the game.
 * Used for populating the decks and makes it easy to add new cards.
 */
public class CardDatabase {

    /**
     * Method for creating an ArrayList representing the current catalog of cards.
     * The card objects are created and added to the catalog ArrayList here.
     * @return
     */
    public static List<YuGiOhCard> getAllCards() {
        ArrayList<YuGiOhCard> catalog = new ArrayList<>();

        // Monster cards
        // Parameters: (Name, ImageFileName, Attack, Defense, Level)
        catalog.add(new MonsterCard("Blue-Eyes White Dragon", "BlueEyesWhiteDragon.jpg", 3000, 2500, 8));
        catalog.add(new MonsterCard("Shrek", "ShrekYugioh.jpg", 2001, 0, 4));
        catalog.add(new MonsterCard("Megamind", "megamindcard1.jpg", 1, 1, 4));
        catalog.add(new MonsterCard("Maurice", "mauricecardaxis.jpg", 200, 300, 4));
        catalog.add(new MonsterCard("Alvin", "alvinchipmg.jpg", 3333, 1111, 12 ));
        catalog.add(new MonsterCard("Pibble", "pibblee.jpg", 4200, 690, 2));
        catalog.add(new MonsterCard("67", "67c.jpg", 6767, 67, 67));
        catalog.add(new MonsterCard("Waltuh", "ww.jpg", 991, 9, 4));

        // Spell cards
        // Parameters: (Name, ImageFileName, EffectType)
        catalog.add(new SpellCard("Ding Ding Ding", "dingding.jpg", "BOARD_WIPE"));
        catalog.add(new SpellCard("Pot of Greed", "potofgreed.jpg", "DRAW_TWO"));
        catalog.add(new SpellCard("Raigeki", "raigeki.jpg", "OPPONENT_WIPE"));

        return catalog;
    }
}
