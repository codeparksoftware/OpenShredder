package com.codepark.openshredder.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.system.OSDetector;
import com.codepark.openshredder.system.SystemUtil;
import com.codepark.openshredder.types.StorageType;

public class ChooseDiskForm extends JPanel {
	public static final String[] columnNames = { "Path", "Size", "Mount Point" };
	private static final Logger logger = Logger.getLogger(ChooseDiskForm.class.getName());

	public ChooseDiskForm() {
		setLayout(new BorderLayout(0, 0));
		setJTable((JPanel) this);
		getDisks();
	}

	private void getDisks() {
		if (OSDetector.isUnix()) {
			getDisksforLinux();
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
		StorageType storage = new StorageType();
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			lstType.add(store.type());
			storage.setStoreName(store.toString().replaceAll(store.name(), "").replace("(", "").replace(")", "")
					+ System.getProperty("file.separator"));
			try {
				storage.setSize(store.getTotalSpace());
			} catch (IOException e) {
				logger.log(Level.Error, e.getMessage());
			}
			storage.setMountPoint(storage.getStoreName());
			try {
				lstSize.add(String.valueOf(store.getTotalSpace()));
			} catch (IOException e) {
				logger.log(Level.Error, e.getMessage());
			}
			lst.add(store.name());
			disk.addRow(storage);
			System.out.println(store.name());
			System.out.println(store.type());

		}

	}

	private void getDisksforLinux() {

		List<String> lstType = SystemUtil.GetExec("lsblk -o TYPE");
		List<String> lstLabel = SystemUtil.GetExec("lsblk -o LABEL");
		List<String> lstNames = SystemUtil.GetExec("lsblk -o KNAME");
		List<String> lstSize = SystemUtil.GetExec("lsblk -o SIZE");
		List<String> lstMPoint = SystemUtil.GetExec("lsblk -o MOUNTPOINT");
		StorageType storage = new StorageType();
		AbstractDiskModel disk = (AbstractDiskModel) table.getModel();
		disk.RemoveAll();
		for (int i = 1; i < lstNames.size(); i++) {
			storage.setMountPoint(lstMPoint.get(i));
			storage.setStoreName(getPathOfStorage(storage.getMountPoint(), lstNames.get(i)));
			storage.setLabel(lstLabel.get(i));
			storage.setStorageType(lstType.get(i));
			storage.setSize(Long.parseLong(lstSize.get(i)));

			if (storage.getStoreName() == "" && new File("/dev/" + storage.getStoreName()).exists()) {
				storage.setStoreName("/dev/" + storage.getStoreName());
			} else if (new File("/dev/" + storage.getStoreName()).exists() == false) {
				return;
			}
			if (!storage.getMountPoint().trim().isEmpty()) {// Only
				// mountpoint
				disk.addRow(storage);
			}
		}
	}

	private String getPathOfStorage(String mountPoint, String name) {
		if (mountPoint.trim().isEmpty())
			return "";
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			if (store.toString().contains(mountPoint) && store.toString().contains(name)) {
				return store.name();
			}
		}
		return "";
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
					logger.log(Level.Info, table.getValueAt(row, 0).toString());
					setPath(table.getValueAt(row, 0).toString());
					setSelectedmountPoint((String) table.getValueAt(row, 2));
					try {
						Window window = SwingUtilities.getWindowAncestor(ChooseDiskForm.this);
						window.setVisible(false);
					} catch (Throwable e1) {
						logger.log(Level.Error, e1.getMessage());
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
		// table.getColumnModel().getColumn(3).setPreferredWidth((int)
		// this.getWidth() / 5);
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
