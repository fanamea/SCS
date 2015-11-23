package setups;

import inventoryPlannningAlgorithm.BookbinderTan;

import java.util.ArrayList;
import java.util.HashMap;

import repast.simphony.random.RandomHelper;
import lotSizingAlgorithms.CapacitatedSilverMeal;
import lotSizingAlgorithms.SilverMeal;
import modules.Link;
import agents.Business;
import agents.Customer;
import agents.Manufacturer;
import agents.MaterialSource;
import agents.Node;
import agents.Retailer;
import artefacts.Material;
import demandPattern.Constant;
import demandPattern.NormalDistribution;

public class OneFactoryConstant extends Setup{
		
	public OneFactoryConstant(){
		
		super();
		
		sources.add(new MaterialSource(this, 3));
		manufacturers.add(new Manufacturer(this, 2));
		customers.add(new Customer(this, new Constant(10.0)));
		
		links.add(new Link(sources.get(0), manufacturers.get(0)));
		links.add(new Link(manufacturers.get(0), customers.get(0)));
		
		//Define Product
		
		//init
		init();
		
		//Parameters
		MaterialSource source = sources.get(0);
		source.setCapacity(50);
		
		for(Manufacturer m : manufacturers){
			
			m.setPlanningPeriod(10);
			
			//Bill of Material
			HashMap<Material, Double> billOfMaterial = new HashMap<Material, Double>();
			Material upstrProduct = m.getUpstrLinks().get(0).getMaterial();
			billOfMaterial.put(upstrProduct, 1.0);
			m.setBillOfMaterial(billOfMaterial);
			
			//Inventory
			m.setHoldingCost(2);
			m.setServiceLevel(0.95);
			m.setInventoryPlanningAlgorithm(new BookbinderTan());
			
			//Order
			m.setOrderPlanningAlgorithm(new SilverMeal(15, 1));
			
			//Production			
			m.setProductionCapacity(50);
			m.setProductionTime(1);
			m.setSetUpCost(50);
			m.setLotSizingAlgorithm(new CapacitatedSilverMeal(15, 1, 50));
		}
		
		for(Link link : links){
			link.setFixCost(15);
			link.setDistrDuration(RandomHelper.createUniform(1, 1));
		}
		
	}

}
