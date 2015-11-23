package artefacts;

import java.util.ArrayList;

import modules.Link;

public class Order {
	
	protected static int count;
	
	protected int Id;
	protected Link link;
	protected int date;
	protected double size;
	protected double sent;
	protected double arrived;
	protected OrderReq orderReq;
	protected ArrayList<Shipment> shipments;
	protected boolean rOrder;
	
	public Order(Link link, int d, double s, OrderReq orderReq){
		this.Id = count++;
		this.link = link;
		this.date = d;
		this.size = s;
		this.sent = 0;
		this.arrived = 0;
		this.orderReq = orderReq;
		this.shipments = new ArrayList<Shipment>();
		rOrder = false;
	}
	
	public Order(Link link, int d, double s){
		this.Id = count++;
		this.link = link;
		this.date = d;
		this.size = s;
		this.sent = 0;
		this.arrived = 0;
		this.shipments = new ArrayList<Shipment>();
		rOrder = false;
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
		return Math.abs(size)<=sent;				
	}
	
	public boolean hasArrived(){
		return Math.abs(size)<=arrived;
	}
	
	public double getShortageSent(){
		return Math.abs(size)-sent;
	}
	
	public double getShortageArrived(){
		return Math.abs(size)-arrived;
	}
	
	public void addShipment(Shipment shipment){
		this.shipments.add(shipment);
	}
	
	public ArrayList<Shipment> getShipments(){
		return this.shipments;
	}
	
	public OrderReq getOrderReq(){
		return this.orderReq;
	}
	
	public void setReturnOrder(boolean b){
		this.rOrder = b;
	}
	
	public boolean isReturnOrder(){
		return rOrder;
	}
	
	public String toString(){
		return "Order: ID-" + this.Id + ", Date-" + this.date + ", Size-" + this.size + ", OrderReq-" + orderReq;
	}

}
