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

    // modified TST
    private static class TST<Value> {
        private int n;              // size
        private Node<Value> root;   // root of TST

        private static class Node<Value> {
            private char c;                        // character
            private Node<Value> left, mid, right;  // left, middle, and right subtries
            private Value val;                     // value associated with string
        }

        /**
         * Initializes an empty string symbol table.
         */
        public TST() {
        }

        /**
         * Returns the number of key-value pairs in this symbol table.
         * @return the number of key-value pairs in this symbol table
         */
        public int size() {
            return n;
        }

        /**
         * Does this symbol table contain the given key?
         * @param key the key
         * @return {@code true} if this symbol table contains {@code key} and
         *     {@code false} otherwise
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public boolean contains(String key) {
            if (key == null) {
                throw new IllegalArgumentException("argument to contains() is null");
            }
            return get(key) != null;
        }

        /**
         * Returns the value associated with the given key.
         * @param key the key
         * @return the value associated with the given key if the key is in the symbol table
         *     and {@code null} if the key is not in the symbol table
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public Value get(String key) {
            if (key == null) {
                throw new IllegalArgumentException("calls get() with null argument");
            }
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            Node<Value> x = get(root, key, 0);
            if (x == null) return null;
            return x.val;
        }

        // return subtrie corresponding to given key
        private Node<Value> get(Node<Value> x, String key, int d) {
            if (x == null) return null;
            if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
            char c = key.charAt(d);
            if      (c < x.c)              return get(x.left,  key, d);
            else if (c > x.c)              return get(x.right, key, d);
            else if (d < key.length() - 1) return get(x.mid,   key, d+1);
            else                           return x;
        }

        /**
         * Inserts the key-value pair into the symbol table, overwriting the old value
         * with the new value if the key is already in the symbol table.
         * If the value is {@code null}, this effectively deletes the key from the symbol table.
         * @param key the key
         * @param val the value
         * @throws IllegalArgumentException if {@code key} is {@code null}
         */
        public void put(String key, Value val) {
            if (key == null) {
                throw new IllegalArgumentException("calls put() with null key");
            }
            if (!contains(key)) n++;
            else if(val == null) n--;       // delete existing key
            root = put(root, key, val, 0);
        }

        private Node<Value> put(Node<Value> x, String key, Value val, int d) {
            char c = key.charAt(d);
            if (x == null) {
                x = new Node<>();
                x.c = c;
            }
            if      (c < x.c)               x.left  = put(x.left,  key, val, d);
            else if (c > x.c)               x.right = put(x.right, key, val, d);
            else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
            else                            x.val   = val;
            return x;
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
            if (query == null) {
                throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
            }
            if (query.length() == 0) return null;
            int length = 0;
            Node<Value> x = root;
            int i = 0;
            while (x != null && i < query.length()) {
                char c = query.charAt(i);
                if      (c < x.c) x = x.left;
                else if (c > x.c) x = x.right;
                else {
                    i++;
                    if (x.val != null) length = i;
                    x = x.mid;
                }
            }
            return query.substring(0, length);
        }

        /**
         * Returns all keys in the symbol table as an {@code Iterable}.
         * To iterate over all of the keys in the symbol table named {@code st},
         * use the foreach notation: {@code for (Key key : st.keys())}.
         * @return all keys in the symbol table as an {@code Iterable}
         */
        public Iterable<String> keys() {
            Queue<String> queue = new Queue<>();
            collect(root, new StringBuilder(), queue);
            return queue;
        }


        public boolean containWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
            }
            Node<Value> x = get(root, prefix, 0);
            return x != null;
        }

        /**
         * Returns all of the keys in the set that start with {@code prefix}.
         * @param prefix the prefix
         * @return all of the keys in the set that start with {@code prefix},
         *     as an iterable
         * @throws IllegalArgumentException if {@code prefix} is {@code null}
         */
        public Queue<String> keysWithPrefix(String prefix) {
            if (prefix == null) {
                throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
            }
            Queue<String> queue = new Queue<>();
            Node<Value> x = get(root, prefix, 0);
            if (x == null) return queue;
            if (x.val != null) queue.enqueue(prefix);
            collect(x.mid, new StringBuilder(prefix), queue);
            return queue;
        }

        // all keys in subtrie rooted at x with given prefix
        private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
            if (x == null) return;
            collect(x.left,  prefix, queue);
            if (x.val != null) queue.enqueue(prefix.toString() + x.c);
            collect(x.mid,   prefix.append(x.c), queue);
            prefix.deleteCharAt(prefix.length() - 1);
            collect(x.right, prefix, queue);
        }


        /**
         * Returns all of the keys in the symbol table that match {@code pattern},
         * where . symbol is treated as a wildcard character.
         * @param pattern the pattern
         * @return all of the keys in the symbol table that match {@code pattern},
         *     as an iterable, where . is treated as a wildcard character.
         */
        public Iterable<String> keysThatMatch(String pattern) {
            Queue<String> queue = new Queue<>();
            collect(root, new StringBuilder(), 0, pattern, queue);
            return queue;
        }

        private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
            if (x == null) return;
            char c = pattern.charAt(i);
            if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue);
            if (c == '.' || c == x.c) {
                if (i == pattern.length() - 1 && x.val != null) queue.enqueue(prefix.toString() + x.c);
                if (i < pattern.length() - 1) {
                    collect(x.mid, prefix.append(x.c), i+1, pattern, queue);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }
            if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue);
        }


    }


    private final TST<Integer> dictionary;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        this.dictionary = new TST<>();
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