package com.g8e.gameserver.network.compressing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class Compress {
    public static byte[] compress(String data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DeflaterOutputStream dos = new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_COMPRESSION))) {
            dos.write(data.getBytes());
            dos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
