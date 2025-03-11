package org.example.social_network.utils.observer;


import org.example.social_network.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}