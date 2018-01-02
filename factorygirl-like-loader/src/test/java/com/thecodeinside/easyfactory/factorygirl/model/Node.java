package com.thecodeinside.easyfactory.factorygirl.model;

public class Node {
    private String name;
    private Node nextNode;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setNextNode(Node next) {
        this.nextNode = next;
    }

    public Node getNextNode() {
        return this.nextNode;
    }

    @Override
    public String toString() {
        return "Node [name=" + name + ", nextNode=" + nextNode.getName() + "]";
    }
}
