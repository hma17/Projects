public class OffByN implements CharacterComparator {
    int equalN;
    public OffByN(int N) {
        equalN = N;
    }


    public boolean equalChars(int x, int y) {
        int a = (int) x;
        int b = (int) y;

        if ((a + equalN == b) || (a - equalN == b)) {
            return true;
        }
        return false;
    }

}
