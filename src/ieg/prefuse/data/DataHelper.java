package ieg.prefuse.data;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.TreeMap;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.Predicate;
import prefuse.data.tuple.TupleSet;
import prefuse.util.collections.IntIterator;

public class DataHelper {

    /**
     * build a graph from a table.  
     * 
     * @param table the table from which to build a graph
     * @return the created graph
     * */
    public static Graph buildGraph(Table table) {
        return buildGraph(table, null, null, null);
    }

    /**
     * build a graph from a table.  
     * 
     * @param table the table from which to build a graph
     * @param groupField the field to group tuples
     * @param sortField the field to sort tuples
     * @param missingValue 
     * @return the created graph
     * */
    public static Graph buildGraph(Table table, String groupField, String sortField, Predicate missingValue) {
        // create a graph with node schema like input table schema
        Schema schema = table.getSchema();
        Graph graph = new Graph(schema.instantiate(), true);
        if (groupField != null)
            graph.getEdgeTable().addColumn(groupField, String.class);

        // remember previous node for each time series
        TreeMap<String, Node> prevTupleMap = new TreeMap<String, Node>();

//      for (int i = 0; i < table.getTupleCount(); i++) {
//      Tuple t = (Tuple) table.getTuple(i);

        IntIterator rows = sortField!= null ? table.rowsSortedBy(sortField, true) : table.rows();
        
        while (rows.hasNext()) {
            Tuple t = (Tuple) table.getTuple(rows.nextInt());
            String code = groupField != null ? t.getString(groupField) : "default";
            
            if (missingValue != null && missingValue.getBoolean(t)) {
                // skip this tuple and interrupt the graph
                prevTupleMap.remove(code);
                continue;
            }
            

            // create a new graph node and fill it with input values
            Node cur = graph.addNode();
            for (int col = 0; col < t.getColumnCount(); col++) {
                cur.set(col, t.get(col));
            }

            // connect if possible
            if (prevTupleMap.containsKey(code)) {
                Edge edge = graph.addEdge(prevTupleMap.get(code), cur);
                if (groupField != null)
                    edge.set(groupField, code);
            }

            // remember node for connection
            prevTupleMap.put(code, cur);
        }

        return graph;
    }

    /**
     * build a debug string on meta data of a table column
     * @param t the table to debug
     * @param col the column number of the data field to retrieve
     * @return a debug string
     */
    public static String debugColumnMeta(Table t, int col) {
        return t.getColumnName(col)
                + " type="
                + t.getColumnType(col).getName()
                + " min="
                + t.getString(
                        t.getMetadata(t.getColumnName(col)).getMinimumRow(), col)
                + " max="
                + t.getString(
                        t.getMetadata(t.getColumnName(col)).getMaximumRow(), col);
    }
    
    /**
     * build a debug string on values of a column
     * @param t the table to debug
     * @param col the column number of the data field to retrieve
     * @return a debug string
     */
    public static String debugColumnValues(Table t, int col) {
        StringBuilder sb = new StringBuilder(t.getString(0, col));
        for (int i=1; i < t.getRowCount(); i++) {
            sb.append(", ");
            sb.append(t.getString(i, col));
        }
        return sb.toString();
    }
    
    /**
     * Helper method to dump specific columns of a table or other tuple set to some output stream
     * @param out the output to use
     * @param table the Table to print
     * @param cols the name of the columns
     */
    @SuppressWarnings("unchecked")
    public static void printTable(PrintStream out, TupleSet table, String... cols) {
        
        for (String c : cols) 
            out.printf(" %16s", c + " ");
            
        out.println();

        Iterator<Tuple> i = table.tuples();
        while (i.hasNext()) {
            Tuple tuple = i.next();
            
            for (String c : cols) 
                if (tuple.canGetString(c))
                    out.printf(" %16s", tuple.getString(c) + " ");

            out.println();
        }
    }

    /**
     * Helper method to dump a table to some output stream
     * @param out the output to use
     * @param table the Table to print
     */
    public static void printTable(PrintStream out, Table table) {
        String[] cols = new String[table.getColumnCount()];
        for (int i=0; i< cols.length; i++) 
            cols[i] = table.getColumnName(i);
        
        printTable(out, table, cols);
    }
}
