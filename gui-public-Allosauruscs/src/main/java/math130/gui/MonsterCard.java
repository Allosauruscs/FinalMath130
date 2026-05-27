package math130.gui;

public class MonsterCard extends YuGiOhCard {
    private int atk;
    private int def; // New attribute
    private int level;
    private boolean isAttackMode = true; // Tracks card stance: true = ATK, false = DEF
    private boolean hasAttacked = false;

    // Update constructor to take 5 arguments
    public MonsterCard(String name, String imagePath, int atk, int def, int level) {
        super(name, imagePath);
        this.atk = atk;
        this.def = def;
        this.level = level;
    }

    public int getAtk() { return this.atk; }
    public int getDef() { return this.def; } // New getter
    public int getLevel() { return this.level; }
    public boolean hasAttacked() { return hasAttacked; }
    public void setHasAttacked(boolean hasAttacked) { this.hasAttacked = hasAttacked; }
    public boolean isAttackMode() { return isAttackMode; }
    public void setAttackMode(boolean attackMode) { this.isAttackMode = attackMode; }
}
