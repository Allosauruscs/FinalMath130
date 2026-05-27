package math130.gui;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;

public class Player {
    public String name;
    private ArrayList<YuGiOhCard> hand;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public void drawFrom(Deck deck) {
        YuGiOhCard drawnCard = deck.drawCard();
        if (drawnCard != null) {
            hand.add(drawnCard);
            System.out.println(" drew: " + drawnCard);
        }


    }
    public void showHand() {
        System.out.println(name + "'s Hand: " + hand);
    }

    public void summonMonster(YuGiOhCard card) {
        YuGiOhCard selected = card;
        if (selected instanceof MonsterCard) {


        }
    }
}
