package com.laan.jposbasicapp;

import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static byte[] readData(InputStream din) throws IOException {
        byte[] real = null;
        byte[] data = new byte[257];
        int count = din.read(data);
        real = new byte[count + 1];
        for (int i = 0; i <= count; i++)
            real[i] = data[i];
        return real;
    }

    public static void main(String [] args){
        try (
                ServerSocket serverSocket = new ServerSocket(4444);
                Socket clientSocket = serverSocket.accept();

                InputStream inputStream = clientSocket.getInputStream();
        ) {
            String inputLine, outputLine;
            byte[] decoded = null;

            while (true) {
                decoded = readData(inputStream);
                if (decoded.length>0)
                    System.out.println(Hex.encodeHexString(decoded));
            }
    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
