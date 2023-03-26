package parceldeliverytracker;

import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    private static MerkleTree instance;
    private List<Transaction> tranxLst;
    private String root = "0";

    /**
     * * @implNote * Set the transaction list to the MerkleTree object. * *
     *
     * @param tranxLst
     */
    private MerkleTree(List<Transaction> tranxLst) {
        super();
        this.tranxLst = tranxLst;
    }

    public static MerkleTree getInstance(List<Transaction> tranxLst) {
        if (instance == null) {
            return new MerkleTree(tranxLst);
        }
        return instance;
    }

    public String getRoot() {
        return root;
    }

    /**
     * * @implNote * Build merkle tree * * @implSpec * + build() : void
     */
    public void build() {
        List<String> tempLst = new ArrayList<>();
        for (Transaction tranx : this.tranxLst) {
            tempLst.add(tranx.toString());
        }
        List<String> hashes = genTranxHashLst(tempLst);
        while (hashes.size() != 1) {
            hashes = genTranxHashLst(hashes);
        }
        this.root = hashes.get(0).toString();
    }

    /**
     * * @implNote * Generate hashes of transactions * * @implSpec * -
     * genTranxHashLst(List<String>) : List<String>
     */
    private List<String> genTranxHashLst(List<String> tranxLst) {
        List<String> hashLst = new ArrayList<>();
        int i = 0;
        while (i < tranxLst.size()) {
            String left = tranxLst.get(i);
            i++;
            String right = null;
            if (i != tranxLst.size()) {
                right = tranxLst.get(i);
            }

            String hash = Hasher.sha256(left.concat(right));
            hashLst.add(hash);
            i++;
        }
        return hashLst;
    }
}
