package math130.gui;

/**
 * The {@code SpellCard} class represents a non-monster support card within the
 * Yu-Gi-Oh! game engine, extending the core {@link YuGiOhCard} structure [1].
 * <p>
 * Unlike monster entities, spell cards do not possess combat statistics like
 * Attack (ATK) or Defense (DEF). Instead, they carry a specialized effect type
 * signature (such as {@code "BOARD_WIPE"} or {@code "DRAW_TWO"}). When activated
 * during the Main Phase, the game's state engine processes this signature to
 * execute dynamic board-wide modifications [1].
 * </p>
 *
 * @author Allosauruscs
 * @version 2026.05.27
 * @see YuGiOhCard
 * @see MonsterCard
 */
public class SpellCard extends YuGiOhCard {

    /**
     * The unique operational keyword identifying the type of card behavior
     * this spell executes upon activation (e.g., "BOARD_WIPE", "DRAW_TWO").
     */
    private String effectType;

    /**
     * Constructs a fully initialized {@code SpellCard} instance with distinct
     * identification parameters and a dedicated system effect classification [1].
     *
     * @param name       the literal name of the spell (e.g., "Dark Hole", "Pot of Greed") [1]
     * @param imagePath  the localized file stream string name matching the image asset [1]
     * @param effectType the system operational identifier string mapping to its game code function [1]
     */
    public SpellCard(String name, String imagePath, String effectType) {
        super(name, imagePath);
        this.effectType = effectType;
    }

    /**
     * Retrieves the operational effect type identifier string attached to this card instance [1].
     * <p>
     * This signature is checked by the core controller loop to route the spell to its
     * corresponding game engine behavior block [1].
     * </p>
     *
     * @return a {@code String} representing the active keyword effect signature [1]
     */
    public String getEffectType() {
        return effectType;
    }
}
