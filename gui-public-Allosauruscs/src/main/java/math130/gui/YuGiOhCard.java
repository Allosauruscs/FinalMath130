package math130.gui;

/**
 * The {@code YuGiOhCard} class serves as the abstract blueprint and base component
 * for all playable card variations within the game engine.
 * <p>
 * This superclass encapsulates general properties common to every card type,
 * such as structural identifying names and resource asset filepaths. By leveraging
 * object-oriented inheritance, specialized card types like {@link MonsterCard}
 * and {@link SpellCard} extend this class to inherit its core payload properties.
 * </p>
 *
 * @author Allosauruscs
 * @version 2026.05.27
 * @see MonsterCard
 * @see SpellCard
 */
public class YuGiOhCard {

    /**
     * The unique identifying name of the card.
     */
    private String name;

    /**
     * The resource subdirectory filepath pointing to the card's visual artwork asset.
     */
    private String imagePath;

    /**
     * Constructs a new {@code YuGiOhCard} instance with a specified name and image file pathway.
     *
     * @param name      the literal name of the card (e.g., "Blue-Eyes White Dragon")
     * @param imagePath the localized file stream name matching the asset in resources (e.g., "pibble.jpg")
     */
    public YuGiOhCard(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * Retrieves the resource folder filepath pointing to the card's visual image asset.
     *
     * @return a {@code String} containing the active texture filename or file trajectory
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Updates the card's visual texture resource target by defining a new asset filepath.
     *
     * @param imagePath the new localized string path to assign to this card instance
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Retrieves the clean text identifying name of the card instance.
     *
     * @return a {@code String} representing the card title name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the card, mapping directly to its identifying title.
     * <p>
     * Overriding this core function ensures that UI nodes, terminal debug streams,
     * and array log systems render the card's actual name rather than its background
     * memory address location.
     * </p>
     *
     * @return the string literal value of the card's name
     */
    @Override
    public String toString() {
        return name;
    }
}
