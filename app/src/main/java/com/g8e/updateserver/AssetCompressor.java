package com.g8e.updateserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import com.g8e.updateserver.AssetLoader.Asset;

public class AssetCompressor {

    @SuppressWarnings("unchecked")
    public static byte[] compressAssetData(Asset asset) throws IOException {
        if (asset.data instanceof byte[] bs) {
            return compressByteArray(bs);
        } else if (asset.data instanceof List<?>) {
            // Handle the case where the asset is a directory, i.e., a List<Asset>
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
                // Compress each asset in the directory list
                for (Asset childAsset : (List<Asset>) asset.data) {
                    byte[] compressedChildData = compressAssetData(childAsset);
                    objectStream.write(compressedChildData);
                }
            }
            return byteStream.toByteArray();
        }
        return new byte[0]; // Empty byte array if unsupported type
    }

    private static byte[] compressByteArray(byte[] data) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream);
                BufferedOutputStream bufferedStream = new BufferedOutputStream(gzipStream)) {
            bufferedStream.write(data);
        }
        return byteStream.toByteArray();
    }

}
