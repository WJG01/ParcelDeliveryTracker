package parceldeliverytracker;

//binary form
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {

	private static final long serialVersionUID = -3891440441905013644L;

	public final int SIZE = 10;

	public String merkleRoot ;

	public List<String> dataLst = new ArrayList<>();

	public void add(String tranx) {
		if (dataLst.size() < SIZE)
			dataLst.add(tranx);
	}

	public void genMerkleRoot() {
		MerkleTree mt = MerkleTree.getInstance(this.dataLst);
		mt.build();
		this.merkleRoot = mt.getRoot();
	}

}
