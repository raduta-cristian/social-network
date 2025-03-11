package org.example.social_network.domain;

import java.util.Objects;

public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    public Tuple(E1 e1, E2 e2){
        this.e1 = e1;
        this.e2 = e2;
    }

    public E1 getLeft() {
        return e1;
    }

    public E2 getRight() {
        return e2;
    }

    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return e1 + "," + e2;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj.getClass() != this.getClass())
            return false;
        if(obj == this)
            return true;
        Tuple<E1, E2> new_obj = (Tuple) obj;
        return (e1.equals(new_obj.getLeft()) && e2.equals(new_obj.getRight())) ||
                (e2.equals(new_obj.getLeft()) && e1.equals(new_obj.getRight()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(e1.hashCode() + e2.hashCode());
    }
}
