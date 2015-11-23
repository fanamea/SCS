package agents;

import java.util.ArrayList;

import modules.Link;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import cern.jet.random.AbstractDistribution;
import artefacts.DemandData;
import artefacts.Order;
import artefacts.OrderReq;
import artefacts.ReturnOrder;
import artefacts.Shipment;
import demandPattern.DemandPattern;
import demandPattern.NormalDistribution;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;
import setups.Setup;

public class Customer extends Node{
	
	private Setup setup;
	private DemandPattern pattern;
	private DemandData demandData;
	private DescriptiveStatistics received;
	private boolean negativeOrders;
	private double sd;
	
	public Customer(Setup setup, DemandPattern pattern){
		super(setup, 1);
		this.setup = setup;
		this.pattern = pattern;
		this.demandData = new DemandData();
		this.received = new DescriptiveStatistics();
		this.negativeOrders = false;
		this.sd = 0.0;
	}
	
	public void initNode(){		
	}
	
	public void setSD(double sd){
		this.pattern = new NormalDistribution(100.0, sd);
		this.sd = sd;
	}
	
	public double getSD(){
		return this.sd;
	}
	
	public ArrayList<Double> getSampleData(int size){
		ArrayList<Double> data = new ArrayList<Double>();
		for(int i=0; i<size; i++){
			data.add(pattern.getNextDouble());
		}
		return data;
	}
	
	
	public void placeOrder(){
		////System.out.println("placeOrderCustomer");
		double size = pattern.getNextDouble();
		int tick = (int)RepastEssentials.GetTickCount();
		/*
		if(tick % 2 == 0){
			size = 110;
		}
		else{
			size = 90;
		}
		*/
		if(!this.negativeOrders){
			size = Math.max(0.0, size);
		}
		////System.out.println("Customer Order: " + size);
		ArrayList<Order> orderList = new ArrayList<Order>();
		Link link = upstrLinks.get(0);
		int currentTick = (int)RepastEssentials.GetTickCount();
		
		if(size>0){
			Order newOrder = new Order(link, currentTick, size);
			orderList.add(newOrder);
			upstrLinks.get(0).putOrders(orderList);
		}		
		else{
			ReturnOrder rOrder = new ReturnOrder(link, currentTick, size);
			rOrder.setReturnOrder(true);
			Shipment returnShipment = new Shipment(link, currentTick, rOrder.getSize(), link.genDuration(), rOrder);
			rOrder.addShipment(returnShipment);
			link.induceShipmentUp(returnShipment);
			rOrder.incrSent(returnShipment.getSize());
		}
		this.demandData.handDemandData((int)RepastEssentials.GetTickCount(), size);
	}
	
	public double getSampleOrder(){
		return this.pattern.getNextDouble();
	}
	
	public double getLastOrder(){
		return this.demandData.getDemandData((int)RepastEssentials.GetTickCount());
	}
	
	public double getVarianceOrders(){
		return this.demandData.getVariance();
	}
	
	public void efectShipment(Shipment shipment){		
	}
	
	public void setNegativeOrders(boolean b){
		this.negativeOrders = b;
	}
	
	@ScheduledMethod(start=1, interval=1, priority=10)
	public void receiveShipments(){
		ArrayList<Shipment> shipments = new ArrayList<Shipment>();
		shipments = this.upstrLinks.get(0).getArrivingShipmentsDown();
		for(Shipment shipment : shipments){
			this.received.addValue(shipment.getSize());
		}
	}
	
	public boolean isOrderShipable(Order order){
		return false;
	}
	
	public int getShipableAmount(Order order){
		return 0;
	}
	
	public double getSumOrders(){
		return this.demandData.getDemandStats().getSum();
	}
	
	public double getSumReceived(){
		return this.received.getSum();
	}
	
	public double getBacklog(){
		return getSumOrders()-getSumReceived();
	}
	
	public String getInformationString(){
		String string = "Customer " + this.Id + "\n";
		return string;
	}

	

}
