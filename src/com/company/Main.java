package com.company;

import java.util.HashSet;
import java.util.TreeSet;

import static com.company.IPAddress.testSet;
import static com.company.Subnet.testFullSubnet;

public class Main {

    public static void main(String[] args) {
        testSet(new HashSet<IPAddress>());
        testSet(new TreeSet<IPAddress>());
        testFullSubnet(new Subnet("192.168.1.0/24"), new HashSet<IPAddress>());
    }
}
