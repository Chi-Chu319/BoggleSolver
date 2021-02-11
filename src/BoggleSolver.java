import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Quick3string;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.TrieST;

import java.util.ArrayList;


public class BoggleSolver
{

//    private int[] radix = new int[256];
    private TrieST<Integer> dictionary;



    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        this.dictionary = new TrieST<>();
        for (String s: dictionary) this.dictionary.put(s, score(s));
    }



    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        int m = board.cols();
        int n = board.rows();

        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i< m; i++){
            for(int j = 0; j< m; j++) {
                boolean[][] visited = new boolean[m][n];
                StringBuilder chars = new StringBuilder();
                dfs(i, j, chars, strings, visited, board);
            }
        }

        return strings;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (dictionary.contains(word)) return dictionary.get(word);
        else return 0;
    }
    private int score(String word){
        switch (word.length()){
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }


    }

//    private boolean contain(String str){
//        // if the chars in the string are contained in the board
//        int[] radix_replica = new int[256];
//        for(char c:str.toCharArray()){
//            radix_replica[c]++;
//            if (radix_replica[c] > radix[c]) return false;
//        }
//        return true;
//    }


    private int BinarySearch(String[] strings, String key, int lo, int hi){
        // not found
        if(lo>=hi) return -1;
        int mid = lo + (hi - lo)/2;
        for(int i = 0; i < key.length(); i++){
            if(i >= strings[mid].length() || strings[mid].charAt(i)>key.charAt(i)){ return BinarySearch(strings, key, lo, mid); }
            else if(strings[mid].charAt(i)<key.charAt(i)){ return BinarySearch(strings, key, mid + 1, hi); }
//            else continue;
        }
        // found
        return mid;
    }


    private void dfs(int i, int j, StringBuilder chars, ArrayList<String> strings, boolean[][] visited, BoggleBoard board){
//        System.out.println(i);
//        System.out.println(j);
        chars.append(board.getLetter(i, j));
        visited[i][j] = true;

        int bound_i = Math.min(i + 2, board.cols());
        int bound_j = Math.min(j + 2, board.rows());
        // if is the end of the dfs search
        boolean end = true;

        for (int adj_i = Math.max(0, i - 1); adj_i < bound_i; adj_i++){
            for (int adj_j = Math.max(0, j - 1); adj_j < bound_j; adj_j++) {
                if(adj_i == i && adj_j == j) continue;
                if (!visited[adj_i][adj_j]) {
                    // do dfs further
                    end = false;
                    dfs(adj_i, adj_j, chars, strings, visited, board);
                }
            }
        }
        if (end){
            String match = dictionary.longestPrefixOf(chars.toString());
            if (match != null && match.length()>=3) strings.add(chars.toString());
        }

        chars = new StringBuilder(chars.substring(0, chars.length()-1));

    }

    private static void main(String[] args) {

    }
}