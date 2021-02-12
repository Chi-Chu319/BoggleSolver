import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
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

//        File myObj = new File("F:/Personal_skill_development/AlgorithmsPart2/Assignments/week4/BoggleSolver/src/boards/board-test1.txt");
//        myObj.createNewFile();
//        FileWriter myWriter = new FileWriter("F:/Personal_skill_development/AlgorithmsPart2/Assignments/week4/BoggleSolver/src/boards/board-test1.txt");
//        In in = new In("F:/Personal_skill_development/AlgorithmsPart2/Assignments/week4/BoggleSolver/src/boards/board-test.txt");
//        for (String line : in.readAllLines()){
//            line = line.strip();
//            System.out.println(line);
//            myWriter.write(line+"\n");
//        }
//        myWriter.close();
    }
}
