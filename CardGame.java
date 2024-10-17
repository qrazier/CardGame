import java.util.*;

public class CardGame {

    private static final String[] cards = {
            "2@", "2#", "2^", "2*", "3@", "3#", "3^", "3*", "4@", "4#", "4^", "4*", "5@", "5#", "5^", "5*",
            "6@", "6#", "6^", "6*", "7@", "7#", "7^", "7*", "8@", "8#", "8^", "8*", "9@", "9#", "9^", "9*",
            "10@", "10#", "10^", "10*", "J@", "J#", "J^", "J*", "Q@", "Q#", "Q^", "Q*", "K@", "K#", "K^", "K*",
            "A@", "A#", "A^", "A*"
    };

    public static void main(String[] args) {
        System.out.println("Cards: " + Arrays.toString(cards));
        int noOfPlayer = 4;

        //  Shuffle the cards
        List<String> shuffledCards = new ArrayList<>(Arrays.asList(cards));
        Collections.shuffle(shuffledCards);
        System.out.println("Shuffled cards: " + shuffledCards + "\n");

        // Distribute the cards
        List<List<String>> players = distributeCards(noOfPlayer, shuffledCards);

        // Display player's hand
        for (int i = 0; i < players.size(); i++) {
            // Sort each player's hand based on alphanumeric and symbol order
            players.get(i).sort((card1, card2) -> compareCards(card1, card2));
            System.out.println("Player " + (i + 1) + "'s hand: " + players.get(i));
        }

        System.out.println();
        // Determine the winner
        System.out.println("Player " + determineWinner(players) + " is the winner!");
    }

    // Distribute the cards to the players
    private static List<List<String>> distributeCards(int noOfPlayer, List<String> cards) {
        List<List<String>> players = new ArrayList<>();

        // Create the players
        for (int i = 0; i < noOfPlayer; i++) {
            players.add(new ArrayList<>());
        }

        // Distribute the cards
        for (int i = 0; i < cards.size(); i++) {
            players.get(i % noOfPlayer).add(cards.get(i));
        }

        return players;
    }

    // Determine the winner based on the rules
    private static int determineWinner(List<List<String>> players) {
        // To store values for the winning condition
        int[] highestCounts = new int[players.size()];
        String[] highestAlphanumeric = new String[players.size()];
        List<List<String>> highestCards = new ArrayList<>();

        // Initialize a list for each player's highest cards
        for (int i = 0; i < players.size(); i++) {
            highestCards.add(new ArrayList<>());
        }

        // Find the highest count of matching alphanumeric values for each player
        for (int i = 0; i < players.size(); i++) {
            // To store the number of cards with same alphanumeric part
            Map<String, Integer> alphanumericCount = new HashMap<>();
            int maxCount = 0;
            String highestAlpha = "";

            // Count occurrences of each alphanumeric part
            for (String card : players.get(i)) {
                String alphanumeric = card.substring(0, card.length() - 1);
                alphanumericCount.put(alphanumeric, alphanumericCount.getOrDefault(alphanumeric, 0) + 1);
            }

            // Find the alphanumeric part(s) with the highest count
            for (Map.Entry<String, Integer> entry : alphanumericCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    highestAlpha = entry.getKey();  // Store the current highest alphanumeric
                } else if (entry.getValue() == maxCount) {
                    // If counts are equal, compare alphanumeric values and take the higher one
                    if (compareAlphanumerics(entry.getKey(), highestAlpha) > 0) {
                        highestAlpha = entry.getKey();
                    }
                }
            }

            // Store the highest count and the alphanumeric value for each player
            highestCounts[i] = maxCount;
            highestAlphanumeric[i] = highestAlpha;

            // Collect all cards that match the highest alphanumeric value
            for (String card : players.get(i)) {
                if (card.startsWith(highestAlphanumeric[i])) {
                    highestCards.get(i).add(card);
                }
            }
            System.out.println("Player " + (i + 1) + " highestCounts: " + highestCounts[i] + "" + highestCards.get(i));
        }

        System.out.println();
        // Determine the winner based on highest counts, and tie-breaking rules
        int winner = 0;
        for (int i = 1; i < players.size(); i++) {
            System.out.println("Player " + (winner + 1) + ": " + highestCards.get(winner));
            System.out.println("Player " + (i + 1) + ": " + highestCards.get(i));
            if (highestCounts[i] > highestCounts[winner]) {
                winner = i;
                System.out.println("Winner: Player " + (winner + 1));
            } else if (highestCounts[i] == highestCounts[winner]) {
                // If counts are tied, use the tie-breaking rule
                String bestCardPlayerI = highestCards.get(i).getLast();
                String bestCardWinner = highestCards.get(winner).getLast();

                if (compareCards(bestCardPlayerI, bestCardWinner) > 0) {
                    winner = i;
                    System.out.println("Winner: Player " + (winner + 1));
                } else {
                    System.out.println("Winner: Player " + (winner + 1));
                }
            } else {
                System.out.println("Winner: Player " + (winner + 1));
            }
            System.out.println();
        }

        // Display match result
        System.out.println("\nMatch Result:");
        System.out.println("Player " + (winner + 1) + " wins with the following cards: " + highestCards.get(winner));

        return winner + 1; // Returning player number (index + 1)
    }

    // Compare two alphanumerics
    private static int compareAlphanumerics(String alpha1, String alpha2) {
        String values = "2345678910JQKA";
        int value1 = values.indexOf(alpha1);
        int value2 = values.indexOf(alpha2);
        return value1 - value2;
    }

    // Compare two cards based on alphanumeric and symbol parts
    private static int compareCards(String card1, String card2) {
        String values = "2345678910JQKA";
        String symbols = "@#^*";

        // Compare alphanumeric parts first
        int value1 = values.indexOf(card1.substring(0, card1.length() - 1));
        int value2 = values.indexOf(card2.substring(0, card2.length() - 1));
        if (value1 != value2) {
            return value1 - value2;
        }

        // If alphanumeric parts are the same, compare symbols
        char symbol1 = card1.charAt(card1.length() - 1);
        char symbol2 = card2.charAt(card2.length() - 1);
        return symbols.indexOf(symbol1) - symbols.indexOf(symbol2);
    }
}
