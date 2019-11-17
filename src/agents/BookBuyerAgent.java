package agents;

import jade.core.Agent;
import behaviours.RequestPerformer;
import gui.BookBuyerGui;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.ArrayList;
import java.util.List;
import services.Helpers;

public class BookBuyerAgent extends Agent {
    private String bookTitle;
    private AID[] sellerAgents;
    private int ticker_timer = 10000;
    private BookBuyerAgent this_agent = this;
    private BookBuyerGui gui;
    private Helpers helper = new Helpers().getHelp();
    private BookBuyerGui buyerAgent;
    
    
    protected void setup() {
        gui = getBookBuyerGui(this_agent);
        helper.setBuyerAgent(this_agent);
        gui.showGui();
    }
    
    public BookBuyerGui getBookBuyerGui(BookBuyerAgent agent){
        if (buyerAgent == null){
            buyerAgent = new BookBuyerGui(agent);
        }
        
        return buyerAgent;
    }
    
    public void buyBook(BookBuyerAgent myAgent,String book){
        if(book != null) {
            bookTitle = book;
            System.out.println("Trying to buy " + bookTitle);
                    
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);
                    
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Found the following seller agents:");
                        sellerAgents = new AID[result.length];
                        for(int i = 0; i < result.length; i++) {
                            sellerAgents[i] = result[i].getName();
                            System.out.println(sellerAgents[i].getName());
                        }
                        
                    }catch(FIPAException fe) {
                        fe.printStackTrace();
                    }
                    
                    myAgent.addBehaviour(new RequestPerformer(this_agent));
                    
        } else {
            System.out.println("No target book title specified");
            doDelete();
        }
    }
    
    protected void takeDown() {
        System.out.println("Buyer agent " + getAID().getName() + " terminating");
    }
    
    public AID[] getSellerAgents() {
        return sellerAgents;
    }
    
    public String getBookTitle() {
        return bookTitle;
    }
}
