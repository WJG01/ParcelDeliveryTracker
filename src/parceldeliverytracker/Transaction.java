package parceldeliverytracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {

    public final int SIZE = 10;

    //public String merkleRoot ;

    public List<String> dataLst = new ArrayList<>();

    public void add(String tranx) {
        if (dataLst.size() < SIZE) {
            dataLst.add(tranx);
        }
        //genMerkleRoot();
    }

    public List<String> getTransactionsLst() {
        return this.dataLst;
    }

    public String genMerkleRoot() {
        MerkleTree mt = MerkleTree.getInstance(this.dataLst);
        mt.build();
        return mt.getRoot();
    }

    @Override
    public String toString() {
        return "Transaction [SIZE=" + SIZE + ", dataLst=" + dataLst + "]";
    }
}
