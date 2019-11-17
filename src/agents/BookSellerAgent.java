package agents;

import java.util.Hashtable;

import behaviours.OfferRequestServer;
import behaviours.PurchaseOrderServer;
import gui.BookSellerGui;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import services.Helpers;

public class BookSellerAgent extends Agent{
    
    private Hashtable catalogue;
    private BookSellerGui gui;
    private Helpers helper = new Helpers().getHelp();
    private static BookSellerGui sellerAgent;
    
    protected void setup() {
        catalogue = new Hashtable();
        
        gui = getBookSellerGui(this);
        helper.setSellerAgent(this);
        gui.showGui();
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-selling");
        sd.setName("book-trading");
        dfd.addServices(sd);
        
        try {
            DFService.register(this, dfd);
        }catch(FIPAException fe) {
            fe.printStackTrace();
        }
        
        addBehaviour(new OfferRequestServer(this));
        
        addBehaviour(new PurchaseOrderServer(this));
    }
    
    public BookSellerGui getBookSellerGui(BookSellerAgent agent){
        if (sellerAgent == null){
            sellerAgent = new BookSellerGui(agent);
        }
        
        return sellerAgent;
    }
    
    protected void takeDown() {
        try {
            DFService.deregister(this);
        }catch(FIPAException fe) {
            fe.printStackTrace();
        }
        
        gui.dispose();
        
        System.out.println("Seller agent " + getAID().getName() + "terminating");
    }
    
    public void updateCatalogue(final String title, final int price) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                catalogue.put(title, price);
                System.out.println(title + " inserted with a price of " + price);
            }
        });
    }
    
    public Hashtable getCatalogue() {
        return catalogue;
    }
}
