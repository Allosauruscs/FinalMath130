package math130.gui;

/**
 * The parent class YuGiOhCard here gives what every card has which is a name and image.
 * Used as base for MonsterCard and SpellCard classes, the sub classes.
 */
public class YuGiOhCard {

    /**
     * each card has a name
     */
    private String name;

    /**
     * each card has an image
     */
    private String imagePath;

    /**
     * Constructor for YuGiOhCard
     * @param name
     * @param imagePath
     */
    public YuGiOhCard(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * to get image of the card
     * @return
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * not currently used but maybe if a card needs to change it's image path.
     * @param imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * to get card's name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * to get a string of the card's name
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}
