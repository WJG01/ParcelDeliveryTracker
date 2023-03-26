package parceldeliverytracker;

import java.util.LinkedList;
import java.util.List;

public class ReadBlockChain {

    static LinkedList<Blockk> blockChainList = new LinkedList<Blockk>();

    public static List<String> getBlockChainTransactions(String file) {
        Blockchainn bc = Blockchainn.getInstance(file);
        bc.distribute();
//        blockChainList = bc.getExistingBlockChain(file);
//
//        for (int i = 1; i < blockChainList.size(); i++) {
//            ArrayList<Transaction> TranxList = blockChainList.get(i).getTransactionsLst();
//
//            for (Transaction transaction : TranxList) {
//                System.out.println(transaction.toString());
//
//            }
//        }
//
//        System.out.println(blockChainList.toString());
        return null;
    }
}
