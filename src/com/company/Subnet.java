/**
 * @author Kevin Zheng 2BI
 */

package com.company;

import java.util.*;

public class Subnet {

    /**
     * network address
     */
    private IPAddress net;
    /**
     * network mask
     */
    private IPAddress mask;

    /**
     * create netmask from network ip and number of bits
     *
     * @param net  network address
     * @param cidr number of bits
     */
    public Subnet(IPAddress net, int cidr) {
        createMask(net, cidr);
    }

    private void createMask(IPAddress net, int cidr) {
        this.net=net;
        this.mask=IPAddress.createNetmask(cidr);
        // System.out.format("n %08x\n", this.net.getIP() & 0xffffffffl);
        // System.out.format("m %08x\n", this.mask.getIP());
        // System.out.format(" %08x\n", (this.net.getIP() & mask.getIP()));

        if ((this.net.getIP() & mask.getIP()) != this.net.getIP()) {
            throw new IllegalArgumentException("bad network");
        }
    }

    /**
     * create netmask from ip (four number) and number of bits
     *
     * @param a3
     * @param a2
     * @param a1
     * @param a0
     * @param cidr
     */
    public Subnet(int a3, int a2, int a1, int a0, int cidr) {
        this(new IPAddress(a3, a2, a1, a0), cidr);
    }

    public Subnet(String mask) {
        String[] parts=mask.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("ill formed subnet");
        }
        IPAddress ip=new IPAddress(parts[0]);
        int cidr=Integer.parseInt(parts[1]);
        createMask(ip, cidr);
    }

    public IPAddress getNet() {
        return net;
    }

    public IPAddress getMask() {
        return mask;
    }

    public IPAddress getBroadcast() {
        return new IPAddress(net.getIP()+~mask.getIP());
    }

    /**
     * is IP in this network
     *
     * @param ip
     * @return
     */
    public boolean contains(IPAddress ip) {
        // System.out.format("i %08x\n", ip.getIP());
        // System.out.format("m %08x\n", mask.getIP());
        // System.out.format("i&m %08x\n", ip.getIP() & mask.getIP());
        // System.out.format("n %08x\n", net.getIP());

        return (ip.getIP() & mask.getIP()) == net.getIP();
    }

    @Override
    public String toString() {
        return "Subnet [net="+net+", mask="+mask+"]";
    }

    public static void testFullSubnet(Subnet subnet, Set<IPAddress> set) {
        IPAddress net = subnet.getNet();
        IPAddress mask = subnet.getMask();
        IPAddress broadcast = new IPAddress( net.getIP() + ~mask.getIP() );

        long startTime = System.nanoTime();
        for (int i = net.getIP(); i <= broadcast.getIP(); i++) {
            set.add(new IPAddress(i));
        }
        long endTime = System.nanoTime();
        System.out.println("Zeit für das Eintragen: " + (endTime - startTime) + " ns");

        startTime = System.nanoTime();
        for (int i = net.getIP(); i <= broadcast.getIP(); i++) {
            IPAddress ip = new IPAddress(i);
            if (!subnet.contains(ip) || !set.contains(ip)) {
                System.out.println("Error: IPAddresse nicht im Set: " + ip);
            }
        }
        endTime = System.nanoTime();
        System.out.println("Zeit für das Testen für alle IPAdressen im Subnet: " + (endTime - startTime) + " ns");
    }

    public static void testNextHop(Map<Subnet, IPAddress> nextHop) {
        nextHop.put(new Subnet(new IPAddress("10.0.0.0"), 8), new IPAddress("10.0.0.1"));
        nextHop.put(new Subnet(new IPAddress("172.16.0.0"), 16), new IPAddress("172.16.0.1"));
        nextHop.put(new Subnet(new IPAddress("192.168.2.0"), 24), new IPAddress("192.168.2.1"));

        for (Map.Entry<Subnet, IPAddress> entry : nextHop.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

        Subnet knownNetwork = new Subnet(new IPAddress("10.0.0.0"), 8);
        Subnet unknownNetwork = new Subnet(new IPAddress("100.0.0.0"), 8);
        System.out.println("Next hop for " + knownNetwork + ": " + nextHop.get(knownNetwork));
        System.out.println("Next hop for " + unknownNetwork + ": " + nextHop.get(unknownNetwork));
    }

    public IPAddress getNetworkAddress() {
        return new IPAddress(net.getIP() & mask.getIP());
    }

    /**
     * Main TreeMap
     * @param args
     */
    public static void main(String[] args) {
        TreeMap<Subnet, IPAddress> nextHopT = new TreeMap<>(new MySubnetComparator());
        testNextHop(nextHopT);

        TreeMap<Subnet, IPAddress> nextHopT2 = new TreeMap<>(new Comparator<Subnet>() {
            @Override
            public int compare(Subnet s1, Subnet s2) {
                return s1.getNetworkAddress().compareTo(s2.getNetworkAddress());
            }
        });
        testNextHop(nextHopT2);

        HashMap<Subnet, IPAddress> nextHopH = new HashMap<>();
        testNextHop(nextHopH);
    }

    static class MySubnetComparator implements Comparator<Subnet> {
        @Override
        public int compare(Subnet s1, Subnet s2) {
            return s1.getNetworkAddress().compareTo(s2.getNetworkAddress());
        }
    }
}
