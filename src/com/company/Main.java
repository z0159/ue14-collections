package com.company;

import java.util.HashSet;
import java.util.TreeSet;

import static com.company.IPAddress.testSet;

public class Main {

    public static void main(String[] args) {
        testSet(new HashSet<IPAddress>());
        testSet(new TreeSet<IPAddress>());
    }
}
