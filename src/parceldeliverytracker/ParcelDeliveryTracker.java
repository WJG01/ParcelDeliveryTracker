package parceldeliverytracker;



import java.io.File;

public class ParcelDeliveryTracker {


    private static String masterFolder = "master";
    private static String fileName = masterFolder + "/chain.bin";

    public static void insertRecord(String record) {
        Blockchain bc = Blockchain.getInstance(fileName);
        if (!new File(masterFolder).exists()) {
            System.err.println("> creating Blockchain binary !");
            new File(masterFolder).mkdir();
            bc.genesis();
        } else {
            /* dummy transaction */
            Transaction tranxLst = new Transaction();
            tranxLst.add(record);
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
