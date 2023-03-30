/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws Exception {

//        DeliveryInfoClass testing = new DeliveryInfoClass("T00001","O01234","mingming",
//                "0123456","jalan sultan,31550","memei","123456","013-4857365","jalan bukit bintang","Books");
//        insertRecord(testing);
    }

    public static void insertRecord(DeliveryInfoClass newDeliveryInfo) throws Exception {
        Blockchain bc = Blockchain.getInstance(fileName);
        File folder = new File(masterFolder);
        File file = new File(fileName);

        //create digital signature for significant data
        digitalSignature(newDeliveryInfo);

        // encrypt new delivery info using asymmetric cryptography
        String encrypted = asymmEncrypt(newDeliveryInfo.toString());

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
            bc.addTransaction(encrypted);
        } else {
            bc.fetchPreviousBlock();
            bc.addTransaction(encrypted);
        }
        //Showing blockchain structure after adding record
        bc.distribute();
    }

    public static String asymmEncrypt(String newDeliveryInfo) throws Exception {
        File masterFolderKey = new File("MyKeyPair");
        File fileNamePublicKey = new File("MyKeyPair" + "/PublicKey");
        File fileNamePrivateKey = new File("MyKeyPair" + "/PrivateKey");


        if (!masterFolderKey.exists()) {
            masterFolderKey.mkdir();
        }

        if (!fileNamePublicKey.exists() || !fileNamePrivateKey.exists()) {
            //create public and private key //show keyPairing
            MyKeyPair.create();
            byte[] publicKey = MyKeyPair.getPublicKey().getEncoded();
            byte[] privateKey = MyKeyPair.getPrivateKey().getEncoded();

            //DIR = MyKeyPair; public file = PublicKey; privateKey = PrivateKey
            MyKeyPair.put(publicKey, "MyKeyPair/PublicKey");
            MyKeyPair.put(privateKey, "MyKeyPair/PrivateKey");
        }

        AsymmetricEncrypt asym = new AsymmetricEncrypt();
        PublicKey pubKeyRead = KeyAccess.getPublicKey("MyKeyPair/PublicKey");
        return asym.encrypt(newDeliveryInfo.toString(), pubKeyRead);
    }

    private static void digitalSignature(DeliveryInfoClass newDeliveryInfo) throws Exception {

        //create key pair
        MyKeyPair.create();
        PublicKey publicKey = MyKeyPair.getPublicKey();
        PrivateKey privateKey = MyKeyPair.getPrivateKey();
        //convert them into string
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        //create new signature
        MySignature sig = new MySignature();
        String signature = sig.sign(newDeliveryInfo.toString(), privateKey);


        //store created signature + public key + private key + transaction content into text file
        Path directoryPath = Paths.get("DigitalSignatureKeyStore");
        Path filePath = Paths.get("DigitalSignatureKeyStore/digitalSignature.txt");

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        String contentToWrite = String.join("/anotherField/", newDeliveryInfo.trackingID(), newDeliveryInfo.toString(),
                signature, privateKeyStr, publicKeyStr);

        FileHandler.writeFile(filePath, contentToWrite);
    }
}

