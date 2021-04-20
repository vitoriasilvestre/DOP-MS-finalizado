/*******************************************************************************
 * Copyright (c) 2015 LG Electronics. All Rights Reserved. This software is the
 * confidential and proprietary information of LG Electronics. You shall not
 * disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with LG Electronics.
 *******************************************************************************/
package br.ufc.great.caos.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Performs discovery of cloudlet to device
 */
public class DiscoveryThread implements Runnable {
    DatagramSocket mSocket;
    
    public void run() {
        try {
            // Keep a socket open to listen to all the UDP trafic that is
            // destined for this port
            mSocket = new DatagramSocket(31020, InetAddress.getByName("0.0.0.0"));
            mSocket.setBroadcast(true);

            while (true) {
                // Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                mSocket.receive(packet);

                // See if the packet holds the right command (message)
                String message = new String(packet.getData()).trim();
                if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
                    byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();

                    // Send a response
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, packet.getAddress(),
                                    packet.getPort());
                    mSocket.send(sendPacket);
                }
            }
        } catch (IOException ex) {
            
        }
    }

    /**
     * Get instance DiscoveryThreadHolder
     * @return Instance DiscoveryThreadHolder
     */
    public static DiscoveryThread getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    /**
     * Performs instantiation of DiscoveryThread 
     */
    private static class DiscoveryThreadHolder {

        private static final DiscoveryThread INSTANCE = new DiscoveryThread();
    }
}