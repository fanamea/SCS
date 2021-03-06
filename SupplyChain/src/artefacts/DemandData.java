package artefacts;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Observation;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import repast.simphony.essentials.RepastEssentials;

public class DemandData implements Iterable<Integer>{
	
	private TreeMap<Integer, Double> dataMap;
	private DataSet demandDataSet;
	private DescriptiveStatistics demandStats;
	
	public DemandData(){
		
		this.dataMap = new TreeMap<Integer, Double>();
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
	
	public double getSquareMean(int period){
		int tick = (int)RepastEssentials.GetTickCount();
		DescriptiveStatistics substats = getSquaredSubStats(tick-period+1, tick);
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
	
	public void handDemandData(int tick, double demand){
		//System.out.println("handDemandData: " + tick + ", demand: " + demand);
		DataPoint dp = new Observation(demand);
		dp.setIndependentValue("Tick", tick);
		this.dataMap.put(tick, demand);
		this.demandDataSet.add(dp);
		this.demandStats.addValue(demand);
	}
	
	public void handDemandData(double demand){
		int tick = (int)RepastEssentials.GetTickCount();
		////System.out.println("handDemandData: " + tick + ", demand: " + demand);
		DataPoint dp = new Observation(demand);
		dp.setIndependentValue("Tick", tick);
		this.dataMap.put(tick, demand);
		this.demandDataSet.add(dp);
		this.demandStats.addValue(demand);
	}
	
	
	public double getDemandData(int tick){
		return this.dataMap.get(tick);
	}
	
	public TreeMap<Integer, Double> getDataMap(){
		return this.dataMap;
	}
	
	public DataSet getDemandDataSet(){
		return this.demandDataSet;
	}
	
	public DataSet getDemandDataSet(int period){
		DataSet ds = new DataSet();
		int currentTick = (int)RepastEssentials.GetTickCount();
		
		for(int i = currentTick - period; i<currentTick; i++){
			DataPoint dp = new Observation(this.dataMap.get(i));
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
		TreeMap<Integer, Double> subMap = new TreeMap<Integer, Double>(dataMap.subMap(start, true, end, true));
		for(Double d : subMap.values()){
			temp.addValue(d);
		}
		return temp;
	}
	
	public DescriptiveStatistics getSquaredSubStats(int start, int end){
		DescriptiveStatistics temp = new DescriptiveStatistics();
		TreeMap<Integer, Double> subMap = new TreeMap<Integer, Double>(dataMap.subMap(start, true, end, true));
		for(Double d : subMap.values()){
			temp.addValue(Math.pow(d, 2));
		}
		return temp;
	}

}
