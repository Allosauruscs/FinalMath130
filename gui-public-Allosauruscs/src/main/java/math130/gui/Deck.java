package math130.gui;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<YuGiOhCard> cards;
    private int size;

    public Deck() {
        cards = new ArrayList<>();
    }

    public void addCard(YuGiOhCard card) {
        cards.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public YuGiOhCard drawCard() {
        if (cards.isEmpty()) {
            System.out.println("ggwp");
            return null;
        }
        return cards.remove(0);
    }
    public int size() {
        return cards.size();

    }



}

