package math130.gui;

/**
 * The {@code MonsterCard} class represents a combat-oriented monster entity
 * within the Yu-Gi-Oh! game engine, extending the core {@link YuGiOhCard} structure.
 * <p>
 * This class inherits foundational attributes (name and image path) from its superclass
 * and introduces specialized state elements mandatory for card combat resolution. It tracks
 * mathematical combat parameters (Attack and Defense points), tactical position flags
 * (Attack Position vs. Defense Position), and active turn usage limitations
 * (preventing duplicate attack actions).
 * </p>
 *
 * @author Allosauruscs
 * @version 2026.05.27
 * @see YuGiOhCard
 * @see SpellCard
 */
public class MonsterCard extends YuGiOhCard {

    /**
     * The raw Attack (ATK) power score utilized during active damage calculations.
     */
    private int atk;

    /**
     * The raw Defense (DEF) barrier score checked when targeted in Defense Position.
     */
    private int def;

    /**
     * The card level or star rating, used for calculating summoning tier requirements.
     */
    private int level;

    /**
     * Tracks the tactical posture of the monster node on the playfield matrix.
     * <code class="prettyprint">true</code> indicates Attack Mode (upright);
     * <code class="prettyprint">false</code> indicates Defense Mode (sideways).
     */
    private boolean isAttackMode = true;

    /**
     * Tracks turn execution state restrictions.
     * Set to <code class="prettyprint">true</code> the millisecond an attack is declared
     * to enforce "Once Per Turn" attack boundaries.
     */
    private boolean hasAttacked = false;

    /**
     * Constructs a fully initialized {@code MonsterCard} instance with distinct
     * identification parameters, static power attributes, and star-level tiering.
     *
     * @param name      the literal name of the monster (e.g., "Shrek", "67")
     * @param imagePath the localized file stream string name matching the image asset
     * @param atk       the raw damage scoring value to assign to this card instance
     * @param def       the raw defensive block rating to assign to this card instance
     * @param level     the numerical tier ranking or star value of the entity
     */
    public MonsterCard(String name, String imagePath, int atk, int def, int level) {
        super(name, imagePath);
        this.atk = atk;
        this.def = def;
        this.level = level;
    }

    /**
     * Retrieves the raw Attack (ATK) power value attached to this monster instance.
     *
     * @return an {@code int} representing active offensive combat damage potential
     */
    public int getAtk() {
        return this.atk;
    }

    /**
     * Retrieves the raw Defense (DEF) power barrier attached to this monster instance.
     *
     * @return an {@code int} representing active defensive block rating potential
     */
    public int getDef() {
        return this.def;
    }

    /**
     * Retrieves the star tier rating or Level value of this monster card.
     *
     * @return an {@code int} matching the entity level parameter value
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Checks if this specific monster copy has already declared an attack sequence
     * during the active turn phase lifecycle.
     *
     * @return <code class="prettyprint">true</code> if the monster is locked out from attacking;
     *         <code class="prettyprint">false</code> if it retains attack authorization.
     */
    public boolean hasAttacked() {
        return hasAttacked;
    }

    /**
     * Configures the attack state history flag to lock or unlock this card's combat availability.
     *
     * @param hasAttacked pass <code class="prettyprint">true</code> to apply a combat lock;
     *                    pass <code class="prettyprint">false</code> during turn upkeep to clear it.
     */
    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Identifies whether the card instance is currently configured in Attack Position.
     *
     * @return <code class="prettyprint">true</code> if in Attack Mode (0° layout rotation);
     *         <code class="prettyprint">false</code> if in Defense Mode (90° layout rotation).
     */
    public boolean isAttackMode() {
        return isAttackMode;
    }

    /**
     * Dynamically flips the structural stance value of the card between Attack and Defense points trackers.
     *
     * @param attackMode pass <code class="prettyprint">true</code> to shift to Attack Position;
     *                   pass <code class="prettyprint">false</code> to shift to Defense Position.
     */
    public void setAttackMode(boolean attackMode) {
        this.isAttackMode = attackMode;
    }
}
