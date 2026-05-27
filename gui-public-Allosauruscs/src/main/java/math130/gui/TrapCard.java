package math130.gui;

public class TrapCard extends YuGiOhCard {
    private String counterEffect;

    public TrapCard(String name, String imagePath, String counterEffect) {
        super(name, imagePath);
        this.counterEffect = counterEffect;
    }

    public String getSlow() {
        return counterEffect;
    }

    public void setSlow(String slow) {
        this.counterEffect = slow;
    }
}
