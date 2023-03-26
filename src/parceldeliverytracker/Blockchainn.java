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
    private static String fileName = masterFolder + "/chain.bin";
    /**
     * singleton pattern
     */
    private static Blockchainn _instance;

    public String chainFile;


    public Blockchainn(String chainFile) {
        super();
        this.chainFile = chainFile;
        System.out.println("> Blockchain object is created!");
    }

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
            Blockchainn.getInstance(fileName);
            Blockk genesisBlock = new Blockk(0, "0");
            blockDB.add(genesisBlock);
            //persist();
        } else {
            this.chainFile = fileName;
            blockDB = getExistingBlockChain(this.chainFile);
        }

    }

    public static Blockchainn getInstance(String chainFile) {
        if (_instance == null)
            _instance = new Blockchainn(chainFile);
        return _instance;
    }

    /**
     * get()
     */
    public static LinkedList<Blockk> getExistingBlockChain(String file) {
        try (FileInputStream fin = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fin);
        ) {
            return (LinkedList<Blockk>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void genesis() {
        Blockk genesisBlock = new Blockk(0, "0");
        blockDB.add(genesisBlock);
        persist();
    }

    public void fetchPreviousBlock() {
        blockDB = getExistingBlockChain(this.chainFile);
    }

    public void addTransaction(Blockchainn bc, Transaction txn) {
// Get the last block in the chain


        Blockk lastBlock = blockDB.get(blockDB.size() - 1);

        // Check if the transaction list in the last block is full
        if (lastBlock.getTransactionsLst().size() == MAX_TXN_LIST_SIZE || lastBlock.getBlockHeader().getIndex() == 0) {
            // Create a new block with the transaction list and add it to the chain
            Blockk newBlock = new Blockk(lastBlock.getBlockHeader().getIndex() + 1, lastBlock.getBlockHeader().getCurrentHash());
            newBlock.addTransaction(txn);
            ArrayList<Transaction> transactionLst = newBlock.getTransactionsLst();
            newBlock.setTranxLst(transactionLst);
            newBlock.getBlockHeader().setIndex(lastBlock.getBlockHeader().getIndex() + 1);
            newBlock.genMerkleRoot();
            //System.out.println(newBlock.toString());
            blockDB.add(newBlock);
            persist();

        } else {
            // Append the transaction to the transaction list in the last block
            ArrayList<Transaction> lastBlockTxn = lastBlock.getTransactionsLst();
            System.out.println("Existing transaction list: " + lastBlockTxn);
            lastBlockTxn.add(txn);
            lastBlock.setTranxLst(lastBlockTxn);
            //ArrayList<Transaction> transactionLst = lastBlock.getTransactionsLst();
            String merkleRoot = lastBlock.genMerkleRoot();
            lastBlock.getBlockHeader().setMerkleRoot(merkleRoot);
            persist();
        }
    }

    /**
     * persist()
     */
    private void persist() {
        try (FileOutputStream fout = new FileOutputStream(this.chainFile);
             ObjectOutputStream out = new ObjectOutputStream(fout);
        ) {
            System.out.println("Writing data: " + blockDB);
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