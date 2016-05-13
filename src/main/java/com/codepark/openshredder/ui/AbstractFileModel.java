package com.codepark.openshredder.ui;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import com.codepark.openshredder.types.FileType;
import com.codepark.openshredder.types.FileTypeFactory;
import com.codepark.openshredder.types.IFile;
import com.codepark.openshredder.types.WipeMethod;

public class AbstractFileModel extends AbstractTableModel {

	private String[] columnNames;
	protected Vector dataVector;

	public AbstractFileModel(String[] columns) {
		columnNames = columns;
		dataVector = new Vector<>();
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return dataVector.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		FileModel fm = (FileModel) dataVector.get(row);
		return fm.GetValue(column);

	}

	public IFile getDataRow(int row) {
		FileModel fm = (FileModel) dataVector.get(row);
		WipeMethod method = fm.getWipeMethod();
		FileType type = fm.getType();
		String path = fm.getFileName();
		return FileTypeFactory.createIFile(type, path, method);
	}

	@Override
	public Class getColumnClass(int column) {
		if (column == 0)
			return ImageIcon.class;
		return Object.class;
	}

	public void setValueAt(Object value, int row, int column) {
		FileModel fm = (FileModel) dataVector.get(row);
		fm.setValue(value, column);
		fireTableCellUpdated(row, column);
	}

	public void RemoveAll() {

		int i = dataVector.size();
		dataVector.clear();
		fireTableRowsDeleted(0, i);
	}

	public void addEmptyRow() {
		// dataVector.add(new FileModel());
		// fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
	}

	public void addRow(IFile f) {

		dataVector.add(new FileModel(f));
		
		fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
		fireTableDataChanged();
	}

	public void DeleteRow(int rowIndex) {

		dataVector.removeElementAt(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
		fireTableDataChanged();
	}

}
