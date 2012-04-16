
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class GlucMain extends JFrame {

	public static final String VersionsString = "V1.0";
	
	private JMenuBar MenueLeiste;
	
	private JMenu PatientenMenu;
	private JMenuItem PatientSpeichernMenuItem;
	private JMenuItem PatientErgaenzenMenuItem;
	private JMenuItem PatientenVerwaltenMenuItem;

	private JMenu GrafikMenu;
	private JMenu GrafikAuswahlMenu;
	private JMenuItem GrafikNuechternBZMenuItem;
	private JMenuItem GrafikBMIMenuItem;
	private JMenuItem GrafikHbA1cMenuItem;

	private JMenu DruckenMenu;
	private JMenuItem WerteDruckenMenuItem;
	private JMenu GrafikDruckenAuswahlMenu;
	private JMenuItem GrafikNuechternBZDruckenMenuItem;
	private JMenuItem GrafikBMIDruckenMenuItem;
	private JMenuItem GrafikHbA1cDruckenMenuItem;

	private JMenu HilfeMenu;
	private JMenuItem HilfeMenuItem;
	private JMenuItem InfoMenuItem;
	private JMenuItem KontaktMenuItem;

	private JMenu EndeMenu;
	private JMenuItem EndeMenuItem;
	
	private JMenu BMIMenu;
	private JMenuItem BMIMenuItem;

	private JButton GrafikNuechternBZButton;
	private JButton GrafikBMIButton;
	private JButton GrafikHbA1cButton;
	
	private JPanel MenueLeistePanel;
	private JPanel DatumPanel;
	private JPanel NuechternBZPanel;
	private JPanel DatumBZPanel;
	private JPanel BMIPanel;
	private JPanel HbA1cPanel;
	private JPanel BMIaussenPanel;
	private JPanel PlatzhalterMittePanel;
	private JPanel PlatzhalterUntenPanel;
	private JPanel HbA1caussenPanel;
	private JPanel LosButtonPanel;
	private JPanel LoescheButtonPanel;
	private JPanel WerteSpeichernButtonPanel;
	private JPanel WerteSpeichernUnterButtonPanel;
	private JPanel LosLoescheButtonPanel;
	
	private JLabel DatumLabel;
	private JLabel NuechternBZLabel;
	private JLabel BMILabel;
	private JLabel HbA1cLabel;
	
	private JTextField DatumField;
	private JTextField NuechternBZField;
	private JTextField BMIField;
	private JTextField HbA1cField;
	
	static String PatVorname = "";
	static String PatNachname = "";
	static String PatGeburtsdatum = "01.01.1960";
	static int PatAlter = 0;
	static int PatNuechternBZ = 0;
	static int PatBMIZehntel = 0;
	static int PatHbA1cZehntel = 0;
	
	static int NuechternBZErniedrigt = 60;
	static int NuechternBZGrenzwertig = 100;
	static int NuechternBZPathologisch = 110;

	static int HbA1cZehntelGrenzwertig = 65;
	static int HbA1cZehntelPathologisch = 75;

	static int BMIZehntelErniedrigt = 190;
	static int BMIZehntelGrenzwertig = 250;
	static int BMIZehntelPathologisch = 300;

	static char PatGeschlecht = 'm';   	

	private JLabel DatumPlatzhalterLabel;
	private JLabel NuechternBZMassLabel;
	private JLabel GewichtMassLabel;
	private JLabel HbA1cMassLabel;
	
	private DateFormat fDatum;
	private Calendar Kalender;  

	
	public enum Grafikart {
		NUECHTERNBZ, BMI, HBA1C, NUECHTERNBZ_VERLAUF, BMI_VERLAUF, HBA1C_VERLAUF
	}
	
	public static HashSet<GlucPatient> PatDB;
	public static Grafikart welcheGrafik;
	public static Boolean DBvorhanden = false;
	public static Boolean PatImSpeicher = false;
	public static String AktuellerPatientensatz = "";
	public static ArrayList<GlucMesspunkt> MesspunktefuerGrafik;

	public GlucMain() {
		
		setTitle("GlucoseTracker " + VersionsString);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Box GrundBox = Box.createVerticalBox();
		setLayout(new FlowLayout());
		setResizable(false);
		welcheGrafik = Grafikart.NUECHTERNBZ;
		
		//Menü einrichten:
		MenueLeiste = new JMenuBar();
		
		PatientenMenu = new JMenu("Patienten");
		PatientSpeichernMenuItem = new JMenuItem("Daten für neuen Patienten speichern");
		PatientErgaenzenMenuItem = new JMenuItem("Daten zu Patient hinzufügen");
		PatientenVerwaltenMenuItem = new JMenuItem("Patienten verwalten");

		GrafikMenu = new JMenu("Grafik");
		GrafikAuswahlMenu = new JMenu("Grafik anzeigen");
		GrafikNuechternBZMenuItem = new JMenuItem("Nüchtern-BZ");
		GrafikBMIMenuItem = new JMenuItem("BMI");
		GrafikHbA1cMenuItem = new JMenuItem("HbA1c");

		DruckenMenu = new JMenu("Drucken");
		WerteDruckenMenuItem = new JMenuItem("Werte drucken");
		GrafikDruckenAuswahlMenu = new JMenu("Grafik drucken");
		GrafikNuechternBZDruckenMenuItem = new JMenuItem("Nüchtern-BZ");
		GrafikBMIDruckenMenuItem = new JMenuItem("BMI");
		GrafikHbA1cDruckenMenuItem = new JMenuItem("HbA1c");

		HilfeMenu = new JMenu("Hilfe");
		HilfeMenuItem = new JMenuItem("Hilfe");
		InfoMenuItem = new JMenuItem("Über GlucoseTracker");
		KontaktMenuItem = new JMenuItem("Kontakt");

		EndeMenu = new JMenu("Ende");
		EndeMenuItem = new JMenuItem("Programm beenden");
	
		BMIMenu = new JMenu("BMI-Rechner");
		BMIMenuItem = new JMenuItem("BMI berechnen");

		MenueLeistePanel = new JPanel();
		MenueLeistePanel.setLayout(new FlowLayout(FlowLayout.LEADING));

		PatientenMenu.add(PatientSpeichernMenuItem);
		PatientenMenu.add(PatientErgaenzenMenuItem);
		PatientenMenu.add(PatientenVerwaltenMenuItem);
		GrafikMenu.add(GrafikAuswahlMenu);
		GrafikAuswahlMenu.add(GrafikNuechternBZMenuItem);
		GrafikAuswahlMenu.add(GrafikBMIMenuItem);
		GrafikAuswahlMenu.add(GrafikHbA1cMenuItem);
		DruckenMenu.add(WerteDruckenMenuItem);
		DruckenMenu.add(GrafikDruckenAuswahlMenu);
		GrafikDruckenAuswahlMenu.add(GrafikNuechternBZDruckenMenuItem);
		GrafikDruckenAuswahlMenu.add(GrafikBMIDruckenMenuItem);
		GrafikDruckenAuswahlMenu.add(GrafikHbA1cDruckenMenuItem);
		HilfeMenu.add(HilfeMenuItem);
		HilfeMenu.add(InfoMenuItem);
		HilfeMenu.add(KontaktMenuItem);
		EndeMenu.add(EndeMenuItem);
		BMIMenu.add(BMIMenuItem);
		MenueLeiste.add(PatientenMenu);
		MenueLeiste.add(GrafikMenu);
		MenueLeiste.add(DruckenMenu);
		MenueLeiste.add(HilfeMenu);
		MenueLeiste.add(EndeMenu);
		MenueLeiste.add(Box.createHorizontalGlue());
		MenueLeiste.add(BMIMenu);
		
		add(GrundBox);

		//Panels anordnen:		
		DatumPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		NuechternBZPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		DatumBZPanel = new JPanel();
		DatumBZPanel.setLayout(new BoxLayout(DatumBZPanel, BoxLayout.X_AXIS));
		PlatzhalterMittePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		BMIPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		BMIaussenPanel = new JPanel();
		BMIaussenPanel.setLayout(new BoxLayout(BMIaussenPanel, BoxLayout.X_AXIS));
		PlatzhalterUntenPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		HbA1cPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		HbA1caussenPanel = new JPanel();
		HbA1caussenPanel.setLayout(new BoxLayout(HbA1caussenPanel, BoxLayout.X_AXIS));
		LosButtonPanel = new JPanel();
		WerteSpeichernButtonPanel = new JPanel();
		WerteSpeichernUnterButtonPanel = new JPanel();
		LoescheButtonPanel = new JPanel();
		LosLoescheButtonPanel = new JPanel();
		LosLoescheButtonPanel.setLayout(new BoxLayout(LosLoescheButtonPanel, BoxLayout.X_AXIS));
		

		//Beschriftung der Panels:
		DatumLabel = new JLabel("Datum: ");
		NuechternBZLabel = new JLabel("Nüchtern-BZ (mg/dl): ");
		BMILabel = new JLabel("BMI (kg/m²): ");
		HbA1cLabel = new JLabel("HbA1c (%): ");
		DatumPanel.add(DatumLabel);

		NuechternBZPanel.add(NuechternBZLabel);
		BMIPanel.add(BMILabel);
		HbA1cPanel.add(HbA1cLabel);
		
		//Textfelder in den Panels:
		fDatum =  new SimpleDateFormat("dd.MM.yyyy");
		Kalender = Calendar.getInstance();
		DatumField = new JTextField(8);
		DatumField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		DatumField.setText(fDatum.format(Kalender.getTime()));
		NuechternBZField = new JTextField(5);
		NuechternBZField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		BMIField = new JTextField(5);
		BMIField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		HbA1cField = new JTextField(5);
		HbA1cField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
		DatumPanel.add(DatumField);
		NuechternBZPanel.add(NuechternBZField);
		BMIPanel.add(BMIField);
		HbA1cPanel.add(HbA1cField);
		
		//Platzhalter für die Normgrenzen:
		DatumPlatzhalterLabel = new JLabel(" ");   //damit das DatumField nicht zu hoch wird
		NuechternBZMassLabel = new JLabel("=");
		GewichtMassLabel = new JLabel("=");
		HbA1cMassLabel = new JLabel("=");
		DatumPanel.add(DatumPlatzhalterLabel);
		NuechternBZPanel.add(NuechternBZMassLabel);
		BMIPanel.add(GewichtMassLabel);
		HbA1cPanel.add(HbA1cMassLabel);
		
		//Buttons, um direkt zu den Grafiken zu gelangen:
		GrafikNuechternBZButton = new JButton("Grafik");
		GrafikNuechternBZButton.setEnabled(false);
		GrafikBMIButton = new JButton("Grafik");
		GrafikBMIButton.setEnabled(false);
		GrafikHbA1cButton = new JButton("Grafik");
		GrafikHbA1cButton.setEnabled(false);

		//Buttons zur Anzeige der Perzentilen bzw. Löschen der Felder:
		JButton LosButton = new JButton("Auswerten"); 
		getRootPane().setDefaultButton(LosButton);
		JButton WerteSpeichernButton = new JButton("Speichern"); 
		JButton WerteSpeichernUnterButton = new JButton("Speichern unter..."); 
		JButton LoescheButton = new JButton("Lösche Felder"); 

		LosButtonPanel.add(LosButton);
		WerteSpeichernButtonPanel.add(WerteSpeichernButton);
		WerteSpeichernUnterButtonPanel.add(WerteSpeichernUnterButton);
		LoescheButtonPanel.add(LoescheButton);

		//Alles auf Formular anordnen:
		setJMenuBar(MenueLeiste);
		DatumBZPanel.add(DatumPanel);
		DatumBZPanel.add(Box.createHorizontalGlue());
		DatumBZPanel.add(NuechternBZPanel);
		DatumBZPanel.add(GrafikNuechternBZButton);
		GrundBox.add(DatumBZPanel);
		BMIaussenPanel.add(PlatzhalterMittePanel);
		BMIaussenPanel.add(Box.createHorizontalGlue());
		BMIaussenPanel.add(BMIPanel);
		BMIaussenPanel.add(GrafikBMIButton);
		GrundBox.add(BMIaussenPanel);
		HbA1caussenPanel.add(PlatzhalterUntenPanel);
		HbA1caussenPanel.add(Box.createHorizontalGlue());
		HbA1caussenPanel.add(HbA1cPanel);
		HbA1caussenPanel.add(GrafikHbA1cButton);
		GrundBox.add(HbA1caussenPanel);
		LosLoescheButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		LosLoescheButtonPanel.add(LosButtonPanel);
		LosLoescheButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		LosLoescheButtonPanel.add(WerteSpeichernButtonPanel);
		LosLoescheButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		LosLoescheButtonPanel.add(WerteSpeichernUnterButtonPanel);
		LosLoescheButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		LosLoescheButtonPanel.add(LoescheButtonPanel);
		LosLoescheButtonPanel.add(Box.createRigidArea(new Dimension(5,0)));
		GrundBox.add(LosLoescheButtonPanel);
		pack();
		
		
		//Actionlistener für die beiden Buttons:
		class LosButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				//Inhalt der Textfields einlesen:

				try {
					PatNuechternBZ = Integer.valueOf(NuechternBZField.getText()).intValue();				
					GrafikNuechternBZButton.setEnabled(true);
					if((PatNuechternBZ >= NuechternBZGrenzwertig) & (PatNuechternBZ <= NuechternBZPathologisch)) 
					{
						NuechternBZField.setBackground(new Color(255,255,100));		//gelb
					}
					else if(PatNuechternBZ > NuechternBZPathologisch)
					{
						NuechternBZField.setBackground(new Color(255,100,100)); 	//rot
					}
					else if(PatNuechternBZ < NuechternBZErniedrigt)
					{
						NuechternBZField.setBackground(new Color(150,150,255));				//blau
					}
					else NuechternBZField.setBackground(new Color(100,255,100));	//grün
				}
				catch(Exception ex) {
					PatNuechternBZ = 0;
					NuechternBZField.setText("");
				}	
				
				try {PatBMIZehntel 
					= (int)((Float.valueOf(BMIField.getText().replace(',','.')).floatValue() * 10)+0.5);
					GrafikBMIButton.setEnabled(true);
					if((PatBMIZehntel >= BMIZehntelGrenzwertig) & (PatBMIZehntel <= BMIZehntelPathologisch)) 
					{
						BMIField.setBackground(new Color(255,255,100));			//gelb
					}
					else if(PatBMIZehntel > BMIZehntelPathologisch)
					{
						BMIField.setBackground(new Color(255,100,100));			//rot
					}
					else if(PatBMIZehntel < BMIZehntelErniedrigt)
					{
						BMIField.setBackground(new Color(150,150,255));			//blau
					}
					else BMIField.setBackground(new Color(100,255,100));		//grün
				}
				catch(Exception ex) {
					PatBMIZehntel = 0;
					BMIField.setText("");
				}
				
				try {PatHbA1cZehntel 
					= (int)((Float.valueOf(HbA1cField.getText().replace(',','.')).floatValue() * 10)+0.5);
					GrafikHbA1cButton.setEnabled(true);
					if((PatHbA1cZehntel >= HbA1cZehntelGrenzwertig) & (PatHbA1cZehntel <= HbA1cZehntelPathologisch)) 
					{
						HbA1cField.setBackground(new Color(255,255,100));		//gelb
					}
					else if(PatHbA1cZehntel > HbA1cZehntelPathologisch)
					{
						HbA1cField.setBackground(new Color(255,100,100));		//rot
					}
					else HbA1cField.setBackground(new Color(100,255,100));		//grün
				}
				catch(Exception ex) {
					PatHbA1cZehntel = 0;
					HbA1cField.setText("");
				}
								
				pack();   //Größe des Fensters an geänderte Beschriftung anpassen
			}
		}
		
		class LoescheButtonListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					DatumField.setText("");
					NuechternBZField.setText("");
					BMIField.setText("");
					HbA1cField.setText("");
					NuechternBZMassLabel.setText("=");
					GewichtMassLabel.setText("=");
					HbA1cMassLabel.setText("=");
					NuechternBZField.setBackground(new JTextField().getBackground());
					BMIField.setBackground(new JTextField().getBackground());
					HbA1cField.setBackground(new JTextField().getBackground());
					GrafikNuechternBZButton.setEnabled(false);
					GrafikBMIButton.setEnabled(false);
					GrafikHbA1cButton.setEnabled(false);
					pack();   //Größe des Fensters an geänderte Beschriftung anpassen
				}
		}

	
		LosButton.addActionListener(new LosButtonListener());
		LoescheButton.addActionListener(new LoescheButtonListener());

		//Actionlistener für Menü-Items:

		class PatientSpeichernMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				//Inhalt der Textfields einlesen:

				try {PatNuechternBZ = Integer.valueOf(NuechternBZField.getText()).intValue();}
				catch(Exception ex) {
					PatNuechternBZ = 0;
					NuechternBZField.setText("0");
				}	
				try {PatBMIZehntel 
					= (int)((Float.valueOf(BMIField.getText().replace(',','.')).floatValue() * 10)+0.5);}
				catch(Exception ex) {
					PatBMIZehntel = 0;
					BMIField.setText("0");
				}
				try {PatHbA1cZehntel 
					= (int)((Float.valueOf(HbA1cField.getText().replace(',','.')).floatValue() * 10)+0.5);}
				catch(Exception ex) {
					PatHbA1cZehntel = 0;
					HbA1cField.setText("0");
				}
								
				pack();   //Größe des Fensters an geänderte Beschriftung anpassen

				GlucPatEingabe PatEingabe = new GlucPatEingabe();
			}
		}

		class PatientErgaenzenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				//Inhalt der Textfields einlesen:

				try {PatNuechternBZ = Integer.valueOf(NuechternBZField.getText()).intValue();}
				catch(Exception ex) {
					PatNuechternBZ = 0;
					NuechternBZField.setText("0");
				}	
				try {PatBMIZehntel 
					= (int)((Float.valueOf(BMIField.getText().replace(',','.')).floatValue() * 10)+0.5);}
				catch(Exception ex) {
					PatBMIZehntel = 0;
					BMIField.setText("0");
				}
				try {PatHbA1cZehntel 
					= (int)((Float.valueOf(HbA1cField.getText().replace(',','.')).floatValue() * 10)+0.5);}
				catch(Exception ex) {
					PatHbA1cZehntel = 0;
					HbA1cField.setText("0");
				}
								
				pack();   //Größe des Fensters an geänderte Beschriftung anpassen

				GlucPatAnzeige AnzeigeFenster = new GlucPatAnzeige();
			}
		}

		class PatientenVerwaltenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GlucPatAnzeige AnzeigeFenster = new GlucPatAnzeige();
			}
		}

		class GrafikNuechternBZMenuItemListener implements ActionListener {
			Graphics g;
			public void actionPerformed(ActionEvent e) {
				welcheGrafik = Grafikart.NUECHTERNBZ;
				GlucGrafik BildschirmGrafik = new GlucGrafik();
			}
		}
		
		class GrafikGewichtMenuItemListener implements ActionListener {
			Graphics g;
			public void actionPerformed(ActionEvent e) {
				welcheGrafik = Grafikart.BMI;
				GlucGrafik BildschirmGrafik = new GlucGrafik();
			}
		}

		class GrafikHbA1cMenuItemListener implements ActionListener {
			Graphics g;
			public void actionPerformed(ActionEvent e) {
				welcheGrafik = Grafikart.HBA1C;
				GlucGrafik BildschirmGrafik = new GlucGrafik();
			}
		}

		class GrafikNuechternBZDruckenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				welcheGrafik = Grafikart.NUECHTERNBZ;
				GlucGrafik DruckGrafikFrame = new GlucGrafik();
				DruckGrafikFrame.druckeGrafikFrame();				
			}
		}

		class GrafikGewichtDruckenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				welcheGrafik = Grafikart.BMI;
				GlucGrafik DruckGrafikFrame = new GlucGrafik();
				DruckGrafikFrame.druckeGrafikFrame();				
			}
		}
		
		class GrafikHbA1cDruckenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				welcheGrafik = Grafikart.HBA1C;
				GlucGrafik DruckGrafikFrame = new GlucGrafik();
				DruckGrafikFrame.druckeGrafikFrame();				
			}
		}

		class HilfeMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				URI hilfeURI;
				File hilfeDatei = new File("GlucoseTracker.html");
				hilfeURI = hilfeDatei.toURI();
				try {
					GlucHilfe HilfeFenster = new GlucHilfe(hilfeURI.toURL());
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(null, "Hilfedatei nicht gefunden");
				}
			}
		}
		
		class InfoMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(InfoMenuItem, 
						"Programm GlucoseTracker " + VersionsString + ": (c) 2011 - 2012 Christina Czeschik." + 
						"\n" + 
						"Nicht zu medizinischen Zwecken.",
						"Info über GlucoseTracker " + VersionsString,
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		class KontaktMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop
						.getDesktop()
						.mail(
								new URI("mailto:jcczeschik@gmail.com?subject=GlucoseTracker"));
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(KontaktMenuItem, 
							"Versenden der E-Mail fehlgeschlagen");
				}
				
			}
		}
		
		class EndeMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}

		class BMIMenuItemListener implements ActionListener {
			
			class BMISchliessenListener implements WindowListener {

				public void windowActivated(WindowEvent arg0) {
				}

				public void windowClosed(WindowEvent arg0) {
				}

				public void windowClosing(WindowEvent arg0) {
					BMIField.setText(new DecimalFormat("#.##").format(((float)PatBMIZehntel)/10));
				}

				public void windowDeactivated(WindowEvent arg0) {
				}

				public void windowDeiconified(WindowEvent arg0) {
				}

				public void windowIconified(WindowEvent arg0) {
				}

				public void windowOpened(WindowEvent arg0) {
				}

			}
			public void actionPerformed(ActionEvent e) {
				GlucBMIRechner BMIFrame = new GlucBMIRechner();
				BMIFrame.addWindowListener(new BMISchliessenListener());
			}
		}

		PatientenVerwaltenMenuItem.addActionListener(new PatientenVerwaltenMenuItemListener());
		PatientErgaenzenMenuItem.addActionListener(new PatientErgaenzenMenuItemListener());
		PatientSpeichernMenuItem.addActionListener(new PatientSpeichernMenuItemListener());
		GrafikNuechternBZMenuItem.addActionListener(new GrafikNuechternBZMenuItemListener());
		GrafikBMIMenuItem.addActionListener(new GrafikGewichtMenuItemListener());
		GrafikHbA1cMenuItem.addActionListener(new GrafikHbA1cMenuItemListener());
		GrafikNuechternBZButton.addActionListener(new GrafikNuechternBZMenuItemListener());
		GrafikBMIButton.addActionListener(new GrafikGewichtMenuItemListener());
		GrafikHbA1cButton.addActionListener(new GrafikHbA1cMenuItemListener());
		WerteDruckenMenuItem.addActionListener(new GlucPrinter());
		GrafikNuechternBZDruckenMenuItem.addActionListener(new GrafikNuechternBZDruckenMenuItemListener());
		GrafikBMIDruckenMenuItem.addActionListener(new GrafikGewichtDruckenMenuItemListener());
		GrafikHbA1cDruckenMenuItem.addActionListener(new GrafikHbA1cDruckenMenuItemListener());
		HilfeMenuItem.addActionListener(new HilfeMenuItemListener());
		InfoMenuItem.addActionListener(new InfoMenuItemListener());
		KontaktMenuItem.addActionListener(new KontaktMenuItemListener());
		EndeMenuItem.addActionListener(new EndeMenuItemListener());
		BMIMenuItem.addActionListener(new BMIMenuItemListener());
		
		WerteSpeichernButton.addActionListener(new PatientSpeichernMenuItemListener());
		WerteSpeichernUnterButton.addActionListener(new PatientErgaenzenMenuItemListener());
				
		pack();
		setLocation(400,300);
		setVisible(true);
		
		File ladeDatei = new File("PatDB.gt");
		try {
			FileInputStream fis = new FileInputStream(ladeDatei);
			ObjectInputStream ois = new ObjectInputStream(fis);
			GlucMain.PatDB = (HashSet<GlucPatient>) ois.readObject();
			ois.close();
			DBvorhanden = true;
		}
		catch (Exception ex) {
			PatDB = new HashSet<GlucPatient>();
			JOptionPane.showMessageDialog(MenueLeiste, 
					"Patientendatenbank nicht gefunden - neue Datenbank wird erstellt.");
			DBvorhanden = true;
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GlucMain();
			}
		});

	}


}
