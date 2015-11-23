package artefacts;

import java.util.ArrayList;

import modules.Link;

public class ReturnOrder extends Order{
	
	
	public ReturnOrder(Link link, int d, double s){
		super(link, d, s);
		rOrder = true;
	}
	
	public Integer getDate(){
		return this.date;
	}
	
	public Link getLink(){
		return this.link;
	}
	
	public double getSize(){
		return this.size;
	}
	
	public void incrSent(double i){
		sent += i;
	}
	
	public void incrArrived(double i){
		arrived += i;
	}
	
	public boolean isSent(){
		return size<=sent;
	}
	
	public boolean hasArrived(){
		return size<=arrived;
	}
	
	public double getShortageSent(){
		return size-sent;
	}
	
	public double getShortageArrived(){
		return size-arrived;
	}
	
	public void addShipment(Shipment shipment){
		this.shipments.add(shipment);
	}
	
	public ArrayList<Shipment> getShipments(){
		return this.shipments;
	}
	
	public String toString(){
		return "ReturnReq: ID-" + this.Id + ", Date-" + this.date + ", Size-" + this.size;
	}

}
