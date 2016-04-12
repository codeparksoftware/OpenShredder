package com.codepark.openshredder.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.codepark.openshredder.system.FileInfo;
import com.codepark.openshredder.system.OSDetector;
import com.codepark.openshredder.system.SystemUtil;

public class ChooseDiskForm extends JPanel {
	public static final String[] columnNames = { "Type", "Label", "Storage", "Size", "Path", "Mount Point" };

	public ChooseDiskForm(boolean mountPointOnly) {
		setLayout(new BorderLayout(0, 0));
		setJTable((JPanel) this);
		getDisks(mountPointOnly);
	}

	private void getDisks(boolean mountPointOnly) {
		if (OSDetector.isUnix()) {
			getDisksforLinux(mountPointOnly);
		} else if (OSDetector.isWindows()) {
			getDisksforWin();

		}
	}

	private void getDisksforWin() {
		AbstractDiskModel disk = (AbstractDiskModel) table.getModel();
		disk.RemoveAll();
		List<String> lstType = new ArrayList<String>();
		List<String> lstSize = new ArrayList<String>();
		List<String> lst = new ArrayList<String>();
		for (FileStore store : FileSystems.getDefault().getFileStores()) {

			lstType.add(store.type());
			try {
				lstSize.add(String.valueOf(store.getTotalSpace()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lst.add(store.name());

			System.out.println(store.name());
			System.out.println(store.type());
		

		}

	}

	private void getDisksforLinux(boolean mountPointOnly) {

		List<String> lstType = SystemUtil.GetExec("lsblk -o TYPE");
		List<String> lstLabel = SystemUtil.GetExec("lsblk -o LABEL");
		List<String> lst = SystemUtil.GetExec("lsblk -o KNAME");
		List<String> lstSize = SystemUtil.GetExec("lsblk -o SIZE");

		List<String> lstMPoint = SystemUtil.GetExec("lsblk -o MOUNTPOINT");
		FileInfo fi = new FileInfo("/media");

		AbstractDiskModel disk = (AbstractDiskModel) table.getModel();
		disk.RemoveAll();
		for (int i = 1; i < lst.size(); i++) {

			String[] line = new String[6];
			line[0] = lstType.get(i);
			line[1] = lstLabel.get(i);
			line[2] = lst.get(i);
			line[3] = lstSize.get(i);
			line[4] = fi.getPath(lstMPoint.get(i), line[2]);

			if (line[4] == "")
				line[4] = "/dev/" + line[2];
			line[5] = lstMPoint.get(i);
			if (!mountPointOnly)
				disk.addRow(line);
			else if (mountPointOnly && !line[5].trim().isEmpty()) {// Only
																	// mountpoint
				disk.addRow(line);
			}
		}
	}

	private void setJTable(JPanel panel_2) {
		table = new JTable(new AbstractDiskModel(columnNames));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JTable table = (JTable) e.getSource();
				Point p = e.getPoint();
				int row = table.rowAtPoint(p);
				if (e.getClickCount() == 2) {
					setPath((String) table.getValueAt(row, 4));
					setSelectedmountPoint((String) table.getValueAt(row, 5));
					try {
						Window window = SwingUtilities.getWindowAncestor(ChooseDiskForm.this);
						window.setVisible(false);
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		//
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth((int) this.getWidth() / 2);
		table.getColumnModel().getColumn(2).setPreferredWidth((int) this.getWidth() / 10);
		table.getColumnModel().getColumn(3).setPreferredWidth((int) this.getWidth() / 5);
		JScrollPane js = new JScrollPane(table);
		panel_2.add(js);

	}

	private JTable table;
	private String selectedPath = "";
	private String selectedmountPoint = "";

	public String getPath() {
		return selectedPath;
	}

	public void setPath(String str) {
		selectedPath = str;
	}

	public String getSelectedmountPoint() {
		return selectedmountPoint;
	}

	private void setSelectedmountPoint(String selectedmountPoint) {
		this.selectedmountPoint = selectedmountPoint;
	}
}
