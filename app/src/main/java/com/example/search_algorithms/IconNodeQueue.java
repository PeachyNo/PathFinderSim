package com.example.search_algorithms;

public class IconNodeQueue {
    private int size;
    private QueueNode head;

    IconNodeQueue() {
        size = 0;
    }

    public IconNode pop(){
        if(head != null && size > 0) {
            IconNode ret = head.current;
            head = head.next;
            size--;
            return ret;
        }
        return null;
    }

    public void add(IconNode c){
        if (head != null){
            head.add(c);
            size++;
        }
        else {
            head = new QueueNode(c);
            size++;
        }
    }

    public int getSize(){
        return size;
    }

    public IconNodeQueue copy(){
        QueueNode c = head;
        IconNodeQueue ret = new IconNodeQueue();

        while(c != null && c.next != null){
            ret.add(c.current);
            c = c.next;
        }
        return ret;
    }




    private class QueueNode {
        private IconNode current;
        private QueueNode next;

        QueueNode (IconNode c) {
            current = c;
            next = new QueueNode();
        }
        QueueNode () {
            current = null;
            next = null;
        }

        private void add(IconNode c){
            if(next != null){
                next.add(c);

            }
            else {
                current = c;
                next = new QueueNode();
            }
        }
        private QueueNode getNext(){
            return next;
        }
    }
}
