package parceldeliverytracker;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Blockchainn {
    public static final int MAX_TXN_LIST_SIZE = 10;
    // data structure
    private static LinkedList<Blockk> blockDB = new LinkedList<>();
    private static String masterFolder = "master";
    private static String fileName = masterFolder +"/chain.bin";
    /**
     * singleton pattern
     */
    private static Blockchainn _instance;
//    public static Blockchainn getInstance(String chainFile ) {
//        if(_instance == null)
//            _instance = new Blockchainn( chainFile );
//        return _instance;
//    }

    public String chainFile;

    public Blockchainn() throws IOException {


        File folder = new File(masterFolder);
        File file = new File(fileName);

        if (!folder.exists() || !file.exists()) {
            if (!folder.exists()) {
                folder.mkdir();
                if (!file.exists())
                    file.createNewFile();
            }
            System.out.println("fileName :" + fileName);
            this.chainFile = fileName;
            System.out.println("chainFile :"+ this.chainFile);
            Blockk genesisBlock = new Blockk(0, new ArrayList<Transaction>(), "0");
            blockDB.add(genesisBlock);
        } else {
            this.chainFile = fileName;
            blockDB = getExistingBlockChain();
            Blockk lastBlock = blockDB.get(blockDB.size() - 1);
            Blockk newBlock = new Blockk(lastBlock.getBlockHeader().getIndex() + 1, new ArrayList<Transaction>(), lastBlock.getBlockHeader().getCurrentHash());
            blockDB.add(newBlock);
        }

    }

    public void addTransaction( ArrayList<Transaction> txnList) {
// Get the last block in the chain
        Blockk lastBlock = blockDB.get(blockDB.size() - 1);

        while(!txnList.isEmpty()){

            for ( int i= 0; i < MAX_TXN_LIST_SIZE && !txnList.isEmpty(); i++){
                Transaction transaction = txnList.remove(0);
                lastBlock.addTransaction(transaction);
            }
            lastBlock.genMerkleRoot();
            blockDB.add(lastBlock);
            persist();
        }

        // Check if the transaction list in the last block is full
        if (lastBlock.getTransactionsLst().size() == MAX_TXN_LIST_SIZE) {
            // Create a new block with the transaction list and add it to the chain
            Blockk newBlock = new Blockk(lastBlock.getBlockHeader().getIndex() + 1, new ArrayList<Transaction>(), lastBlock.getBlockHeader().getCurrentHash());
            newBlock.addTransaction(txn);
            newBlock.addTransaction(txn);
            //ArrayList<Transaction> transactionLst =  newBlock.getTransactionsLst();
            newBlock.genMerkleRoot();
            blockDB.add(newBlock);
            persist();
        } else {
            // Append the transaction to the transaction list in the last block
            ArrayList<Transaction> lastBlockTxn = lastBlock.getTransactionsLst();
            System.out.println("Existing transaction list: " + lastBlockTxn);
            lastBlockTxn.add(txn);
            lastBlockTxn.add(txn);
            lastBlock.setTranxLst(lastBlockTxn);
            //ArrayList<Transaction> transactionLst = lastBlock.getTransactionsLst();
            lastBlock.genMerkleRoot();
            persist();
        }
    }

    /**
     * get()
     */
    public LinkedList<Blockk> getExistingBlockChain() {
        try (FileInputStream fin = new FileInputStream(this.chainFile);
             ObjectInputStream in = new ObjectInputStream(fin);
        ) {
            return (LinkedList<Blockk>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * persist()
     */
    private void persist() {
        try (FileOutputStream fout = new FileOutputStream(this.chainFile);
             ObjectOutputStream out = new ObjectOutputStream(fout);
        ) {
            out.writeObject(blockDB);
            System.out.println(">> Master file is updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * distribute()
     */
    public void distribute() {
        String chain = new GsonBuilder().setPrettyPrinting().create().toJson(blockDB);
        System.out.println(chain);
    }
}