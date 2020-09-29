package com.example.search_algorithms;

public class PriorityQueue {
    PriorityQueueNode head;
    int size;

    PriorityQueue(){
        size = 0;
        head = null;
    }

    public void add(int cy, int cx, int cPriority, IconNodeQueue cPrevPath){
        size++;
        if(head != null){
            head.add(cy,cx,cPriority,cPrevPath);
        }
        else{
            head = new PriorityQueueNode(cy,cx,cPriority,cPrevPath);
        }
    }

    public void pop(){
        if(head != null){
            head = head.next;
            size--;
        }
    }

    public int getHX(){
        if(head != null){
            return head.x;
        }
        return -1;
    }

    public int getHY(){
        if(head != null){
            return head.y;
        }
        return -1;
    }

    public IconNodeQueue getPrevQueue(){
        if(head != null){
            return head.prevPath;
        }
        return null;
    }

    public int getSize(){
        return size;
    }

    private class PriorityQueueNode{
        PriorityQueueNode next;
        IconNodeQueue prevPath;
        int priority;
        int y;
        int x;

        PriorityQueueNode(){
            y = -1;
            x = -1;
            priority = -1;
            prevPath = new IconNodeQueue();
        }

        PriorityQueueNode(int cy, int cx, int cPriority, IconNodeQueue previousPath){
            y = cy;
            x = cx;
            priority = cPriority;
            next = null;
            prevPath = previousPath;
        }

        PriorityQueueNode(int cy, int cx, int cPriority, IconNodeQueue previousPath,
                          PriorityQueueNode cnext){
            y = cy;
            x = cx;
            priority = cPriority;
            next = cnext;
            prevPath = previousPath;
        }

        void add(int cy, int cx, int cPriority, IconNodeQueue cPrevPath){
            if (cPriority > priority){
                if(next != null){
                    next.add(cy,cx,cPriority, cPrevPath);
                    return;
                }
                next = new PriorityQueueNode(cy,cx,cPriority,cPrevPath);
                return;
            }
            PriorityQueueNode temp = new PriorityQueueNode(y,x,priority,prevPath,next);
            y = cy;
            x = cx;
            priority = cPriority;
            prevPath = cPrevPath;
            next = temp;
            return;
        }
    }
}
