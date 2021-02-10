import edu.princeton.cs.algs4.BoyerMoore;
import edu.princeton.cs.algs4.Queue;
import java.util.ArrayList;
import java.util.HashSet;


public class BoggleSolver
{

    private int n;
    private int m;
    private boolean[] radix = new boolean[256];
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
                radix[board.getLetter(i, j)] = true;
            }
        }

        // returned iterable
        Queue<String> queue = new Queue<>();

        String str = ConstructString(board);

        int count = 0;
        for (String word:dictionary){
            // if the board does not contain the character.
            // or the string is longer than 16.
            if (word.length() < 3 || word.length() > 16 || !contain(word)) continue;
            count++;
            BoyerMoore b = new BoyerMoore(word);
            // found
            if(b.search(str)<str.length()) queue.enqueue(word);
        }

        System.out.println(dictionary.length);
        System.out.println(count);

        return queue;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        // todo binary search to find if the dict contain the word.
        return switch (word.length()) {
            case 1, 2 -> 0;
            case 3, 4 -> 1;
            case 5 -> 2;
            case 6 -> 3;
            case 7 -> 5;
            // eight or more
            default -> 11;
        };
    }

    private boolean contain(String str){
        // if the chars in the string are contained in the board
        for(char c:str.toCharArray()){
            if (!radix[c]) return false;
        }
        return true;
    }

    private String ConstructString(BoggleBoard board){
        StringBuilder str = new StringBuilder();
        HashSet<String> strs = new HashSet<>();
        for(int i = 0; i< board.cols(); i++){
            for(int j = 0; j< board.rows(); j++) {
                boolean[][] visited = new boolean[m][n];
                ArrayList<Character> arrayList = new ArrayList<>();
                dfs(i, j, arrayList, strs, visited);
            }
        }

        // constructing the str
        for(String s : strs){
            str.append(" ");
            str.append(s);
        }

        return str.toString();
    }

    private void dfs(int i, int j, ArrayList<Character> arrayList, HashSet<String> strs, boolean[][] visited){
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
                    dfs(adj_i, adj_j, arrayList, strs, visited);
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
                }
                s.append(c);
            }
            strs.add(s.toString());
        }
        visited[i][j] = false;
        arrayList.remove(arrayList.size()-1);
    }

    private static void main(String[] args) {

    }
}