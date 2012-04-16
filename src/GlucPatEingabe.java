import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.*;
import java.util.*;

import javax.swing.*;

public class GlucPatEingabe extends JFrame {

	private JPanel VornamePanel;
	private JPanel NachnamePanel;
	private JPanel NamePanel;
	private JPanel mwButtonPanel;
	private JPanel NuechternBZPanel;
	private JPanel mwNuechternBZPanel;
	private JPanel GeburtsdatumPanel;
	private JPanel BMIPanel;
	private JPanel GeburtsdatumBMIPanel;
	private JPanel MessdatumPanel;
	private JPanel HbA1cPanel;
	private JPanel MessdatumHbA1cPanel;
	private JPanel PatSpeichernButtonPanel;
	private JPanel WeitereWerteButtonPanel;
	private JPanel LoeschenButtonPanel;
	private JPanel SpeichernLoeschenButtonPanel;
	
	private JLabel NachnameLabel;
	private JLabel VornameLabel;
	private JLabel GeburtsdatumLabel;
	private JLabel NuechternBZLabel;
	private JLabel BMILabel;
	private JLabel HbA1cLabel;
	private JLabel MessdatumLabel;
	
	private JTextField NachnameField;
	private JTextField VornameField;
	private JTextField GeburtsdatumField;
	private JTextField NuechternBZField;
	private JTextField BMIField;
	private JTextField HbA1cField;
	private JTextField MessdatumField;
	
	private JRadioButton maennlichButton;
	private JRadioButton weiblichButton;
	
	private JButton PatSpeichernButton;
	private JButton WeitereWerteButton;
	private JButton LoeschenButton;

	private DateFormat fDatum;
	private Calendar Kalender;  
	
	private GlucPatient aktuellerPatient;

	
	public void neuenMesspunkthinzufuegen(GlucPatient patient, GlucMesspunkt
			messpunkt) {
		Iterator<GlucPatient> DBit = GlucMain.PatDB.iterator();
		while(DBit.hasNext()) {
			GlucPatient itPatient = (GlucPatient) DBit.next();
			if((itPatient.PatientenNachname.equals(patient.PatientenNachname)) &
					(itPatient.PatientenNachname.equals(patient.PatientenNachname)) &
					(itPatient.PatientenGeburtsdatum.equals(patient.PatientenGeburtsdatum))) {
				patient.PatientenMesspunkte.add(messpunkt);
				JOptionPane.showMessageDialog(null,	"Messpunkt hinzugefügt");

				DBit.remove();                        //aktuellen Patienten aus PatDB entfernen...
				GlucMain.PatDB.add(patient);		  //und mit neuem Messpunkt wieder hinzufügen
			}
		}
	}
	
	
	public GlucPatEingabe() {
		this.setTitle("Patient speichern");
		setLocation(300,200);
		Box EingabeBox = Box.createVerticalBox();
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		this.setVisible(true);
		this.add(EingabeBox);
		
		fDatum =  new SimpleDateFormat("dd.MM.yyyy");
		Kalender = Calendar.getInstance();
		
		//Panels anordnen:		
		VornamePanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		NachnamePanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		NamePanel = new JPanel();
		NamePanel.setLayout(new BoxLayout(NamePanel, BoxLayout.X_AXIS));
		mwButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		NuechternBZPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		mwNuechternBZPanel = new JPanel();
		mwNuechternBZPanel.setLayout(new BoxLayout(mwNuechternBZPanel, BoxLayout.X_AXIS));
		GeburtsdatumPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		BMIPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		GeburtsdatumBMIPanel = new JPanel();
		GeburtsdatumBMIPanel.setLayout(new BoxLayout(GeburtsdatumBMIPanel, BoxLayout.X_AXIS));
		MessdatumPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		HbA1cPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		MessdatumHbA1cPanel = new JPanel();
		MessdatumHbA1cPanel.setLayout(new BoxLayout(MessdatumHbA1cPanel, BoxLayout.X_AXIS));
		PatSpeichernButtonPanel = new JPanel();
		WeitereWerteButtonPanel = new JPanel();
		LoeschenButtonPanel = new JPanel();
		SpeichernLoeschenButtonPanel = new JPanel();
		SpeichernLoeschenButtonPanel.setLayout(new BoxLayout(SpeichernLoeschenButtonPanel, BoxLayout.X_AXIS));

		//Beschriftung der Panels:
		VornameLabel = new JLabel("Vorname: ");
		NachnameLabel = new JLabel("Nachname: ");
		GeburtsdatumLabel = new JLabel("Geburtsdatum: ");
		NuechternBZLabel = new JLabel("Nüchtern-BZ (mg/dl): ");
		BMILabel = new JLabel("BMI (kg/m²): ");
		HbA1cLabel = new JLabel("HbA1c (%): ");
		MessdatumLabel = new JLabel("Datum der Messung: ");
		
		VornamePanel.add(VornameLabel);
		NachnamePanel.add(NachnameLabel);
		GeburtsdatumPanel.add(GeburtsdatumLabel);
		MessdatumPanel.add(MessdatumLabel);
		NuechternBZPanel.add(NuechternBZLabel);
		BMIPanel.add(BMILabel);
		HbA1cPanel.add(HbA1cLabel);
		
		//Textfelder in den Panels:
		VornameField = new JTextField(15);
		VornameField.setText("");
		NachnameField = new JTextField(15);
		NachnameField.setText(GlucMain.PatNachname);
		GeburtsdatumField = new JTextField(8);
		NuechternBZField = new JTextField(5);
		NuechternBZField.setText(""+GlucMain.PatNuechternBZ);
		BMIField = new JTextField(5);
		BMIField.setText(""+(float)GlucMain.PatBMIZehntel/10);
		HbA1cField = new JTextField(5);
		HbA1cField.setText(""+(float)GlucMain.PatHbA1cZehntel/10);
		MessdatumField = new JTextField(8);
		MessdatumField.setText(fDatum.format(Kalender.getTime()));
		VornamePanel.add(VornameField);
		NachnamePanel.add(NachnameField);
		GeburtsdatumPanel.add(GeburtsdatumField);
		NuechternBZPanel.add(NuechternBZField);
		BMIPanel.add(BMIField);
		HbA1cPanel.add(HbA1cField);
		MessdatumPanel.add(MessdatumField);

		//Radiobuttons und Gruppieren der Buttons:
		maennlichButton = new JRadioButton("männlich");
		weiblichButton = new JRadioButton("weiblich");
		ButtonGroup mwButtonGroup = new ButtonGroup();
		mwButtonGroup.add(maennlichButton);
		mwButtonGroup.add(weiblichButton);
		mwButtonPanel.add(maennlichButton);
		mwButtonPanel.add(weiblichButton);
		if(GlucMain.PatGeschlecht == 'm') {maennlichButton.setSelected(true);}
		else {weiblichButton.setSelected(true);}

		//Buttons zum Speichern bzw. Löschen der Felder:
		PatSpeichernButton = new JButton("Speichere Patient"); 
		getRootPane().setDefaultButton(PatSpeichernButton);
		WeitereWerteButton = new JButton("Weitere Werte"); 
		LoeschenButton = new JButton("Lösche Felder"); 
		PatSpeichernButtonPanel.add(PatSpeichernButton);
		WeitereWerteButtonPanel.add(WeitereWerteButton);
		LoeschenButtonPanel.add(LoeschenButton);
		
		//Alles auf Formular anordnen:
		NamePanel.add(VornamePanel);
		NamePanel.add(NachnamePanel);
		EingabeBox.add(NamePanel);
		mwNuechternBZPanel.add(mwButtonPanel);
		mwNuechternBZPanel.add(Box.createHorizontalGlue());
		mwNuechternBZPanel.add(NuechternBZPanel);
		EingabeBox.add(mwNuechternBZPanel);
		GeburtsdatumBMIPanel.add(GeburtsdatumPanel);
		GeburtsdatumBMIPanel.add(Box.createHorizontalGlue());
		GeburtsdatumBMIPanel.add(BMIPanel);
		EingabeBox.add(GeburtsdatumBMIPanel);
		MessdatumHbA1cPanel.add(MessdatumPanel);
		MessdatumHbA1cPanel.add(Box.createHorizontalGlue());
		MessdatumHbA1cPanel.add(HbA1cPanel);
		EingabeBox.add(MessdatumHbA1cPanel);
		SpeichernLoeschenButtonPanel.add(PatSpeichernButtonPanel);
		SpeichernLoeschenButtonPanel.add(Box.createRigidArea(new Dimension(60,0)));
		SpeichernLoeschenButtonPanel.add(LoeschenButtonPanel);
		EingabeBox.add(SpeichernLoeschenButtonPanel);
		pack();
		

		class PatSpeichernButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				char geschlecht = 'm';
				String vorname = "";
				String nachname = "";
				String geburtsdatum = "";
				String messdatum = "01.01.1900";
				int nuechternbz = 0;
				int bmizehntel = 0;
				int hba1czehntel = 0;
				if(weiblichButton.isSelected()) {geschlecht = 'w';}

				try {
					vorname = VornameField.getText();
					nachname = NachnameField.getText();
					geburtsdatum = GeburtsdatumField.getText();
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Ungültige Eingabe!");
				}
				if (vorname.equals("") || nachname.equals("")) {
					JOptionPane.showMessageDialog(null, "Bitte geben Sie einen vollständigen " +
							"Namen ein.");
					return;
				}
				if (geburtsdatum.equals("")) {
					JOptionPane.showMessageDialog(null, "Bitte geben Sie ein Geburtsdatum ein.");
					return;
				}
				aktuellerPatient = new GlucPatient(vorname, nachname,
						geburtsdatum, geschlecht);
				try {
					messdatum = MessdatumField.getText();
				}
				catch(Exception ex) {
					JOptionPane.showMessageDialog(null,	"Bitte geben Sie ein gültiges Datum ein!");
				}
				try {
					nuechternbz = Integer.valueOf(NuechternBZField.getText()).intValue();
					if(nuechternbz == 0) nuechternbz = 9999;
				}
					catch(Exception ex) {
						nuechternbz = 9999;
					}
				try {
					bmizehntel 
					  = (int)((Float.valueOf(BMIField.getText()).floatValue() * 10)+0.5);
					if(bmizehntel == 0) bmizehntel = 9999;
				}
					catch(Exception ex) {
						bmizehntel = 9999;
					}
				try {
					hba1czehntel  
					  = (int)((Float.valueOf(HbA1cField.getText()).floatValue() * 10)+0.5);
					if(hba1czehntel == 0) hba1czehntel = 9999;
				}
					catch(Exception ex) {
					hba1czehntel = 9999;
				}
				GlucMesspunkt neuerMesspunkt = new GlucMesspunkt(
						messdatum, nuechternbz, bmizehntel, hba1czehntel);
				
				GlucMain.PatImSpeicher = true;
				GlucMain.AktuellerPatientensatz = aktuellerPatient.PatientenNachname + ", " + 
						aktuellerPatient.PatientenVorname;
				aktuellerPatient.PatientenMesspunkte.add(neuerMesspunkt);
				GlucMain.PatDB.add(aktuellerPatient);
				
				File speicherDatei = new File("PatDB.gt");
				try {
					FileOutputStream fos = new FileOutputStream(speicherDatei, false); 
					//alte PatDB.pf soll überschrieben werden, daher "false"
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(GlucMain.PatDB);
					oos.flush();
					oos.close();
					JOptionPane.showMessageDialog(null,	"Patient gespeichert");
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null,	"Speichern nicht erfolgreich!");
				}
				GlucPatEingabe.this.dispose();
			}
		}
			
		class LoeschenButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				VornameField.setText("");
				NachnameField.setText("");
				GeburtsdatumField.setText("");
				NuechternBZField.setText("");
				BMIField.setText("");
				HbA1cField.setText("");
				MessdatumField.setText("");
			}
		}

		PatSpeichernButton.addActionListener(new PatSpeichernButtonItemListener());
		LoeschenButton.addActionListener(new LoeschenButtonItemListener());

	}
}
