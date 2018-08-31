package pl.edu.pwr.wordnetloom.common.model;

import java.io.Serializable;
import java.util.*;

public class Graph<T extends GenericEntity & Serializable> implements Serializable, Cloneable {

    private Map<Long, Integer> nodesIds;
    private List<Node> nodes;
    private T root;

    public Graph(T root){
        nodesIds = new HashMap<>();
        nodes = new ArrayList<>();
        this.root = root;
        Node nodeRoot = new Node(root);
        putNode(nodeRoot);
    }

    public T getRoot(){
        return root;
    }

    private void putNode(T object){
        Node node = new Node(object);
        nodes.add(node);
        int newNodeIndex = nodes.size() -1;
        nodesIds.put(object.getId(), newNodeIndex);
    }

    private void putNode(Node node){
        nodes.add(node);
        int newNodeIndex = nodes.size() - 1;
        nodesIds.put(node.getObject().getId(), newNodeIndex);
    }

    private Node getNode(T object){
        if(!containsNode(object)){
            return null;
        }
        return nodes.get(nodesIds.get(object.getId()));
    }

    private boolean containsNode(T object){
        return nodesIds.containsKey(object.getId());
    }

    public void add(T firstNode, T secondNode){
//        Node node;
//        if(nodes.containsKey(secondNode)){
//            node = nodes.get(secondNode);
//        } else {
//            node = new Node(secondNode);
//        }
//        nodes.get(firstNode.getId()).addChild(node);
//        nodes.put(secondNode.getId(), node);
        Node node = getNode(secondNode);
        if(node == null){
            node = new Node(secondNode);
        }
        Node parentNode = getNode(firstNode);
        parentNode.addChild(node);
        putNode(node);
    }

    public List<T> getChildren(T object) {
//        if(!nodes.containsKey(object)){
//            return null;
//        }
//        Node node = nodes.get(object);
//        List<T> list = new ArrayList<>();
//        for(Node n : node.getChildren()){
//            list.add(n.getObject());
//        }
//
//        return list;
        Node node = getNode(object);
        if(node == null){
            return null;
        }
        List<T> list = new ArrayList<>();
        for(Node n : node.getChildren()){
            list.add(n.getObject());
        }
        return list;
    }

    private class Node{
        private T object;
        private List<Node> children;

        Node(T object){
            this.object = object;
            children = new ArrayList<>();
        }

        public void addChild(Node node){
            children.add(node);
        }

        public void addChiled(T object){
            Node node = new Node(object);
            addChild(node);
        }

        public List<Node> getChildren(){
            return children;
        }

        public T getObject(){
            return object;
        }
    }
}
