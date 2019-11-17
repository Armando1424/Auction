/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package services;

import agents.BookBuyerAgent;
import agents.BookSellerAgent;
import gui.BookBuyerGui;
import gui.BookSellerGui;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author armando
 */
public class Helpers {
    private List<BookBuyerAgent> buyerAgents = new ArrayList<BookBuyerAgent>();
    private List<BookSellerAgent> sellerAgents = new ArrayList<BookSellerAgent>();
    private static Helpers helper;
    
    public Helpers getHelp(){
        if (helper == null){
            helper = new Helpers();
        }
        
        return helper;
    }
    
    public void setBuyerAgent(BookBuyerAgent agent){
        buyerAgents.add(agent);
    }
    
    public List<BookBuyerAgent> getBuyerAgents(){
        return buyerAgents;
    }
  
    public void setSellerAgent(BookSellerAgent agent){
        sellerAgents.add(agent);
    }
    
    public List<BookSellerAgent> getSellerAgents(){
        return sellerAgents;
    }
}
