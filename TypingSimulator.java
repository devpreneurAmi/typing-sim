import java.util.*;

public class TypingSimulator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random rand = new Random();

    private static final String[] PARAGRAPHS = {
        "The quick brown fox jumps over the lazy dog.",
        "Programming is not about typing fast; it's about thinking clearly.",
        "Discipline beats motivation every single time.",
        "In the world of coding, consistency is the secret weapon that wins every battle.",
        "The best way to learn to type faster is to slow down, focus on accuracy, and build muscle memory."
    };

    private static final String[] WORDS = {
        "the","be","to","of","and","a","in","that","have","i","it","for","not","on","with","he",
        "as","you","do","at","this","but","his","by","from","they","we","say","her","she","or","an",
        "will","my","one","all","would","there","their","what","so","up","out","if","about","who","get",
        "which","go","me","when","make","can","like","time","no","just","him","know","take","people","into",
        "year","your","good","some","could","them","see","other","than","then","now","look","only","come",
        "its","over","think","also","back","after","use","two","how","our","work","first","well","way",
        "even","new","want","because","any","these","give","day","most","us"
    };

    public static void main(String[] args) {
        System.out.println("=== Typing Simulator (Built-in Data) ===");
        System.out.println("Choose test type:");
        System.out.println("1) Paragraphs with capitals and punctuation");
        System.out.println("2) Lowercase-only words");
        System.out.println("3) Monkey-type short test (10, 15, or 25 words)");
        System.out.println("Type 'exit' to quit.\n");

        while (true) {
            System.out.print("Enter choice (1/2/3 or exit): ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("exit")) break;

            String target = null;
            switch (choice) {
                case "1": target = getRandomParagraph(); break;
                case "2": target = generateWords(askWordCount(40), true); break;
                case "3": target = monkeyTypeOption(); break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
            }

            if (target == null) continue;

            System.out.println("\n--- Target Text ---\n" + target + "\n");
            System.out.println("(Press Enter to start)");
            scanner.nextLine();

            System.out.println("Start typing now:");
            long start = System.nanoTime();
            String typed = scanner.nextLine();
            long end = System.nanoTime();

            showResults(target, typed, start, end);
        }

        System.out.println("Goodbye!");
        scanner.close();
    }

    private static String getRandomParagraph() {
        return PARAGRAPHS[rand.nextInt(PARAGRAPHS.length)];
    }

    private static int askWordCount(int def) {
        System.out.printf("How many words do you want? (press Enter for %d): ", def);
        String in = scanner.nextLine().trim();
        if (in.equalsIgnoreCase("exit")) return -1;
        if (in.isEmpty()) return def;
        try {
            return Math.max(1, Integer.parseInt(in));
        } catch (Exception e) {
            System.out.println("Invalid number, using default: " + def);
            return def;
        }
    }

    private static String generateWords(int count, boolean lowerOnly) {
        if (count <= 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String w = WORDS[rand.nextInt(WORDS.length)];
            if (!lowerOnly && rand.nextDouble() < 0.1) {
                w = Character.toUpperCase(w.charAt(0)) + w.substring(1);
            }
            sb.append(w);
            if (i < count - 1) sb.append(' ');
        }
        return sb.toString();
    }

    private static String monkeyTypeOption() {
        System.out.print("Enter 10, 15, or 25 words: ");
        while (true) {
            String in = scanner.nextLine().trim();
            if (in.equalsIgnoreCase("exit")) return null;
            if (in.equals("10") || in.equals("15") || in.equals("25")) {
                return generateWords(Integer.parseInt(in), true);
            }
            System.out.print("Invalid. Enter 10, 15, or 25: ");
        }
    }

    private static void showResults(String target, String typed, long start, long end) {
        double timeSec = (end - start) / 1_000_000_000.0;
        int total = target.length(), typedLen = typed.length();
        int correct = 0, mistakes = 0;

        for (int i = 0; i < Math.min(total, typedLen); i++) {
            if (target.charAt(i) == typed.charAt(i)) correct++; else mistakes++;
        }
        if (typedLen > total) mistakes += typedLen - total;
        if (typedLen < total) mistakes += total - typedLen;

        double accuracy = (correct * 100.0) / total;
        double minutes = timeSec / 60.0;
        double wpm = (correct / 5.0) / Math.max(1e-9, minutes);

        System.out.println("\n=== Results ===");
        System.out.printf("Time taken: %.2f sec\nAccuracy: %.2f%%\nWPM: %.2f\n", timeSec, accuracy, wpm);
        System.out.printf("Correct chars: %d | Mistakes: %d | Total: %d\n", correct, mistakes, total);

        System.out.println();
    }
}
