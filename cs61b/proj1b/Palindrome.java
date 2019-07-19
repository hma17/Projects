public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            d.addLast(word.charAt(i));
        }
        return d;
    }
    public boolean isPalindrome(String word) {
        Deque d = wordToDeque(word);
        String s = "";
        if (d.isEmpty()) {
            return true;
        }
        for (int i = 0; i < word.length(); i++) {
            s += d.removeLast();
        }
        if (word.equals(s)) {
            return true;
        }
        return false;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() <= 1) {
            return true;
        }
        for (int i = 0, j = word.length() - 1; i <= word.length() / 2; i++, j--) {
            if (!cc.equalChars(word.charAt(i), word.charAt(j))) {
                if (i == j) {
                    return true;
                }
                return false;
            }
        } return true;
    }
}
