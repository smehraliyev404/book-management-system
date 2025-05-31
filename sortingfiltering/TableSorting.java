package sortingfiltering;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class TableSorting {

    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;
    private Map<Integer, SortOrder> sortOrderMap = new HashMap<>();
    private List<Integer> sortSequence = new ArrayList<>();

    public TableSorting(JTable table, TableRowSorter<DefaultTableModel> sorter) {
        this.table = table;
        this.sorter = sorter;
        setupTableSorting();
    }

    private void setupTableSorting() {
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
    }

    private class ColumnHeaderListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int column = table.columnAtPoint(e.getPoint());
            toggleSortOrder(column);
        }
    }

    private void toggleSortOrder(int column) {
        SortOrder currentOrder = sortOrderMap.getOrDefault(column, SortOrder.UNSORTED);

        switch (currentOrder) {
            case UNSORTED:
                sortOrderMap.put(column, SortOrder.ASCENDING);
                sortSequence.add(column);
                break;
            case ASCENDING:
                sortOrderMap.put(column, SortOrder.DESCENDING);
                break;
            case DESCENDING:
                sortOrderMap.remove(column);
                sortSequence.remove((Integer) column);
                break;
        }

        List<RowSorter.SortKey> sortKeys = buildSortKeys();
        sorter.setSortKeys(sortKeys);
    }

    private List<RowSorter.SortKey> buildSortKeys() {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        for (int colIndex : sortSequence) {
            if (sortOrderMap.containsKey(colIndex)) {
                sortKeys.add(new RowSorter.SortKey(colIndex, sortOrderMap.get(colIndex)));
            }
        }
        return sortKeys;
    }
}
