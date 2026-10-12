package edu.hitsz.observer;

import java.util.LinkedList;
import java.util.List;

// 炸弹道具发布者
public class BombPublisher {

    private List<Observer> observers = new LinkedList<>();

    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    public int notifyObservers() {
        int totalScore = 0;
        // 遍历列表副本以避免ConcurrentModificationException
        List<Observer> observersToNotify = new LinkedList<>(observers);
        for (Observer observer : observersToNotify) {
            totalScore += observer.update();
        }
        return totalScore;
    }
}