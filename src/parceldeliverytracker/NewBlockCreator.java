/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Stream;

/**
 * @author weiju
 */
public class NewBlockCreator {

    private static final String masterFolder = "master";
    private static final String fileName = masterFolder + "/chain.bin";

    public static void main(String[] args) {
//        try {
//            //digitalSignature(newTransaction);
//
//            insertRecord(newDeliveryInfo);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        Blockchainn bc = Blockchainn.getInstance(fileName);
//        bc.fetchPreviousBlock();
//        bc.distribute();
//        try {
//            digitalSignature(newDeliveryInfo);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }


    public static void insertRecord(DeliveryInfoClass newDeliveryInfo) throws Exception {
        Blockchainn bc = Blockchainn.getInstance(fileName);
        File folder = new File(masterFolder);
        File file = new File(fileName);

        digitalSignature(newDeliveryInfo);

        Asymmetric asym = new Asymmetric();
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
            bc.distribute();
        } else {
            bc.fetchPreviousBlock();
            bc.addTransaction(bc, encrypted);
            bc.distribute();
        }
        ReadBlockChain.getBlockChainTransactions();

        //ReadBlockChain.getBlockChainTransactions("master/chain.bin");
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
                //newDeliveryInfo + "," + signature + "," + privateKeyStr + "," + publicKeyStr;
        FileHandler.writeFile(filePath, contentToWrite);
    }
}

