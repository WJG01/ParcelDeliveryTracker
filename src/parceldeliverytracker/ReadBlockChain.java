package parceldeliverytracker;

import java.security.PrivateKey;
import java.util.LinkedList;
import java.util.List;

public class ReadBlockChain {

    static LinkedList<Blockk> blockChainList = new LinkedList<Blockk>();

    public static List<String> getBlockChainTransactions(String searchValue) throws Exception {
        String file = "master/chain.bin";
        Blockchainn bc = Blockchainn.getInstance(file);
        //bc.distribute();
        blockChainList = bc.getExistingBlockChain(file);
        PrivateKey privKeyRead = KeyAccess.getPrivateKey("MyKeyPair/PrivateKey");


        for (int i = 1; i < blockChainList.size(); i++) {
            List<String> TranxList = blockChainList.get(i).getTransactionsLst();

            for (String transaction : TranxList) {
//                String[] tranx = transaction.split(",");
//                String tranxSingle = tranx[0];
                System.out.println(transaction);

            }
        }
        return null;
    }
}
