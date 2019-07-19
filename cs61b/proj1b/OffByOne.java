public class OffByOne implements CharacterComparator {

    @Override
    public boolean equalChars(char x, char y) {
        int a = (int) x;
        int b = (int) y;

        if ((a + 1 == b) || (a - 1 == b)) {
            return true;
        }
        return false;
    }

}
