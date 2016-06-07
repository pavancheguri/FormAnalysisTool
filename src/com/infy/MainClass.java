package com.infy;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

@SuppressWarnings("serial")
public class MainClass  extends JFrame {

	private JFrame mainFrame;
	private JPanel controlPanel;
	private JPanel browsePanel;
	private JLabel fieldsLabel;
	private JLabel rlbsLabel;
	private JButton exportButton;
	private JLabel templateLink;
	private JLabel referenceLink;
	private JPanel linksPanel;

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
			final DefaultComboBoxModel<String> fileNames = new DefaultComboBoxModel<String>();
			FileParser parser = new FileParser("C:\\Temp\\EDLDUMP.txt");
			List<com.infy.File> objs = parser.processLineByLine();

			for ( com.infy.File obj : objs ){
				fileNames.addElement(obj.getName());
			}

			final JComboBox<String> filesCombo = new JComboBox<String>(fileNames);    
			filesCombo.setSelectedIndex(0);

			JScrollPane filesListScrollPane = new JScrollPane(filesCombo);  

			JButton showButton = new JButton("Generate");
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

							if( fields != null){
								String text ="<html>";
								text= text+"<table border='1'><thead>"
										+ "<th>Tag Name</th>" 
										+ "<th>Length</th>" 
										+ "<th>mandatory/optional</th>"
										+ "<th>delete after use</th>"
										+ "</thead>";


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
									text= text+"</tr>";
									i++;
									j=0;
									k=0;
								}
								text=text+"</html><br/>";
								fieldsLabel.setText(text);


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

											FileOutputStream fos = new FileOutputStream(new File("C:\\Temp\\"+fileName+"_template.docx")); 
											docx.write(fos);   
											fos.close();   

											Desktop.getDesktop().open(new File("C:\\Temp\\"+fileName+"_template.docx"));

										} catch (IOException e1) {
											JOptionPane.showMessageDialog(mainFrame,"Please close the docx file and try again!","Error",JOptionPane.ERROR_MESSAGE);
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

								templateLink.setText("<HTML><U>"+fileName+"_Template</U></HTML>");

								//jScroll=new JScrollPane(jt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  

								text ="<html>";

								RLBSParser rparser = new RLBSParser("RLBS.txt");
								java.util.Set<RLBS> rlbsList = rparser.processLineByLine();

								String dtn = null;
								for (com.infy.File obj : objs){
									if(obj.getName().equals(fileName)){
										dtn = obj.getDtn();
										break;
									}
								}
								text = text+"<table border='1'><tr>"
										+ "<th>Structure Name</th>" 
										+ "<th>Details</th></tr>" ; 

								for (RLBS rlbs : rlbsList ){

									String details = rlbs.getDetails(dtn) ;
									if( details !=null  ) {
										text=text+"<tr><td>"+ rlbs.getName() +"</td><td>"+details+"</td></tr>";
									}
								}
								text=text+"</tbody></table></html>";
								rlbsLabel.setText(text);
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
		mainFrame = new JFrame("Form Analysis Tool!");
		mainFrame.setSize(100, 500);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel pane = new JPanel();

		fieldsLabel = new JLabel("Fields",JLabel.CENTER);
		rlbsLabel = new JLabel("Values",JLabel.CENTER);
		//jScroll = new JScrollPane();
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());
		browsePanel = new JPanel();
		browsePanel.setLayout(new FlowLayout());

		exportButton  = new JButton("Export to Excel");

		pane.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth=2;
		gbc.weightx = 1;
		gbc.weighty = 1;
		pane.add(controlPanel, gbc);

		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth=1;
		pane.add(fieldsLabel, gbc);

		referenceLink = new JLabel();
		templateLink = new JLabel();
		referenceLink.setText("<HTML><U>Sample Reference</U></HTML>");
		templateLink.setText("<HTML><U>Template</U></HTML>");

		referenceLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // To indicate the the link is click able
		templateLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth=2;

		linksPanel = new JPanel(new GridLayout(2,1,0,20));

		linksPanel.add(templateLink);
		linksPanel.add(referenceLink);

		linksPanel.setVisible(false);
		pane.add(linksPanel,gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth=2;		
		pane.add(rlbsLabel, gbc);

		exportButton.setEnabled(false);
		gbc.fill = GridBagConstraints.CENTER;
		gbc.weighty = 0.0;   //request any extra vertical space
		gbc.anchor = GridBagConstraints.PAGE_END; //bottom of space
		gbc.insets = new Insets(10,0,0,0);  //top padding
		gbc.gridx = 0;       //aligned with button 2
		gbc.gridwidth = 2;   //2 columns wide
		gbc.gridy = 4;       //third row
		pane.add(exportButton, gbc);

		pane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Form Analysis Tool"),
						BorderFactory.createEmptyBorder(10,10,10,10)));
		//pane.add(controlPanel);
		mainFrame.add(pane);
		//Display the window.
		mainFrame.pack();
		mainFrame.setSize(700,500);
		mainFrame.setVisible(true);
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

}