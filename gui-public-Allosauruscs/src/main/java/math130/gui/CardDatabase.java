package math130.gui;

import java.util.ArrayList;
import java.util.List;

public class CardDatabase {
    // Inside CardDatabase.java...
    public static List<YuGiOhCard> getAllCards() {
        List<YuGiOhCard> catalog = new ArrayList<>();

        // --- EXISTING MONSTERS ---
        catalog.add(new MonsterCard("Blue-Eyes White Dragon", "BlueEyesWhiteDragon.jpg", 3000, 2500, 8));
        catalog.add(new MonsterCard("Shrek", "ShrekYugioh.jpg", 2500, 2000, 4));
        catalog.add(new MonsterCard("Megamind", "megamindcard1.jpg", 2000, 1500, 4));
        catalog.add(new MonsterCard("Maurice", "mauricecardaxis.jpg", 1500, 1200, 4));
        catalog.add(new MonsterCard("Alvin", "alvinchipmg.jpg", 1000, 800, 4));

        catalog.add(new MonsterCard("Pibble", "pibble.jpg", 1800, 1000, 4));
        catalog.add(new MonsterCard("67", "67.gif", 6767, 67, 67));


        // --- EXISTING SPELLS ---
        catalog.add(new SpellCard("Dark Hole", "darkhole.jpg", "BOARD_WIPE"));
        catalog.add(new SpellCard("Pot of Greed", "potofgreed.jpg", "DRAW_TWO"));
        catalog.add(new SpellCard("Raigeki", "raigeki.jpg", "OPPONENT_WIPE"));

        return catalog;
    }

}
