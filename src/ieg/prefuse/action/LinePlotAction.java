package ieg.prefuse.action;

import java.util.Iterator;

import prefuse.action.GroupAction;
import prefuse.data.Schema;
import prefuse.data.tuple.TupleSet;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;

/**
 * <p>
 * Added: 2012-06-14 / AR (based on LineChart example by Jeff Heer)<br>
 * Modifications: 2012-0X-XX / XX / ...
 * </p>
 * 
 * @author Alexander Rind (based on LineChart example by Jeff Heer)
 */
public class LinePlotAction extends GroupAction {
    
    private String m_src;

    public LinePlotAction(String group, String source) {
        super(group);
        m_src = source;
    }

    @Override
    public void run(double frac) {
        // get visual table of my visualization
        VisualTable lines = getTable();

        // add line segments
        @SuppressWarnings("rawtypes")
        Iterator items = m_vis.visibleItems(m_src);
        for ( VisualItem v1=null, v2; items.hasNext(); v1=v2 ) {
            v2 = (VisualItem)items.next();
            if ( v1 != null ) {
                VisualItem item = lines.addItem();
                item.set("v1", v1);
                item.set("v2", v2);
            }
        }
    }

    /**
     * Create a new table for representing line segments.
     */
    protected VisualTable getTable() {
        TupleSet ts = m_vis.getGroup(m_group);
        if ( ts == null ) {
            Schema lineSchema = PrefuseLib.getVisualItemSchema();
            lineSchema.addColumn(VisualItem.X2, double.class);
            lineSchema.addColumn(VisualItem.Y2, double.class);
            lineSchema.addColumn("v1", VisualItem.class);
            lineSchema.addColumn("v2", VisualItem.class);
            
            VisualTable vt = m_vis.addTable(m_group, lineSchema);
            return vt;
        } else if ( ts instanceof VisualTable ) {
            // empty table
            ts.clear();
            return (VisualTable)ts;
        } else {
            throw new IllegalStateException(
                "Group already exists, not being used for line segments");
        }
    }
}
