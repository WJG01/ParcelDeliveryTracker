package parceldeliverytracker;

import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Blockchain {
    public static final int MAX_TXN_LIST_SIZE = 10;
    // data structure
    private static LinkedList<Block> blockDB = new LinkedList<>();
    /**
     * singleton pattern
     */
    private static Blockchain _instance;

    public String chainFile;


    public Blockchain(String chainFile) {
        super();
        this.chainFile = chainFile;
        System.out.println("> Blockchain object is created!");
    }

    public static Blockchain getInstance(String chainFile) {
        if (_instance == null)
            _instance = new Blockchain(chainFile);
        return _instance;
    }

    /**
     * get()
     */
    public static LinkedList<Block> getExistingBlockChain(String file) {
        try (FileInputStream fin = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fin);
        ) {
            return (LinkedList<Block>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void genesis() {
        Block genesisBlock = new Block(0, "0");
        blockDB.add(genesisBlock);
        persist();
    }

    public void fetchPreviousBlock() {
        blockDB = getExistingBlockChain(this.chainFile);
    }

    public void addTransaction(String txn) {
        // Get the last block in the chain
        List<String> lastBlockTransactionLst = new ArrayList<>();
        Transaction lastBlockTransaction = new Transaction();


        Block lastBlock = blockDB.get(blockDB.size() - 1);
        int lastBlockIndex = lastBlock.getBlockHeader().getIndex();

        if (lastBlockIndex != 0) {
            lastBlockTransaction = lastBlock.getTransaction();
            lastBlockTransactionLst = lastBlockTransaction.getTransactionsLst();
        }


        // Check if the transaction list in the last block is full / last block is genesis block
        if (lastBlockIndex == 0 || lastBlockTransactionLst.size() == MAX_TXN_LIST_SIZE) {
            // Create a new block with the transaction list and add it to the chain
            Block newBlock = new Block(lastBlock.getBlockHeader().getIndex() + 1, lastBlock.getBlockHeader().getCurrentHash());
            Transaction tranxLst = new Transaction();
            tranxLst.add(txn);
            newBlock.setTranxLst(tranxLst);
            String merkleRoot = tranxLst.genMerkleRoot();
            newBlock.getBlockHeader().setMerkleRoot(merkleRoot);
            newBlock.getBlockHeader().setCurrentHash(merkleRoot);
            blockDB.add(newBlock);

        } else {
            lastBlockTransaction.add(txn);
            lastBlock.setTranxLst(lastBlockTransaction);
            String merkleRoot = lastBlockTransaction.genMerkleRoot();
            lastBlock.getBlockHeader().setMerkleRoot(merkleRoot);
            lastBlock.getBlockHeader().setCurrentHash(merkleRoot);
        }
        persist();
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