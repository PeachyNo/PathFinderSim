package com.example.search_algorithms;

public class SearchQueue {
    private SearchQueueNode head;
    private int size;

    SearchQueue(){
        head = new SearchQueueNode();
        size = 0;
    }

    public void add(int y, int x, IconNodeQueue q){
        head.add(y,x,q);
        size++;
    }

    public int getX(){
        return head.nodeX;
    }

    public int getY(){
        return head.nodeY;
    }

    public int getSize(){
        return size;
    }

    public void pop(){
        if(size != 0 && head != null){
            size--;
            head = head.next;
        }
    }

    public void clear(){
        head = new SearchQueueNode();
        size = 0;
    }

    public IconNodeQueue getPathCopy(){
        return head.prevPath.copy();
    }


    private class SearchQueueNode{
        int nodeX;
        int nodeY;
        SearchQueueNode next;
        IconNodeQueue prevPath;



        SearchQueueNode(){
            nodeX = -1;
            nodeY = -1;
            next = null;
            prevPath = new IconNodeQueue();
        }

        SearchQueueNode(int y,int x, IconNode n){
            nodeX = x;
            nodeY = y;
            next = new SearchQueueNode();
        }

        public void add(int y, int x, IconNodeQueue q){
            if(next != null){
                next.add(y,x,q);
                return;
            }
            nodeX = x;
            nodeY = y;
            next = new SearchQueueNode();
            prevPath = q;
        }

        //public void addQueue()
    }
}
