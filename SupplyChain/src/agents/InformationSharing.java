package agents;

import java.util.ArrayList;
import java.util.HashMap;

import setups.Setup;
import artefacts.DemandData;

public class InformationSharing {
	
	private Setup setup;
	private Customer customer;
	private HashMap<Business, DemandData> demandData;
	private DemandData customerDemandData;
	
	
	public InformationSharing(Setup setup){
		this.setup = setup;
		this.customer = setup.getCustomers().get(0);
	}
	
	public void init(){
		this.demandData = new HashMap<Business, DemandData>();
		this.customer = setup.getCustomers().get(0);
		
		for(Business b : setup.getBusinesses()){
			demandData.put(b, b.getDemandData());
			if(b.getTier()==2){
				this.customerDemandData = b.getDemandData();
			}
		}
	}
	
	public DemandData getCustomerDemandData(){
		return this.customerDemandData;
	}
}
