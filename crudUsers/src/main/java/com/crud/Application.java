package com.crud;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
        System.err.println("*** CRUD Users Service is running ***");
        
    }
}
