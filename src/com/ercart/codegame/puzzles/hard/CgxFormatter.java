package com.ercart.codegame.puzzles.hard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * @author dkyryk
 */
public class CgxFormatter {

    private static final String PREFIX = "    ";
    private static final  Map<String, String> LITERALS = new HashMap<>();
    private static final int LITERAL_KEY_LENGTH = 6;

    private static final Pattern STRING_LITERAL = Pattern.compile("'(.)*?'");
    private static final String CR = "\n";

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int linesCount = in.nextInt();
        if (in.hasNextLine()) {
            in.nextLine();
        }
        StringBuilder source = new StringBuilder();
        for (int i = 0; i < linesCount; i++) {
            source.append(in.nextLine());
        }

        System.out.println(beatifyContent(source.toString()));
    }

    private static String beatifyContent(String source) {
        String withoutLiterals = replaceLiterals(source);
        String minimized = withoutLiterals.replaceAll("\\s", "");
        String structure = buildStructure(minimized);

        return setLiterals(structure);
    }

    private static String buildStructure(String minimized) {
        Node root = Node.createRoot();
        root.prefix = "";
        parseContent(root, minimized, 0);
        return root.print();
    }

    private static String setLiterals(String structure) {
        String result = structure;
        for (Map.Entry<String, String> entry : LITERALS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static String replaceLiterals(String source) {
        Matcher matcher = STRING_LITERAL.matcher(source);
        int index = 0;
        String d = source;
        while (matcher.find()) {
            String group = matcher.group(0);
            String key = String.format("#%05d", index);
            d = d.replace(group, key);
            LITERALS.put(key, group);
            index++;
        }
        return d;
    }

    private static void parseContent(Node node, String source, int startIndex) {
        node.startIndex = startIndex;
        int i = startIndex;
        int unprocessedFieldsStartIndex = startIndex;
        while (i < source.length()) {

            if (source.charAt(i) == '(') {
                Node child = new Node();
                child.setPrefix(node);
                parseContent(child, source, i + 1);
                if (i != 0 && source.charAt(i - 1) == '=') {
                    child.name = source.substring(i - 1 - LITERAL_KEY_LENGTH, i - 1);
                    child.startIndex = i - 1 - LITERAL_KEY_LENGTH;
                }
                if (unprocessedFieldsStartIndex < child.startIndex - 1) {
                    String fields = source.substring(unprocessedFieldsStartIndex, child.startIndex - 1);
                    addSimpleFields(node, fields);
                }
                unprocessedFieldsStartIndex = child.endIndex + 1;
                node.children.add(child);
                i = child.endIndex + 1;
            } else if (source.charAt(i) == ')') {
                node.endIndex = i;
                if (unprocessedFieldsStartIndex < i) {
                    String fields = source.substring(unprocessedFieldsStartIndex, i);
                    addSimpleFields(node, fields);
                }
                return;
            } else {
                i++;
            }
        }
        if (unprocessedFieldsStartIndex < i) {
            String fields = source.substring(unprocessedFieldsStartIndex, i);
            addSimpleFields(node, fields);
        }
    }

    private static void addSimpleFields(Node node, String fields) {
        Stream.of(fields.split(";"))
            .filter(field -> field.length() > 0)
            .map(field -> {
                Node fieldNode = new Node();
                fieldNode.setPrefix(node);
                fieldNode.value = field;
                return fieldNode;
            }).forEach(fieldNode -> node.children.add(fieldNode));
    }


    private static class Node {
        String prefix;
        List<Node> children = new ArrayList<>();
        String value;
        String name;
        int startIndex;
        int endIndex;

        boolean isRoot = false;

        private static Node createRoot() {
            Node node = new Node();
            node.isRoot = true;
            return node;
        }

        private String print() {
            StringBuilder builder = new StringBuilder();
            if (value != null) {
                builder.append(prefix).append(value);
                return builder.toString();
            }
            if (name != null) {
                builder.append(prefix).append(name).append("=").append(CR);
            }
            if (!isRoot) {
                builder.append(prefix).append("(").append(CR);
            }
            if (!children.isEmpty()) {
                String childrenPrint = children.stream()
                        .map(Node::print)
                        .collect(joining(";" + CR));
                builder.append(childrenPrint).append(CR);
            }
            if (!isRoot) {
                builder.append(prefix).append(")");
            }
            return builder.toString();
        }

        private void setPrefix(Node parent) {
            this.prefix = parent.isRoot ? "" : parent.prefix + PREFIX;
        }
    }

}
