package modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import repast.simphony.essentials.RepastEssentials;
import agents.Business;
import agents.InformationSharing;
import artefacts.DemandData;
import artefacts.LeadTimeData;
import artefacts.Material;

public class InformationModule {
	
	private Business biz;
	private InformationSharing sharing;
	private DemandData orderData;
	private DemandData internDemandData;
	private DemandData externDemandData;
	private DemandData combinedDemandData;
	
	private boolean informationSharing;
	
	private HashMap<Link, LeadTimeData> leadTimeData;
	private DescriptiveStatistics allLeadTimeData;
	private TreeMap<Integer, Double> fcIntern;
	private TreeMap<Integer, Double> fcExtern;
	private TreeMap<Integer, Double> fcCombined;
	private TreeMap<Integer, Double> histFcCombined;
	
	private double holdingCostProduct;
	private double holdingCostResources;
	private double productionCost;
	private double orderCost;
	private double sumBacklog;
	private double arrivingProduction;
	private double startedProduction;
	private double arrivingShipments;
	private double planningTimeShipments;
	private double ordered;
	private double meanDemand;
	private HashMap<Integer, Double> orderPlan;
	private HashMap<Integer, Double> adjustedDueListHist;
	
	
	
	public InformationModule(Business biz){
		this.biz = biz;
		this.sharing = biz.getSetup().getInformationSharing();
		this.orderData = new DemandData();
		this.internDemandData = new DemandData();
		this.combinedDemandData = this.internDemandData;
		this.informationSharing = false;
		this.fcIntern = new TreeMap<Integer, Double>();
		this.fcExtern = new TreeMap<Integer, Double>();
		this.fcCombined = new TreeMap<Integer, Double>();
		this.histFcCombined = new TreeMap<Integer, Double>();
		
		this.orderPlan = new HashMap<Integer, Double>();
		this.adjustedDueListHist = new HashMap<Integer, Double>();		
		this.allLeadTimeData = new DescriptiveStatistics();
		this.leadTimeData = new HashMap<Link, LeadTimeData>();
		for(Link link : biz.getUpstrLinks()){
			leadTimeData.put(link, new LeadTimeData());
		}
	}
	
	public void forecast(int start, int end){
		biz.getForecastModule().setDemandData(internDemandData);
		fcIntern = biz.getForecastModule().getForecast(start, end);
		
		System.out.println("internDemandData: " + internDemandData.getDataMap());
		System.out.println("informationSharing: " + informationSharing);
		
		
		if(informationSharing){
			biz.getForecastModule().setDemandData(externDemandData);
			this.fcExtern = biz.getForecastModule().getForecast(start, end);
			//TrustModul
			this.fcCombined = fcExtern;
			
		}
		else{			
			this.fcCombined = fcIntern;
			this.histFcCombined.putAll(fcIntern);
		}				
	}
	
	
	
	
	
	
	public double getOrdered(){
		return this.ordered;
	}
	
	public void setOrdered(double d){
		this.ordered = d;
	}
	
	public double getMeanOrders(){
		return this.orderData.getMean();
	}
	
	public double getSDOrders(){
		return this.orderData.getStandardDeviation();
	}
	
	public void putLeadTimeData(Link link, double d){
		this.leadTimeData.get(link).handData(d);
		this.allLeadTimeData.addValue(d);		
	}
	
	public void putLeadTimeData(Link link, int tick, double d){
		this.leadTimeData.get(link).handData(tick, d);
		this.allLeadTimeData.addValue(d);		
	}
	
	public double getMeanLeadTime(Link link){
		return leadTimeData.get(link).getMean();
	}
	
	public double getMeanLeadTime(Material material, int period){
		Link link = biz.getOrderPlanModule().getLink(material);
		return leadTimeData.get(link).getMean(period);
	}
	
	public double getSDLeadTime(Link link){
		return leadTimeData.get(link).getStandardDeviation();
	}
	
	public double getSDLeadTimeAll(){
		return this.allLeadTimeData.getStandardDeviation();
	}
	
	public double getSDLeadTime(Material material){
		Link link = biz.getOrderPlanModule().getLink(material);
		return leadTimeData.get(link).getStandardDeviation();
	}
	
	public double getSDLeadTime(Material material, int period){
		Link link = biz.getOrderPlanModule().getLink(material);
		return leadTimeData.get(link).getStandardDeviation(period);
	}
	
	public double getMeanLeadTime(Material material){
		Link link = biz.getOrderPlanModule().getLink(material);
		return getMeanLeadTime(link);		
	}
	
	public void putAdjustedDueList(TreeMap<Integer, Double> list){
		this.adjustedDueListHist.putAll(list);
	}
	
	public double getAdjustedDueListEntry(int tick){
		return this.adjustedDueListHist.get(tick);
	}
	
	public void putOrderData(int tick, double order){
		this.orderData.handDemandData(tick, order);
	}
	
	public void setPlanningLeadTimeShipments(double d){
		this.planningTimeShipments = d;
	}
	
	public double getMeanLeadTimeAll(){
		return this.allLeadTimeData.getMean();
	}
	
	public double getPlanningTimeShipments(){
		return this.planningTimeShipments;
	}
	
	public double getBacklogProductionEnd(){
		return biz.getProductionOpsModule().getBacklogEnd();
	}
	
	public double getBacklogProductionStart(){
		return biz.getProductionOpsModule().getBacklogStart();
	}
	
	public void setExtDemandData(DemandData demandData){
		this.externDemandData = demandData;
	}
	
	public void setIntDemandData(DemandData demandData){
		this.internDemandData = demandData;
	}
	
	public void addExtDemandData(int tick, double demand){
		this.externDemandData.handDemandData(tick, demand);
	}
	
	public void addIntDemandData(int tick, double demand){
		this.internDemandData.handDemandData(tick, demand);
	}
	
	public double getSDDemand(){
		return this.combinedDemandData.getStandardDeviation();
	}
	
	public double getSDDemand(int period){
		return this.combinedDemandData.getStandardDeviation(period);
	}
	
	public double getVarianceOrders(){
		return this.orderData.getVariance();
	}
	
	public double getVarianceOrders(int period){
		return this.orderData.getVariance(period);
	}
	
	public double getVarianceOrders(int start, int end){
		return this.orderData.getVariance(start, end);
	}
	
	public double getMeanDemand(){
		return this.combinedDemandData.getMean();
	}
	
	public double getMeanDemand(int period){
		return this.combinedDemandData.getMean(period);
	}
	
	public double getMeanSquareDemand(int period){
		return this.combinedDemandData.getSquareMean(period);
	}
	
	
	public double getVarianceCustomerOrders(){
		return this.externDemandData.getVariance();
	}
	
	public void putOrder(int tick, double d){
		if(orderPlan.get(tick)==null){
			orderPlan.put(tick, d);
		}
		else{
			orderPlan.put(tick, orderPlan.get(tick)+d);
		}
	}
	
	public double getPlannedOrder(){
		int currentTick = (int)RepastEssentials.GetTickCount();
		if(this.orderPlan.get(currentTick)==null){
			return 0.0;
		}
		else{
			return this.orderPlan.get((int)RepastEssentials.GetTickCount());
		}
	}
	
	public void setInformationSharing(boolean b){
		this.informationSharing = b;
		if(b){
			if(this.sharing==null){System.out.println("sharing = null");}
			this.externDemandData = this.sharing.getCustomerDemandData();
			this.combinedDemandData = this.externDemandData;
		}
		else{
			this.combinedDemandData = this.internDemandData;
		}
	}
	
	public double getPlannedProduction(){
		return biz.getProductionPlanModule().getPlannedProduction((int)RepastEssentials.GetTickCount());
	}
	
	public double getOrderAmountIn(){
		return internDemandData.getDemandData((int)RepastEssentials.GetTickCount());
	}
	
	public double getOrderAmountOut(){
		return orderData.getDemandData((int)RepastEssentials.GetTickCount());
	}
	
	public DemandData getInternDemandData(){
		return this.internDemandData;
	}
	
	public DemandData getExternDemandData(){
		return this.externDemandData;
	}
	
	public void setFcIntern(TreeMap<Integer, Double> fc){
		this.fcIntern = fc;
	}
	
	public void setFcExtern(TreeMap<Integer, Double> fc){
		this.fcExtern = fc;
	}
	
	public TreeMap<Integer, Double> getLastForecast(){
		return this.fcCombined;
	}
	
	public void addHoldingCostProduct(double cost){
		this.holdingCostProduct += cost;
	}
	
	public void addHoldingCostResources(double cost){
		this.holdingCostResources += cost;
	}
	
	public void addProductionCost(double cost){
		this.productionCost += cost;
	}
	
	public void addOrderCost(double cost){
		this.orderCost += cost;
	}
	
	public void addBacklog(double backlog){
		this.sumBacklog += backlog;
	}
	
	public void setArrivingProduction(double d){
		this.arrivingProduction = d;
	}
	
	public void setArrivingShipments(double d){
		this.arrivingShipments = d;
	}
	
	public void setStartedProduction(double d){
		this.startedProduction = d;
	}
	
	public double getStartedProduction(){
		return this.startedProduction;
	}
	
	public double getArrivingProduction(){
		return this.arrivingProduction;
	}
	
	public double getArrivingShipments(){
		return this.arrivingShipments;
	}
	
	public double getHoldingCostProduct(){
		return this.holdingCostProduct;
	}
	
	public double getHoldingCostResources(){
		return this.holdingCostResources;
	}
	
	public double getProductionCost(){
		return this.productionCost;
	}
	
	public double getOrderCost(){
		return this.orderCost;
	}
	
	public double getSumBacklog(){
		return this.sumBacklog;
	}
	
	public double getMeanBacklog(){
		return this.sumBacklog/RepastEssentials.GetTickCount();
	}
	
	public void setMeanDemandPol(double mean){
		this.meanDemand = mean;
	}
	
	public double getMeanDemandPol(){
		return this.meanDemand;
	}
		
	public double getSumFC(int start, int end){
		double sum = 0;
		for(Double d : this.histFcCombined.subMap(start, true, end, true).values()){
			sum+=d;
		}
		return sum;
	}
	
	

}
