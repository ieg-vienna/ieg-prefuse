package ieg.prefuse;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.BoundedRangeModel;

import prefuse.Constants;
import prefuse.Display;
import prefuse.action.layout.AxisLayout;
import prefuse.activity.Activity;
import prefuse.data.query.NumberRangeModel;
import prefuse.util.ui.ValuedRangeModel;

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
public class RangeModelTransformationDisplay extends Display {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3993382454989162781L;
	
	public synchronized void zoom(final Point2D p, double scale) {
		ArrayList<Integer> allAxes = new ArrayList<Integer>();
		allAxes.add(Constants.X_AXIS);
		allAxes.add(Constants.Y_AXIS);
		zoom(p,scale,allAxes);
	}
	
	public synchronized void zoom(final Point2D p, double scale,ArrayList<Integer> axes) {
		
		ArrayList<ValuedRangeModel> rangeModels = new ArrayList<ValuedRangeModel>();
		ArrayList<Integer> axisTypes = new ArrayList<Integer>();
		ArrayList<Double> minPositions = new ArrayList<Double>();
		ArrayList<Double> maxPositions = new ArrayList<Double>();
		
		for(Object iKey : m_vis.getActions().keys()) {
			Activity iAc = m_vis.getActions().get((String)iKey);
			if(iAc instanceof AxisLayout && axes.contains(((AxisLayout)iAc).getAxis())) {
				rangeModels.add(((AxisLayout)iAc).getRangeModel());
				axisTypes.add(((AxisLayout)iAc).getAxis());
				minPositions.add(((AxisLayout)iAc).getAxis() == Constants.X_AXIS ? ((AxisLayout)iAc).getLayoutBounds().getMinX() : ((AxisLayout)iAc).getLayoutBounds().getMinY());
				maxPositions.add(((AxisLayout)iAc).getAxis() == Constants.X_AXIS ? ((AxisLayout)iAc).getLayoutBounds().getMaxX() : ((AxisLayout)iAc).getLayoutBounds().getMaxY());
			} else if(iAc instanceof RangeModelTransformationProvider) {
				for(int iAx : ((RangeModelTransformationProvider)iAc).getAxes()) {
					rangeModels.add(((RangeModelTransformationProvider)iAc).getRangeModel(iAx));
					axisTypes.add(iAx);
					minPositions.add(((RangeModelTransformationProvider)iAc).getMinPosition(iAx));
					maxPositions.add(((RangeModelTransformationProvider)iAc).getMaxPosition(iAx));
				}
			}
		}
		
		for(int i=0; i<rangeModels.size(); i++) {
			ValuedRangeModel iModel = rangeModels.get(i);
			if (iModel instanceof NumberRangeModel) {
				//((NumberRangeModel)iModel).setValueRange(((Number)iModel.getLowValue()).
					//	0,0,(Number)iModel.getMinValue(),(Number)iModel.getMaxValue());
			}
		}		
	}
}
