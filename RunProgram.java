import java.util.Scanner;
public class RunProgram {
    
    public static void main(String[] pogs) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the 16 lowercase letters of the board");
        
        char[][] board = new char[4][4];

        for (int i = 0; i < 16; i++) {
            board[i / 4][i % 4] = in.nextLine().charAt(0);    
        }

        System.out.println("Chosen chars are are:");
        for (char ch[] : board) {
            for (char c : ch) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println("is that ok? 'n' to repeat input");

        if (in.nextLine().equalsIgnoreCase("n")) {
            main(pogs);
            in.close();
            return;
        } else {
            in.close();
            Cheat.run(board);
        }

       
    }
}
