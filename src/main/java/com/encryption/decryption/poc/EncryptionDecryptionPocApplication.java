package com.encryption.decryption.poc;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.encryption.decryption.poc.dao.AlgorithmRepo;
import com.encryption.decryption.poc.model.Algorithm;

@SpringBootApplication
public class EncryptionDecryptionPocApplication implements CommandLineRunner {

	@Autowired
	private AlgorithmRepo algorithmRepo;

	public static void main(String[] args) {
		SpringApplication.run(EncryptionDecryptionPocApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		List<Algorithm> findAllAlgorithms = algorithmRepo.findAll();
		List<Algorithm> AEStypeAlgorithms = findAllAlgorithms.stream()
				.filter(algorithm -> algorithm.getType().equalsIgnoreCase("AES")).collect(Collectors.toList());

		if (AEStypeAlgorithms.size() != 0) {
			String key = AEStypeAlgorithms.get(0).getKey(); // add any key But 128 bit key or 16 bytes key
			String initVector = "RandomInitVector"; // add any IV But 128 bit IV or 16 bytes IV

			String originalValue = "Smith Michel";
			System.out.println("originalValue  : " + originalValue);

			String encryptValue = encrypt(key, initVector, originalValue);
			System.out.println("encryptValue  : " + encryptValue);

			String decryptValue = decrypt(key, initVector, encryptValue);
			System.out.println("decryptValue  : " + decryptValue);
		} else {

			// add here new type algorithm code

		}

	}

	public static String encrypt(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());

			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));

			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

}
