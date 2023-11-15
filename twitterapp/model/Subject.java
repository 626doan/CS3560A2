package twitterapp.model;

import javax.swing.tree.DefaultMutableTreeNode;

import java.util.ArrayList;

public abstract class Subject extends DefaultMutableTreeNode {
    private ArrayList<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }
}
