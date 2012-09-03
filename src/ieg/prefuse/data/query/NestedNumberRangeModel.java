package ieg.prefuse.data.query;

import prefuse.data.query.NumberRangeModel;

/**
 * 
 * 
 * <p>
 * Added:          / TL<br>
 * Modifications: 
 * </p>
 * 
 * @author Tim Lammarsch
 *
 */
public class NestedNumberRangeModel extends NumberRangeModel {
	protected Number[] min;
	protected Number[] max;
	
	/**
	 * 
	 */
	public NestedNumberRangeModel(Number lo, Number hi,Number[] min, Number[] max) {
		super(lo,hi,lo,hi);
		
		recalculateMinMax();
	}
	
	/**
	 * 
	 */
	private void recalculateMinMax() {
		m_min = 1;
		for(int i=0; i<min.length; i++) {
			//m_min m_min.byteValue()
			//m_min *= min[i];
		}
	}

	/**
	 * @return the min
	 */
	public Number[] getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Number[] min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public Number[] getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Number[] max) {
		this.max = max;
	}
}
