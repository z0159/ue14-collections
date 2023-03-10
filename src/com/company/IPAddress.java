/**
 * @author Kevin Zheng 2BI
 */

package com.company;

import java.util.Objects;
import java.util.Set;

public class IPAddress implements Comparable<IPAddress> {

    /**
     * ip as integer
     */
    private int ip;

    /**
     * create IP from 4 Numbers
     *
     * @param a3
     * @param a2
     * @param a1
     * @param a0
     */
    public IPAddress(int a3, int a2, int a1, int a0) {
        createIP(a3, a2, a1, a0);
    }

    private void createIP(int a3, int a2, int a1, int a0) {
        this.ip=(a3 << 24)+(a2 << 16)+(a1 << 8)+a0;
        // System.out.format("%d.%d.%d.%d: %08x\n", a3,a2,a1,a0,ip);
    }

    /**
     * create IP from given integer (internal use)
     *
     * @param ip
     */
    IPAddress(int ip) {
        this.ip=ip;
    }

    public IPAddress(String ip) {
        String[] nums=ip.split("\\.");
        if (nums.length != 4) {
            throw new IllegalArgumentException("ill formed ip");
        }
        createIP(Integer.parseInt(nums[0]),
                Integer.parseInt(nums[1]),
                Integer.parseInt(nums[2]),
                Integer.parseInt(nums[3]));
    }

    /**
     * create IP/Netmask with given number of bits
     *
     * @param cidr number of bits
     * @return
     */
    public static IPAddress createNetmask(int cidr) {
        int mask=(int) (0xffffffff00000000l >> cidr);
        // System.out.format("cm %2d: %08x\n", cidr, mask);
        return new IPAddress(mask);
    }

    /**
     * @return ip address as integer
     */
    public int getIP() {
        return ip;
    }

    @Override
    public String toString() {
        int a0=(ip) & 0xff;
        int a1=(ip >>> 8) & 0xff;
        int a2=(ip >>> 16) & 0xff;
        int a3=(ip >>> 24) & 0xff;

        return "IPAddress ["+a3+"."+a2+"."+a1+"."+a0+"]";
    }

    /**
     * zum Testen
     * @param sIP
     */
    static void testSet(Set<IPAddress> sIP) {
        sIP.add(new IPAddress("10.0.0.1"));
        sIP.add(new IPAddress("10.0.0.2"));
        sIP.add(new IPAddress("10.0.0.1"));
        System.out.println("sIP.size() = "+sIP.size()); // IntelliJ: soutv
        for (IPAddress ip : sIP) {
            System.out.println("ip = "+ip);
        }
    }

    /**
     * schaut nach ob die IPs gleich sind
     * @param o
     * @return ip
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPAddress ipAddress=(IPAddress) o;
        return ip == ipAddress.ip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public int compareTo(IPAddress other) {
        return Integer.compare(this.ip, other.ip);
    }


}