/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author weiju
 */
public class ParcelDeliveryTracker {

    private static final String masterFolder = "master";
    private static final String fileName = masterFolder + "/chain.bin";

    public static void insertRecord(String input) throws Exception {
        Blockchainn bc = Blockchainn.getInstance(fileName);
        File folder = new File(masterFolder);
        File file = new File(fileName);

        //create public and private key //show keyPairing
        MyKeyPair.create();
        byte[] publicKey = MyKeyPair.getPublicKey().getEncoded();
        byte[] privateKey = MyKeyPair.getPrivateKey().getEncoded();

        //DIR = MyKeyPair; public file = PublicKey; privateKey = PrivateKey
        MyKeyPair.put(publicKey, "MyKeyPair/PublicKey");
        MyKeyPair.put(privateKey, "MyKeyPair/PrivateKey");

        Asymmetric asym = new Asymmetric();
        PublicKey pubKeyRead = KeyAccess.getPublicKey("MyKeyPair/PublicKey");

        String encrypted = asym.encrypt(input,pubKeyRead);

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
            bc.addTransaction(bc, input);
           // bc.distribute();
        } else {
            bc.fetchPreviousBlock();
            bc.addTransaction(bc, input);
           // bc.distribute();
        }
        ReadBlockChain.getBlockChainTransactions("");


        //ReadBlockChain.getBlockChainTransactions("master/chain.bin");
    }
}

//	public static void main(String[] args) {
//		/* Test generate merkle root */ /* dummy transaction */ String line1 = "bob|alice|debit|100";
//		String line2 = "mick|alice|debit|200";
//		String line3 = "peter|alice|debit|300";
//		String line4 = "ali|alice|debit|400";
//		List<String> lst = new ArrayList<>();
//		lst.add(line1);
//		lst.add(line2);
//		lst.add(line3);
//		lst.add(line4);
//		MerkleTree mt = MerkleTree.getInstance(lst);
//		mt.build();
//		String root = mt.getRoot();
//		System.out.println("Merkle Root = " + root);
//	}
