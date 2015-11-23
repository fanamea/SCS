package artefacts;

import java.util.Comparator;

public class ReturnReqComparator implements Comparator<ReturnOrder> {

	@Override
	public int compare(ReturnOrder arg0, ReturnOrder arg1) {
		return arg0.getDate().compareTo(arg1.getDate());
	}
	
}
