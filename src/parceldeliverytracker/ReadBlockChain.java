package parceldeliverytracker;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReadBlockChain {

    static LinkedList<Blockk> blockChainList = new LinkedList<Blockk>();
    public static List<String> extractedResult = new ArrayList<>();

    public static List<String> getBlockChainTransactions() throws Exception {
        String file = "master/chain.bin";
        Blockchainn bc = Blockchainn.getInstance(file);
        //bc.distribute();
        blockChainList = bc.getExistingBlockChain(file);
        PrivateKey privKeyRead = KeyAccess.getPrivateKey("MyKeyPair/PrivateKey");
        Asymmetric asym = new Asymmetric();



        for (int i = 1; i < blockChainList.size(); i++) {
            List<String> TranxList = blockChainList.get(i).getTransaction().getTransactionsLst();

            for (String transaction : TranxList) {
//                String[] tranx = transaction.split(",");
//                String tranxSingle = tranx[0];
                String decryptedTransaction =asym.decrypt(transaction, privKeyRead);
                System.out.println(decryptedTransaction);
                extractedResult.add(decryptedTransaction);

            }
        }
        return null;
    }
}
