package com.codepark.openshredder.ui;

import static java.awt.event.ItemEvent.SELECTED;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableCellRenderer;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.common.Strings;
import com.codepark.openshredder.help.About;
import com.codepark.openshredder.help.AboutUs;
import com.codepark.openshredder.help.CheckVersion;
import com.codepark.openshredder.jarinfo.JarAttributes;
import com.codepark.openshredder.jarinfo.JarUtil;
import com.codepark.openshredder.types.EmptySpace;
import com.codepark.openshredder.types.FileStorage;
import com.codepark.openshredder.types.FileType;
import com.codepark.openshredder.types.Folder;
import com.codepark.openshredder.types.IFile;
import com.codepark.openshredder.types.RegularFile;
import com.codepark.openshredder.types.WipeMethod;

public class MainForm extends JFrame {

	private static final Logger logger = Logger.getLogger(MainForm.class.getName());
	private WipeMethod method = WipeMethod.DoD;
	private JButton btnDosyaEkle, btnKlasorEkle, btnRemoveFile, btnRemoveAll, btnStart, btnPart, btnDisk;
	private JTable table;

	public static final String[] columnNames = { "Icon", "File", "Size", "Type", "Last Modified Time",
			"Last Accessed Time", "Created Time", "Algorithm" };
	private static final String CONFIRM_REMOVE = "Are you sure want to remove  the folder and all its contents?";
	private static final String ADD_FILE = "Add single file";

	public MainForm() {

		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainForm.class.getResource("/com/codepark/openshredder/images/main_icon.png")));
		setTitle(Strings.PROD_NAME);
		this.setSize(104, 768);
		initializeMenu();
		setContentUI();
		pack();

		// TheVersionClass();
	}

	private void setContentUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setToolTipText(Strings.PROD_NAME + " navigator");
		addButtons(toolBar);
		toolBar.setRollover(true);

		// Lay out the main panel.
		setPreferredSize(new Dimension(1024, 768));
		panel_1.add(toolBar, BorderLayout.NORTH);

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		setJTable(panel_2);
	}

	private void setJTable(JPanel panel_2) {
		table = new JTable(new AbstractFileModel(columnNames));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		TableCellRenderer renderer = new EvenOddRenderer();
		table.setDefaultRenderer(Object.class, renderer);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		table.setFont(new Font(table.getFont().getName(), Font.PLAIN, 11));
		table.getColumnModel().getColumn(0).setMaxWidth(33);
		table.getColumnModel().getColumn(1).setPreferredWidth((int) this.getWidth() / 2);
		table.getColumnModel().getColumn(1).setMinWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth((int) this.getWidth() / 15);
		table.getColumnModel().getColumn(3).setPreferredWidth((int) this.getWidth() / 15);
		table.getColumnModel().getColumn(4).setPreferredWidth((int) this.getWidth() / 5);
		table.getColumnModel().getColumn(5).setPreferredWidth((int) this.getWidth() / 5);
		table.getColumnModel().getColumn(6).setPreferredWidth((int) this.getWidth() / 5);
		table.getColumnModel().getColumn(7).setPreferredWidth((int) this.getWidth() / 15);

		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT);

		table.setDropTarget(new DropTarget() {
			@Override
			public synchronized void drop(DropTargetDropEvent dtde) {
				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					Transferable t = dtde.getTransferable();
					List fileList = null;
					try {
						fileList = (List) t.getTransferData(DataFlavor.javaFileListFlavor);

					} catch (IOException | UnsupportedFlavorException e) {
						logger.log(Level.Error,e.getMessage());
						return;
					}
					for (int i = 0; i < fileList.size(); i++) {
						if (((File) fileList.get(i)).isFile()) {
							{
								initializeRegularFile(((File) fileList.get(i)).getAbsolutePath());
							}
						} else if (((File) fileList.get(i)).isDirectory()) {
							new Worker(((File) fileList.get(i)).getAbsolutePath()).execute();
							// loadListByPath(((File)
							// fileList.get(i)).getAbsolutePath());
						}
					}
					// super.drop(dtde);throwing exception why?
				}
			}
		});

		JScrollPane js = new JScrollPane(table);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);

		JMenuItem itmAddFile = new JMenuItem(ADD_FILE);
		itmAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addFile();
			}
		});
		popupMenu.add(itmAddFile);

		JMenuItem itmNewMenuItem = new JMenuItem("Add Folder");
		itmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDirectory();
			}
		});
		popupMenu.add(itmNewMenuItem);
		popupMenu.addSeparator();
		JMenuItem itmRemoveSelected = new JMenuItem("Remove Selected");
		itmRemoveSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelected();

			}
		});
		popupMenu.add(itmRemoveSelected);

		JMenuItem itmRemoveAll = new JMenuItem("Remove All");
		itmRemoveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearList();
			}
		});
		popupMenu.add(itmRemoveAll);
		table.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					JTable source = (JTable) e.getSource();
					int row = source.rowAtPoint(e.getPoint());
					int column = source.columnAtPoint(e.getPoint());
					if (!source.isRowSelected(row))
						source.changeSelection(row, column, false, false);
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		panel_2.add(js);

	}

	private void initializeFolder(String path) {

		IFile folder = null;
		try {
			folder = new Folder(path);
			folder.setWipeMethod(this.method);
			initializeObject(folder);
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());
		}
	}

	private void initializeRegularFile(String path) {
		IFile regularFile = null;
		try {
			regularFile = new RegularFile(path);
			regularFile.setWipeMethod(this.method);
			initializeObject(regularFile);
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());
		}

	}

	private void initializeFileStorage(String path) {
		IFile fileStorage = null;
		try {
			fileStorage = new FileStorage(path);
			fileStorage.setWipeMethod(this.method);
			initializeObject(fileStorage);
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());
		}
	}

	private void initializeEmptySpace(String path) {
		IFile fileStorage = null;
		try {
			fileStorage = new EmptySpace(path);
			initializeObject(fileStorage);
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());
		}
	}

	private void initializeObject(IFile f) {
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getValueAt(i, 1).toString().equals(f.getPath())) {
				return;
			}
		}
		AbstractFileModel abs = (AbstractFileModel) table.getModel();
		abs.addRow(f);
	}

	private void initializeMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem mnitmExit = new JMenuItem("Exit");
		mnitmExit.setIcon(new ImageIcon(MainForm.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
		mnitmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		fileMenu.add(mnitmExit);

		JMenu menuEdit = new JMenu("Wipe Method");
		menuBar.add(menuEdit);

		JRadioButtonMenuItem rdbtnmnitmZeroPass = new JRadioButtonMenuItem("Zero Pass");
		rdbtnmnitmZeroPass.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					method = WipeMethod.Zero;
				} else
					method = WipeMethod.DoD;
			}
		});
		menuEdit.add(rdbtnmnitmZeroPass);

		JRadioButtonMenuItem rdbtnmnitmDodPass = new JRadioButtonMenuItem("DoD 3 Pass");
		rdbtnmnitmDodPass.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == SELECTED) {
					method = WipeMethod.DoD;
				} else
					method = WipeMethod.Zero;
			}
		});

		rdbtnmnitmDodPass.setSelected(true);
		menuEdit.add(rdbtnmnitmDodPass);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnmnitmDodPass);
		bg.add(rdbtnmnitmZeroPass);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(menuBar, BorderLayout.NORTH);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem itmAboutUs = new JMenuItem("About Us");
		itmAboutUs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AboutUs ab = new AboutUs();
				ab.setLocationRelativeTo(null);
				ab.setVisible(true);
			}
		});
		mnHelp.add(itmAboutUs);

		JMenuItem itmAboutShredder = new JMenuItem("About Open Shredder");
		mnHelp.add(itmAboutShredder);
		itmAboutShredder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About ab = new About();
				ab.setLocationRelativeTo(null);
				ab.setVisible(true);
			}
		});
		JMenuItem itmCheckUpdate = new JMenuItem("Check for Update");
		mnHelp.add(itmCheckUpdate);
		itmCheckUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkUpdate();
			}
		});
	}

	private void checkUpdate() {
		String oldFile = JarUtil.getExecutablePath();
		JarAttributes jar = new JarAttributes(JarUtil.makeJarURLforLocal(oldFile));
		String remoteFile = jar.getUpdatePath();
		logger.log(Level.Info,remoteFile);
		logger.log(Level.Info, oldFile);
		CheckVersion frm = null;
		try {
			frm = new CheckVersion(oldFile, remoteFile);
			Dialog d = new Dialog();
			d.createDialog(frm, "OpenShredder update", ((BaseProgressPanel) frm).getDimension());
		} catch (FileNotFoundException e1) {
			logger.log(Level.Error,e1.getMessage());
		}
	}

	private void addFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("."));
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int tut = jfc.showOpenDialog(MainForm.this);
		if (tut == JFileChooser.APPROVE_OPTION) {
			initializeRegularFile(jfc.getSelectedFile().getAbsolutePath());

		}
	}

	private void addDirectory() {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("."));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int tut = jfc.showOpenDialog(MainForm.this);
		if (tut == JFileChooser.APPROVE_OPTION) {
			new Worker(jfc.getSelectedFile().getAbsolutePath()).execute();
			// loadListByPath(jfc.getSelectedFile().getAbsolutePath());

		}
	}

	private void removeSelected() {
		if (table.getRowCount() > 0 && table.getSelectedRow() != -1) {
			int i = table.getSelectedRow();
			String dirPath = (String) table.getValueAt(i, 1);
			AbstractFileModel abs = (AbstractFileModel) table.getModel();
			FileType type = (FileType) table.getValueAt(i, 3);// file_type //
																// index
			if (type == FileType.RegularFile)
				abs.DeleteRow(i);
			else if (type == FileType.Folder) {
				int tut = JOptionPane.showConfirmDialog(MainForm.this, CONFIRM_REMOVE);
				if (tut == JOptionPane.YES_OPTION) {

					for (int j = table.getRowCount(); j >= 0; j--) {
						String str = (String) table.getValueAt(j, 1);
						if (str.contains(dirPath)) {
							abs.DeleteRow(j);
							j++;
						}
					}
					abs.DeleteRow(i);
				}

			}
		}
	}

	private void clearList() {
		if (table.getRowCount() > 0) {
			AbstractFileModel abs = (AbstractFileModel) table.getModel();
			abs.RemoveAll();
		}
	}

	protected void addButtons(JToolBar toolBar) {

		btnDosyaEkle = new JButton("Add File");
		btnDosyaEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addFile();
			}

		});
		btnDosyaEkle.setIcon(
				new ImageIcon(MainForm.class.getResource("/com/codepark/openshredder/images/Document-1-Add-icon.png")));
		btnDosyaEkle.setToolTipText("Add single file");
		toolBar.add(btnDosyaEkle);

		// toolBar.addSeparator();

		btnKlasorEkle = new JButton("Add Folder");
		btnKlasorEkle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addDirectory();
			}

		});
		btnKlasorEkle.setIcon(
				new ImageIcon(MainForm.class.getResource("/com/codepark/openshredder/images/folder-add-icon.png")));
		btnKlasorEkle.setToolTipText("Add specific folder with subfolders and which includes files");
		toolBar.add(btnKlasorEkle);

		// toolBar.addSeparator();

		btnRemoveFile = new JButton("Remove Selected");
		btnRemoveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeSelected();
			}

		});
		btnRemoveFile.setIcon(new ImageIcon(
				MainForm.class.getResource("/com/codepark/openshredder/images/Document-1-Remove-icon.png")));

		btnRemoveFile.setToolTipText("Removes selected lines in the list.");
		toolBar.add(btnRemoveFile);

		// toolBar.addSeparator();

		btnRemoveAll = new JButton("Clear List");
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearList();
			}

		});
		btnRemoveAll.setIcon(new ImageIcon(
				MainForm.class.getResource("/com/codepark/openshredder/images/window-app-list-close-icon.png")));
		btnRemoveAll.setToolTipText("Clear all  entry in the list.");
		toolBar.add(btnRemoveAll);

		toolBar.addSeparator();
		btnStart = new JButton("Shred Files");
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getRowCount() == 0) {
					MessageBox.showMessage("Shred list is empty.\nPlease add an object to shred.");
					return;
				}
				List<IFile> lst = new ArrayList<IFile>();
				for (int i = table.getRowCount() - 1; i >= 0; i--) {

					AbstractFileModel afm = (AbstractFileModel) table.getModel();
					IFile file = afm.getDataRow(i);
					lst.add(file);
				}
				ShredProgress prg = new ShredProgress(lst);
				Dialog d = new Dialog();
				d.createDialog((JComponent) e.getSource(), prg, "Shred File!...", prg.getDimension());
				checkFileList();
				if (table.getRowCount() == 0) {
					MessageBox.showMessage("File shredding finished!");
				}
			}

		});
		btnStart.setToolTipText("Wipe Files in the List");
		btnStart.setIcon(new ImageIcon(MainForm.class.getResource("/com/codepark/openshredder/images/clear.png")));
		toolBar.add(btnStart);

		toolBar.addSeparator();

		btnPart = new JButton("Wipe Empty Space");
		btnPart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addEmptySpace((JComponent) e.getSource());
			}

		});
		btnPart.setToolTipText("Wipe Empty  space that mounted disks and partitions");
		btnPart.setIcon(new ImageIcon(getClass().getResource("/com/codepark/openshredder/images/cleanup.png")));
		// toolBar.add(btnPart);

		btnDisk = new JButton("Full Disk Wipe");
		btnDisk.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				addFileStorage((JComponent) e.getSource());
			}

		});
		btnDisk.setToolTipText("Secure clear  physical disk");
		btnDisk.setIcon(new ImageIcon(
				getClass().getResource("/com/codepark/openshredder/images/Apps-Drive-Harddisk-icon.png")));
		// toolBar.add(btnDisk);

	}

	private void addFileStorage(JComponent comp) {
		ChooseDiskForm frm = new ChooseDiskForm();
		Dialog d = new Dialog();
		d.createDialog(comp, frm, "Choose Mounted Disk", new Dimension(450, 500));
		if (frm.getPath().trim() != "") {
			String tmp = frm.getPath();
			initializeFileStorage(tmp);
		}
	}

	private void addEmptySpace(JComponent comp) {
		ChooseDiskForm frm = new ChooseDiskForm();
		Dialog d = new Dialog();
		d.createDialog(comp, frm, "Choose File Storage ", new Dimension(450, 500));
		if (frm.getSelectedmountPoint().trim() != "") {
			String tmp = frm.getSelectedmountPoint();
			initializeEmptySpace(tmp);
		}
	}

	private void checkFileList() {
		AbstractFileModel abs = (AbstractFileModel) table.getModel();
		for (int i = 0; i < table.getRowCount(); i++) {
			if (new File(table.getValueAt(i, 1).toString()).exists() == false) {
				abs.DeleteRow(i);
				i--;
			}
		}

	}

	protected void loadListByPath(String path) {

		File f = new File(path);

		if (f.isDirectory() && f.exists()) {
			initializeFolder(path);
			//
			File[] list = f.listFiles();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (list[i].isFile()) {
						initializeRegularFile(list[i].getAbsolutePath());
					} else if (list[i].isDirectory()) {

						loadListByPath(list[i].getAbsolutePath());
					}
				}
			} else {
				logger.log(Level.Error, path);

			}
		}
	}

	@Override
	protected void finalize() throws Throwable {

		super.finalize();
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	class Worker extends SwingWorker<Void, Void> {

		String root;

		public Worker(String root) {
			this.root = root;
		}

		@Override
		protected Void doInBackground() throws Exception {
			loadListByPath(root);
			return null;

		}

		@Override
		protected void done() {

			super.done();
		}

	}

}
