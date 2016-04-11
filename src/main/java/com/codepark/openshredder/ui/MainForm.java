package com.codepark.openshredder.ui;

import static java.awt.event.ItemEvent.SELECTED;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellRenderer;

import com.codepark.openshredder.base.WipeMethod;
import com.codepark.openshredder.help.About;
import com.codepark.openshredder.help.AboutUs;

import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm extends JFrame {

	private JDialog dialog;
	private WipeMethod metod = WipeMethod.DoD;
	private JButton btnDosyaEkle, btnKlasorEkle, btnRemoveFile, btnRemoveAll, btnStart, btnPart, btnDisk;
	private JTable table;
	private Properties prop;
	public static final String[] columnNames = { "Icon", "File", "Size", "Type", "Last Modified Time",
			"Last Accessed Time", "Created Time" };

	public MainForm() {

		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainForm.class.getResource("/com/codepark/openshredder/images/main_icon.png")));
		setTitle("Open Shredder");
		this.setSize(104, 768);
		initializeMenu();
		setContentUI();
		pack();

		// TheVersionClass();
	}

	public void TheVersionClass() {
		InputStream resourceAsStream = this.getClass()
				.getResourceAsStream("/META-INF/maven/com.soebes.examples/version-examples-i/pom.properties");
		this.prop = new Properties();

		try {
			this.prop.load(resourceAsStream);
			while (this.prop.keys().hasMoreElements())
				System.out.println(this.prop.keys().nextElement().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setContentUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar("Shred Navigator");
		toolBar.setToolTipText("Shred Navigator");
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
		table.getColumnModel().getColumn(0).setMaxWidth(33);
		table.getColumnModel().getColumn(1).setPreferredWidth((int) this.getWidth() / 2);
		table.getColumnModel().getColumn(1).setMinWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth((int) this.getWidth() / 15);
		table.getColumnModel().getColumn(3).setPreferredWidth((int) this.getWidth() / 15);
		table.getColumnModel().getColumn(4).setPreferredWidth((int) this.getWidth() / 5);
		table.getColumnModel().getColumn(5).setPreferredWidth((int) this.getWidth() / 5);
		table.getColumnModel().getColumn(6).setPreferredWidth((int) this.getWidth() / 5);
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

					} catch (UnsupportedFlavorException e) {

						return;
					} catch (IOException e) {

						return;
					}
					for (int i = 0; i < fileList.size(); i++) {
						if (((File) fileList.get(i)).isFile()) {
							initializeData(((File) fileList.get(i)));
						} else if (((File) fileList.get(i)).isDirectory()) {
							LoadListByPath(((File) fileList.get(i)).getAbsolutePath());
						}
					}
					// super.drop(dtde);throwing exception why?
				}
			}
		});

		JScrollPane js = new JScrollPane(table);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(table, popupMenu);

		JMenuItem itmAddFile = new JMenuItem("Add File");
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

	private void initializeData(File f) {
		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getValueAt(i, 1).toString().equals(f.getAbsolutePath())) {
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
					metod = WipeMethod.Zero;
				} else
					metod = WipeMethod.DoD;
			}
		});
		menuEdit.add(rdbtnmnitmZeroPass);

		JRadioButtonMenuItem rdbtnmnitmDodPass = new JRadioButtonMenuItem("DoD 3 Pass");
		rdbtnmnitmDodPass.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == SELECTED) {
					metod = WipeMethod.DoD;
				} else
					metod = WipeMethod.Zero;
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
				ab.setVisible(true);
			}
		});
		mnHelp.add(itmAboutUs);

		JMenuItem itmAboutShredder = new JMenuItem("About Open Shredder");
		mnHelp.add(itmAboutShredder);
		itmAboutShredder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				About ab = new About();
				ab.setVisible(true);
			}
		});
	}

	private void addFile() {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("."));
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int tut = jfc.showOpenDialog(MainForm.this);
		if (tut == JFileChooser.APPROVE_OPTION) {
			initializeData(jfc.getSelectedFile());
		}
	}

	private void addDirectory() {
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("."));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int tut = jfc.showOpenDialog(MainForm.this);
		if (tut == JFileChooser.APPROVE_OPTION) {
			LoadListByPath(jfc.getSelectedFile().getAbsolutePath());

		}
	}

	private void removeSelected() {
		if (table.getRowCount() > 0 && table.getSelectedRow() != -1) {
			int i = table.getSelectedRow();
			String dirPath = (String) table.getValueAt(i, 1);
			AbstractFileModel abs = (AbstractFileModel) table.getModel();
			FileType type = (FileType) table.getValueAt(i, 3);// file_type //
																// index
			if (type == FileType.File)
				abs.DeleteRow(i);
			else if (type == FileType.Directory) {
				int tut = JOptionPane.showConfirmDialog(MainForm.this,
						"Are you sure want to remove  the folder and all its contents?");
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
		btnStart = new JButton("Wipe");
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (table.getRowCount() == 0) {
					showMessage("List is empty.\nPlease add file or directory to shred.");
					return;
				}
				List<String> lst = new ArrayList<String>();
				for (int i = table.getRowCount() - 1; i >= 0; i--) {
					lst.add((String) table.getValueAt(i, 1));
				}
				ProgressPanel prg = new ProgressPanel(lst, metod, false);
				createDialog((JComponent) e.getSource(), prg, "Shred File!...", prg.getDimension());
				checkFileList();
				if (table.getRowCount() == 0) {
					showMessage("File shredding finished!");
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
				ChooseDiskForm frm = new ChooseDiskForm(true);
				createDialog((JComponent) e.getSource(), frm, "Choose File Storage ", new Dimension(450, 500));
				if (frm.getSelectedmountPoint().trim() != "") {
					String tmp = frm.getSelectedmountPoint();
					List<String> lst = new ArrayList<>();
					lst.add(tmp);
					ProgressPanel p = new ProgressPanel(lst, metod, true);
					createDialog((Component) e.getSource(), p, "Wipe Free Space (" + tmp + ")", p.getDimension());
					if (p.isFinished() == true) {
						showMessage(tmp + " wipe free space finished.");
					}
				}
			}
		});
		btnPart.setToolTipText("Wipe Empty  space that mounted disks and partitions");
		btnPart.setIcon(new ImageIcon(getClass().getResource("/com/codepark/openshredder/images/cleanup.png")));
		toolBar.add(btnPart);

		btnDisk = new JButton("Full Disk Wipe");
		btnDisk.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				ChooseDiskForm frm = new ChooseDiskForm(false);
				createDialog((JComponent) arg0.getSource(), frm, "Choose Mounted Disk", new Dimension(450, 500));
				if (frm.getPath().trim() != "") {
					String tmp = frm.getPath();
					List<String> lst = new ArrayList<>();
					lst.add(tmp);
					ProgressPanel p = new ProgressPanel(lst, metod, false);
					createDialog((Component) arg0.getSource(), p, "Disk Wipe (" + tmp + ")", p.getDimension());
					if (p.isFinished() == true) {
						showMessage(tmp + " wipe finished");
					}
				}
			}
		});
		btnDisk.setToolTipText("Secure clear  physical disk");
		btnDisk.setIcon(new ImageIcon(
				getClass().getResource("/com/codepark/openshredder/images/Apps-Drive-Harddisk-icon.png")));
		toolBar.add(btnDisk);

	}

	private void showMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
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

	protected void LoadListByPath(String text) {

		File f = new File(text);

		if (f.exists() && f.isDirectory()) {
			initializeData(f);

			File[] list = f.listFiles();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					if (list[i].isFile())
						initializeData(list[i]);
					else if (list[i].isDirectory()) {

						LoadListByPath(list[i].getAbsolutePath());
					}
				}
			} else {
				System.err.println(text);
			}
		}
	}

	private void createDialog(Component arg0, Component frm, String str, Dimension dim) {

		JComponent comp = (JComponent) arg0;
		Window win = (Window) SwingUtilities.getWindowAncestor(comp);
		if (win != null) {
			dialog = new JDialog(win, str, ModalityType.APPLICATION_MODAL);
			dialog.getContentPane().add(frm);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setSize(dim);
			dialog.setLocationRelativeTo(comp);
		}

		dialog.setVisible(true); // here the modal dialog takes over
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
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
}
