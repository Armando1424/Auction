package gui;

import agents.BookBuyerAgent;
import java.awt.BorderLayout;
import java.awt.Dimension;
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

import agents.BookSellerAgent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import services.Helpers;

public class BookSellerGui extends JFrame {
    private BookSellerAgent myAgent;
    
    private JTextField bookField, initialPriceField, minimalPriceField;
    private boolean ready = false;
    private List<Object[]> myRows = new ArrayList<Object[]>();
    private JButton finishButton,startButton;
    private Helpers helper = new Helpers().getHelp();
    private JTable table;
    private DefaultTableModel dtm;
    private HashMap<Integer, BookBuyerAgent> goodBuyers = new HashMap<Integer, BookBuyerAgent>();
    private AID[] sellerAgents;
    private JTextArea textArea;
    
    public BookSellerGui(BookSellerAgent a) {
        super(a.getLocalName());
        
        myAgent = a;
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        
        JPanel littleP = new JPanel();
        littleP.setLayout(new FlowLayout());
        startButton = new JButton("Start Auction");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    //put your code here
                    
                    if (ready) {
                        finishButton.setEnabled(true);
                        startButton.setEnabled(false);
                        
                        List<BookBuyerAgent> myAgents = helper.getBuyerAgents();
                        for(BookBuyerAgent buyer: myAgents){
                            BookBuyerGui buyerGui = buyer.getBookBuyerGui(buyer);
                            buyerGui.setData(myRows);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(BookSellerGui.this, "Please type a Book","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    
                    
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        finishButton = new JButton("Finish Auction");
        finishButton.setEnabled(false);
        startButton.setEnabled(true);
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    //put your code here
                    for (int i = 0; i <  table.getRowCount(); i++) {
                        myAgent.updateCatalogue(table.getValueAt(i, 0).toString(), Integer.parseInt(table.getValueAt(i, 3).toString()));
                    }
                    
                    for (int i = 0; i < table.getRowCount(); i++) {
                        BookBuyerAgent goodBuyer = goodBuyers.get(i);
                        goodBuyer.buyBook(goodBuyer, table.getValueAt(i, 0).toString());
                        table.setValueAt(goodBuyers.get(i).getName().toString(), i, 4);
                    }
                    
                    Thread.sleep(10000);
                    for(BookBuyerAgent buyerAgent: helper.getBuyerAgents()){
                        buyerAgent.doDelete();
                    }
                    
                }catch(Exception e) {
                    JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        littleP.add(startButton);
        littleP.add(finishButton);
        p.add(littleP);
        
        String[] columnNames = {"Book","Minimal price","Initial price","Current price","Buyer"};
        dtm= new DefaultTableModel(null,columnNames);
        table = new JTable(dtm);
        
        table.setPreferredScrollableViewportSize(new Dimension(700, 150));
        JScrollPane scrollPane = new JScrollPane(table);
        p.add(scrollPane);
        
        p.add(new JLabel("Book:"));
        bookField = new JTextField();
        p.add(bookField);
        p.add(new JLabel("Initial auction price:"));
        initialPriceField = new JTextField();
        p.add(initialPriceField);
        p.add(new JLabel("Minimal price for sell:"));
        minimalPriceField = new JTextField();
        p.add(minimalPriceField);
        
        JButton addButton = new JButton("Add to auction");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    //put your code here
                    String book = bookField.getText().trim();
                    String minimalPrice = minimalPriceField.getText().trim();
                    String initialPrice = initialPriceField.getText().trim();
                    String currentPrice = initialPrice;
                    String buyer = "nobody";
                    
                    Object[] newRow={book,minimalPrice,initialPrice,currentPrice,buyer};
                    dtm.addRow(newRow);
                    
                    Object[] myRow = {book,currentPrice,"No bidding yet"};
                    myRows.add(myRow);
                    
                    bookField.setText("");
                    minimalPriceField.setText("");
                    initialPriceField.setText("");
                    ready = true;
                    
                }catch(Exception e) {
                    JOptionPane.showMessageDialog(BookSellerGui.this, "Invalid values","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        p.add(addButton);
        
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
    
    public void setBestBidding(BookBuyerAgent goodBuyer, String val, int row, int col){
        int currentPrice = Integer.parseInt(table.getValueAt(row, col).toString());
        if (currentPrice < Integer.parseInt(val)) {
            //agrgar un diccionario con el index de los libros y el goodBayer
            goodBuyers.put(row, goodBuyer);
            table.setValueAt(val, row, col);
            List<BookBuyerAgent> myAgents = helper.getBuyerAgents();
            for(BookBuyerAgent buyer: myAgents){
                BookBuyerGui buyerGui = buyer.getBookBuyerGui(buyer);
                buyerGui.setBidding(val, row, 1);
            }
        }
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
