package parceldeliverytracker;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockReader {

    static LinkedList<Block> blockChainList = new LinkedList<Block>();
    public static List<String> extractedResult = new ArrayList<>();

    public static DeliveryInfoClass searchBlockChainTransactions(String searchTrackingID, String inputRecipientIC) throws Exception {
        String file = "master/chain.bin";
        Blockchain bc = Blockchain.getInstance(file);
        blockChainList = bc.getExistingBlockChain(file);
        PrivateKey privKeyRead = KeyAccess.getPrivateKey("MyKeyPair/PrivateKey");
        AsymmetricEncrypt asym = new AsymmetricEncrypt();


        for (int i = 1; i < blockChainList.size(); i++) {
            List<String> TranxList = blockChainList.get(i).getTransaction().getTransactionsLst();

            for (String transaction : TranxList) {
                String decryptedTransaction =asym.decrypt(transaction, privKeyRead);
                System.out.println("Existing record : " + decryptedTransaction);

                String[] field = decryptedTransaction.split("\\|");
                if (field[0].equals(searchTrackingID) && field[6].equals(inputRecipientIC)) {
                    //foundRecordBlockChain = record;
                    return new DeliveryInfoClass(field[0], field[1], field[2], field[3], field[4], field[5], field[6], field[7], field[8], field[9]);

                }
            }
        }
        return null;
    }
}