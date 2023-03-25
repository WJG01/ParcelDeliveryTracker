/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parceldeliverytracker;

import java.io.File;

/**
 *
 * @author weiju
 */
public class ParcelDeliveryTracker {

    private static final String masterFolder = "master";
    private static final String fileName = masterFolder + "/chain.bin";

    public static void main(String[] args) {
        Blockchain bc = Blockchain.getInstance(fileName);
        if (!new File(masterFolder).exists()) {
            System.err.println("> creating Blockchain binary !");
            new File(masterFolder).mkdir();
            bc.genesis();
        } else {
            /* dummy transaction */
            String line1 = "bob|alice|debit|100";
            String line2 = "mick|alice|debit|200";
            Transaction tranxLst = new Transaction();
            tranxLst.add(line1);
            tranxLst.add(line2);
            tranxLst.genMerkleRoot();
            String previousHash = bc.get().getLast().getBlockHeader().getCurrentHash();
            Block b1 = new Block(previousHash);
            b1.setTranxLst(tranxLst);

            bc.nextBlock(b1);
            System.out.println(b1);
            bc.distribute();
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
