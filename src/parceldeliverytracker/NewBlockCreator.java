/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author weiju
 */
public class NewBlockCreator {

    private static final String masterFolder = "master";
    private static final String fileName = masterFolder + "/chain.bin";


    public static void insertRecord(DeliveryInfoClass newDeliveryInfo) throws Exception {
        Blockchain bc = Blockchain.getInstance(fileName);
        File folder = new File(masterFolder);
        File file = new File(fileName);

        //create digital signature for significant data
        digitalSignature(newDeliveryInfo);

        AsymmetricEncrypt asym = new AsymmetricEncrypt();
        PublicKey pubKeyRead = KeyAccess.getPublicKey("MyKeyPair/PublicKey");
        String encrypted = asym.encrypt(newDeliveryInfo.toString(), pubKeyRead);

        if (!folder.exists() || !file.exists()) {
            if (!folder.exists()) {
                folder.mkdir();
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            bc.genesis();
            bc.addTransaction(bc, encrypted);
        } else {
            bc.fetchPreviousBlock();
            bc.addTransaction(bc, encrypted);
        }
        //Showing blockchain structure after adding record
        bc.distribute();
    }

    private static void digitalSignature(DeliveryInfoClass newDeliveryInfo) throws Exception {

        MyKeyPair.create();
        PublicKey publicKey = MyKeyPair.getPublicKey();
        PrivateKey privateKey = MyKeyPair.getPrivateKey();
        MySignature sig = new MySignature();
        String signature = sig.sign(newDeliveryInfo.toString(), privateKey);


        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        Path directoryPath = Paths.get("DigitalSignatureKeyStore");
        Path filePath = Paths.get("DigitalSignatureKeyStore/digitalSignature.txt");

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        String contentToWrite = String.join(",", newDeliveryInfo.trackingID(), newDeliveryInfo.toString(), signature, privateKeyStr, publicKeyStr);
        FileHandler.writeFile(filePath, contentToWrite);
    }
}

