package com.infy;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

@SuppressWarnings("serial")
public class MainClass  extends JFrame  {

	private JFrame mainFrame;
	private JPanel controlPanel;
	private JPanel browsePanel;
	private JTextPane fieldsLabel;
	private JTextPane rlbsLabel;
	private JButton exportButton;
	private JLabel templateLink;
	private JLabel referenceLink;
	private JPanel linksPanel;
	private JPanel searchPanel;
	private JPanel fieldsPanel;
	private JPanel rlbsPanel;
	private JButton searchButton;
	private JButton clearButton;
	JComboBox<String> filesCombo = null;
	JScrollPane filesListScrollPane =null;
	JScrollPane fieldScrollPane =null;
	JScrollPane rlbsScrollPane =null;

	DefaultComboBoxModel<String> members = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> updatedMembers = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> description = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> dtn = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> assigned = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> status = new DefaultComboBoxModel<String>();
	JComboBox<String> membersCombo = new JComboBox<String>(members);
	JComboBox<String> descCombo = new JComboBox<String>(description);
	JComboBox<String> dtnCombo = new JComboBox<String>(dtn);
	JComboBox<String> assignedCombo = new JComboBox<String>(assigned);
	JComboBox<String> statusCombo = new JComboBox<String>(status);
	JScrollPane membersScrollPane = new JScrollPane(membersCombo);
	JScrollPane descScrollPane = new JScrollPane(descCombo); 
	JScrollPane dtnScrollPane = new JScrollPane(dtnCombo); 
	JScrollPane assignedScrollPane = new JScrollPane(assignedCombo); 
	JScrollPane statusScrollPane = new JScrollPane(statusCombo);

	public MainClass(){
		createAndShowGUI();
	}

	public static void main(String[] args){
		try {
			//Metal	Nimbus	CDE/Motif	Windows	Windows Classic
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				System.out.println(info.getName());
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		MainClass  mainClass = new MainClass();      
		mainClass.formAnalysis();
	}

	/**
	 * This Method is to perform the complete form analysis logic
	 */
	private void formAnalysis(){                                    

		try{

			ExcelParser excelParser = new ExcelParser("C:\\Temp\\FormsList.xlsx");
			List<EDLFile> edlfiles = excelParser.processLineByLine();
			Map<String,String> tagPosMap = excelParser.getTagPositions();
			/*
			 * FileParser parser = new FileParser("C:\\Temp\\EDLDUMP.txt");
				List<com.infy.File> objs = parser.processLineByLine();

				for ( com.infy.File obj : objs ){
					fileNames.addElement(obj.getName());
				}
			 */

			resetAll(edlfiles);


			clearButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) { 

					try{

						descCombo.setSelectedIndex(-1);
						dtnCombo.setSelectedIndex(-1);
						assignedCombo.setSelectedIndex(-1);
						statusCombo.setSelectedIndex(-1);
						membersCombo.setSelectedIndex(-1);
						resetAll(edlfiles);
						membersCombo.setModel(members);
						filesCombo.setModel(members);
						descCombo.setModel(description);
						dtnCombo.setModel(dtn);
						assignedCombo.setModel(assigned);
						statusCombo.setModel(status);

					}catch(Exception ep){

					}
				}
			});

			JPanel filterPanel= new JPanel();
			filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel searchFilter= new JPanel();
			searchFilter.setLayout(new FlowLayout(FlowLayout.CENTER));
			JRadioButton radForm = new JRadioButton("Form Name");
			JRadioButton radDesc = new JRadioButton("Description");
			JRadioButton radDtn = new JRadioButton("DTN");
			JRadioButton radAssign= new JRadioButton("Assigned");
			JRadioButton radStatus = new JRadioButton("Status");
			JRadioButton radAll = new JRadioButton("All",true);
			JLabel  searchlabel= new JLabel("        ", JLabel.LEFT);
			final JTextField searchText = new JTextField(10);
			searchText.setText("*");
			searchText.setEnabled(false);
			JButton filterButton = new JButton("Filter");
			filterButton.setEnabled(false);

			JLabel statusLabel = new JLabel();

			radForm.setMnemonic(KeyEvent.VK_C);
			radDesc.setMnemonic(KeyEvent.VK_M);
			radDtn.setMnemonic(KeyEvent.VK_P);

			radForm.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {         
					searchlabel.setText("<html><b><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +radForm.getText()+":</font></b></html>");
					searchText.setEnabled(true);
					filterButton.setEnabled(true);
					membersCombo.setEnabled(true);
					descCombo.setEnabled(false);
					dtnCombo.setEnabled(false);
					assignedCombo.setEnabled(false);
					statusCombo.setEnabled(false);
					searchButton.setEnabled(true);
					clearButton.setEnabled(true);
					searchText.setText("*");

				}           
			});

			radDesc.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {             
					searchlabel.setText("<html><b><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +radDesc.getText()+":</font></b></html>");
					searchText.setEnabled(true);
					filterButton.setEnabled(true);
					membersCombo.setEnabled(false);
					descCombo.setEnabled(true);
					dtnCombo.setEnabled(false);
					assignedCombo.setEnabled(false);
					statusCombo.setEnabled(false);
					searchButton.setEnabled(true);
					clearButton.setEnabled(true);
					searchText.setText("*");

				}           
			});

			radDtn.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {             
					searchlabel.setText("<html><b><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +radDtn.getText()+":</font></b></html>");
					searchText.setEnabled(true);
					filterButton.setEnabled(true);
					membersCombo.setEnabled(false);
					descCombo.setEnabled(false);
					dtnCombo.setEnabled(true);
					assignedCombo.setEnabled(false);
					statusCombo.setEnabled(false);
					searchButton.setEnabled(true);
					clearButton.setEnabled(true);
					searchText.setText("*");

				}           
			});

			radAssign.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {             
					searchlabel.setText("<html><b><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +radAssign.getText()+":</font></b></html>");
					//searchlabel.setFont(new Font(searchlabel.getFont().getFontName(), Font.BOLD, searchlabel.getFont().getSize()));;
					searchText.setEnabled(true);
					filterButton.setEnabled(true);
					membersCombo.setEnabled(false);
					descCombo.setEnabled(false);
					dtnCombo.setEnabled(false);
					assignedCombo.setEnabled(true);
					statusCombo.setEnabled(false);
					searchButton.setEnabled(true);
					clearButton.setEnabled(true);
					searchText.setText("*");
				}           
			});

			radStatus.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {             
					searchlabel.setText("<html><b><font color='blue'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +radStatus.getText()+":</font></b></html>");
					searchText.setEnabled(true);
					filterButton.setEnabled(true);
					membersCombo.setEnabled(false);
					descCombo.setEnabled(false);
					dtnCombo.setEnabled(false);
					assignedCombo.setEnabled(false);
					statusCombo.setEnabled(true);
					searchButton.setEnabled(true);
					clearButton.setEnabled(true);
					searchText.setText("*");

				}           
			});

			radAll.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					searchlabel.setText("        ");
					searchText.setText("*");
					searchText.setEnabled(false);
					filterButton.setEnabled(false);
					membersCombo.setEnabled(true);
					descCombo.setEnabled(true);
					dtnCombo.setEnabled(true);
					assignedCombo.setEnabled(true);
					statusCombo.setEnabled(true);
					searchButton.setEnabled(true);
					clearButton.setEnabled(true);

					resetAll(edlfiles);
					membersCombo.setModel(members);
					filesCombo.setModel(members);
					descCombo.setModel(description);
					dtnCombo.setModel(dtn);
					assignedCombo.setModel(assigned);
					statusCombo.setModel(status);

				}           
			});

			filterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {     
					DefaultComboBoxModel<String> temp = new DefaultComboBoxModel<String>();
					if(searchlabel.getText().contains("Form Name:")){
						if(searchText.getText().trim().equals("*")){
							resetAll(edlfiles);
							membersCombo.setModel(members);
							filesCombo.setModel(members);
							descCombo.setModel(description);
							dtnCombo.setModel(dtn);
							assignedCombo.setModel(assigned);
							statusCombo.setModel(status);
						}else{
							for( int i=0; i<members.getSize();i++){
								if(members.getElementAt(i).toUpperCase().contains(searchText.getText().toUpperCase())){
									temp.addElement(members.getElementAt(i));
								}
							}
							membersCombo.removeAllItems();
							membersCombo.setModel(temp);
							members=temp;
						}
					}

					if(searchlabel.getText().contains("Description:")){
						if(searchText.getText().trim().equals("*")){
							resetAll(edlfiles);
							membersCombo.setModel(members);
							filesCombo.setModel(members);
							descCombo.setModel(description);
							dtnCombo.setModel(dtn);
							assignedCombo.setModel(assigned);
							statusCombo.setModel(status);
						}else{
							for( int i=0; i<description.getSize();i++){
								if(description.getElementAt(i).contains(searchText.getText().toUpperCase())){
									temp.addElement(description.getElementAt(i));
								}
							}
							descCombo.removeAllItems();
							descCombo.setModel(temp);
							description=temp;
						}
					}
					if(searchlabel.getText().contains("DTN:")){
						if(searchText.getText().trim().equals("*")){
							resetAll(edlfiles);
							membersCombo.setModel(members);
							filesCombo.setModel(members);
							descCombo.setModel(description);
							dtnCombo.setModel(dtn);
							assignedCombo.setModel(assigned);
							statusCombo.setModel(status);
						}else{
							for( int i=0; i<dtn.getSize();i++){
								if(dtn.getElementAt(i).contains(searchText.getText())){
									temp.addElement(dtn.getElementAt(i));
								}
							}
							dtnCombo.removeAllItems();
							dtnCombo.setModel(temp);
							dtn=temp;
						}
					}
					if(searchlabel.getText().contains("Assigned:")){
						if(searchText.getText().trim().equals("*")){
							resetAll(edlfiles);
							membersCombo.setModel(members);
							filesCombo.setModel(members);
							descCombo.setModel(description);
							dtnCombo.setModel(dtn);
							assignedCombo.setModel(assigned);
							statusCombo.setModel(status);
						}else{
							for( int i=0; i<assigned.getSize();i++){
								if(assigned.getElementAt(i).toUpperCase().contains(searchText.getText().toUpperCase())){
									temp.addElement(assigned.getElementAt(i));
								}
							}
							assignedCombo.removeAllItems();
							assignedCombo.setModel(temp);
							assigned=temp;
						}
					}
					if(searchlabel.getText().contains("Status:")){
						if(searchText.getText().trim().equals("*")){
							resetAll(edlfiles);
							membersCombo.setModel(members);
							filesCombo.setModel(members);
							descCombo.setModel(description);
							dtnCombo.setModel(dtn);
							assignedCombo.setModel(assigned);
							statusCombo.setModel(status);
						}else{
							for( int i=0; i<status.getSize();i++){
								if(status.getElementAt(i).toUpperCase().contains(searchText.getText().toUpperCase())){
									temp.addElement(status.getElementAt(i));
								}
							}
							statusCombo.removeAllItems();
							statusCombo.setModel(temp);
							status=temp;
						}
					}





				}
			});

			searchText.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						if(searchText.getText().trim().equalsIgnoreCase("*")) searchText.setText("");
					}catch(Exception et){

					}
				}
			});

			//Group the radio buttons.
			ButtonGroup group = new ButtonGroup();
			group.add(radAll);
			group.add(radForm);
			group.add(radDesc);
			group.add(radDtn);
			group.add(radAssign);
			group.add(radStatus);

			filterPanel.add(radAll); 
			filterPanel.add(radForm);
			filterPanel.add(radDesc);
			filterPanel.add(radDtn); 
			filterPanel.add(radAssign);
			filterPanel.add(radStatus);

			filterPanel.add(searchlabel); 
			filterPanel.add(searchText);
			filterPanel.add(filterButton); 
			filterPanel.setBorder(BorderFactory.createTitledBorder("<html><font color='blue'>Filter Criteria</font></html>"));
			searchPanel.add(filterPanel);


			// Lay out everything
			JPanel jpnMember= new JPanel();			
			jpnMember.add(new JLabel("Form Name"));
			jpnMember.add(membersScrollPane);
			jpnMember.setLayout(new BoxLayout(jpnMember, BoxLayout.Y_AXIS));

			searchFilter.add(jpnMember);

			JPanel jpnDescription= new JPanel();			
			jpnDescription.add(new JLabel("Description"));
			jpnDescription.add(descScrollPane);
			jpnDescription.setLayout(new BoxLayout(jpnDescription, BoxLayout.Y_AXIS));

			searchFilter.add(jpnDescription);

			JPanel jpndtn= new JPanel();			
			jpndtn.add(new JLabel("DTN"));
			jpndtn.add(dtnScrollPane);
			jpndtn.setLayout(new BoxLayout(jpndtn, BoxLayout.Y_AXIS));

			searchFilter.add(jpndtn);

			JPanel jpnAssign= new JPanel();			
			jpnAssign.add(new JLabel("Assigned"));
			jpnAssign.add(assignedScrollPane);
			jpnAssign.setLayout(new BoxLayout(jpnAssign, BoxLayout.Y_AXIS));

			searchFilter.add(jpnAssign);

			JPanel jpnStatus= new JPanel();			
			jpnStatus.add(new JLabel("Status"));
			jpnStatus.add(statusScrollPane);
			jpnStatus.setLayout(new BoxLayout(jpnStatus, BoxLayout.Y_AXIS));

			searchFilter.add(jpnStatus);
			JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonsPanel.add(searchButton);
			buttonsPanel.add(clearButton);
			searchPanel.add(searchFilter);
			searchPanel.add(buttonsPanel);
			searchButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) { 

					try{
						if (membersCombo.getSelectedIndex() == -1 && 
								descCombo.getSelectedIndex() == -1 &&  
								dtnCombo.getSelectedIndex() == -1 && 
								assignedCombo.getSelectedIndex() == -1 && 
								statusCombo.getSelectedIndex() == -1) { //TODO
							JOptionPane.showMessageDialog(mainFrame,"Select atleast one parameter","Warning",JOptionPane.WARNING_MESSAGE);
						}else{
							boolean flag = true;
							for ( EDLFile edlfile : edlfiles){
								if( descCombo.getSelectedIndex() !=-1 && edlfile.getDescription().equals(descCombo.getSelectedItem() )){
									membersCombo.setSelectedItem(edlfile.getMember());
									dtnCombo.setSelectedItem(edlfile.getDtn().toString());
									assignedCombo.setSelectedItem(edlfile.getAssigned());
									statusCombo.setSelectedItem(edlfile.getStatus());
								}
								if( membersCombo.getSelectedIndex() !=-1 && edlfile.getMember().equals(membersCombo.getSelectedItem() )){
									descCombo.setSelectedItem(edlfile.getDescription());
									dtnCombo.setSelectedItem(edlfile.getDtn().toString());
									assignedCombo.setSelectedItem(edlfile.getAssigned());
									statusCombo.setSelectedItem(edlfile.getStatus());
								}
								if( flag && dtnCombo.getSelectedIndex() !=-1 && edlfile.getDtn().toString().equals(dtnCombo.getSelectedItem() )){
									System.out.println(edlfile.getMember().toString());
									updatedMembers.addElement(edlfile.getMember());
									descCombo.setSelectedItem(edlfile.getDescription());
									assignedCombo.setSelectedItem(edlfile.getAssigned());
									statusCombo.setSelectedItem(edlfile.getStatus());
									flag=false;
								}
								if( flag && assignedCombo.getSelectedIndex() !=-1 && edlfile.getAssigned().toString().equals(assignedCombo.getSelectedItem() )){
									System.out.println(edlfile.getMember().toString());
									updatedMembers.addElement(edlfile.getMember());
									descCombo.setSelectedItem(edlfile.getDescription());
									dtnCombo.setSelectedItem(edlfile.getDtn().toString());
									statusCombo.setSelectedItem(edlfile.getStatus());
									flag=false;
								}
								if( flag && statusCombo.getSelectedIndex() !=-1 && edlfile.getStatus().toString().equals(statusCombo.getSelectedItem() )){
									System.out.println(edlfile.getMember().toString());
									updatedMembers.addElement(edlfile.getMember());
									descCombo.setSelectedItem(edlfile.getDescription());
									dtnCombo.setSelectedItem(edlfile.getDtn().toString());
									assignedCombo.setSelectedItem(edlfile.getAssigned());
									flag=false;
								}
							}
							if(!flag){
								membersCombo.removeAllItems();
								membersCombo.setModel(updatedMembers);
								filesCombo.removeAllItems();
								filesCombo.setModel(updatedMembers);

								System.out.println(members.getSize());
							}

						}
					}catch(Exception en ){

					}
				}
			});


			filesCombo = new JComboBox<String>(members);    
			filesCombo.setSelectedIndex(0);

			filesListScrollPane = new JScrollPane(filesCombo);  

			JButton showButton = new JButton("Show");
			JLabel llab= new JLabel("Select the Form Name");
			showButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) { 

					try{
						if (filesCombo.getSelectedIndex() != -1) {                     

							String fileName = filesCombo.getItemAt (filesCombo.getSelectedIndex()).toString();
							java.util.List<String> fields = null;
							try{
								FormParser fparser = new FormParser(fileName+".txt");
								fields = fparser.processLineByLine();
							}catch(Exception ex){
								JOptionPane.showMessageDialog(mainFrame,"Form does not exists in - C:\\Temp","Warning",JOptionPane.WARNING_MESSAGE);
							}
							//d6d9df
							if( fields != null){
								String text ="<html>";
								text= text+"<table border='1' bgcolor='#e9ecf2'><tr style='color:#000080'>"
										+ "<th>Tag Name</th>" 
										+ "<th>Length</th>" 
										+ "<th>Mandatory/Optional</th>"
										+ "<th>Delete after use</th>" 
										+ "<th>Start Position</th>"
										+ "</tr>";


								String[] fieldsHeader = {"Tag Name","Length", 
										"mandatory/optional","delete after use"};

								String[][] fieldsData = new String[fields.size()][fieldsHeader.length];
								int i =0,j=0,k=0;

								//List<Field> fieldsList = new ArrayList<Field>();
								//Map<String,String> values = new HashMap<String,String>();
								for (String field : fields){
									text=text+"<tr align='center'>";
									//Field fieldObj  = new Field();

									String[] flds = field.split(" ");

									for( String fld : flds){
										if( fld !=null && !fld.trim().equalsIgnoreCase("")) {
											if( k==0 || k==1 || k==3 || k==4 ){
												text = text +"<td>"+ fld +"</td>";
												fieldsData[i][j] = fld.trim();
												j++;
											}
											k++;
										}
									}
									String temp = tagPosMap.get(flds[0]);
									temp = temp!=null?temp:"";
									text = text +"<td>"+ temp +"</td>";
									text= text+"</tr>";
									i++;
									j=0;
									k=0;
								}
								text=text+"</html><br/>";

								fieldsLabel.setContentType("text/html"); // let the text pane know this is what you want
								fieldsLabel.setText(text);
								fieldsLabel.setEditable(false); // as before
								fieldsLabel.setBackground(null); // this is the same as a JLabel
								fieldsLabel.setBorder(null);
								//fieldScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 
								//fieldScrollPane.add(fieldsLabel);
								//fieldScrollPane.setBounds(50, 30, 300, 50);
								//fieldsPanel.add(fieldScrollPane);
								fieldsPanel.add(fieldsLabel);
								Popup popup = new Popup();
								popup.add(fieldsLabel);

								processLinks(fileName,fieldsData,fieldsHeader);

								//jScroll=new JScrollPane(jt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  

								text ="<html>";

								RLBSParser rparser = new RLBSParser("RLBS.txt");
								java.util.Set<RLBS> rlbsList = rparser.processLineByLine();

								String dtn = null;
								for (com.infy.EDLFile obj : edlfiles){
									if(obj.getMember().equals(fileName)){
										dtn = obj.getDtn().toString();
										break;
									}
								}
								text = text+"<table border='1' bgcolor='#e9ecf2'><tr style='color:#000080'>"
										+ "<th>Structure Name</th>" 
										+ "<th>Simplex/Duplex</th><th>Position</th></tr>" ; 

								for (RLBS rlbs : rlbsList ){

									String details = rlbs.getDetails(dtn) ;
									if( details !=null  ) {
										String[] parts = details.split(" ");
										text=text+"<tr><td>"+ rlbs.getName() +"</td>"
												+ "<td>"+parts[0]+"</td><td>"+parts[1]+"</td></tr>";
									}
								}
								text=text+"</tbody></table></html>";

								rlbsLabel.setContentType("text/html"); // let the text pane know this is what you want
								rlbsLabel.setText(text);
								rlbsLabel.setEditable(false); // as before
								rlbsLabel.setBackground(null); // this is the same as a JLabel
								rlbsLabel.setBorder(null);
								rlbsPanel.add(rlbsLabel);

								Popup popup1 = new Popup();
								popup1.add(fieldsLabel);

								//JOptionPane.showMessageDialog(null, "", "Search results",
								//JOptionPane.INFORMATION_MESSAGE);	  
							}
						}
					}catch(Exception ep){
						ep.printStackTrace();
						JOptionPane.showMessageDialog(mainFrame,"Unknown exception occurred, please refer log files");
					}
				}
			}); 
			
			
			controlPanel.add(llab);
			controlPanel.add(filesListScrollPane);          
			controlPanel.add(showButton); 
			mainFrame.setVisible(true); 
			mainFrame.repaint();
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(mainFrame,"File not found -- ' C:\\Temp\\EDLDUMP.txt '");
			e.printStackTrace();
		}
	}

	/**
	 * This method is to create and show GUI elements on the JFrame
	 */
	public  void createAndShowGUI() {
		//Create and set up the window.
		mainFrame = new JFrame("");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel pane = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();

		fieldsLabel = new JTextPane();
		rlbsLabel = new JTextPane();

		fieldsPanel = new JPanel();
		rlbsPanel = new JPanel();

		fieldScrollPane = new JScrollPane(fieldsLabel);
		rlbsScrollPane = new JScrollPane(fieldsLabel);

		//jScroll = new JScrollPane();
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		browsePanel = new JPanel();
		browsePanel.setLayout(new FlowLayout());

		exportButton  = new JButton("Export to Excel");
		searchButton  = new JButton("Search");
		clearButton  = new JButton("Reset");

		searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.Y_AXIS));
		searchPanel.setBorder(
				BorderFactory.createCompoundBorder( 
						BorderFactory.createTitledBorder("<html><font color='#000080'>Search</font></html>"),
						BorderFactory.createEmptyBorder(0,0,0,0)));
		pane.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx=2.0;
		pane.add(searchPanel, gbc);


		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth=1;
		pane.add(controlPanel, gbc);


		fieldScrollPane = new JScrollPane(fieldsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		fieldScrollPane.setSize(20, 100);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 2;		
		gbc.gridwidth=1;
		gbc.ipady=120;
		//gbc.weightx=3.0;
		pane.add(fieldScrollPane, gbc);

		rlbsScrollPane = new JScrollPane(rlbsPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		rlbsScrollPane.setSize(20, 100);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth=1;		
		pane.add(rlbsScrollPane, gbc);



		referenceLink = new JLabel();
		templateLink = new JLabel();
		referenceLink.setText("<HTML><U><font color='green'>Sample Reference</font></U></HTML>");
		templateLink.setText("<HTML><U><font color='blue'>Template</font></U></HTML>");

		referenceLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // To indicate the the link is click able
		templateLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


		linksPanel = new JPanel(new GridLayout(1,3,20,10));

		gbc.gridx = 0;       //aligned with button 2		
		gbc.gridy = 4;       //third row
		gbc.ipady=0;
		gbc.gridwidth = 1;   //2 columns wide

		gbc.fill = GridBagConstraints.CENTER;


		exportButton.setEnabled(false);
		linksPanel.setVisible(false);
		linksPanel.add(templateLink);
		linksPanel.add(referenceLink);
		linksPanel.add(exportButton);

		pane.add(linksPanel, gbc);

		pane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("<html><font color='#FF4500'>Form Analysis Tool</font></html>"),
						BorderFactory.createEmptyBorder(0,0,0,0)));
		//pane.add(controlPanel);
		mainFrame.add(pane);
		//Display the window.
		mainFrame.pack();
		mainFrame.setSize(1000,650);
		mainFrame.setVisible(true);
		//mainFrame.setResizable(false);
	}

	/**
	 * this Method is to fill the XL sheet with Form fields data
	 * @param table
	 * @param file
	 */
	@SuppressWarnings("resource")
	void fillData(JTable table, File file) {

		try {
			TableModel dtm = table.getModel();
			Workbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet("new sheet");
			Row row = null;
			Cell cell = null;

			row = sheet.createRow(0);
			for (int j=0;j<dtm.getColumnCount();j++) {
				row.createCell(j).setCellValue(dtm.getColumnName(j)); 
			}

			for (int i=0;i<dtm.getRowCount();i++) {
				row = sheet.createRow(i+1);
				for (int j=0;j<dtm.getColumnCount();j++) {
					cell = row.createCell(j);
					cell.setCellValue((String) dtm.getValueAt(i, j));
				}
			}

			FileOutputStream out = new FileOutputStream(file,true);
			workbook.write(out);
			out.close();
			//JOptionPane.showMessageDialog(mainFrame,"File Successfully Exported to 'C:\\Temp' Folder");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame,"File not found exception.\nPlease verify the file name and file type...");
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(mainFrame,"File IO exception.\nPlease check the file name and file type...");
		}

	}

	private void processLinks(String fileName,String[][] fieldsData,String[] fieldsHeader){
		linksPanel.setVisible(true);
		templateLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {


					XWPFDocument docx = new XWPFDocument();
					CTSectPr sectPr = docx.getDocument().getBody().addNewSectPr();
					XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(docx, sectPr);

					//write header content
					CTP ctpHeader = CTP.Factory.newInstance();
					CTR ctrHeader = ctpHeader.addNewR();
					CTText ctHeader = ctrHeader.addNewT();
					String headerText = fileName+" Form" ;
					ctHeader.setStringValue(headerText);	
					XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, docx);
					headerParagraph.setAlignment(ParagraphAlignment.RIGHT);

					XWPFParagraph[] parsHeader = new XWPFParagraph[1];
					parsHeader[0] = headerParagraph;
					policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

					//write footer content
					CTP ctpFooter = CTP.Factory.newInstance();
					CTR ctrFooter = ctpFooter.addNewR();
					CTText ctFooter = ctrFooter.addNewT();
					String footerText = "Created By Form Analysis Tool!";
					ctFooter.setStringValue(footerText);	
					XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, docx);
					XWPFParagraph[] parsFooter = new XWPFParagraph[1];
					parsFooter[0] = footerParagraph;
					policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);

					//write body content
					XWPFParagraph bodyParagraph = docx.createParagraph();
					bodyParagraph.setAlignment(ParagraphAlignment.CENTER);
					XWPFRun tmpRun = bodyParagraph.createRun();
					tmpRun.setBold(true);
					tmpRun.setText("This is body content.");
					Robot robot =new Robot();
					
					Rectangle bounds = MouseInfo.getPointerInfo().getDevice().getDefaultConfiguration().getBounds();
					robot.keyPress(KeyEvent.VK_ALT);
				    robot.keyPress(KeyEvent.VK_PRINTSCREEN);
					BufferedImage image = robot.createScreenCapture(bounds);
					
				    robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
				    robot.keyRelease(KeyEvent.VK_ALT);
							//new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(image, "png", baos);
					InputStream is = new ByteArrayInputStream(baos.toByteArray());
					//docx.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_JPEG, "my pic", Units.toEMU(500), Units.toEMU(500));

					FileOutputStream fos = new FileOutputStream(new File("C:\\Temp\\"+fileName+"_template.docx")); 
					docx.write(fos);   
					fos.close();   

					Desktop.getDesktop().open(new File("C:\\Temp\\"+fileName+"_template.docx"));

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(mainFrame,"Please close the docx file and try again!","Error",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		referenceLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\Temp\\ref.pdf"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JTable jt=new JTable(fieldsData,fieldsHeader);  
		exportButton.setEnabled(true);
		exportButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { 
				String file =(String) filesCombo.getItemAt
						(filesCombo.getSelectedIndex());

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("C:\\Temp\\"+file+"_report.xls"));

				int userSelection = fileChooser.showSaveDialog(mainFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					fillData(jt,fileToSave);
				}
			}
		});

		templateLink.setText("<HTML><U><b><font color='blue'>"+fileName+"_Template</font></b></U></HTML>");

	}
	private void resetAll(List<EDLFile> edlfiles){
		for ( EDLFile edlfile : edlfiles){
			members.addElement(edlfile.getMember());
			description.addElement(edlfile.getDescription());
			if(dtn.getIndexOf(edlfile.getDtn().toString())==-1){
				dtn.addElement(edlfile.getDtn().toString());
			}
			if(assigned.getIndexOf(edlfile.getAssigned().toString())==-1){
				assigned.addElement(edlfile.getAssigned());
			}
			if(status.getIndexOf(edlfile.getStatus().toString())==-1){
				status.addElement(edlfile.getStatus());
			}
		}
	}
}