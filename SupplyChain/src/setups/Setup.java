package setups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import modules.Link;
import demandPattern.Constant;
import demandPattern.NormalDistribution;
import demandPattern.RandomWalk;
import agents.Business;
import agents.Customer;
import agents.InformationSharing;
import agents.Manufacturer;
import agents.MaterialSource;
import agents.Node;
import agents.Retailer;
import artefacts.TierComparator;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.random.RandomHelper;

public abstract class Setup {
	
	protected ArrayList<MaterialSource> sources;
	protected ArrayList<Business> businesses;
	protected ArrayList<Manufacturer> manufacturers;
	protected ArrayList<Retailer> retailers;
	protected ArrayList<Customer> customers;
	protected ArrayList<Link> links;
	protected ArrayList<ArrayList<Integer>> tiers;
	protected InformationSharing informationSharing;
	protected boolean sharing;
	protected double sdLeadTime;
	protected double meanLeadTime;
	
	public Setup(){
		sources = new ArrayList<MaterialSource>();
		manufacturers = new ArrayList<Manufacturer>();
		retailers = new ArrayList<Retailer>();
		businesses = new ArrayList<Business>();
		customers = new ArrayList<Customer>();
		links = new ArrayList<Link>();
		this.sharing = false;
		this.sdLeadTime = 0;
		this.meanLeadTime = 4;
	}
		
	public void init(){
		
		this.informationSharing = new InformationSharing(this);
		
		for(MaterialSource source : sources){
			source.initNode();
		}
		for(Retailer retailer : retailers){
			retailer.initNode();
		}
		for(Manufacturer m : manufacturers){
			m.initNode();
		}
		
		for(Customer customer : customers){
			customer.initNode();
		}
		
		businesses.addAll(retailers);
		businesses.addAll(manufacturers);
		
		this.informationSharing.init();	
		
		Collections.sort(businesses, new TierComparator());
		
	}
	
	@ScheduledMethod(start=1, interval=1, priority=12)
	public void prepareTick(){		
		for(Business biz : this.businesses){
			biz.prepareTick();
		}		
	}
	
	@ScheduledMethod(start=1, interval=0, priority=11)
	public void planFirstPeriods(){		
		for(Business biz : this.businesses){
			biz.planFirstPeriods();
		}	
	}
	
	@ScheduledMethod(start=1, interval=1, priority=10)
	public void activateAgents(){
		for(Customer customer : this.customers){
			customer.placeOrder();
		}
		
		
		/*
		for(Business biz : this.businesses){		
			biz.plan();
			biz.placeOrders();
		}
		*/
		
		
		
		
		for(Business biz : this.businesses){
			biz.receiveShipments();
			biz.produce();
			biz.fetchOrders();
			biz.dispatchShipments();
			biz.plan();
			biz.placeOrders();
			biz.collectData();
			//biz.collectRetailerData();
		}
		
		for(MaterialSource source : this.sources){
			source.shipOrders();
		}
	}
	
	
	@ScheduledMethod(start=5210, interval=0, priority=1)
	public void printVariance(){
		for(Business biz : businesses){
			System.out.println("Tier: " + biz.getTier() + ", SD: " + Math.sqrt(biz.getVarianceOrders5200()) + ", VarOrder: " + biz.getVarianceOrders5200() + ", MeanOrder: " + biz.getMeanOrder());
		}
	}
	
	public void setSDDemand(double sd){
		this.customers.get(0).setSD(sd);
	}
	
	public double getSDDemand(){
		return this.customers.get(0).getSD();
	}
	
	public void setSharing(boolean b){
		for(Business biz : businesses){
			biz.setInformationSharing(b);
		}
		this.sharing = b;
	}
	
	public boolean getSharing(){
		return this.sharing;
	}
	
	public void setMeanLeadTime(double mean){
		for(Link link : links){
				link.setDistrDuration(RandomHelper.createUniform(mean, mean));		
		}
		this.meanLeadTime = mean;
	}
	
	public void setSDLeadTime(double sd){
		double mean = this.meanLeadTime;
		for(Link link : links){
			if(sd==0){
				link.setDistrDuration(RandomHelper.createUniform(mean, mean));
			}
			else{
				
				double var = Math.pow(sd, 2);
				double alpha = mean*mean/var;
				double lambda = 1/(var/mean);
				link.setDistrDuration(RandomHelper.createGamma(alpha, lambda));
			}			
		}
		this.sdLeadTime = sd;
	}
	
	public double getMeanLeadTime(){
		return this.meanLeadTime;
	}
	
	public double getSDLeadTime(){
		return this.sdLeadTime;
	}
	
	public ArrayList<Business> getBusinesses(){
		return this.businesses;
	}
	
	public ArrayList<Customer> getCustomers(){
		return this.customers;
	}
	
	public ArrayList<Manufacturer> getManufacturers(){
		return this.manufacturers;
	}
	
	public ArrayList<Retailer> getRetailers(){
		return this.retailers;
	}
	
	public ArrayList<MaterialSource> getMaterialSources(){
		return this.sources;
	}
	
	public ArrayList<Link> getLinks(){
		return this.links;
	}
	
	public InformationSharing getInformationSharing(){
		return this.informationSharing;
	}
	
	public void print(){
		
	}
	
	//@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void printInformation(){
		String string = "Date: " + (int)RepastEssentials.GetTickCount() + "\n";
		for(Manufacturer man : manufacturers){
			string += man.getInformationString();
		}
		for(Retailer ret: retailers){
			string += ret.getInformationString();
		}
		string += "\n";
		string += "MATERIAL SOURCE: " + sources.get(0).getInformationString();
		string += "\n \n";
		System.out.print(string);
	}

}
