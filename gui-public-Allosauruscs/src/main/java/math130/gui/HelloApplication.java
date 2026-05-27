package math130.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1439,850);
//        stage.sizeToScene();
//        stage.setFullScreen(true);
        stage.getIcons().add(new Image(("BlueEyesWhiteDragon.jpg")));
        stage.setTitle("Yu-Gi-No!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();


//        Deck deck = new Deck();
//
//        deck.addCard(new YuGiOhCard("Fire King", "SacredFireKingGarunix.jpg"));
//
//        // Populate deck with example cards
////        deck.addCard(new YuGiOhCard("Dark Magician"));
////        deck.addCard(new YuGiOhCard("Pot of Greed"));
////        deck.addCard(new YuGiOhCard("Mirror Force"));
////        deck.addCard(new YuGiOhCard("Monster Reborn"));
////        deck.addCard(new YuGiOhCard("Blue-Eyes White Dragon"));
//
//        deck.shuffle();
//
//
//        Player player1 = new Player("Yugi");
//
//        // Draw starting hand (e.g., 4 cards)
//        System.out.println("--- Draw Phase (Starting Hand) ---");
//        for (int i = 0; i < 4; i++) {
//            player1.drawFrom(deck);
//        }
//
//        player1.showHand();

//        ArrayList<YuGiOhCard> deck = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            deck.add(new YuGiOhCard("Blue Eyes"));
//        }
//
//        ArrayList<YuGiOhCard> hand = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            YuGiOhCard cardDrawn = deck.remove(0);
//            hand.add(cardDrawn);
//        }

//        Deck deckTest = new Deck();
//        for (int i = 0; i < 40; i++) {
//            deckTest.add(new YuGiOhCard("Blue"));
//        }
//        try {
//            // 1. Instantiate the data
//            YuGiOhCard myCard = new YuGiOhCard("Dark Magician", "edwintrap.jpg");
//
//            // 2. Pass image data to UI
//            Image cardImage = new Image(getClass().getResourceAsStream(myCard.getImagePath()));
//
//
//        } catch (Exception e) {
//            System.out.println("Error loading card image. Check your file path!");
//            e.printStackTrace();
//        }



    }
}