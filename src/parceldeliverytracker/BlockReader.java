package parceldeliverytracker;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BlockReader {

    static LinkedList<Block> blockChainList = new LinkedList<Block>();
    public static List<String> extractedResult = new ArrayList<>();

    public static List<String> getBlockChainTransactions() throws Exception {
        String file = "master/chain.bin";
        Blockchain bc = Blockchain.getInstance(file);
        blockChainList = bc.getExistingBlockChain(file);
        PrivateKey privKeyRead = KeyAccess.getPrivateKey("MyKeyPair/PrivateKey");
        AsymmetricEncrypt asym = new AsymmetricEncrypt();



        for (int i = 1; i < blockChainList.size(); i++) {
            List<String> TranxList = blockChainList.get(i).getTransaction().getTransactionsLst();

            for (String transaction : TranxList) {
                String decryptedTransaction =asym.decrypt(transaction, privKeyRead);
                extractedResult.add(decryptedTransaction);

            }
        }
        return null;
    }
}
