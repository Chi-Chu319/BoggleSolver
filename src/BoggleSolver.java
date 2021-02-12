import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Queue;


import java.util.ArrayList;
import java.util.HashSet;


public class BoggleSolver
{

    private class GraphLite{
        private final int m;
        private final int n;
        private final ArrayList<Bag<Node>> adj;

        private class Node{
            private final int i;
            private final int j;

            public Node(int i, int j){
                this.i = i;
                this.j = j;
            }
            public int i() {return i;}
            public int j() {return j;}
        }

        public GraphLite(int m, int n){
            this.m = m;
            this.n = n;

            // constructing node array
            Node[][] nodes = new Node[m][n];
            for(int i = 0; i < m; i++){
                for(int j = 0; j < n; j++){
                    nodes[i][j] = new Node(i, j);
                }
            }
            adj = new ArrayList<>(m*n);
            for(int i = 0; i < m*n; i++) adj.add(new Bag<>());


            for(int i = 0; i < m; i++){
                for(int j = 0; j < n; j++){
                    Bag<Node> bag = new Bag<>();

                    int bound_i = Math.min(i + 2, m);
                    int bound_j = Math.min(j + 2, n);


                    for (int adj_i = Math.max(0, i - 1); adj_i < bound_i; adj_i++) {
                        for (int adj_j = Math.max(0, j - 1); adj_j < bound_j; adj_j++) {
                            if (adj_i == i && adj_j == j) continue;
                            bag.add(nodes[adj_i][adj_j]);
                        }
                    }
                    adj.set(i*n+j, bag);
                }
            }
        }

        public Bag<Node> adj(int i, int j) {
            return adj.get(i*n+j);
        }
    }

    // modified Trie
    private static class TrieSTLite {
        private static final int R = 26;        // extended ASCII


        private Node root;      // root of trie
        private int n;          // number of keys in trie

        // R-way trie node
        private static class Node {
            private Integer val;
            private Node[] next = new Node[R];
        }

        /**
         * Initializes an empty string symbol table.
         */
        public TrieSTLite() {
        }

        private int resolveIdx(char c){
            return c - 'A';
        }

        public boolean containWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
            }
            Node x = get(root, prefix, 0);
            return x != null;
        }

        /**
         * Returns the value associated with the given key.
         * @param key the key
         * @return the value associated with the given key if the key is in the symbol table
         *     and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public Integer get(String key) {
            if (key == null) throw new IllegalArgumentException("argument to get() is null");
            Node x = get(root, key, 0);
            if (x == null) return null;
            return  x.val;
        }

        /**
         * Does this symbol table contain the given key?
         * @param key the key
         * @return {@code true} if this symbol table contains {@code key} and
         *     {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[resolveIdx(c)], key, d+1);
        }

        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is {@code null}, this effectively deletes the key from the symbol table.
         * @param key the key
         * @param val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(String key, Integer val) {
            if (key == null) throw new IllegalArgumentException("first argument to put() is null");
            if (val == null) delete(key);
            else root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, Integer val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                if (x.val == null) n++;
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[resolveIdx(c)] = put(x.next[resolveIdx(c)], key, val, d+1);
            return x;
        }

        /**
         * Returns the number of key-value pairs in this symbol table.
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return n;
        }

        /**
         * Is this symbol table empty?
         * @return {@code true} if this symbol table is empty and {@code false} otherwise
         */
        public boolean isEmpty() {
            return size() == 0;
        }

        /**
         * Returns all keys in the symbol table as an {@code Iterable}.
         * To iterate over all of the keys in the symbol table named {@code st},
         * use the foreach notation: {@code for (Key key : st.keys())}.
         * @return all keys in the symbol table as an {@code Iterable}
         */
        public Iterable<String> keys() {
            return keysWithPrefix("");
        }

        /**
         * Returns all of the keys in the set that start with {@code prefix}.
         * @param prefix the prefix
         * @return all of the keys in the set that start with {@code prefix},
         *     as an iterable
         */
        public Iterable<String> keysWithPrefix(String prefix) {
            Queue<String> results = new Queue<>();
            Node x = get(root, prefix, 0);
            collect(x, new StringBuilder(prefix), results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, Queue<String> results) {
            if (x == null) return;
            if (x.val != null) results.enqueue(prefix.toString());
            for (char c = 0; c < R; c++) {
                prefix.append(c);
                collect(x.next[resolveIdx(c)], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        /**
         * Returns all of the keys in the symbol table that match {@code pattern},
         * where . symbol is treated as a wildcard character.
         * @param pattern the pattern
         * @return all of the keys in the symbol table that match {@code pattern},
         *     as an iterable, where . is treated as a wildcard character.
         */
        public Iterable<String> keysThatMatch(String pattern) {
            Queue<String> results = new Queue<>();
            collect(root, new StringBuilder(), pattern, results);
            return results;
        }

        private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
            if (x == null) return;
            int d = prefix.length();
            if (d == pattern.length() && x.val != null)
                results.enqueue(prefix.toString());
            if (d == pattern.length())
                return;
            char c = pattern.charAt(d);
            if (c == '.') {
                for (char ch = 0; ch < R; ch++) {
                    prefix.append(ch);
                    collect(x.next[ch], prefix, pattern, results);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
            else {
                prefix.append(c);
                collect(x.next[resolveIdx(c)], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

        /**
         * Returns the string in the symbol table that is the longest prefix of {@code query},
         * or {@code null}, if no such string.
         * @param query the query string
         * @return the string in the symbol table that is the longest prefix of {@code query},
         *     or {@code null} if no such string
         * @throws IllegalArgumentException if {@code query} is {@code null}
         */
        public String longestPrefixOf(String query) {
            if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
            int length = longestPrefixOf(root, query, 0, -1);
            if (length == -1) return null;
            else return query.substring(0, length);
        }

        // returns the length of the longest string key in the subtrie
        // rooted at x that is a prefix of the query string,
        // assuming the first d character match and we have already
        // found a prefix match of given length (-1 if no such match)
        private int longestPrefixOf(Node x, String query, int d, int length) {
            if (x == null) return length;
            if (x.val != null) length = d;
            if (d == query.length()) return length;
            char c = query.charAt(d);
            return longestPrefixOf(x.next[resolveIdx(c)], query, d+1, length);
        }

        /**
         * Removes the key from the set if the key is present.
         * @param key the key
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void delete(String key) {
            if (key == null) throw new IllegalArgumentException("argument to delete() is null");
            root = delete(root, key, 0);
        }

        private Node delete(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) {
                if (x.val != null) n--;
                x.val = null;
            }
            else {
                char c = key.charAt(d);
                x.next[resolveIdx(c)] = delete(x.next[resolveIdx(c)], key, d+1);
            }

            // remove subtrie rooted at x if it is completely empty
            if (x.val != null) return x;
            for (int c = 0; c < R; c++)
                if (x.next[c] != null)
                    return x;
            return null;
        }

    }


    private final TrieSTLite dictionary;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        this.dictionary = new TrieSTLite();
        for (String s: dictionary) this.dictionary.put(s, score(s));
    }



    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        int m = board.rows();
        int n = board.cols();

        // constructing graph.
        GraphLite g = new GraphLite(m, n);



        HashSet<String> strings = new HashSet<>();
        for(int i = 0; i< m; i++){
            for(int j = 0; j< n; j++) {
                boolean[][] visited = new boolean[m][n];
                StringBuilder chars = new StringBuilder();
                dfs(i, j, chars, strings, visited, board, g);
            }
        }

//        ArrayList<String> stringList = new ArrayList<>();
//        for (String s:strings) stringList.add(s);
//
//        String[] array = stringList.toArray(new String[0]);
//
//        Quick3string.sort(array);
//
//        stringList = new ArrayList(Arrays.asList(array));

        return strings;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (dictionary.contains(word)) return dictionary.get(word);
        else return 0;
    }

    // computing scores for words in dict
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


    private void dfs(int i, int j, StringBuilder chars, HashSet<String> strings, boolean[][] visited, BoggleBoard board, GraphLite g){

        visited[i][j] = true;
        char c = board.getLetter(i, j);
        if (c == 'Q'){
            chars.append(c);
            chars.append('U');
        }else{
            chars.append(c);
        }


        boolean stop = false;

        if(!dictionary.containWithPrefix(chars.toString())) stop = true;

        if (!stop) {

            if(chars.length() >= 3 && dictionary.contains(chars.toString())) strings.add(chars.toString());


            for(GraphLite.Node n : g.adj(i, j)){
                if (!visited[n.i()][n.j()]) dfs(n.i(), n.j(), chars, strings, visited, board, g);
            }

        }

        visited[i][j] = false;
        if ( chars.charAt(chars.length() - 1) == 'U' && chars.length()>=2 && chars.charAt(chars.length()-2) == 'Q'){
            chars.deleteCharAt(chars.length()-1);
            chars.deleteCharAt(chars.length()-1);
        }
        else{
            chars.deleteCharAt(chars.length()-1);
        }
    }

}