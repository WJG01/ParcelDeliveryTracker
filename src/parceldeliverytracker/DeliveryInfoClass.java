package parceldeliverytracker;

//binary form

import java.io.Serializable;

public record DeliveryInfoClass(String trackingID, String orderID, String senderName, String senderPhone,
                                String senderAddress,
                                String recipientName, String recipientIC, String recipientPhone,
                                String recipientAddress,String parcelContent) implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3891440441905013644L;

    @Override
    public String trackingID() {
        return trackingID;
    }

    @Override
    public String orderID() {
        return orderID;
    }

    @Override
    public String parcelContent() {
        return parcelContent;
    }

    @Override
    public String senderName() {
        return senderName;
    }

    @Override
    public String senderPhone() {
        return senderPhone;
    }

    @Override
    public String senderAddress() {
        return senderAddress;
    }

    @Override
    public String recipientName() {
        return recipientName;
    }

    @Override
    public String recipientIC() {
        return recipientIC;
    }

    @Override
    public String recipientPhone() {
        return recipientPhone;
    }

    @Override
    public String recipientAddress() {
        return recipientAddress;
    }

    @Override
    public String toString() {
        return trackingID + "|" + orderID + "|" + senderName + "|" + senderPhone + "|" + senderAddress + "|" + recipientName + "|" + recipientIC + "|" +
				recipientPhone + "|" + recipientAddress+ "|" + parcelContent;

    }
}
