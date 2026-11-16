package com.innova;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

//Advanced Encryption Standard(AES)
//AES uses to protect sensitive data
public class AES_GCM_256 {

	    private static final String ALGORITHM = "AES/GCM/NoPadding";
	    private static final int AES_KEY_SIZE_BITS = 256;
	    private static final int IV_SIZE_BYTES = 12; // Standard for GCM
	    private static final int TAG_SIZE_BITS = 128; // Standard for security

	    public static void main(String[] args) {
	        try {
	            // 1. Setup sample payload
	            String secretMessage = "AES uses to protect sensitive data";
	            System.out.println("AES--->" + secretMessage);

	            // 2. Generate cryptographically strong key
	            SecretKey secretKey = generateKey();

	            // 3. Perform encryption
	            String cipherTextBase64 = encrypt(secretMessage, secretKey);
	            System.out.println("1. Encrypted Output (Base64): " + cipherTextBase64);

	            // 4. Perform decryption
	            String decryptedMessage = decrypt(cipherTextBase64, secretKey);
	            System.out.println("2. Decrypted Recovery: " + decryptedMessage);

	        } catch (Exception e) {
	            System.err.println("Cryptographic processing failed: " + e.getMessage());
	        }
	    }

	    public static SecretKey generateKey() throws Exception {
	        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	        keyGenerator.init(AES_KEY_SIZE_BITS);
	        return keyGenerator.generateKey();
	    }

	    public static String encrypt(String plainText, SecretKey key) throws Exception {
	        // Generate a dynamic, unique Initialization Vector (IV)
	        byte[] iv = new byte[IV_SIZE_BYTES];
	        new SecureRandom().nextBytes(iv);

	        Cipher cipher = Cipher.getInstance(ALGORITHM);
	        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE_BITS, iv);
	        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

	        byte[] cipherTextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

	        // Pack IV and Ciphertext together into a single transmission payload
	        byte[] finalPayload = new byte[iv.length + cipherTextBytes.length];
	        System.arraycopy(iv, 0, finalPayload, 0, iv.length);
	        System.arraycopy(cipherTextBytes, 0, finalPayload, iv.length, cipherTextBytes.length);

	        return Base64.getEncoder().encodeToString(finalPayload);
	    }

	    public static String decrypt(String base64Payload, SecretKey key) throws Exception {
	        byte[] finalPayload = Base64.getDecoder().decode(base64Payload);

	        // Unpack the 12-byte IV from the start
	        byte[] iv = new byte[IV_SIZE_BYTES];
	        System.arraycopy(finalPayload, 0, iv, 0, iv.length);

	        // Extract the remaining text bytes
	        int cipherTextLength = finalPayload.length - IV_SIZE_BYTES;
	        byte[] cipherTextBytes = new byte[cipherTextLength];
	        System.arraycopy(finalPayload, IV_SIZE_BYTES, cipherTextBytes, 0, cipherTextLength);

	        Cipher cipher = Cipher.getInstance(ALGORITHM);
	        GCMParameterSpec spec = new GCMParameterSpec(TAG_SIZE_BITS, iv);
	        cipher.init(Cipher.DECRYPT_MODE, key, spec);

	        byte[] dynamicPlainBytes = cipher.doFinal(cipherTextBytes);
	        return new String(dynamicPlainBytes, StandardCharsets.UTF_8);
	    }
	}


