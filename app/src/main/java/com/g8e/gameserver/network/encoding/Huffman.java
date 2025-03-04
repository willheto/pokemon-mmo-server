package com.g8e.gameserver.network.encoding;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

    static class Node implements Comparable<Node> {
        char character;
        int frequency;
        Node left, right;

        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        Node(int frequency, Node left, Node right) {
            this.character = '\0';
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node o) {
            return this.frequency - o.frequency;
        }

        boolean isLeaf() {
            return this.left == null && this.right == null;
        }
    }

    public static Map<Character, Integer> buildFrequencyTable(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    public static Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            Node merged = new Node(left.frequency + right.frequency, left, right);
            priorityQueue.add(merged);
        }

        return priorityQueue.poll();
    }

    public static void buildEncodingMap(Node node, String code, Map<Character, String> encodingMap) {
        if (node.isLeaf()) {
            encodingMap.put(node.character, code);
        } else {
            buildEncodingMap(node.left, code + "0", encodingMap);
            buildEncodingMap(node.right, code + "1", encodingMap);
        }
    }

    // Encode text using the encoding map
    public static String encode(String text, Map<Character, String> encodingMap) {
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(encodingMap.get(c));
        }
        return encoded.toString();
    }

    public static String decode(String encodedText, Node root) {
        StringBuilder decoded = new StringBuilder();
        Node current = root;
        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.isLeaf()) {
                decoded.append(current.character);
                current = root;
            }
        }
        return decoded.toString();
    }

}
