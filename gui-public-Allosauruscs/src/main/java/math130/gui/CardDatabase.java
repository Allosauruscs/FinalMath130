package math130.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CardDatabase} class serves as the central data repository and
 * catalog blueprint manager for the Yu-Gi-Oh! card game engine.
 * <p>
 * This utility repository populates and aggregates all initial playable card assets,
 * housing both {@link MonsterCard} and {@link SpellCard} objects within a unified,
 * polymorphically compatible collection stream.
 * </p>
 *
 * @author Allosauruscs
 * @version 2026.05.27
 * @see YuGiOhCard
 * @see MonsterCard
 * @see SpellCard
 */
public class CardDatabase {

    /**
     * Retrieves a compiled master catalog containing fresh data blueprints for every
     * available card asset configured within the application environment.
     * <p>
     * The list returned aggregates a balanced distribution of high-level and low-level
     * custom monster entities (such as Blue-Eyes, Shrek, and 67) as well as operational
     * spell interactions (such as Dark Hole and Pot of Greed). This composite list
     * is universally targeted by background execution loops to dynamically instantiate
     * and scale randomized player and opponent deck structures.
     * </p>
     *
     * @return a {@link List} populated with instantiated {@link YuGiOhCard} base objects,
     *         guaranteed to contain both active monster configurations and specialized spell blueprints.
     */
    public static List<YuGiOhCard> getAllCards() {
        List<YuGiOhCard> catalog = new ArrayList<>();

        // --- EXISTING MONSTERS ---
        // Parameters: (Name, ImageFileName, Attack, Defense, Level)
        catalog.add(new MonsterCard("Blue-Eyes White Dragon", "BlueEyesWhiteDragon.jpg", 3000, 2500, 8));
        catalog.add(new MonsterCard("Shrek", "ShrekYugioh.jpg", 2500, 2000, 4));
        catalog.add(new MonsterCard("Megamind", "megamindcard1.jpg", 2000, 1500, 4));
        catalog.add(new MonsterCard("Maurice", "mauricecardaxis.jpg", 1500, 1200, 4));
        catalog.add(new MonsterCard("Alvin", "alvinchipmg.jpg", 1000, 800, 4));
        catalog.add(new MonsterCard("Pibble", "pibblee.jpg", 4200, 690, 2));
        catalog.add(new MonsterCard("67", "67c.jpg", 6767, 67, 67));
        catalog.add(new MonsterCard("Waltuh", "ww.jpg", 991, 9, 4));

        // --- EXISTING SPELLS ---
        // Parameters: (Name, ImageFileName, EffectType)
        catalog.add(new SpellCard("Ding Ding Ding", "dingding.jpg", "BOARD_WIPE"));
        catalog.add(new SpellCard("Pot of Greed", "potofgreed.jpg", "DRAW_TWO"));
        catalog.add(new SpellCard("Raigeki", "raigeki.jpg", "OPPONENT_WIPE"));

        return catalog;
    }
}
