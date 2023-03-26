/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author weiju
 */
public class ParcelDeliveryTracker {

    private static final String masterFolder = "master";
    private static final String fileName = masterFolder + "/chain.bin";

    public static void main(String[] args) {
        Transaction newTransaction = new Transaction("O352353",0123123,"senderAdress",99453453,"recipientAdress");
        try {
            Blockchainn newBlockchain = new Blockchainn();
            newBlockchain.addTransaction(newTransaction);
            newBlockchain.distribute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
