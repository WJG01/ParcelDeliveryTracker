package parceldeliverytracker;

//binary form
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record DeliveryInfoClass(String orderID, String senderIC, String senderAddress, String recipientIC,
						  String recipientAdress,String sensitiveone, String sensitivetwo) implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3891440441905013644L;

	public DeliveryInfoClass(String orderID, String senderIC, String senderAddress, String recipientIC, String recipientAdress,String sensitiveone, String sensitivetwo) {
		this.orderID = orderID;
		this.senderIC = senderIC;
		this.senderAddress = senderAddress;
		this.recipientIC = recipientIC;
		this.recipientAdress = recipientAdress;
		this.sensitiveone = sensitiveone;
		this.sensitivetwo = sensitivetwo;
	}

	@Override
	public String orderID() {
		return orderID;
	}

	@Override
	public String senderIC() {
		return senderIC;
	}

	@Override
	public String senderAddress() {
		return senderAddress;
	}

	@Override
	public String recipientIC() {
		return recipientIC;
	}

	@Override
	public String recipientAdress() {
		return recipientAdress;
	}

	@Override
	public String toString() {
		return orderID+"|"+senderIC+"|" +senderAddress+"|"+recipientIC+
				"|"+recipientAdress+"|"+sensitiveone+"|"+sensitivetwo;
	}
}
