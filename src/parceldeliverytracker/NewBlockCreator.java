/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author weiju
 */
public class NewBlockCreator {

    private static final String masterFolder = "master";
    private static final String fileName = masterFolder + "/chain.bin";

    public static void main(String[] args) {
        try {
            DeliveryInfoClass newDeliveryInfo = new DeliveryInfoClass("orderID", "senderIC", "senderAddress",
                    " recipientIC", "recipientAddress", "sensitiveone", "sensitivetwo");
            //digitalSignature(newTransaction);

            insertRecord(newDeliveryInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        Blockchainn bc = Blockchainn.getInstance(fileName);
//        bc.fetchPreviousBlock();
//        bc.distribute();
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
            // bc.distribute();
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


        FileWriter fileWriter = new FileWriter("DigitalSignatureKeyStore/digitalSignature.txt");
        String content = newDeliveryInfo.toString() + "," + signature + "," + privateKeyStr + "," + publicKeyStr;
        fileWriter.write(content);
        fileWriter.close();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("DigitalSignatureKeyStore/digitalSignature.txt"));
        String readContent = bufferedReader.readLine();
        String[] field = readContent.split(",");


        String publicKeystring = field[3];

//        // Remove the header and footer of the key string
//        publicKeystring = publicKeystring.replace("-----BEGIN PUBLIC KEY-----\n", "");
//        publicKeystring = publicKeystring.replace("\n-----END PUBLIC KEY-----", "");
//
//        // Convert the key string to a byte array
//        byte[] publicKeyBytes = publicKeystring.getBytes();
//
//
//        // Construct a new PublicKey object from the byte array
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//        PublicKey publicKeyread = keyFactory.generatePublic(keySpec);

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKeyread = keyFactory.generatePublic(keySpec);

        //System.out.println("Public key: " + publicKey);
        boolean isValid = sig.verify(field[0], field[1], publicKeyread);
        System.out.println("Result:" + isValid);

        bufferedReader.close();


    }
}

