import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.Scanner;

/* L33TCODE CHALLENGE 210 */
public class Cheat {
    static List<Tile> tiles = new ArrayList<Tile>();
    static int key = 0;
    static long CODE = 0;

    public static void main(String[] pos) {

        // generate lists
        List<String> list = new ArrayList<String>();
        List<String> input = new ArrayList<String>();
        try {
            // read input file, seperated by spaces
            Scanner s = new Scanner(new File("wiki-500k.txt"));
            Scanner b = new Scanner(new File("input.txt"));
            // populate lists
            while (s.hasNext()) {
                list.add(s.next());
            }
            while (b.hasNext()) {
                input.add(b.next());
            }
            // close scanners
            b.close();
            s.close();

        } catch (Exception ex) {
            System.out.println(ex);
        }

        // convert to string array
        String[] uhOH = new String[list.size()];

        // populate from list
        for (int i = 0; i < list.size(); i++) {
            uhOH[i] = list.get(i);
        }
        list.clear(); // remove the used space?

        // initialize a 4x4 board
        char[][] board = new char[4][4];

        // populate board based off input
        for (int i = 0; i < input.size(); i++) {
            board[i / 4][i % 4] = input.get(i).charAt(0);
        }

        //run function
        System.out.println(
                "FINAL ANSWER IS: " +
                        findWords(board, uhOH));

        System.out.println("only took me " + CODE + " operations :)");
    }

    public static List<String> findWords(char[][] board, String[] words) {
        // GOOD NEWS: it works!
        // BAD (very) NEWS: it's so incredibly inefficient :(

        // generate default answer array
        List<String> ans = new ArrayList<String>();

        // loop through all possibe words
        findword: for (String word : words) {
            generateBoard(board); // generate board
            if (word.length() > tiles.size())
                continue findword;

            // System.out.println("word is: " + word);
            // LOOP THROUGH EVERY WORD
            char[] wordChars = word.toCharArray();
            List<Tile> letters = findAll(wordChars[0]);
            for (Tile t : letters) {
                t.getKey(); // initiate default key for all tiles
            }

            for (int i = 1; i < wordChars.length; i++) {
                List<Tile> possibles = findAll(wordChars[i]);

                // loop through letters
                for (Tile t : letters) {

                    // System.out.println("TILE IS " + t);

                    for (int p = 0; p < possibles.size(); p++) {

                        if (t.pos.isNear(possibles.get(p).pos) && t.noKeyMatch(possibles.get(p))) {
                            possibles.get(p).save = true; // save it!
                            // pass on every key with a minor addition
                            for (int k = 0; k < t.keys.size(); k++) {
                                String tempKey = t.keys.get(k);
                                possibles.get(p).keys.add(tempKey + p);
                                // System.out.println(possibles.get(p) + " got the key: " + (tempKey+p));
                            }
                        }
                    }

                    t.save = false;
                }

                for (int p = 0; p < possibles.size(); p++) {
                    if (!possibles.get(p).save) {
                        possibles.remove(p);
                        p--;
                    }
                }

                letters = possibles;

                if (letters.isEmpty()) {
                    continue findword;
                }
            }

            if (letters.isEmpty()) {
                continue findword;
            }

            ans.add(word);
        }

        Collections.sort(ans, (x, y) -> {
            return x.length() - y.length();
        });
        // return answer
        return ans;
    }

    static List<Tile> findAll(char c) {
        List<Tile> sols = new ArrayList<Tile>();
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).c == c) {
                sols.add(tiles.get(i));
            }
            CODE++;
        }
        return sols;
    }

    static void generateBoard(char[][] board) {
        // clear board
        tiles.clear();
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {
                // add the tile
                tiles.add(new Tile(board[row][column], new Position(row, column)));
            }
        }
    }

    static class Tile {
        char c;
        Position pos;
        List<String> keys = new ArrayList<String>();
        boolean save;
        static char keyToAdd = 'a'; // static

        public Tile(char ch, Position poss) {
            c = ch;
            pos = poss;
        }

        public String toString() {
            return c + " at " + pos; // identified by tile and position
        }

        public boolean noKeyMatch(Tile t) {
            int badKeys = 0;
            for (String key : keys) {
                for (String enemKey : t.keys) {
                    // a key that's contained within a larger key is a older version of the same
                    // path
                    if (key.contains(enemKey)) {
                        badKeys++;
                    }

                    // if most of the keys do not work, don't try creating the word
                    if (badKeys >= keys.size() - 1) {
                        return false;
                    }
                }
            }

            return true;
        }

        void getKey() {
            keys.add(keyToAdd++ + "");
        }
    }

    static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return x + ", " + y;
        }

        public boolean isNear(Position p) {
            int distance = (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y);
            return 1 == distance || 2 == distance; // 2: diagonal 1: adjacent
        }
    }
}