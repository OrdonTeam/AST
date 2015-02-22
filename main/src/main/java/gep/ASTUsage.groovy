package gep

import ast.WithLogging
import groovy.transform.Canonical;

@Canonical
class ASTUsage{

    public static void main(String[] args) {
        new ASTUsage().greet()
    }

    @WithLogging
    def greet() {
        println "Hello World"
    }
}