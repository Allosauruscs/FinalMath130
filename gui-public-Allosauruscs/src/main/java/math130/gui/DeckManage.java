//package math130.gui;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class DeckManage {
//    private List<String> deck;
//    // ObservableList allows JavaFX components to see changes instantly
//    private ObservableList<String> hand;
//
//    public DeckManage() {
//        this.deck = new ArrayList<>();
//        this.hand = FXCollections.observableArrayList();
//    }
//
//    public void initializeDeck() {
//        deck.clear();
//        hand.clear();
//        deck.add("Dark Magician");
//        deck.add("Blue-Eyes White Dragon");
//        deck.add("Pot of Greed");
//        deck.add("Monster Reborn");
//        deck.add("Ash Blossom");
//        Collections.shuffle(deck);
//    }
//
//    public boolean drawCard(int numberOfCards) {
//        for (int i = 0; i < numberOfCards; i++) {
//            if (deck.isEmpty()) {
//                return false; // Indicates Deck Out / Loss
//            }
//            String drawnCard = deck.remove(0);
//            hand.add(drawnCard);
//        }
//        return true;
//    }
//
//    public ObservableList<String> getHand() { return hand; }
//    public int getDeckSize() { return deck.size(); }
//}
//
