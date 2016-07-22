package com.infy;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@SuppressWarnings("serial")
public class MainClass  extends JFrame  {

	private JFrame mainFrame;
	private JPanel controlPanel;
	private JPanel browsePanel;
	private JTextPane fieldsLabel;
	private JTextPaneTip rlbsLabel;
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
	private String fileName;
	private String[][] fieldsData= null;
	private String[] fieldsHeader= {"Tag Name","Length", "mandatory/optional","delete after use"};;

	DefaultComboBoxModel<String> members = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> updatedMembers = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> description = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> updatedDesc = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> dtn = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> updatedDtn = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> assigned = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> updatedAssigned = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> status = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<String> updatedStatus = new DefaultComboBoxModel<String>();
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
	public HashMap<String,String> toolTips = new HashMap<String,String>();

	String selValue="";
	
	public MainClass(){
		createAndShowGUI();
	}

	public static void main(String[] args){
		try {
			//Metal	Nimbus	CDE/Motif	Windows	Windows Classic
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
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
			toolTips = excelParser.getToolTips();
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

			radForm.setMnemonic(KeyEvent.VK_C);
			radDesc.setMnemonic(KeyEvent.VK_M);
			radDtn.setMnemonic(KeyEvent.VK_P);

			radForm.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {         
					searchlabel.setText("<html><b><font color='#000080'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +radForm.getText()+":</font></b></html>");
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
					searchlabel.setText("<html><b><font color='#000080'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +radDesc.getText()+":</font></b></html>");
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
					searchlabel.setText("<html><b><font color='#000080'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +radDtn.getText()+":</font></b></html>");
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
					searchlabel.setText("<html><b><font color='#000080'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +radAssign.getText()+":</font></b></html>");
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
					searchlabel.setText("<html><b><font color='#000080'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " +radStatus.getText()+":</font></b></html>");
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
			filterPanel.setBorder(BorderFactory.createTitledBorder("<html><font color='#000080'>Filter Criteria</font></html>"));
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

			membersCombo.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					selValue = "membersCombo";
					for ( EDLFile edlfile : edlfiles){
						if( membersCombo.getSelectedIndex() !=-1 && edlfile.getMember().equals(membersCombo.getSelectedItem())){
							descCombo.setSelectedItem(edlfile.getDescription());
							dtnCombo.setSelectedItem(edlfile.getDtn().toString());
							assignedCombo.setSelectedItem(edlfile.getAssigned());
							statusCombo.setSelectedItem(edlfile.getStatus());
						}
					}
				}
			});

			descCombo.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					selValue = "descCombo";
					for ( EDLFile edlfile : edlfiles){

						if( descCombo.getSelectedIndex() !=-1 && edlfile.getDescription().equals(descCombo.getSelectedItem())){
							membersCombo.setSelectedItem(edlfile.getMember());
							dtnCombo.setSelectedItem(edlfile.getDtn().toString());
							assignedCombo.setSelectedItem(edlfile.getAssigned());
							statusCombo.setSelectedItem(edlfile.getStatus());
						}
					}
				}
			});

			dtnCombo.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					selValue = "dtnCombo";
				}
			});

			assignedCombo.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					selValue = "assignedCombo";
				}
			});

			statusCombo.addActionListener (new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					selValue = "statusCombo";
				}
			});
			//System.out.println(selValue);

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
							updatedMembers = new DefaultComboBoxModel<String>();
							updatedDesc = new DefaultComboBoxModel<String>();
							updatedDtn = new DefaultComboBoxModel<String>();
							updatedAssigned = new DefaultComboBoxModel<String>();
							updatedStatus = new DefaultComboBoxModel<String>();;
							if(selValue.equalsIgnoreCase("dtnCombo") ){
								String ii = dtnCombo.getSelectedItem().toString();
								for ( int i=0;i<edlfiles.size();i++){										
									if(edlfiles.get(i).getDtn().toString().equalsIgnoreCase(ii)){
										if(updatedMembers.getIndexOf(edlfiles.get(i).getMember())==-1){
											//System.out.println(edlfiles.get(i).getDtn()+ ":" +ii+":"+edlfiles.get(i).getMember());
											updatedMembers.addElement(edlfiles.get(i).getMember());
											updatedDesc.addElement(edlfiles.get(i).getDescription());
											if(updatedStatus.getIndexOf(edlfiles.get(i).getStatus().toString())==-1){
												updatedStatus.addElement(edlfiles.get(i).getStatus());
											}
											if(updatedAssigned.getIndexOf(edlfiles.get(i).getAssigned().toString())==-1){
												updatedAssigned.addElement(edlfiles.get(i).getAssigned());
											}
										}
									}
								}
							}else if (selValue.equalsIgnoreCase("assignedCombo") ){
								String selValue = (String) assignedCombo.getSelectedItem() ;
								for ( EDLFile edlfile : edlfiles){
									if ( edlfile.getAssigned().equalsIgnoreCase(selValue) ){
										if(updatedMembers.getIndexOf(edlfile.getMember())==-1){
											updatedMembers.addElement(edlfile.getMember());
											updatedDesc.addElement(edlfile.getDescription());
											if(updatedDtn.getIndexOf(edlfile.getDtn().toString())==-1){
												updatedDtn.addElement(edlfile.getDtn().toString());
											}
											if(updatedStatus.getIndexOf(edlfile.getStatus().toString())==-1){
												updatedStatus.addElement(edlfile.getStatus());
											}
										}
									}
								}
							}else if (selValue.equalsIgnoreCase("statusCombo") ){
								String selValue=(String) statusCombo.getSelectedItem();
								for ( EDLFile edlfile : edlfiles){
									if ( edlfile.getStatus().equalsIgnoreCase(selValue )){
										if(updatedMembers.getIndexOf(edlfile.getMember())==-1){
											updatedMembers.addElement(edlfile.getMember());
											updatedDesc.addElement(edlfile.getDescription());
											if(updatedDtn.getIndexOf(edlfile.getDtn().toString())==-1){
												updatedDtn.addElement(edlfile.getDtn().toString());
											}	
											if(updatedAssigned.getIndexOf(edlfile.getAssigned().toString())==-1){
												updatedAssigned.addElement(edlfile.getAssigned());
											}
										}
									}
								}
							}
							
							if ( updatedMembers.getSize() >0){
								//System.out.println("Size of updated list: "+updatedMembers.getSize());
								filesCombo.removeAllItems();
								filesCombo.setModel(updatedMembers);
								membersCombo.setModel(updatedMembers);
								descCombo.removeAllItems();
								descCombo.setModel(updatedDesc);
								if( updatedDtn.getSize() >0 ){
									dtnCombo.removeAllItems();
									dtnCombo.setModel(updatedDtn);
								}
								if( updatedAssigned.getSize() >0){
									assignedCombo.removeAllItems();
									assignedCombo.setModel(updatedAssigned);
								}
								if( updatedStatus.getSize() >0){
									statusCombo.removeAllItems();
									statusCombo.setModel(updatedStatus);
								}
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

							fileName = filesCombo.getItemAt (filesCombo.getSelectedIndex()).toString();
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


								

								fieldsData = new String[fields.size()][fieldsHeader.length];
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
										+ "<th>Print Settings</th></tr>" ; 

								for (RLBS rlbs : rlbsList ){

									String details = rlbs.getDetails(dtn) ;
									if( details !=null  ) {
										String[] parts = details.split(" ");
										text=text+"<tr><td>"+ rlbs.getName() +"</td>"
												+ "<td>"+parts[0]+" &nbsp;"+parts[1];
										if(parts.length >2){
											for(int l=2;l<parts.length;l++){
												if(!parts[l].equalsIgnoreCase("") 
														&& !parts[l].equalsIgnoreCase(")") 
														&& !parts[l].equalsIgnoreCase("-"))
												text=text+" &nbsp;"+parts[l];
											}
										}												
										text=text+"</td></tr>";
									}
								}
								text=text+"</tbody></table></html>";
								rlbsLabel.setContentType("text/html"); // let the text pane know this is what you want
								rlbsLabel.setText(text);
								rlbsLabel.setEditable(false); // as before
								rlbsLabel.setBackground(null); // this is the same as a JLabel
								rlbsLabel.setBorder(null);
								rlbsLabel.setToolTipText("");
								rlbsPanel.add(rlbsLabel);
								rlbsPanel.repaint();
								Popup popup1 = new Popup();
								popup1.add(rlbsLabel);

								//JOptionPane.showMessageDialog(null, "", "Search results",
								//JOptionPane.INFORMATION_MESSAGE);	  
							}

						}
						
						resetAll(edlfiles);
						membersCombo.setModel(members);
						filesCombo.setModel(members);
						descCombo.setModel(description);
						dtnCombo.setModel(dtn);
						assignedCombo.setModel(assigned);
						statusCombo.setModel(status);
						
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
			
			JOptionPane.showMessageDialog(mainFrame,
				     "Make sure you place correct files in C:\\Temp Folder \n"
				     + "'FormsList.xlsx' \n'RLBS.txt' \nIndividual form files...",
				     "Message!", JOptionPane.PLAIN_MESSAGE);
			
			StringBuilder sb = new StringBuilder(e.toString());
		    for (StackTraceElement ste : e.getStackTrace()) {
		        sb.append("\n\tat ");
		        sb.append(ste);
		    }
		    String trace = sb.toString();
			JOptionPane.showMessageDialog(mainFrame,
				     "An unexpected error has occurred:\n" + e.getMessage() + '\n' +
				     trace +  
				     "\n\nPlease send this error to : " + "pavankumar.cheguri@infosys.com" + 
				     "\nThanks for your help.",
				     "Error", JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.exit( 0 );  
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
		rlbsLabel = new JTextPaneTip();

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
		referenceLink.setText("<HTML><U><b><font color='#000080'>Mock-up</font></b></U></HTML>");
		templateLink.setText("<HTML><U><b><font color='#000080'>Template</font></b></U></HTML>");

		referenceLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // To indicate the the link is click able
		templateLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		templateLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\Temp\\Template\\"+fileName+".pdf"));

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(mainFrame,"The file: C:\\Temp\\Template\\"+ fileName +".pdf doesn't exist.","Error",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		
		referenceLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\Temp\\Mockup\\"+fileName+".pdf"));

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(mainFrame,"The file: C:\\Temp\\Mockup\\"+ fileName +".pdf doesn't exist.","Error",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		
		
		
		//exportButton.setEnabled(true);
		exportButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { 
				String file =(String) filesCombo.getItemAt
						(filesCombo.getSelectedIndex());
				JTable jt=new JTable(fieldsData,fieldsHeader);  

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("C:\\Temp\\Report\\"+file+"_report.xls"));

				int userSelection = fileChooser.showSaveDialog(mainFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					//System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					fillData(jt,fileToSave);
				}
			}
		});

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
						BorderFactory.createTitledBorder(null,"<html><font color='#000080' size='4'>Form Analysis Tool</font></html>",TitledBorder.CENTER ,TitledBorder.BELOW_TOP ),
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
		/*	templateLink.addMouseListener(new MouseAdapter() {
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

					Desktop.getDesktop().browse(new File("C:\\Temp\\Template\\"+fileName+".pdf").toURI());

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(mainFrame,"The file: C:\\Temp\\Template\\"+ fileName +".pdf doesn't exist.","Error",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} /*catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
		});
		

		referenceLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().open(new File("C:\\Temp\\Mockup\\"+fileName+".pdf"));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(mainFrame,"The file: C:\\Temp\\Mockup\\"+ fileName +".pdf doesn't exist.","Error",JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		*/

		exportButton.setEnabled(true);
		/*exportButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) { 
				String file =(String) filesCombo.getItemAt
						(filesCombo.getSelectedIndex());

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("C:\\Temp\\"+file+"_report.xls"));

				int userSelection = fileChooser.showSaveDialog(mainFrame);

				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					//System.out.println("Save as file: " + fileToSave.getAbsolutePath());
					fillData(jt,fileToSave);
				}
			}
		});
*/
		//templateLink.setText("<HTML><U><b><font color='#000080'>"+fileName+"_Template</font></b></U></HTML>");

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
	

	public class JTextPaneTip extends JTextPane{

		@Override
		public String getToolTipText(MouseEvent event){

			String out="";
			try {
				//System.out.println(this.getText(Utilities.getWordStart(this ,viewToModel(event.getPoint())), Utilities.getWordEnd(this ,viewToModel(event.getPoint()))));
				String tip = this.getDocument().getText(0, this.getDocument().getLength()).substring(Utilities.getWordStart(this ,viewToModel(event.getPoint())), Utilities.getWordEnd(this ,viewToModel(event.getPoint())));
				out = toolTips.get(tip);

			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return out;

		}
}
}
