package artefacts;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Observation;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import repast.simphony.essentials.RepastEssentials;

public class LeadTimeData implements Iterable<Integer>{
	
	private TreeMap<Integer, DescriptiveStatistics> dataMap;
	private DataSet demandDataSet;
	private DescriptiveStatistics demandStats;
	
	public LeadTimeData(){
		
		this.dataMap = new TreeMap<Integer, DescriptiveStatistics>();
		this.demandDataSet = new DataSet();
		this.demandStats = new DescriptiveStatistics();
		
	}
	
	public double getStandardDeviation(){
		return demandStats.getStandardDeviation();
	}
	
	public double getStandardDeviation(int period){
		int tick = (int)RepastEssentials.GetTickCount();
		DescriptiveStatistics substats = getSubStats(tick-period+1, tick);
		Double sd = substats.getStandardDeviation();
		//System.out.println("sd: " + sd);
		if(Double.isNaN(sd) || sd==0){
			//System.out.println("NOT A NUMBER");
			sd = demandStats.getStandardDeviation();
		}		
		//System.out.println("sd: " + sd + ", Werte: " + substats.getN() + ", Werte: " + demandStats.getN() + ", period: " + + period);
		return sd;
	}
	
	public double getMean(){
		return demandStats.getMean();
	}
	
	public double getMean(int period){
		int tick = (int)RepastEssentials.GetTickCount();
		DescriptiveStatistics substats = getSubStats(tick-period+1, tick);
		double mean = substats.getMean();
		if(substats.getN()==0){
			mean = demandStats.getMean();
		}		
		//System.out.println("Mean: " + mean + ", Werte: " + substats.getN() + ", period: " + + period);
		return mean;
	}
	
	public double getVariance(){
		return demandStats.getVariance();
	}
	
	public double getVariance(int period){
		int tick = (int)RepastEssentials.GetTickCount();
		DescriptiveStatistics substats = getSubStats(tick-period+1, tick);
		double var = substats.getVariance();
		if(Double.isNaN(var) | var==0){
			var = demandStats.getVariance();
		}		
		return var;
	}
	
	public double getVariance(int start, int end){
		return getSubStats(start, end).getVariance();
	}
	
	public void handData(int tick, double demand){
		//System.out.println("handDemandData: " + tick + ", demand: " + demand);
		DataPoint dp = new Observation(demand);
		dp.setIndependentValue("Tick", tick);
		if(!this.dataMap.containsKey(tick)){
			this.dataMap.put(tick, new DescriptiveStatistics());
		}
		this.dataMap.get(tick).addValue(demand);
		this.demandDataSet.add(dp);
		this.demandStats.addValue(demand);
	}
	
	public void handData(double demand){
		int tick = (int)RepastEssentials.GetTickCount();
		////System.out.println("handDemandData: " + tick + ", demand: " + demand);
		DataPoint dp = new Observation(demand);
		dp.setIndependentValue("Tick", tick);
		if(!this.dataMap.containsKey(tick)){
			this.dataMap.put(tick, new DescriptiveStatistics());
		}
		this.dataMap.get(tick).addValue(demand);
		this.demandDataSet.add(dp);
		this.demandStats.addValue(demand);
	}
	
	
	public TreeMap<Integer, DescriptiveStatistics> getDataMap(){
		return this.dataMap;
	}
	
	public DataSet getDemandDataSet(){
		return this.demandDataSet;
	}
	
	public DataSet getDemandDataSet(int period){
		DataSet ds = new DataSet();
		int currentTick = (int)RepastEssentials.GetTickCount();
		
		for(int i = currentTick - period; i<currentTick; i++){
			DataPoint dp = new Observation(this.dataMap.get(i).getSum());
			dp.setIndependentValue("Tick", i);
			ds.add(dp);
		}
		return ds;
	}
	
	public DescriptiveStatistics getDemandStats(){
		return this.demandStats;
	}

	@Override
	public Iterator iterator() {
		return this.dataMap.entrySet().iterator();
	}
	
	public DescriptiveStatistics getSubStats(int start, int end){
		DescriptiveStatistics temp = new DescriptiveStatistics();
		TreeMap<Integer, DescriptiveStatistics> subMap = new TreeMap<Integer, DescriptiveStatistics>(dataMap.subMap(start, true, end, true));
		for(DescriptiveStatistics ds : subMap.values()){
			for(Double d : ds.getValues()){
				temp.addValue(d);
			}
		}
		return temp;
	}

}
