package com.laan.jposbasicapp;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.jpos.iso.*;
import org.jpos.iso.packager.GenericPackager;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class App {

    public static void main(String[] args) throws ISOException, DecoderException {


        byte[] decoded = Hex.decodeHex("00FE00000000000200703800000080008016510001300009319800005000000000500024818712453909303330303837303030020349303030303234383138373030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030305041474449524543544F303030303030303030303030303530303030303030303030303030303030303030343335323233343041484F4250443030303032343831383730303030303030303030323032322F30392F3330");
        System.out.println(" ISO8583 message:\n" + Hex.encodeHexString(decoded));
        //TODO:Platform charset ?

        // create generic packager
        InputStream inputStream = App.class.getResourceAsStream("/fields.xml");
        GenericPackager packager = new GenericPackager(inputStream);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(packager);

        // set header isoMsg.setHeader("ISO1987".getBytes());
        isoMsg.setHeader("0000000000".getBytes());


        // set MTI as financial          isoMsg.setMTI("0200");


        // set data fields
        isoMsg.set(0, "0200");

        isoMsg.set(2, "56xx5704xx7829xx");
        isoMsg.set(3, "011000");
        isoMsg.set(4, "78000");
        isoMsg.set(7, "1220145711");
        isoMsg.set(11, "101183");
        isoMsg.set(12, "145711");
        isoMsg.set(13, "1220");
        isoMsg.set(14, "2408");
        isoMsg.set(15, "1220");
        isoMsg.set(18, "6011");
        isoMsg.set(22, "051");
        isoMsg.set(25, "00");
        isoMsg.set(26, "04");
        isoMsg.set(28, "C00000000");
        isoMsg.set(30, "C00000000");
        isoMsg.set(32, "56445700");
        isoMsg.set(37, "567134101183");
        isoMsg.set(41, "N1742");
        isoMsg.set(42, "ATM004");
        isoMsg.set(43, "45 SR LEDERSHIP DUABANAT NUEVA ECIJAQ PH");
        isoMsg.set(49, "608");
        //header + ISO fields
        byte[] msg = ArrayUtils.addAll( Hex.decodeHex("00FE0000000000"),isoMsg.pack());
        System.out.println("New ISO8583 message:\n" + Hex.encodeHexString(msg));
        System.out.println("ISO 8583 message in XML format: ");
        isoMsg.dump(System.out, "");



        //when received 0 - 6 is heder, the rest are fields
        byte [] msgReceived = Arrays.copyOfRange(msg, 7, msg.length);
        ISOMsg isounpacked = new ISOMsg();
        isounpacked.setPackager(packager);
        isounpacked.unpack(msgReceived);
        System.out.println("ISO 8583 message unpacked ");

        isounpacked.dump(System.out, "");



        String hostName = "localhost";
        int portNumber = 4444;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);        // 1st statement
                OutputStream outputStream =  echoSocket.getOutputStream()
        ) {
            //outputStream.write(decoded);
            outputStream.write(msg);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }




    }

}
