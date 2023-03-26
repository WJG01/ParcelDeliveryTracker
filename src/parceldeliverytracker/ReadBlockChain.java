package parceldeliverytracker;


import java.util.LinkedList;
import java.util.List;

public class ReadBlockChain {

    static LinkedList<Block> blockChainList = new LinkedList<Block>();

    public static List<String> getBlockChainTransactions(String searchValue) {
        String file = "master/chain.bin";
        Blockchain bc = Blockchain.getInstance(file);
        bc.distribute();
        blockChainList = bc.get();

        for (int i = 1; i < blockChainList.size(); i++) {
            List<String> TranxList = blockChainList.get(i).getTranxLst();

            for (Transaction transaction : TranxList) {
                System.out.println(transaction.toString());

            }
        }

        System.out.println(blockChainList.toString());
        return null;
    }
}
