package math130.gui;

/**
 * Spell cards extends the YuGiOhCard class as adds effectType to link spells with their effects
 */
public class SpellCard extends YuGiOhCard {


    /**
     * For knowing what each spell card does
     */
    private String effectType;

    /**
     * Constructor for spell card
     * @param name
     * @param imagePath
     * @param effectType
     */
    public SpellCard(String name, String imagePath, String effectType) {
        super(name, imagePath);
        this.effectType = effectType;
    }

    /**
     * to get the effect of the spell so program can use the correct effect for the spell
     * @return
     */
    public String getEffectType() {
        return effectType;
    }
}
