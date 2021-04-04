package com.github.maheshyaddanapudi.quartz.scheduler.APICallsScheduler.service.internal.cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

@Service
public class EncryptionDecryptionService {

    @Value("spring.encryption.static.salt:APICallsQuartzScheduler@1234")
    private String STATIC_SALT;

    @Value("spring.encryption.initialization.vector:AODVNUASDNVVAOVF")
    private String INIT_VECTOR;

    private byte[] IV_KEY_BYTES = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public String encrypt(String secretToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromVector(), generateIv());
        byte[] cipherText = cipher.doFinal(secretToEncrypt.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromVector(), generateIv());
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }

    private SecretKey getKeyFromVector()
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(INIT_VECTOR.toCharArray(), STATIC_SALT.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    private IvParameterSpec generateIv() {
        IvParameterSpec iv = new IvParameterSpec(IV_KEY_BYTES);
        return iv;
    }
}
