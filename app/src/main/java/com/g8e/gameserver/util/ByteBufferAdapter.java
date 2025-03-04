package com.g8e.gameserver.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferAdapter extends TypeAdapter<ByteBuffer> {
    @Override
    public void write(JsonWriter out, ByteBuffer value) throws IOException {
        byte[] bytes = new byte[value.remaining()];
        value.duplicate().get(bytes); // Preserve the original buffer position
        out.value(new String(bytes)); // Or encode the byte array as needed
    }

    @Override
    public ByteBuffer read(JsonReader in) throws IOException {
        String str = in.nextString();
        return ByteBuffer.wrap(str.getBytes());
    }
}
