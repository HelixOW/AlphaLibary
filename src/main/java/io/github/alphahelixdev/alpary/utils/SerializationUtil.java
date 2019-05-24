package io.github.alphahelixdev.alpary.utils;

import io.github.alphahelixdev.alpary.Alpary;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

@EqualsAndHashCode(callSuper = false)
@ToString
public class SerializationUtil {
    public <T> String encodeBase64(T instance) {
        return Base64Coder.encodeString(Alpary.getInstance().gson().toJson(instance));
    }

    public <T> T decodeBase64(String base64, Class<T> identifier) {
        return Alpary.getInstance().gson().fromJson(Base64Coder.decodeString(base64), identifier);
    }
}