package InventoryPolicies;

import agents.Business;
import modules.Inventory;
import modules.PlanningMethods;

public abstract class InventoryPolicy{
	
	Business biz;
	PlanningMethods planningTechniques;
	Inventory inventory;
	
	protected int period;
	protected int lastOrderDate;
	protected double outLevel;
	protected double reorderPoint;
	protected double quantity;
	
	
	public InventoryPolicy(){
		this.planningTechniques = new PlanningMethods();
	}
	
	public void setInventory(Inventory inventory){
		this.inventory = inventory;
	}
	
	public void setBiz(Business biz){
		this.biz = biz;
	}
	
	public double getMeanDemand(int period){
		return biz.getInformationModule().getMeanDemand(period);		
	}
	
	public double getMeanSquareDemand(int period){
		return biz.getInformationModule().getMeanSquareDemand(period);		
	}
	
	public double getMeanDemand(){
		return biz.getInformationModule().getMeanDemand();
	}
	
	public double getMeanLeadTime(){
		return biz.getInformationModule().getMeanLeadTime(inventory.getMaterial());
	}
	
	public double getMeanLeadTime(int period){
		return biz.getInformationModule().getMeanLeadTime(inventory.getMaterial(), period);
	}
	
	public double getSDDemand(int period){
		return biz.getInformationModule().getSDDemand(period);
	}
	
	public double getSDDemand(){
		return biz.getInformationModule().getSDDemand();
	}
	
	public double getSDLeadTime(){
		return biz.getInformationModule().getSDLeadTimeAll();
	}
	
	public double getSDLeadTime(int period){
		return biz.getInformationModule().getSDLeadTime(inventory.getMaterial(), period);
	}
	
	public double getOrderFixCost(){
		return biz.getOrderPlanModule().getOrderFixCost(inventory.getMaterial());
	}
	
	/*
	 * -------------------Analysis
	 */
	
	
	public double getOUTLevel(){
		return this.outLevel;
	}
	
	public double getQ(){
		return this.quantity;
	}
	
	public double getPeriod(){
		return this.period;
	}

	
	public abstract void recalcParams();
	public abstract double getOrder(int currentTick, double inventoryPosition);
	public abstract String getParameterString();

}
