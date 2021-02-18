package com.epam.esm.util.builder;

public class BuilderContainer<T> {

    protected T hypermedia;

    protected BuilderContainer(T hypermedia) {
        this.hypermedia = hypermedia;
    }

    public T getHypermedia() {
        return hypermedia;
    }


}
