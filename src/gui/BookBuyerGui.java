package gui;

import agents.BookBuyerAgent;
import agents.BookSellerAgent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import services.Helpers;

public class BookBuyerGui extends JFrame {
    private BookBuyerAgent myAgent;
    
    private JTextField bookField, initialPriceField, minimalPriceField, maxPriceField;
    private JTable table;
    private DefaultTableModel dtm;
    private Helpers helper = new Helpers().getHelp();
    private int selectedRow = -1;
    private JTextArea textArea;
    
    public BookBuyerGui(BookBuyerAgent a) {
        super(a.getLocalName());
        
        myAgent = a;
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        
        p.add(new JLabel("Books in auction"));
        
        String[] columnNames = {"Book","Current price","Bid"};
        dtm= new DefaultTableModel(null,columnNames);
        table = new JTable(dtm);
        /*Object[] newRow={"java","price","bid"};
        dtm.addRow(newRow);*/
        table.setPreferredScrollableViewportSize(new Dimension(600, 140));
        JScrollPane scrollPane = new JScrollPane(table);
        p.add(scrollPane);
        
        JPanel littleP = new JPanel();
        littleP.add(new JLabel("Max price of bid:"));
        maxPriceField = new JTextField(20);
        littleP.add(maxPriceField);
        JButton biddingButton = new JButton("Bidding");
        biddingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    //put your code here
                    selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        List<BookSellerAgent> sellerAgents = helper.getSellerAgents();
                        for(BookSellerAgent sellerAgent: sellerAgents){
                            BookSellerGui sellerGui = sellerAgent.getBookSellerGui(sellerAgent);
                            sellerGui.setBestBidding(myAgent, maxPriceField.getText(), selectedRow, 3);
                        }
                        setBidding(maxPriceField.getText(), selectedRow, 2);
                        maxPriceField.setText("");
                    }
                    else{
                        JOptionPane.showMessageDialog(BookBuyerGui.this, "There are any row selected","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    
                }catch(Exception e) {
                    JOptionPane.showMessageDialog(BookBuyerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        littleP.add(biddingButton);
        p.add(littleP);
        
        p.add(new JLabel("Books purchased"));
        textArea = new JTextArea(5, 10);
        textArea.setEditable(false);
        JScrollPane scrollPaneTxt = new JScrollPane(textArea);
        p.add(scrollPaneTxt);
        
        getContentPane().add(p, BorderLayout.CENTER);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        });
        
        setResizable(false);
    }
    
    public void setData(List<Object[]> rows){
        for (Object[] newRow: rows)
        {
            dtm.addRow(newRow);
        }
        
    }
    
    public void setBidding(String val, int row, int col)
    {
        table.setValueAt(val, row, col);
    }
    
    public void setTxtToJTxtArea(String msg){
        textArea.append(msg);
    }
    
    public void showGui() {
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)screenSize.getWidth() / 2;
        int centerY = (int)screenSize.getHeight() / 2;
        
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
        super.setVisible(true);
    }
}
