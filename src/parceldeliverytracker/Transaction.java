package parceldeliverytracker;

//binary form
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3891440441905013644L;
	private  String orderID;
	private  long senderIC;
	private  String senderAddress;
	private  long recipientIC;
	private String recipientAdress;


	public Transaction(String orderID, long senderIC, String senderAddress, long recipientIC, String recipientAdress) {
		this.orderID = orderID;
		this.senderIC = senderIC;
		this.senderAddress = senderAddress;
		this.recipientIC = recipientIC;
		this.recipientAdress = recipientAdress;
	}

	public String convertToString(){
		return "orderID: "+orderID+" | senderIC: "+senderIC+" | senderAddress: " +senderAddress+" | recipientIC: "+recipientIC+" | recipientAdress: "+recipientAdress;
	}

	@Override
	public String toString() {
		return "orderID: "+orderID+" | senderIC: "+senderIC+" | senderAddress: " +senderAddress+" | recipientIC: "+recipientIC+" | recipientAdress: "+recipientAdress;
	}
}
