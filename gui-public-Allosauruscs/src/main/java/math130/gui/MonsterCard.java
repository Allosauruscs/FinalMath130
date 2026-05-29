package math130.gui;

/**
 * MonsterCard extends YuGiOhCard class adding atk, def, level, isAttackMode, and hasAttacked variables.
 */
public class MonsterCard extends YuGiOhCard {

    /**
     * atk for the monster card
     */
    private int atk;

    /**
     * def for the monster card
     */
    private int def;

    /**
     * The monster card's level
     */
    private int level;

    /**
     * for checking if the monster card is in atk or def mode, which is important for battle and damage calculations
     */
    private boolean isAttackMode = true;

    /**
     * hasAttacked is to make sure each card can attack once per turn and so that their attack can be reset each turn.
     */
    private boolean hasAttacked = false;

    /**
     * Monster card constructor
     * @param name
     * @param imagePath
     * @param atk
     * @param def
     * @param level
     */
    public MonsterCard(String name, String imagePath, int atk, int def, int level) {
        super(name, imagePath);
        this.atk = atk;
        this.def = def;
        this.level = level;
    }

    /**
     * get atk for battle and damage calculations.
     * @return
     */
    public int getAtk() {
        return this.atk;
    }

    /**
     * gets defense of monster card.
     * @return
     */
    public int getDef() {
        return this.def;
    }

    /**
     * gets level of monster, although I haven't really used this variable yet.
     * @return
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * checking if the monster has attaacked or not.
     * @return
     */
    public boolean hasAttacked() {
        return hasAttacked;
    }

    /**
     * for setting true after a monster has attacked. So each monster can attack once per turn.
     * @param hasAttacked
     */
    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * check position of monster so it can attack or not.
     * @return
     */
    public boolean isAttackMode() {
        return isAttackMode;
    }

    /**
     * to switch monsters between atk and def mode.
     * @param attackMode
     */
    public void setAttackMode(boolean attackMode) {
        this.isAttackMode = attackMode;
    }
}
