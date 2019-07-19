import org.junit.Test;
import static org.junit.Assert.*;
public class DequeTest {
    static Palindrome palindrome = new Palindrome();
    @Test

    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("Loqman");
        String actual = "";
        for (int i = 0; i < "Loqman".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("Loqman", actual);
    }
    @Test
    public void testPalindrome() {
        assertTrue(palindrome.isPalindrome("pop"));
        assertTrue(palindrome.isPalindrome(""));
        assertFalse(palindrome.isPalindrome("pope, pal"));
    }

}
