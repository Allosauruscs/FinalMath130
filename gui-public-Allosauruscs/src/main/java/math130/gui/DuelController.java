package math130.gui;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;

import java.util.ArrayList;
import java.util.Collections;

public class DuelController {

    @FXML
    private GridPane field;
    @FXML
    private HBox yourHand;
    @FXML
    private HBox opponentHandBox; // Links directly to your new Scene Builder container

    @FXML
    private StackPane yourDeck;
    @FXML
    private ImageView deckVisualPile;
    @FXML
    private ImageView opponentDeckVisualPile; // Links to the AI's layout deck graphic

    // AI Backend Lists
    private ArrayList<ImageView> opponentHand = new ArrayList<>();
    private ArrayList<ImageView> opponentDeckPile = new ArrayList<>();

    // Track which layout slot belongs to who in your GridPane 'field'
// Assuming: Row 1 = Opponent Monsters, Row 2 = Player Monsters
    private final int OPPONENT_MONSTER_ROW = 1;
    private final int PLAYER_MONSTER_ROW = 2;

    /**
     * to play sounds when actions happen like drawing a card or battle.
     * @param soundFileName
     */
    private void playSoundEffect(String soundFileName) {
        try {
            // The leading "/" instructs Java to start scanning from the root of your resources folder
            java.net.URL soundURL = getClass().getResource(soundFileName);

            // CRITICAL SAFETY CHECK: Stop crashes if the file isn't compiled yet
            if (soundURL == null) {
                System.out.println("ERROR: Java cannot find /resources/sounds/" + soundFileName);
                System.out.println("Please run 'Build -> Rebuild Project' in IntelliJ!");
                return;
            }

            // Convert the URL mapping into an external stream format string
            String externalPath = soundURL.toExternalForm();

            // Play the track instantly
            javafx.scene.media.AudioClip clip = new javafx.scene.media.AudioClip(externalPath);
            clip.play();
            System.out.println("Playing sound: " + soundFileName);

        } catch (Exception e) {
            System.out.println("Audio execution error: " + e.getMessage());
        }
    }

    /**
     * Creates each card and makes each one unique to prevent errors
     * @param cardData
     * @return
     */
    private ImageView createCardUIElementFromData(YuGiOhCard cardData) {
    ImageView cardView = new ImageView();
    try {
        Image cardImage = new Image(cardData.getImagePath());
        cardView.setImage(cardImage);
    } catch (Exception e) {
        System.out.println("Could not load texture file: " + cardData.getImagePath());
    }

    cardView.setFitWidth(100);
    cardView.setFitHeight(145);
    cardView.setPreserveRatio(true);

    // =======================================================
    // FIX: CREATE A UNIQUE INSTANCE FOR EVERY SINGLE CARD
    // This stops you and the AI from sharing attack/position flags!
    // =======================================================
    if (cardData instanceof MonsterCard) {
        MonsterCard original = (MonsterCard) cardData;
        // Spawn a completely independent copy with its own memory address
        MonsterCard freshInstance = new MonsterCard(
                original.getName(),
                original.getImagePath(),
                original.getAtk(),
                original.getDef(),
                original.getLevel()
        );
        cardView.setUserData(freshInstance); // Attach the unique instance
    }
    else if (cardData instanceof SpellCard) {
        SpellCard original = (SpellCard) cardData;
        SpellCard freshInstance = new SpellCard(
                original.getName(),
                original.getImagePath(),
                original.getEffectType()
        );
        cardView.setUserData(freshInstance); // Attach the unique instance
    }
    else {
        cardView.setUserData(cardData);
    }
    cardView.setOnMouseClicked(event -> handleCardClick(event));
    return cardView;
}

    @FXML
    private Label deckSizeLabel; // Matches your Scene Builder fx:id

    @FXML
    private Label opponentDeckSizeLabel; // Links to your new Scene Builder text label

    /**
     * keep track of amount of cards in deck
     */
    private void updateDeckSizeUI() {
        // Refresh player label
        if (deckSizeLabel != null) {
            deckSizeLabel.setText("Cards in Deck: " + deckPile.size());
        }

        // NEW: Refresh AI label
        if (opponentDeckSizeLabel != null) {
            opponentDeckSizeLabel.setText("AI Cards in Deck: " + opponentDeckPile.size());
        }
    }

    @FXML
    private Label phaseText;

    @FXML
    private ImageView cardPreview;
    @FXML
    private Label textPreview;

    @FXML
    private Label summonText;

    @FXML
    private ScrollPane handScrollPane; // Inject your new ScrollPane fx:id
    @FXML
    private ScrollPane opponentHandScrollPane; // Links to the new parent ScrollPane


    public enum GamePhase {
        DRAW_PHASE,
        MAIN_PHASE,
        BATTLE_PHASE,
        END_PHASE
    }

    private boolean isPlayerTurn = true;
    private boolean hasDrawnThisTurn = false;
    private GamePhase currentPhase = GamePhase.MAIN_PHASE;
    private ImageView attackingCardView = null; // Tracks which card is currently attacking

    // Mock variables for Life Points
    private int playerLP = 8000;
    private int opponentLP = 8000;

    // Place this at the top of your controller class with your other variables
    private ArrayList<ImageView> deckPile = new ArrayList<>();

    /**
     * creates the decks and gives each player a hand of cards
     */
    @FXML
    public void initialize() {
        // 1. Maintain your horizontal scrollpane mouse-wheel filter filters...
        handScrollPane.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
            if (scrollEvent.getDeltaY() != 0) {
                double scrollAmount = scrollEvent.getDeltaY() * 0.05;
                handScrollPane.setHvalue(handScrollPane.getHvalue() - scrollAmount);
                scrollEvent.consume();
            }
        });

        // 2. Fetch the mixed blueprint collection (Monsters + Spells)
        List<YuGiOhCard> availableBlueprints = CardDatabase.getAllCards();

        if (availableBlueprints == null || availableBlueprints.isEmpty()) {
            System.out.println("CRITICAL ERROR: CardDatabase is empty!");
            return;
        }

        int targetDeckSize = 40;

        // 3. Populate your player deck
        while (deckPile.size() < targetDeckSize) {
            for (YuGiOhCard cardData : availableBlueprints) {
                if (deckPile.size() >= targetDeckSize) break;

                // Generate visual item and store it in your deck pile
                ImageView cardUI = createCardUIElementFromData(cardData);
                deckPile.add(cardUI);
            }
        }

        // 4. Populate your AI opponent deck using the same mixed database list
        while (opponentDeckPile.size() < targetDeckSize) {
            for (YuGiOhCard cardData : availableBlueprints) {
                if (opponentDeckPile.size() >= targetDeckSize) break;

                ImageView aiCardUI = createCardUIElementFromData(cardData);
                opponentDeckPile.add(aiCardUI);
            }
        }

        // 5. Shuffle both memory arrays separately
        Collections.shuffle(deckPile);
        Collections.shuffle(opponentDeckPile);

        // Refresh display values and deal out the 5-card opening hands
        updateDeckSizeUI();
        updateLifePointsUI();

        // Deal Player hand
        for (int i = 0; i < 5; i++) {
            drawNextCard();
        }

        // Deal AI hand
        for (int i = 0; i < 5; i++) {
            if (!opponentDeckPile.isEmpty()) {
                ImageView aiCard = opponentDeckPile.remove(opponentDeckPile.size() - 1);
                opponentHand.add(aiCard);
                aiCard.setOnMouseClicked(event -> handleCardClick(event)); // Enable user previews
                opponentHandBox.getChildren().add(aiCard);
            }
        }
        if (deckPile.isEmpty() && deckVisualPile != null) {
            deckVisualPile.setImage(null);
        }
        currentPhase = GamePhase.DRAW_PHASE;
        phaseText.setText("Phase: DRAW PHASE");
        summonText.setText("Welcome to the Duel! Click 'Next Phase' to begin your match.");

        if (opponentDeckPile.isEmpty() && opponentDeckVisualPile != null) {
            opponentDeckVisualPile.setImage(null);
        }

    }

    /**
     * add top card to hand
     */
    private void drawNextCard() {
        if (!deckPile.isEmpty()) {
            ImageView topCard = deckPile.remove(deckPile.size() - 1);
            yourHand.getChildren().add(topCard);
        }
    }

private ImageView selectedCardView = null;

    /**
     * Very important method for clicking cards to see them on the preview or for battle.
     * @param event
     */
    private void handleCardClick(MouseEvent event) {
        ImageView clickedView = (ImageView) event.getSource();
        YuGiOhCard clickedCard = (YuGiOhCard) clickedView.getUserData();

        if (clickedCard == null) return;

        // =======================================================
        // FIX: FORCE LARGE PREVIEW DIMENSIONS ON EVERY SINGLE CLICK
        // This stops different image asset sizes from shrinking the container!
        // =======================================================
        cardPreview.setImage(clickedView.getImage());
        cardPreview.setRotate(0); // Keeps inspection upright
        cardPreview.setPreserveRatio(true); // Prevents stretching distortion
        cardPreview.setFitWidth(336);  // Matches your large Scene Builder target width
        cardPreview.setFitHeight(480); // Matches your large Scene Builder target height
        // =======================================================

        // Update stats text depending on the object type data payload
        if (clickedCard instanceof MonsterCard) {
            MonsterCard monster = (MonsterCard) clickedCard;
            textPreview.setText(monster.getName() + " (ATK: " + monster.getAtk() + " / DEF: " + monster.getDef() + ")");
        } else {
            textPreview.setText("Spell Card: " + clickedCard.getName());
        }

        // SAFETY BLOCK: Block the human player from manipulating the AI's hand layout container
        if (opponentHandBox.getChildren().contains(clickedView)) {
            summonText.setText("Inspecting opponent's hand card: " + clickedCard.getName());
            return; // Exit out early
        }

        // Route to battle logic if clicked during the player's combat turn
        if (currentPhase == GamePhase.BATTLE_PHASE) {
            handleBattleLogic(clickedView);
            return;
        }

        // --- MAIN PHASE HAND CALLS ---
        if (yourHand.getChildren().contains(clickedView)) {
            selectedCardView = clickedView;

            if (clickedCard instanceof SpellCard) {
                summonText.setText(clickedCard.getName() + " selected! Double-click this card in your hand to activate its effect.");

                // Execute spell activations instantly on double-click
                if (event.getClickCount() == 2) {
                    activateSpellEffect((SpellCard) clickedCard, clickedView);
                }
            } else {
                summonText.setText("Card Selected: " + clickedCard.getName() + ". Click a monster zone (Row 2) to summon.");
            }
        }
    }

    /**
     * gives the spell cards their effects
     * @param spell
     * @param spellCardView
     */
    private void activateSpellEffect(SpellCard spell, ImageView spellCardView) {
        if (currentPhase != GamePhase.MAIN_PHASE) {
            summonText.setText("Spells can only be activated during the Main Phase!");
            return;
        }

        // --- 1. POT OF GREED EFFECT ---
        if (spell.getName().equalsIgnoreCase("Pot of Greed")) {
            System.out.println("--- POT OF GREED ACTIVATED: DRAWING 2 CARDS ---");
            summonText.setText("Activating Pot of Greed! Drawing 2 cards...");

            // Remove Pot of Greed from hand first so it doesn't clutter layout
            yourHand.getChildren().remove(spellCardView);
            selectedCardView = null;

            // Draw 2 consecutive cards with a small internal validation check
            for (int i = 0; i < 2; i++) {
                if (deckPile.isEmpty()) {
                    summonText.setText("GAME OVER! You tried to draw from an empty deck via Pot of Greed. Deck Out!");
                    return;
                }
                // Pulls a card out and pushes it into your Hand HBox container
                ImageView topCardView = deckPile.remove(deckPile.size() - 1);
                yourHand.getChildren().add(topCardView);
            }

            updateDeckSizeUI(); // Refresh your visual deck counter on screen
            return; // Exit successfully
        }

        // --- 2. RAIGEKI EFFECT ---
        if (spell.getName().equalsIgnoreCase("Raigeki")) {
            System.out.println("--- RAIGEKI ACTIVATED: WIPING OPPONENT MONSTERS ---");
            int destroyedCount = 0;

            // Loop through the board elements
            for (javafx.scene.Node node : field.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView zone = (ImageView) node;
                    Integer r = GridPane.getRowIndex(zone);
                    int rowIndex = (r == null) ? 0 : r;

                    // CRITICAL TARGET: Only wipe Row 1 (Opponent's Monsters). Row 2 remains completely safe!
                    if (rowIndex == OPPONENT_MONSTER_ROW && zone.getImage() != null) {
                        zone.setImage(null);
                        zone.setUserData(null);
                        zone.setRotate(0);

                        // Reset click listener so zones become targets for future direct attacks or AI summons
                        zone.setOnMouseClicked(event -> handleZoneClick(event));
                        destroyedCount++;
                    }
                }
            }

            summonText.setText("Raigeki activated! Destroyed all " + destroyedCount + " of your opponent's monsters.");

            // Remove Raigeki from hand layout container
            yourHand.getChildren().remove(spellCardView);
            selectedCardView = null;
            return;
        }

        // --- 3. DARK HOLE EFFECT (Your existing logic kept intact) ---
        if (spell.getName().equalsIgnoreCase("Dark Hole")) {
            System.out.println("--- DARK HOLE ACTIVATED: WIPING THE ENTIRE FIELD ---");
            int destroyedCount = 0;

            for (javafx.scene.Node node : field.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView zone = (ImageView) node;
                    Integer r = GridPane.getRowIndex(zone);
                    int rowIndex = (r == null) ? 0 : r;

                    // Targets BOTH Row 1 and Row 2
                    if ((rowIndex == OPPONENT_MONSTER_ROW || rowIndex == PLAYER_MONSTER_ROW) && zone.getImage() != null) {
                        zone.setImage(null);
                        zone.setUserData(null);
                        zone.setRotate(0);
                        zone.setOnMouseClicked(event -> handleZoneClick(event));
                        destroyedCount++;
                    }
                }
            }

            summonText.setText("Dark Hole activated! " + destroyedCount + " monsters on the field were destroyed.");
            yourHand.getChildren().remove(spellCardView);
            selectedCardView = null;
        }
    }

        @FXML
        private Label playerLPLabel;
        @FXML
        private Label opponentLPLabel;

    private void updateLifePointsUI() {
        playerLPLabel.setText("LP: " + playerLP);
        opponentLPLabel.setText("LP: " + opponentLP);

        // Optional: Game Over Check
        if (opponentLP <= 0) {
            opponentLPLabel.setText("LP: 0");
            summonText.setText("CONGRATULATIONS! You Win!");
        }
        if (playerLP <= 0) {
            playerLPLabel.setText("LP: 0");
            summonText.setText("GAME OVER! You Lose!");
        }
    }

    private void handleBattleLogic(ImageView clickedZoneImageView) {
        // Safety check: Ensure attacks can ONLY be declared during the Battle Phase
        if (currentPhase != GamePhase.BATTLE_PHASE) {
            summonText.setText("You can only declare attacks during the Battle Phase!");
            return;
        }

        YuGiOhCard cardData = (YuGiOhCard) clickedZoneImageView.getUserData();
        Integer rIndex = GridPane.getRowIndex(clickedZoneImageView);
        int rowIndex = (rIndex == null) ? 0 : rIndex;

        // STEP A: Selecting your attacking monster
        if (attackingCardView == null) {
            if (cardData == null) {
                summonText.setText("Select a valid monster on your field to attack with!");
                return;
            }

            // Rule 1: Ensure the clicked card belongs to you (Row 2)
            if (rowIndex != PLAYER_MONSTER_ROW) {
                summonText.setText("You cannot attack with an opponent's monster!");
                return;
            }

            // Cast to monster since it is confirmed active on your field row
            MonsterCard monster = (MonsterCard) cardData;

            // Rule 2: Defense Position monsters cannot declare attacks (90 degrees visual rotation)
            if (clickedZoneImageView.getRotate() == 90) {
                summonText.setText(monster.getName() + " cannot attack because it is in Defense Position!");
                return;
            }

            // Rule 3: Enforce the "Once Per Turn" attack limitation constraint check
            if (monster.hasAttacked()) {
                summonText.setText(monster.getName() + " has already declared an attack this turn!");
                return;
            }

            // Setup the attacker selection state
            attackingCardView = clickedZoneImageView;
            summonText.setText(monster.getName() + " ready! Click an enemy monster or any empty opponent zone for a Direct Attack.");

            // Visual feedback: Make your attacking card glow red using JavaFX CSS dropshadow
            clickedZoneImageView.setStyle("-fx-effect: dropshadow(three-pass-box, red, 15, 0.5, 0, 0);");
            return; // Stop execution here and wait for the player's next click (the target)
        }

        // STEP B: Selecting the attack target and calculating results
        YuGiOhCard attackerData = (YuGiOhCard) attackingCardView.getUserData();

        // Rule 4: Prevent friendly fire (Attacking your own cards on Row 2)
        if (rowIndex == PLAYER_MONSTER_ROW) {
            summonText.setText("You can't attack your own cards! Combat selection reset.");
            attackingCardView.setStyle(""); // Remove visual glow highlight
            attackingCardView = null;
            return;
        }

        // TARGET CONDITION 1: Player clicked an empty zone -> Evaluate for Direct Attack
        if (cardData == null) {
            if (opponentHasNoMonsters()) {
                // Pass 'null' as the target to signal a Direct Attack scenario to your engine
                calculateBattleDamage(attackingCardView, attackerData, clickedZoneImageView, null);

                // Lock the monster from attacking again for the remainder of this turn cycle
                if (attackerData instanceof MonsterCard) {
                    ((MonsterCard) attackerData).setHasAttacked(true);
                }
            } else {
                summonText.setText("You cannot attack directly! You must clear your opponent's monsters first.");
            }
        }
        // TARGET CONDITION 2: Player clicked an enemy monster -> Run standard combat calculations
        else {
            calculateBattleDamage(attackingCardView, attackerData, clickedZoneImageView, cardData);

            // Lock the monster from attacking again for the remainder of this turn cycle
            if (attackerData instanceof MonsterCard) {
                ((MonsterCard) attackerData).setHasAttacked(true);
            }
        }

        // Clean up combat states and clear the visual red glow safely
        if (attackingCardView != null) {
            attackingCardView.setStyle("");
            attackingCardView = null;
        }
    }

    private void runOpponentAILogic() {
        System.out.println("--- AI TURN STARTED (MULTI-CARD CAPABLE) ---");

        // =======================================================
        // STEP 1: AI DRAW PHASE (1.5 Second Think Pause Delay)
        // =======================================================
        PauseTransition drawPause = new PauseTransition(Duration.seconds(1.5));
        drawPause.setOnFinished(e1 -> {
            if (!opponentDeckPile.isEmpty()) {
                // Extract the top layout item card from the hidden computer memory deck
                ImageView aiCard = opponentDeckPile.remove(opponentDeckPile.size() - 1);

                // 1. Add item to backend data storage list array tracks
                opponentHand.add(aiCard);

                // 2. Map click mouse event handlers so player can safely preview AI's hand
                aiCard.setOnMouseClicked(event -> handleCardClick(event));

                // 3. Physically push visual Node asset into the scrolling horizontal hand container
                opponentHandBox.getChildren().add(aiCard);

                summonText.setText("AI Phase: Draw Phase! The AI draws a card.");

                // 4. REFRESH TRACKERS: Push updated text count numbers to screen labels
                updateDeckSizeUI();

                // 5. GRAPHIC CHECK: Erase face-down artwork texture instantly if that was their last card
                if (opponentDeckPile.isEmpty() && opponentDeckVisualPile != null) {
                    opponentDeckVisualPile.setImage(null);
                    System.out.println("The AI drew their last card! Removing AI visual deck graphic.");
                }

            } else {
                // DECK OUT RULE: If computer is forced to draw from an empty stack, they lose instantly
                summonText.setText("GAME OVER! The AI has no cards left to draw. You win!");
                if (opponentDeckVisualPile != null) {
                    opponentDeckVisualPile.setImage(null);
                }
                updateDeckSizeUI(); // Force display text update to read 0
                return; // Terminate engine turn tracking loop early
            }

            // =======================================================
            // STEP 2: AI MAIN PHASE LOOP (Smart Recursive Action Chain)
            // =======================================================
            // Recursively runs up to 3 actions per turn cycle (summoning monsters or using spells)
            processAIMainPhaseActions(0, 3);
        });
        drawPause.play();
    }

    private void processAIMainPhaseActions(int currentActionCount, int maxActionsAllowed) {
        // Stop condition A: AI hit its maximum action limit for this turn
        if (currentActionCount >= maxActionsAllowed || opponentHand.isEmpty()) {
            proceedToAIBattlePhase();
            return;
        }

        // Try to execute a single card action (Summon or Spell)
        boolean actionTaken = executeSingleAIMainPhaseAction();

        if (actionTaken) {
            // If the AI successfully played a card, pause for 1.5 seconds so the player can see it,
            // then recursively call this method again to check for another play!
            PauseTransition nextActionPause = new PauseTransition(Duration.seconds(1.5));
            nextActionPause.setOnFinished(e -> processAIMainPhaseActions(currentActionCount + 1, maxActionsAllowed));
            nextActionPause.play();
        } else {
            // If executeSingleAIMainPhaseAction returned false, it means the AI couldn't play anything
            // (e.g., field is full of monsters, or hand has only monsters but no open zones). Move to combat.
            proceedToAIBattlePhase();
        }
    }

    private boolean executeSingleAIMainPhaseAction() {
        if (opponentHand.isEmpty()) return false;

        // Analyze the first card in the computer's hand
        ImageView aiCardView = opponentHand.get(0);
        YuGiOhCard cardData = (YuGiOhCard) aiCardView.getUserData();

        if (cardData == null) return false;

        // --- CASE A: CARD IS A SPELL -> Activate instantly ---
        if (cardData instanceof SpellCard) {
            opponentHand.remove(aiCardView);
            opponentHandBox.getChildren().remove(aiCardView);

            executeAISpellEffect((SpellCard) cardData);
            return true; // Action successfully taken!
        }

        // --- CASE B: CARD IS A MONSTER -> Look for an empty zone ---
        ImageView targetZone = null;
        for (int col = 0; col < 5; col++) {
            boolean occupied = false;
            for (javafx.scene.Node node : field.getChildren()) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                int rIndex = (r == null) ? 0 : r;
                int cIndex = (c == null) ? 0 : c;

                if (rIndex == OPPONENT_MONSTER_ROW && cIndex == col && node instanceof ImageView && ((ImageView)node).getImage() != null) {
                    occupied = true;
                    break;
                }
            }

            if (!occupied) {
                for (javafx.scene.Node node : field.getChildren()) {
                    Integer r = GridPane.getRowIndex(node);
                    Integer c = GridPane.getColumnIndex(node);
                    if (r != null && r == OPPONENT_MONSTER_ROW && c != null && c == col && node instanceof ImageView) {
                        targetZone = (ImageView) node;
                        break;
                    }
                }
                if (targetZone != null) break;
            }
        }

        // Execute summon if an open monster zone was found
        if (targetZone != null) {
            opponentHand.remove(aiCardView);
            opponentHandBox.getChildren().remove(aiCardView);

            MonsterCard monsterData = (MonsterCard) cardData;

            targetZone.setImage(aiCardView.getImage());
            targetZone.setUserData(monsterData);
            targetZone.setRotate(180); // Upside down for perspective

            // Setup player click preview listener
            targetZone.setOnMouseClicked(fieldEvent -> {
                ImageView enemySlot = (ImageView) fieldEvent.getSource();
                MonsterCard enemyData = (MonsterCard) enemySlot.getUserData();
                if (enemyData != null) {
                    cardPreview.setImage(enemySlot.getImage());
                    cardPreview.setRotate(0);
                    textPreview.setText(enemyData.getName() + " (ATK: " + enemyData.getAtk() + " / DEF: " + enemyData.getDef() + ")");
                    summonText.setText("Inspecting opponent's monster: " + enemyData.getName());
                }
                if (currentPhase == GamePhase.BATTLE_PHASE) {
                    handleBattleLogic(enemySlot);
                }
            });

            summonText.setText("AI Phase: Main Phase! AI Summons " + monsterData.getName() + " in Attack Position!");
            return true; // Action successfully taken!
        }

        return false; // No action taken (e.g., hand had a monster but all zones are full)
    }
    private void proceedToAIBattlePhase() {
        // Move to Battle Phase smoothly after a 1.5-second pause from the main phase
        PauseTransition battlePause = new PauseTransition(Duration.seconds(1.5));
        battlePause.setOnFinished(e3 -> {
            // Start the looping battle sequence
            executeAIBattleLogic();
        });
        battlePause.play();
    }

    private void executeAISpellEffect(SpellCard spell) {
        System.out.println("--- AI IS RESOLVING SPELL: " + spell.getName() + " ---");

        // 1. AI POT OF GREED: Draws 2 cards from opponentDeckPile to opponentHandBox
        if (spell.getName().equalsIgnoreCase("Pot of Greed")) {
            summonText.setText("AI Activates: Pot of Greed! The computer draws 2 cards.");

            for (int i = 0; i < 2; i++) {
                if (opponentDeckPile.isEmpty()) {
                    summonText.setText("GAME OVER! AI Deck Out. You win!");
                    return;
                }
                ImageView drawnCard = opponentDeckPile.remove(opponentDeckPile.size() - 1);
                opponentHand.add(drawnCard);
                drawnCard.setOnMouseClicked(event -> handleCardClick(event)); // Enable previewing
                opponentHandBox.getChildren().add(drawnCard);
            }
            return;
        }

        // 2. AI RAIGEKI: Wipes YOUR Monsters (Row 2), leaving its monsters perfectly untouched
        if (spell.getName().equalsIgnoreCase("Raigeki")) {
            int clearCount = 0;
            for (javafx.scene.Node node : field.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView zone = (ImageView) node;
                    Integer r = GridPane.getRowIndex(zone);
                    int rowIndex = (r == null) ? 0 : r;

                    // Targeted board wipe: Deletes items matching player tracking row fields exclusively
                    if (rowIndex == PLAYER_MONSTER_ROW && zone.getImage() != null) {
                        zone.setImage(null);
                        zone.setUserData(null);
                        zone.setRotate(0);
                        zone.setOnMouseClicked(event -> handleZoneClick(event)); // Reset listener
                        clearCount++;
                    }
                }
            }
            summonText.setText("AI Activates: Raigeki! All " + clearCount + " of your monsters were completely destroyed!");
            return;
        }

        // 3. AI DARK HOLE: Wipes BOTH monster fields entirely (Rows 1 and 2)
        if (spell.getName().equalsIgnoreCase("Dark Hole")) {
            int clearCount = 0;
            for (javafx.scene.Node node : field.getChildren()) {
                if (node instanceof ImageView) {
                    ImageView zone = (ImageView) node;
                    Integer r = GridPane.getRowIndex(zone);
                    int rowIndex = (r == null) ? 0 : r;

                    if ((rowIndex == OPPONENT_MONSTER_ROW || rowIndex == PLAYER_MONSTER_ROW) && zone.getImage() != null) {
                        zone.setImage(null);
                        zone.setUserData(null);
                        zone.setRotate(0);
                        zone.setOnMouseClicked(event -> handleZoneClick(event)); // Reset layout listener
                        clearCount++;
                    }
                }
            }
            summonText.setText("AI Activates: Dark Hole! " + clearCount + " monsters on the entire field were wiped out.");
        }
    }

    private void executeAIBattleLogic() {
        System.out.println("--- AI COMBAT PHASE STARTED (MULTI-ATTACK) ---");
        summonText.setText("AI Phase: Entering Battle Phase!");

        // 1. GATHER ALL ELIGIBLE AI ATTACKERS
        ArrayList<ImageView> aiAttackers = new ArrayList<>();

        for (javafx.scene.Node node : field.getChildren()) {
            if (node instanceof ImageView) {
                ImageView zone = (ImageView) node;
                Integer r = GridPane.getRowIndex(zone);
                int rowIndex = (r == null) ? 0 : r;

                // Check if the card belongs to the AI, is face-up, and is NOT in defense position (90 degrees)
                if (rowIndex == OPPONENT_MONSTER_ROW && zone.getImage() != null && zone.getRotate() != 90) {

                    // Only add monsters that haven't declared an attack yet this turn cycle
                    MonsterCard aiMonster = (MonsterCard) zone.getUserData();
                    if (aiMonster != null && !aiMonster.hasAttacked()) {
                        aiAttackers.add(zone);
                    }
                }
            }
        }

        // =======================================================
        // FIX: PASS CONTROL BACK TO PLAYER IF AI HAS NO ELIGIBLE ATTACKERS
        // Prevents game from freezing when the AI field is clear or all monsters are in DEF
        // =======================================================
        if (aiAttackers.isEmpty()) {
            summonText.setText("AI Phase: Battle Phase! AI has no eligible monsters to attack with.");
            System.out.println("AI field is empty or all monsters are locked out. Passing turn back...");

            // Pause for 2 seconds so the player can read the status message before control switches
            PauseTransition noAttackPause = new PauseTransition(Duration.seconds(2.0));
            noAttackPause.setOnFinished(e -> {
                // Hand control back to the human player safely
                isPlayerTurn = true;
                currentPhase = GamePhase.DRAW_PHASE;
                phaseText.setText("Phase: DRAW PHASE");
                summonText.setText("AI Turn Ended! It is now your Draw Phase. Click 'Next Phase' to begin your Main Phase.");

                // Reset human draw tracking values and deal your automated turn card
                hasDrawnThisTurn = false;
                executeAutomaticDrawPhase();
                System.out.println("--- AI TURN PASSED CLEANLY (NO ATTACKERS) ---");
            });
            noAttackPause.play();
            return; // Halt this specific combat trigger safely
        }
        // =======================================================

        // 2. TRIGGER THE SEQUENTIAL COMBAT LOOP HANDLER
        // Passes the collection queue forward to process attacks one-by-one with delays
        processNextAIAttack(aiAttackers, 0);
    }

    private void processNextAIAttack(ArrayList<ImageView> attackersList, int currentIndex) {
        // =======================================================
        // 1. STOP CONDITION: Turn ends only when all attacks are fully completed
        // =======================================================
        if (currentIndex >= attackersList.size()) {
            System.out.println("--- AI COMBAT PHASE COMPLETE ---");
            summonText.setText("AI Phase: End Phase. Wrapping up turn...");

            // Pause for 2 seconds after the final attack so the player can see the field settle
            PauseTransition endTurnPause = new PauseTransition(Duration.seconds(2.0));
            endTurnPause.setOnFinished(e4 -> {
                // Hand control back to the human player safely
                isPlayerTurn = true;
                currentPhase = GamePhase.DRAW_PHASE;
                phaseText.setText("Phase: DRAW PHASE");
                summonText.setText("AI Turn Ended! It is now your Draw Phase. Click 'Next Phase' to begin your Main Phase.");

                // Reset human draw trackers and automatically execute your draw step
                hasDrawnThisTurn = false;
                executeAutomaticDrawPhase();
                System.out.println("--- AI TURN COMPLETED CLEANLY ---");
            });
            endTurnPause.play();
            return; // Halt this method cycle safely
        }

        ImageView aiAttackerView = attackersList.get(currentIndex);

        // Safety verification: Ensure the monster wasn't destroyed earlier in this same battle phase
        // (e.g., if it attacked a stronger card or tied during a previous loop iteration)
        if (aiAttackerView.getImage() == null || aiAttackerView.getUserData() == null) {
            processNextAIAttack(attackersList, currentIndex + 1);
            return;
        }

        MonsterCard aiAttackerData = (MonsterCard) aiAttackerView.getUserData();

        // =======================================================
        // 2. TARGET ACQUISITION: Search Row 2 for a remaining player monster
        // =======================================================
        ImageView playerTargetView = null;
        for (javafx.scene.Node node : field.getChildren()) {
            if (node instanceof ImageView) {
                ImageView zone = (ImageView) node;
                Integer r = GridPane.getRowIndex(zone);
                int rowIndex = (r == null) ? 0 : r;

                if (rowIndex == PLAYER_MONSTER_ROW && zone.getImage() != null) {
                    playerTargetView = zone;
                    break; // Target the first human monster found
                }
            }
        }

        // =======================================================
        // 3. COMBAT RESOLUTION MATHEMATICS
        // =======================================================
        if (playerTargetView == null) {
            // Player has no monsters -> AI Direct Attack!
            playerLP -= aiAttackerData.getAtk();
            summonText.setText("AI's " + aiAttackerData.getName() + " attacks you directly for " + aiAttackerData.getAtk() + " damage!");
        } else {
            MonsterCard playerMonster = (MonsterCard) playerTargetView.getUserData();

            // CASE A: Target is visually in ATTACK POSITION (0 degrees rotation)
            if (playerTargetView.getRotate() != 90) {
                if (aiAttackerData.getAtk() > playerMonster.getAtk()) {
                    // AI Wins
                    int dmg = aiAttackerData.getAtk() - playerMonster.getAtk();
                    playerLP -= dmg;
                    summonText.setText("AI's " + aiAttackerData.getName() + " destroys your " + playerMonster.getName() + " and deals " + dmg + " damage!");

                    playerTargetView.setImage(null);
                    playerTargetView.setUserData(null);
                    // Reset click listener so the empty zone can accept future human summons
                    playerTargetView.setOnMouseClicked(event -> handleZoneClick(event));
                } else if (aiAttackerData.getAtk() < playerMonster.getAtk()) {
                    // Player Counters and Wins
                    int dmg = playerMonster.getAtk() - aiAttackerData.getAtk();
                    opponentLP -= dmg;
                    summonText.setText("AI attacks your stronger " + playerMonster.getName() + "! AI's monster is destroyed and it loses " + dmg + " LP.");

                    aiAttackerView.setImage(null);
                    aiAttackerView.setUserData(null);
                    aiAttackerView.setRotate(0);
                    // Reset click listener so empty zone can accept future AI summons
                    aiAttackerView.setOnMouseClicked(event -> handleZoneClick(event));
                } else {
                    // Mutual Destruction Tie
                    summonText.setText("AI's " + aiAttackerData.getName() + " attacks! Both monsters had equal ATK and were destroyed!");

                    playerTargetView.setImage(null); playerTargetView.setUserData(null);
                    aiAttackerView.setImage(null); aiAttackerView.setUserData(null);
                    aiAttackerView.setRotate(0);

                    playerTargetView.setOnMouseClicked(event -> handleZoneClick(event));
                    aiAttackerView.setOnMouseClicked(event -> handleZoneClick(event));
                }
            }
            // CASE B: Target is visually in DEFENSE POSITION (90 degrees rotation)
            else {
                if (aiAttackerData.getAtk() > playerMonster.getDef()) {
                    // AI pierces defense stance
                    summonText.setText("AI's " + aiAttackerData.getName() + " destroys your defense position " + playerMonster.getName() + "!");

                    playerTargetView.setImage(null);
                    playerTargetView.setUserData(null);
                    playerTargetView.setRotate(0);
                    playerTargetView.setOnMouseClicked(event -> handleZoneClick(event));
                } else if (aiAttackerData.getAtk() < playerMonster.getDef()) {
                    // Player defense holds -> Recoil damage applies to AI LP
                    int dmg = playerMonster.getDef() - aiAttackerData.getAtk();
                    opponentLP -= dmg;
                    summonText.setText("AI attacks your " + playerMonster.getName() + ", but fails to pierce! AI takes " + dmg + " recoil damage.");
                } else {
                    // Standoff tie
                    summonText.setText("AI attacks your " + playerMonster.getName() + "! ATK equals DEF, neither card is destroyed.");
                }
            }
        }
        // =======================================================
        // 4. FLAG OPERATION & UI UPDATE
        // =======================================================
        // Mark this specific AI attacker instance as used for the turn cycle
        if (aiAttackerData != null) {
            aiAttackerData.setHasAttacked(true);
        }

        // Refresh UI display numbers instantly after the calculations conclude
        updateLifePointsUI();

        // =======================================================
        // 5. TIMED TRANSITION: Pause 2.0s so human can read text, then process next attacker
        // =======================================================
        PauseTransition nextAttackPause = new PauseTransition(Duration.seconds(2.0));
        nextAttackPause.setOnFinished(event -> processNextAIAttack(attackersList, currentIndex + 1));
        nextAttackPause.play();
    }

    @FXML
    private void handlePhaseChange(ActionEvent event) {
        // 1. TURN SECURITY: Block manual button inputs if the computer is processing its turn actions
        if (!isPlayerTurn) {
            summonText.setText("It is currently the AI's turn! Please wait.");
            return;
        }

        // 2. CLEAR ACTIVE SELECTIONS: Prevent state bugs or phantom selection highlights across phases
        if (attackingCardView != null) {
            attackingCardView.setStyle("");
            attackingCardView = null;
        }
        selectedCardView = null;

        // Reset the side inspection preview card back to a readable, vertical format
        cardPreview.setRotate(0);

        // 3. STATE MACHINE SWITCH: Loop sequentially through your turn phases
        switch (currentPhase) {


            case MAIN_PHASE:
                // Move from Main Phase into Battle Phase
                currentPhase = GamePhase.BATTLE_PHASE;
                phaseText.setText("Phase: BATTLE PHASE");
                summonText.setText("Battle Phase! Left-click your monster to declare an attack, then choose a target.");
                break;

            case BATTLE_PHASE:
                // Move from Battle Phase into End Phase
                currentPhase = GamePhase.END_PHASE;
                phaseText.setText("Phase: END PHASE");
                summonText.setText("End Phase. Click the 'Next Phase' button to conclude your turn actions.");
                break;

            case END_PHASE:
                // Hand control over to the Computer AI Engine
                isPlayerTurn = false;
                phaseText.setText("Phase: AI TURN");
                summonText.setText("The computer opponent is analyzing the field...");

                // UPKEEP: Reset all human monster flags so they are ready to attack on your next turn
                resetMonsterAttackFlags(PLAYER_MONSTER_ROW);

                // Reset human turn draw tracking variables before passing the cycle
                hasDrawnThisTurn = false;

                // Trigger the automated computer logic routine block
                runOpponentAILogic();
                break;
            case DRAW_PHASE:
                // Move from Draw Phase into Main Phase 1
                currentPhase = GamePhase.MAIN_PHASE;
                phaseText.setText("Phase: MAIN PHASE");
                summonText.setText("Main Phase! Left-click cards in hand to select and summon. Right-click field monsters to flip positions.");
                break;
        }
    }


    private void executeAutomaticDrawPhase() {
        if (hasDrawnThisTurn) return;

        if (deckPile.isEmpty()) {
            summonText.setText("GAME OVER! Deck out.");
            if (deckVisualPile != null) {
                deckVisualPile.setImage(null);
            }
            return;
        }

        hasDrawnThisTurn = true;
        ImageView topCardView = deckPile.remove(deckPile.size() - 1);
        yourHand.getChildren().add(topCardView);
        playSoundEffect("heehee.mp3");
        if (deckPile.isEmpty() && deckVisualPile != null) {
            deckVisualPile.setImage(null);
            System.out.println("The last card was drawn! Removing visual deck graphic from field.");
        }

        // REFRESH THE DECK COUNT LABEL HERE
        updateDeckSizeUI();

        YuGiOhCard data = (YuGiOhCard) topCardView.getUserData();
        summonText.setText("Drew: " + data.getName());
    }

    @FXML
    private void handleZoneClick(MouseEvent event) {
        ImageView clickedZoneImageView = (ImageView) event.getSource();

        // ROUTE TO BATTLE: If in Battle Phase, immediately redirect click logic
        if (currentPhase == GamePhase.BATTLE_PHASE) {
            handleBattleLogic(clickedZoneImageView);
            return; // Exits early so Main Phase summoning rules don't intercept the click!
        }

        // Now your standard Main Phase checks can run safely below...
        if (currentPhase != GamePhase.MAIN_PHASE) {
            summonText.setText("You can only summon cards during your Main Phase!");
            return;
        }

        if (selectedCardView == null) {
            summonText.setText("No card selected from hand! Click a card in your hand first.");
            return;
        }

        if (!yourHand.getChildren().contains(selectedCardView)) {
            summonText.setText("Error: Selected card is no longer in your hand!");
            selectedCardView = null;
            return;
        }

        if (clickedZoneImageView.getImage() != null) {
            summonText.setText("This field zone is already occupied!");
            return;
        }

        // 3. SAFE GRID ROW CALCULATION: Protects against JavaFX NullPointerException on Row 0
        // Locate this block inside your handleZoneClick method:
        Integer rIndex = GridPane.getRowIndex(clickedZoneImageView);
        Integer cIndex = GridPane.getColumnIndex(clickedZoneImageView);

// If Scene Builder defaults the first row/col to null, map them correctly to 0
        int rowIndex = (rIndex == null) ? 0 : rIndex;
        int colIndex = (cIndex == null) ? 0 : cIndex;

        System.out.println("Click registered at Field coordinates -> Row: " + rowIndex + ", Column: " + colIndex);

        // 4. EXECUTE SUMMON SEQUENCE (Only allow Monsters to enter the Monster Row 2)
        if (selectedCardView.getUserData() instanceof MonsterCard) {
            if (rowIndex == 2) {
                MonsterCard cardData = (MonsterCard) selectedCardView.getUserData();

                // A. Determine Position Stance via Mouse Click Input Button
                if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                    // Right-Click: Set Face-Up Defense Position
                    cardData.setAttackMode(false);
                    clickedZoneImageView.setRotate(90); // Turn image 90 degrees sideways
                    summonText.setText(cardData.getName() + " summoned in Defense Position!");
                } else {
                    // Left-Click: Normal Summon Attack Position
                    cardData.setAttackMode(true);
                    clickedZoneImageView.setRotate(0); // Normal upright positioning
                    summonText.setText("Normal Summoned " + cardData.getName() + " in Attack Position!");
                }

                // B. Detach card from your hand container
                yourHand.getChildren().remove(selectedCardView);
                selectedCardView.setOnMouseClicked(null);

                // C. Inject data payloads and visual imagery to the board space
                clickedZoneImageView.setImage(selectedCardView.getImage());
                clickedZoneImageView.setUserData(cardData);

                // Inside handleZoneClick where you define the field slot listener:
                // Locate this block inside handleZoneClick where you define the board click listener:
                clickedZoneImageView.setOnMouseClicked(fieldEvent -> {
                    ImageView fieldSlot = (ImageView) fieldEvent.getSource();
                    MonsterCard currentZoneData = (MonsterCard) fieldSlot.getUserData();

                    if (currentZoneData == null) return;

                    // --- ALWAYS PREVIEW UPRIGHT ON ANY CLICK ---
                    cardPreview.setImage(fieldSlot.getImage());

                    // FIX: Force the big sidebar preview to stay 0 degrees (upright) so it is readable!
                    cardPreview.setRotate(0);

                    textPreview.setText(currentZoneData.getName() + " (ATK: " + currentZoneData.getAtk() + " / DEF: " + currentZoneData.getDef() + ")");
                    summonText.setText("Viewing field card: " + currentZoneData.getName());

                    // --- PHASE INTERACTION ROUTING ---
                    if (currentPhase == GamePhase.MAIN_PHASE) {
                        // RIGHT-CLICK: Toggle Battle Positions
                        if (fieldEvent.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                            boolean currentStance = currentZoneData.isAttackMode();
                            currentZoneData.setAttackMode(!currentStance);

                            if (currentZoneData.isAttackMode()) {
                                fieldSlot.setRotate(0);   // Card on field goes normal upright
                                // cardPreview.setRotate(0); <-- REMOVED (Keep preview vertical)
                                summonText.setText(currentZoneData.getName() + " shifted to Attack Position!");
                            } else {
                                fieldSlot.setRotate(90);  // Card on field rotates sideways
                                // cardPreview.setRotate(90); <-- REMOVED (Keep preview vertical)
                                summonText.setText(currentZoneData.getName() + " shifted to Defense Position!");
                            }
                        }
                    } else if (currentPhase == GamePhase.BATTLE_PHASE) {
                        handleBattleLogic(fieldSlot);
                    }
                });

                // E. Clean selection reference to conclude step safely
                selectedCardView = null;

            } else {
                summonText.setText("Monsters can only be summoned into your Monster Zone (Row 2)!");
            }
        } else {
            summonText.setText("This card type cannot be summoned to a Monster Zone!");
        }
    }


    private void calculateBattleDamage(ImageView attackerSlot, YuGiOhCard attacker, ImageView targetSlot, YuGiOhCard target) {
        MonsterCard attackerMonster = (MonsterCard) attacker;

        // Scenario 1: Direct Attack
        if (target == null) {
            if (opponentHasNoMonsters()) {
                opponentLP -= attackerMonster.getAtk();
                summonText.setText(attackerMonster.getName() + " attacks directly for " + attackerMonster.getAtk() + " damage!");
            } else {
                summonText.setText("You cannot attack directly! Your opponent still has active monsters.");
            }
            updateLifePointsUI();
            return;
        }

        MonsterCard targetMonster = (MonsterCard) target;

        // =======================================================
        // FIX: Differentiate position stance using the Target Slot's visual rotation angles!
        // =======================================================
        boolean targetInAttackMode = (targetSlot.getRotate() != 90);

        // CASE A: TARGET IS VISUALLY IN ATTACK POSITION (0 degrees)
        if (targetInAttackMode) {
            if (attackerMonster.getAtk() > targetMonster.getAtk()) {
                int damage = attackerMonster.getAtk() - targetMonster.getAtk();
                opponentLP -= damage;
                summonText.setText(attackerMonster.getName() + " destroys " + targetMonster.getName() + "! Dealt " + damage + " LP damage.");
                targetSlot.setImage(null);
                targetSlot.setUserData(null);
                targetSlot.setOnMouseClicked(event -> handleZoneClick(event)); // Reset listener
            } else if (attackerMonster.getAtk() < targetMonster.getAtk()) {
                int damage = targetMonster.getAtk() - attackerMonster.getAtk();
                playerLP -= damage;
                summonText.setText("Your " + attackerMonster.getName() + " was weaker and destroyed! You take " + damage + " damage.");
                attackerSlot.setImage(null);
                attackerSlot.setUserData(null);
                attackerSlot.setOnMouseClicked(event -> handleZoneClick(event)); // Reset listener
            } else {
                summonText.setText("Both monsters had equal ATK and were mutually destroyed!");
                attackerSlot.setImage(null); attackerSlot.setUserData(null);
                targetSlot.setImage(null); targetSlot.setUserData(null);
                attackerSlot.setOnMouseClicked(event -> handleZoneClick(event));
                targetSlot.setOnMouseClicked(event -> handleZoneClick(event));
            }
        }
        // CASE B: TARGET IS VISUALLY IN DEFENSE POSITION (90 degrees)
        else {
            if (attackerMonster.getAtk() > targetMonster.getDef()) {
                summonText.setText(attackerMonster.getName() + " destroys face-up defense " + targetMonster.getName() + "! No LP damage taken.");
                targetSlot.setImage(null);
                targetSlot.setUserData(null);
                targetSlot.setRotate(0); // Reset rotation to normal default
                targetSlot.setOnMouseClicked(event -> handleZoneClick(event)); // Reset listener
            } else if (attackerMonster.getAtk() < targetMonster.getDef()) {
                int recoilDamage = targetMonster.getDef() - attackerMonster.getAtk();
                playerLP -= recoilDamage;
                summonText.setText("Opponent's DEF was higher! Your monster bounces off and you take " + recoilDamage + " damage.");
            } else {
                summonText.setText("Attack points equaled defense points. Standoff! Neither monster was destroyed.");
            }
        }

        updateLifePointsUI();
    }

    private boolean opponentHasNoMonsters() {
        for (javafx.scene.Node node : field.getChildren()) {
            if (node instanceof ImageView) {
                ImageView zone = (ImageView) node;
                Integer rIndex = field.getRowIndex(zone);
                int rowIndex = (rIndex == null) ? 0 : rIndex;

                // Look specifically at the opponent's monster row
                if (rowIndex == 1 && zone.getImage() != null) {
                    return false; // Found a face-up monster! Direct attack is blocked.
                }
            }
        }
        return true; // The opponent's monster row is completely empty!
    }
    private void resetMonsterAttackFlags(int targetRowIndex) {
        System.out.println("--- Resetting Attack Flags for Row: " + targetRowIndex + " ---");

        for (javafx.scene.Node node : field.getChildren()) {
            if (node instanceof ImageView) {
                ImageView zone = (ImageView) node;

                // Safe coordinate lookup
                Integer r = GridPane.getRowIndex(zone);
                int rowIndex = (r == null) ? 0 : r;

                // Target the specific row we want to unlock
                if (rowIndex == targetRowIndex) {
                    // Ensure there is actually a card data payload attached to this visual slot
                    if (zone.getUserData() != null && zone.getUserData() instanceof MonsterCard) {
                        MonsterCard monster = (MonsterCard) zone.getUserData();

                        // Force the flag back to false so it can declare attacks again!
                        monster.setHasAttacked(false);
                        System.out.println("-> Successfully unlocked monster: " + monster.getName());
                    }
                }
            }
        }
        System.out.println("-------------------------------------------");
    }
}
