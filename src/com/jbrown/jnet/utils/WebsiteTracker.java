package com.jbrown.jnet.utils;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.*;

public class WebsiteTracker {

    public static void main(String[] args) throws Exception {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();

        if (devices.length == 0) {
            System.out.println("No network interface found");
            return;
        }

        // Use the first network interface
        NetworkInterface device = devices[0];

        // Open a capture on the network interface with a snapshot length of 65536 bytes
        JpcapCaptor captor = JpcapCaptor.openDevice(device, 65536, false, 20);

        while (true) {
            // Capture a packet
            Packet packet = captor.getPacket();

            if (packet instanceof IPPacket) {
                IPPacket ipPacket = (IPPacket) packet;

                // Check for TCP packets (web traffic typically uses TCP)
                if (ipPacket instanceof TCPPacket) {
                    TCPPacket tcpPacket = (TCPPacket) ipPacket;

                    // Check for HTTP traffic (port 80)
                    if (tcpPacket.dst_port == 80 || tcpPacket.src_port == 80) {
                        System.out.println("HTTP traffic detected");
                        System.out.println("Source IP: " + ipPacket.src_ip);
                        System.out.println("Destination IP: " + ipPacket.dst_ip);
                        System.out.println("URL: " + extractUrl(tcpPacket));
                    }
                }
            }
        }
    }

    // Extract URL from HTTP GET request
    private static String extractUrl(TCPPacket tcpPacket) {
        byte[] data = tcpPacket.data;
        String packetData = new String(data);

        if (packetData.contains("GET") && packetData.contains("HTTP/1.")) {
            int start = packetData.indexOf("GET") + 4;
            int end = packetData.indexOf("HTTP/1.");

            return packetData.substring(start, end).trim();
        }

        return "Unknown URL";
    }
}
