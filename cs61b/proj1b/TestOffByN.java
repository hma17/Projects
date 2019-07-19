import org.junit.Test;
import static org.junit.Assert.*;
public class TestOffByN {

    static CharacterComparator offByN = new OffByN(1);
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testEqualChars() {
        assertTrue(offByN.equalChars('a', 'e'));
    }
    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("detrude", offByN));
    }

}
