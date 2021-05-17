import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';    

    private class TrieNode {
        // TODO: Create your TrieNode class here.
	    int[] present_chars = new int[62];
        int key;
        boolean end;
        TrieNode[] children;

        TrieNode(int k) {
            key = k;
            end = false;
            children = new TrieNode[62];
        }

    }

    public TrieNode root;
    
    public Trie() {
        // TODO: Initialise a trie class here.
	   root = new TrieNode(0);
    }

    // inserts string s into the Trie
    void insert(String s) {
        // start from root
        TrieNode node = root;
        // if empty string root.end = true
        if (s.equals("")) { root.end = true; }
        for (int i = 0; i < s.length(); i++) {
            // if char not in children, add as children
            // else set node pointer to the TrieNode
            if (node.children[asciiToIndex((int) s.charAt(i))] == null) {
                node.children[asciiToIndex((int) s.charAt(i))] = new TrieNode((int) s.charAt(i));
                node = node.children[asciiToIndex((int) s.charAt(i))];
            } else {
                node = node.children[asciiToIndex((int) s.charAt(i))];
            }
            // if it is the last letter, set end to true
            if (i == s.length() - 1) { node.end = true; }
        }
    }

    // convert an ascii value to an index value between 0 - 61
    int asciiToIndex(int ascii) {
        // char: 0-9
        if (ascii < 58) {
            return ascii - 48; // index 0-9
        } else if (ascii > 64 && ascii < 91) { // char: A-Z
            return ascii - 55; // index 10-35
        } else { // char: a-z
            return ascii - 61; // index 36-61
        }
    }

    // checks whether string s exists inside the Trie or not
    boolean contains(String s) {
        // TODO
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            // if char not in children return false
            if (node.children[asciiToIndex((int) s.charAt(i))] == null) {
                return false;
            } else {
                node = node.children[asciiToIndex((int) s.charAt(i))];
            }
        }
        return node.end;
    }

    // Search for string with prefix matching the specified pattern sorted by lexicographical order.
    // Return results in the specified ArrayList.
    // Only return at most the first limit results.
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        String prevWord = "";
        String currWord;
        // check if trie is empty
        if (findNext(root.children, -1) != null) {
            while (results.size() < limit) {
                // if no more words break
                if (prevWord.equals(lastWord())) {
                    break;
                } else {
                    currWord = findNextWord(prevWord);
                    if (fitsPrefix(s, currWord)) {
                        results.add(currWord);
                    }
                    prevWord = currWord;
                }
            }
        }
    }

    // find next non null child
    TrieNode findNext(TrieNode[] children, int prevIndex) {
        for (int i = prevIndex + 1; i < children.length; i++) {
            if (children[i] != null) {
                return children[i];
            }
        }
        return null;
    }

    int getIndex(String str, int letter) {
        if (str.equals("") || letter >= str.length()) {
            return -1;
        } else {
            return asciiToIndex(str.charAt(letter));
        }
    }

    // check if word fits the prefix
    boolean fitsPrefix(String prefix, String str) {
        if (prefix.length() > str.length()) {
            return false;
        } else {
            for (int i = 0; i < prefix.length(); i++) {
                if (prefix.charAt(i) != WILDCARD) {
                    if (str.charAt(i) != prefix.charAt(i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // return the last word in the Trie
    String lastWord() {
        StringBuilder last = new StringBuilder();
        TrieNode node = root;
        if (findNext(root.children, -1) != null) {
            while (!node.end) {
                for (int i = node.children.length - 1; i > 0; i--) {
                    if (node.children[i] != null) {
                        last.append((char) node.children[i].key);
                        node = node.children[i];
                        break;
                    }
                }
            }
        }
        return last.toString();
    }

    // check if string has child below
    // if theres child return string of child
    String hasChildren(String str) {
        TrieNode node = root;
        StringBuilder result = new StringBuilder(str);
        for (int i = 0; i < str.length(); i++) {
            node = node.children[getIndex(str, i)];
        }
        if (findNext(node.children, -1) != null) {
            node = findNext(node.children, -1);
            result.append((char) node.key);
            while (! node.end) {
                node = findNext(node.children, -1);
                result.append((char) node.key);
            }
        }
        return result.toString();
    }

    // find next word of trie in order
    String findNextWord(String prevWord) {
        StringBuilder currWord = new StringBuilder();
        TrieNode node = root;
        int currLetter = 0;
        int lastLetter = prevWord.length() - 1;
        if (!hasChildren(prevWord).equals(prevWord)) {
            currWord = new StringBuilder(hasChildren(prevWord));
        } else {
            while (!node.end || !hasChildren(currWord.toString()).equals(currWord.toString())) {
                if (node.end && hasChildren(currWord.toString()).compareTo(prevWord) > 0) {
                    break;
                }
                if (currLetter < lastLetter) { // find same child
                    node = node.children[asciiToIndex((int) prevWord.charAt(currLetter))];
                    currWord.append((char) node.key);
                    currLetter++;
                } else if (currLetter == lastLetter) {
                    if (findNext(node.children, getIndex(prevWord, currLetter)) != null) {
                        node = findNext(node.children, getIndex(prevWord, currLetter));
                        currWord.append((char) node.key);
                        currLetter++;
                    } else {
                        node = root;
                        currWord = new StringBuilder();
                        currLetter = 0;
                        lastLetter--;
                    }
                } else {
                    node = findNext(node.children, -1);
                    currWord.append((char) node.key);
                    currLetter++;
                }
            }
        }
        return currWord.toString();
    }

    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");
        t.insert("abba");
        t.insert("abbde");
        t.insert("abcd");
        t.insert("abcdef");
        t.insert("abd");
        t.insert("abed");
        t.insert("dbec");

        String[] result1 = t.prefixSearch("", 10);
        //String[] result2 = t.prefixSearch("pe.*", 10);
        String[] result3 = t.prefixSearch("a.c", 10);

        for (String s : result3) {
            System.out.println(s);
        }

        //System.out.println(t.findNextWord("abbde"));

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
