/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package parceldeliverytracker;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * @author weiju
 */
public class ViewDeliveryInfoPage extends javax.swing.JFrame {


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonVerify;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea resultTextArea;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchTrackingIDInput;

    /**
     * Creates new form ViewDeliveryInfoPage
     */
    public ViewDeliveryInfoPage() {
        initComponents();
        buttonVerify.setEnabled(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewDeliveryInfoPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewDeliveryInfoPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewDeliveryInfoPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewDeliveryInfoPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewDeliveryInfoPage().setVisible(true);
            }
        });
    }

    public void searchOrder() throws Exception {

        String searchTrackingID = searchTrackingIDInput.getText();

        if (searchTrackingID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the tracking ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Path filePath = Paths.get("insignificantData.txt");

        Stream<String> insignificantRecords = FileHandler.readLines(filePath);

        AtomicBoolean trackingFound = new AtomicBoolean(false);
        insignificantRecords.forEach(line -> {
            System.out.println("Existing insignificant: "+line);
            String[] fields = line.split("\\|");
            String trackingID = fields[0];
            if (trackingID.equals(searchTrackingID)) {
                fillInDetail(line);
                trackingFound.set(true);
                buttonVerify.setEnabled(true);
            }
        });

        if (!trackingFound.get()) {
            JOptionPane.showMessageDialog(null, "Tracking ID not found. Please try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void fillInDetail(String foundLine) {
        String[] fields = foundLine.split("\\|");

        // format each field to a specific width using String.format()
        String basicInfoTitle = String.format("%-22s", "\n Basic Delivery Information:") + "\n   ";
        String trackingId = String.format("%-24s", "\n\n 1. Tracking ID:  ") + String.format("%-25s", fields[0].trim());
        String courierNumber = String.format("%-22s", " 2. Courier Number:") + String.format("%-25s", fields[1].trim());
        String shipOutDate = String.format("%-22s", " 3. ShipOutDate:") + String.format("%-25s", fields[2].trim());
        String deliverDate = String.format("%-22s", " 4. Deliver Date:") + String.format("%-25s", fields[3].trim());
        String parcelWeight = String.format("%-22s", " 5. Parcel Weight:") + String.format("%-13s", fields[4].trim());

        String overviewText =
                basicInfoTitle +
                        trackingId +
                        "\n\n\n" +
                        courierNumber +
                        "\n\n\n" +
                        shipOutDate +
                        "\n\n\n" +
                        deliverDate +
                        "\n\n\n" +
                        parcelWeight;

        // set the font to a fixed width font
        resultTextArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        resultTextArea.setText(overviewText);

    }

    public void verifyDelivery(String inputRecipientIC) throws Exception {

        String searchTrackingID = searchTrackingIDInput.getText();

        if (searchTrackingID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please first search with your tracking ID before verifying", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DeliveryInfoClass foundDeliveryInfo = BlockReader.searchBlockChainTransactions(searchTrackingID, inputRecipientIC);

        if (foundDeliveryInfo != null) {
            MySignature sig = new MySignature();
            Path filePath = Paths.get("DigitalSignatureKeyStore/digitalSignature.txt");
            Stream<String> digitalSignatureRecords = FileHandler.readLines(filePath);

            AtomicBoolean isValid = new AtomicBoolean(false);

            digitalSignatureRecords.forEach(line -> {
                String[] fields = line.split("/anotherField/");
                String trackingID = fields[0];

                if (trackingID.equals(searchTrackingID)) {
                    try {
                        PublicKey publicKeyRead = convertStringToPublicKey(fields[4]);
                        //set isValid to true if match
                        isValid.set(sig.verify(fields[1], fields[2], publicKeyRead));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid public key",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(e);

                    }
                }
            });

            if (isValid.get()) {
                showConfidentialDialog(foundDeliveryInfo);

            } else {
                JOptionPane.showMessageDialog(null, "Verification failed with IC number",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Does not match any record from blockchain", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public PublicKey convertStringToPublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public void showConfidentialDialog(DeliveryInfoClass foundDeliveryInfo) {

        //if (foundDeliveryInfo = null)

        String[] columnNames = {"Field", "Information"};
        Object[][] data = {
                {"Tracking ID", foundDeliveryInfo.trackingID()},
                {"Order ID", foundDeliveryInfo.orderID()},
                {"Sender Name", foundDeliveryInfo.senderName()},
                {"Sender Phone", foundDeliveryInfo.senderPhone()},
                {"Sender Address", foundDeliveryInfo.senderAddress()},
                {"Recipient Name", foundDeliveryInfo.recipientName()},
                {"Recipient IC", foundDeliveryInfo.recipientIC()},
                {"Recipient Phone", foundDeliveryInfo.recipientPhone()},
                {"Recipient Address", foundDeliveryInfo.recipientAddress()},
                {"Parcel Content", foundDeliveryInfo.parcelContent()}
        };
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200)); // set preferred size

        JLabel title = new JLabel("Here is the rest of the delivery details");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // add spacing before title
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 40))); // add spacing after title
        panel.add(scrollPane);

        JOptionPane.showMessageDialog(null, panel, "Verified Successful", JOptionPane.PLAIN_MESSAGE);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        resultTextArea = new javax.swing.JTextArea();
        jLabel1 = new JLabel();
        searchTrackingIDInput = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        buttonVerify = new javax.swing.JButton();
        jLabel2 = new JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Parcel Delivery Tracker");
        setBackground(new java.awt.Color(255, 255, 255));

        resultTextArea.setColumns(20);
        resultTextArea.setRows(5);
        jScrollPane1.setViewportView(resultTextArea);

        jLabel1.setText("Tracking ID: ");

        searchTrackingIDInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTrackingIDInputActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        buttonVerify.setText("Verify");
        buttonVerify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonVerifyActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("TRACK YOUR PARCEL");

        jMenu1.setText("New Delivery Record");
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu1MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Track Your Parcel");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(buttonVerify, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(207, 207, 207))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(59, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(38, 38, 38)
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(115, 115, 115))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(searchTrackingIDInput, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(53, 53, 53))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(23, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(searchTrackingIDInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchButton))
                                .addGap(27, 27, 27)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buttonVerify, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
        );

        setSize(new java.awt.Dimension(531, 550));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void searchTrackingIDInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTrackingIDInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTrackingIDInputActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        try {
            searchOrder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void buttonVerifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonVerifyActionPerformed
        String input = JOptionPane.showInputDialog(null, "Enter your IC number :");

        // Check if the input is valid
        if (input != null && !input.isEmpty()) {
            try {
                verifyDelivery(input);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }//GEN-LAST:event_buttonVerifyActionPerformed

    private void jMenu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu1MouseClicked
        CreateNewDeliveryPage a = new CreateNewDeliveryPage();
        a.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenu1MouseClicked
    // End of variables declaration//GEN-END:variables
}
