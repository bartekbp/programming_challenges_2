package graphs.editstepladders;
import java.io.IOException;
import java.util.*;

public class Main implements Runnable {
    static String readLn(int maxLength) {

        byte line[] = new byte[maxLength];
        int length = 0;
        int input = -1;
        try {
            while (length < maxLength) {
                input = System.in.read();
                if ((input < 0) || (input == '\n')) {
                    break;
                }

                line[length++] += input;
            }

            if ((input < 0) && (length == 0)) {
                return null;
            }

            return new String(line, 0, length);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String args[]) {
        Main myWork = new Main();
        myWork.run();
    }

    public void run() {
        new EditStepLadders().run();
    }
}

class EditStepLadders implements Runnable {
    Set<String> words = new HashSet<String>();
    Map<String, Integer> editDistances = new HashMap<String, Integer>();

    public void run() {
        String word = Main.readLn(100);
        while(word != null && !word.equals("")) {
            words.add(word);
            word = Main.readLn(100);
        }

        int maxEditDistance = 0;
        for(String wordToCalculateDistance: words) {
            maxEditDistance = Math.max(maxEditDistance, findEditDistance(wordToCalculateDistance));
        }

        System.out.println(maxEditDistance);
    }


    private int findEditDistance(String word) {
        Integer wordPreviouslyComputedEditDistance = editDistances.get(word);
        if(wordPreviouslyComputedEditDistance != null) {
            return wordPreviouslyComputedEditDistance;
        }

        int maxEditDistance = 0;
        for(int i = 0; i < word.length(); i++) {
            char currentLetter = word.charAt(i);
            String prefix = word.substring(0, i);
            String suffix = word.substring(i + 1);
            char nextDifferentLetter = findNextDifferentLetter(word, i);
            for(char letter = (char) (currentLetter + 1); letter <= 'z'; letter++) {
                String oneStepAheadWord = prefix + letter + suffix;
                maxEditDistance = Math.max(maxEditDistance, testNextWord(word, oneStepAheadWord));
                
                oneStepAheadWord = prefix + letter + word.substring(i);
                maxEditDistance = Math.max(maxEditDistance, testNextWord(word, oneStepAheadWord));
            }

            if(currentLetter > nextDifferentLetter) {
                maxEditDistance = Math.max(maxEditDistance, testNextWord(word, prefix + currentLetter + word.substring(i)));
            } else if(currentLetter < nextDifferentLetter) {
                maxEditDistance = Math.max(maxEditDistance, testNextWord(word, prefix + suffix));
            }
        }

        for(char letter = 'a'; letter <= 'z'; letter++) {
            maxEditDistance = Math.max(maxEditDistance, testNextWord(word, word + letter));
        }

        editDistances.put(word, maxEditDistance + 1);
        return maxEditDistance + 1;
    }

    private char findNextDifferentLetter(String word, int baseLetterPosition) {
        char baseLetter = word.charAt(baseLetterPosition);
        for(int j = baseLetterPosition + 1; j < word.length(); j++) {
            if(word.charAt(j) != baseLetter) {
                return word.charAt(j);
            }
        }

        return baseLetter;
    }

    private int testNextWord(String word, String oneStepAheadWord) {
        if(words.contains(oneStepAheadWord)) {
            return findEditDistance(oneStepAheadWord);
        }

        return 0;
    }
}
