package math130.gui;

public class YuGiOhCard {
    private String name;
    private String imagePath;
    public YuGiOhCard(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    //    public YuGiOhCard(String name) {
//        Name = name;
//    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
