package br.com.gustavofs;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract class JsonLoader {

    public static String getPayloadFileContent(ResourceLoader resourceLoader, String filename) throws IOException {
        Resource payloadResource = resourceLoader
                .getResource("classpath:payloads/" + filename);

        return Files.readString(payloadResource.getFile().toPath());
    }
}
