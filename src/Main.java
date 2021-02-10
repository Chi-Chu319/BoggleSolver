import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Main {

    public static void main(String[] args){
        // write your code here

//        BoggleSolver solver = new BoggleSolver(null);
//        BoggleBoard board = new BoggleBoard();
//
//        System.out.println(board);
//        System.out.println("The constructed string: ");
//        Thread.sleep(3000);
//        solver.getAllValidWords(board);

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
