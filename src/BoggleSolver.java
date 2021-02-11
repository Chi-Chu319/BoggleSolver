import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Quick3string;
import edu.princeton.cs.algs4.TrieSET;

import java.util.ArrayList;


public class BoggleSolver
{

    private int n;
    private int m;
    private int[] radix = new int[256];
    private String[] dictionary;
    private BoggleBoard board;



    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        this.dictionary = dictionary;
    }



    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        this.m = board.rows();
        this.n = board.cols();
        this.board = board;
        for(int i = 0; i<m;i++){
            for(int j = 0; j<n;j++) {
                char c = board.getLetter(i, j);
                if (c == 'Q') radix['U']++;
                radix[c]++;
            }
        }



        // returned iterable
        Queue<String> queue = new Queue<>();

        String[] strings = ConstructString(board);

        // sort the strings.

        Quick3string.sort(strings);

//        System.out.println("String constructed");



//        int count = 0;
        for (String word:dictionary){
            // if the board does not contain the character.
            // or the string is longer than 16.
            if (word.length() < 3 || word.length() > 16 ||!contain(word)) continue;
            else if (BinarySearch(strings, word, 0, strings.length) != -1) queue.enqueue(word);


//            count ++;
//            System.out.println(word);
//
//            System.out.println(count);
        }

//        System.out.println(dictionary.length);
//        System.out.println(count);

        return queue;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        // todo binary search to find if the dict contain the word.
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

    private boolean contain(String str){
        // if the chars in the string are contained in the board
        int[] radix_replica = new int[256];
        for(char c:str.toCharArray()){
            radix_replica[c]++;
            if (radix_replica[c] > radix[c]) return false;
        }
        return true;
    }



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

    private String[] ConstructString(BoggleBoard board){
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i< board.cols(); i++){
            for(int j = 0; j< board.rows(); j++) {
                boolean[][] visited = new boolean[m][n];
                ArrayList<Character> arrayList = new ArrayList<>();
                dfs(i, j, arrayList, strings, visited);
            }
        }

        return strings.toArray(new String[0]);
    }

    private void dfs(int i, int j, ArrayList<Character> arrayList, ArrayList<String> strings, boolean[][] visited){
//        System.out.println(i);
//        System.out.println(j);
        arrayList.add(board.getLetter(i, j));
        visited[i][j] = true;

        int bound_i = Math.min(i + 2, m);
        int bound_j = Math.min(j + 2, n);
        // if is the end of the dfs search
        boolean end = true;

        for (int adj_i = Math.max(0, i - 1); adj_i < bound_i; adj_i++){
            for (int adj_j = Math.max(0, j - 1); adj_j < bound_j; adj_j++) {
                if(adj_i == i && adj_j == j) continue;
                if (!visited[adj_i][adj_j]) {
                    // do dfs further
                    end = false;
                    dfs(adj_i, adj_j, arrayList, strings, visited);
                }
            }
        }

        // no way to go.
        // end of this dfs search.
        if (end){
            StringBuilder s = new StringBuilder();
            for(char c: arrayList){
                if (c == 'Q'){
                    s.append("QU");
                }else s.append(c);
            }
            strings.add(s.toString());
        }
        visited[i][j] = false;
        arrayList.remove(arrayList.size()-1);
    }

    private static void main(String[] args) {

    }
}