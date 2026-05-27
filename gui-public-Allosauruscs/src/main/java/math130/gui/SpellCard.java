package math130.gui;

public class SpellCard extends YuGiOhCard {
    private String effectType;

    public SpellCard(String name, String imagePath, String effectType) {
        super(name, imagePath);
        this.effectType = effectType;
    }

    public String getEffectType() {
        return effectType;
    }
}
