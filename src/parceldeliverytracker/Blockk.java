package parceldeliverytracker;

import java.io.Serializable;
import java.util.ArrayList;

public class Blockk implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1911051056455957018L;
	/**
     * 
     */
    public Header blockHeader;
    public String merkleRoot;
    /* aggregation relationship */
    //public Transaction tranxLst;
    private ArrayList<Transaction> transactionsLst;

    public Blockk(int index, ArrayList<Transaction> transactionsLst, String previousHash) {
        long now = System.currentTimeMillis();
        /* construct part object upon object construction */
        this.blockHeader = new Header();
        this.blockHeader.setPreviousHash(previousHash);
        this.blockHeader.setTimestamp(now);
        this.transactionsLst = transactionsLst;
        // hashing with sha256 - the input is joined with previousHash+now
        String currentHash = Hasher.sha256(String.join("+", previousHash, String.valueOf(now)));
        this.blockHeader.setCurrentHash(currentHash);
    }

    public Header getBlockHeader() {
        return blockHeader;
    }

    public ArrayList<Transaction> getTransactionsLst() {
        return transactionsLst;
    }

    public void addTransaction(Transaction txn) {
        transactionsLst.add(txn);
        this.blockHeader.setMerkleRoot(genMerkleRoot());
    }

    //set whole list
    public void setTranxLst(ArrayList<Transaction> tranxLst) {
        this.transactionsLst = tranxLst;
    }

    public String genMerkleRoot() {
        MerkleTree mt = MerkleTree.getInstance(this.transactionsLst);
        mt.build();
        return mt.getRoot();
    }


    /* composition relationship - inner class definition for part object */
    public class Header implements Serializable {
        /**
         * 
         */
//        private static final long serialVersionUID = 1L;
        // data member
        public int index;
        public String currentHash, previousHash;
        public long timestamp;
        public String merkleRoot;

        @Override
        public String toString() {
            return "Header [index=" + index + ", currentHash=" + currentHash + ", previousHash=" + previousHash
                    + ", timestamp=" + timestamp + ", merkleRoot=" + merkleRoot + "]";
        }
        // getset methods
        public String getCurrentHash() {
            return currentHash;
        }
        public void setCurrentHash(String currentHash) {
            this.currentHash = currentHash;
        }
        public String getPreviousHash() {
            return previousHash;
        }
        public void setPreviousHash(String previousHash) {
            this.previousHash = previousHash;
        }
        public long getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
        public int getIndex() {
            return index;
        }
        public void setMerkleRoot(String merkleRoot) { this.merkleRoot = merkleRoot;}
    }
    /* aggregation relationship */
    public Transaction tranxLst;
    public void setTranxLst(Transaction tranxLst) {
        this.tranxLst = tranxLst;
    }
    @Override
    public String toString() {
        return "Block [blockHeader=" + blockHeader + ", tranxLst=" + tranxLst + "]";
    }
}
