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

public class GlucWerteForm extends JFrame {

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
	private JPanel ZurueckButtonPanel;
	private JPanel VorButtonPanel;
	private JPanel AbbrechenButtonPanel;
	private JPanel WertSpeichernButtonPanel;
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
	
	private JButton ZurueckButton;
	private JButton VorButton;
	private JButton AbbrechenButton;
	private JButton WertSpeichernButton;
	private JButton LoeschenButton;
	
	private JButton GrafikGroesseButton;
	private JButton GrafikGewichtButton;
	private JButton GrafikKopfumfangButton;

	private DateFormat fDatum;
	private Calendar Kalender;  
		
	private GlucPatient aktuellerPatient;
	private int aktuellermesspunkt = 0;

	public GlucWerteForm() {
		this.setTitle("Aktueller Patient: " + GlucMain.AktuellerPatientensatz);
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
		WertSpeichernButtonPanel = new JPanel();
		LoeschenButtonPanel = new JPanel();
		VorButtonPanel = new JPanel();
		ZurueckButtonPanel = new JPanel();
		AbbrechenButtonPanel = new JPanel();
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
		VornameField.setDisabledTextColor(new Color(90, 90, 90));
		VornameField.setEnabled(false);
		NachnameField = new JTextField(15);
		NachnameField.setDisabledTextColor(new Color(90, 90, 90));
		NachnameField.setEnabled(false);
		GeburtsdatumField = new JTextField(8);
		GeburtsdatumField.setDisabledTextColor(new Color(90, 90, 90));
		GeburtsdatumField.setEnabled(false);

		//Textfelder füllen:
		Iterator<GlucPatient> FindePatit = GlucMain.PatDB.iterator();

		Boolean fertig = false;
		while(FindePatit.hasNext() & (fertig == false)) {
		GlucPatient zwischenPat = (GlucPatient) FindePatit.next();
		
		if(GlucMain.AktuellerPatientensatz.equals(zwischenPat.PatientenNachname + ", " + 
				zwischenPat.PatientenVorname)) {
			aktuellerPatient = zwischenPat;
			fertig = true;
		}
		
		//falls auf dem Main-Formular bereits Daten eingegeben wurden, die jetzt zum Patienten
		//hinzugefügt werden sollen:
		/* if((GlucMain.PatNuechternBZ != 0) & (GlucMain.PatNuechternBZ != 9999) & 
				(GlucMain.PatBMIZehntel != 0) & (GlucMain.PatBMIZehntel != 9999) &
				(GlucMain.PatHbA1cZehntel != 0) & (GlucMain.PatHbA1cZehntel != 9999)) 
		{
			GlucMain.PatVorname = zwischenPat.PatientenVorname;
			GlucMain.PatNachname = zwischenPat.PatientenNachname;
			GlucMain.PatGeschlecht = zwischenPat.PatientenGeschlecht;
			GlucMain.PatGeburtsdatum = zwischenPat.PatientenGeburtsdatum;
			VornameField.setText(GlucMain.PatVorname);
			NachnameField.setText(GlucMain.PatNachname);
			GeburtsdatumField.setText(GlucMain.PatGeburtsdatum);
			NuechternBZField = new JTextField(5);
			if((GlucMain.PatNuechternBZ != 9999) & (GlucMain.PatNuechternBZ != 0)) {
				NuechternBZField.setText(""+GlucMain.PatNuechternBZ);			
			}
			BMIField = new JTextField(5);
			if((GlucMain.PatBMIZehntel != 9999) & (GlucMain.PatBMIZehntel != 0)) {
				BMIField.setText(""+(float)GlucMain.PatBMIZehntel/10);		
			}
			HbA1cField = new JTextField(5);
			if((GlucMain.PatHbA1cZehntel != 9999) & (GlucMain.PatHbA1cZehntel != 0)) {
				HbA1cField.setText(""+(float)GlucMain.PatHbA1cZehntel/10);
			}		
			MessdatumField = new JTextField(8);
			MessdatumField.setText(fDatum.format(Kalender.getTime()));	
		}
		else */ {
			GlucMain.PatVorname = zwischenPat.PatientenVorname;
			GlucMain.PatNachname = zwischenPat.PatientenNachname;
			GlucMain.PatGeschlecht = zwischenPat.PatientenGeschlecht;
			GlucMain.PatGeburtsdatum = zwischenPat.PatientenGeburtsdatum;
			if(zwischenPat.PatientenMesspunkte.isEmpty() == false) {
				GlucMain.PatNuechternBZ = zwischenPat.PatientenMesspunkte.get(0).MessNuechternBZ;
				GlucMain.PatBMIZehntel = zwischenPat.PatientenMesspunkte.get(0).MessBMIZehntel;
				GlucMain.PatHbA1cZehntel = zwischenPat.PatientenMesspunkte.get(0).MessHbA1cZehntel;
			}
			else {
				GlucMain.PatNuechternBZ = 0;
				GlucMain.PatBMIZehntel = 0;
				GlucMain.PatHbA1cZehntel = 0;					
			}
			VornameField.setText(GlucMain.PatVorname);
			NachnameField.setText(GlucMain.PatNachname);
			GeburtsdatumField.setText(GlucMain.PatGeburtsdatum);
			NuechternBZField = new JTextField(5);
			NuechternBZField.setText(""+GlucMain.PatNuechternBZ);
			BMIField = new JTextField(5);
			BMIField.setText(""+(float)GlucMain.PatBMIZehntel/10);
			HbA1cField = new JTextField(5);
			HbA1cField.setText(""+(float)GlucMain.PatHbA1cZehntel/10);
			MessdatumField = new JTextField(8);
			if(zwischenPat.PatientenMesspunkte.isEmpty() == false) {
				MessdatumField.setText(zwischenPat.PatientenMesspunkte.get(0).MessDatum);
			}
			else {MessdatumField.setText("");}
		}			
	}
		
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
		maennlichButton.setEnabled(false);
		weiblichButton.setEnabled(false);
		//Buttons zum Speichern, Abbrechen, Löschen der Felder sowie Vor und Zurück:
		ZurueckButton = new JButton("<=");
		VorButton = new JButton("=>");
		AbbrechenButton = new JButton("Schließen");
		WertSpeichernButton = new JButton("Wert speichern"); 
		getRootPane().setDefaultButton(WertSpeichernButton);
		AbbrechenButton = new JButton("Abbrechen"); 
		LoeschenButton = new JButton("Wert löschen"); 
		
		//Buttons, um direkt zu den Grafiken zu gelangen:
		GrafikGroesseButton = new JButton("Grafik");
		GrafikGewichtButton = new JButton("Grafik");
		GrafikKopfumfangButton = new JButton("Grafik");
		
		WertSpeichernButtonPanel.add(WertSpeichernButton);
		LoeschenButtonPanel.add(LoeschenButton);
		ZurueckButtonPanel.add(ZurueckButton);
		VorButtonPanel.add(VorButton);
		AbbrechenButtonPanel.add(AbbrechenButton);
			
		//Alles auf Formular anordnen:
		NamePanel.add(VornamePanel);
		NamePanel.add(NachnamePanel);
		EingabeBox.add(NamePanel);
		mwNuechternBZPanel.add(mwButtonPanel);
		mwNuechternBZPanel.add(Box.createHorizontalGlue());
		mwNuechternBZPanel.add(NuechternBZPanel);
		mwNuechternBZPanel.add(GrafikGroesseButton);
		EingabeBox.add(mwNuechternBZPanel);
		GeburtsdatumBMIPanel.add(GeburtsdatumPanel);
		GeburtsdatumBMIPanel.add(Box.createHorizontalGlue());
		GeburtsdatumBMIPanel.add(BMIPanel);
		GeburtsdatumBMIPanel.add(GrafikGewichtButton);
		EingabeBox.add(GeburtsdatumBMIPanel);
		MessdatumHbA1cPanel.add(MessdatumPanel);
		MessdatumHbA1cPanel.add(Box.createHorizontalGlue());
		MessdatumHbA1cPanel.add(HbA1cPanel);
		MessdatumHbA1cPanel.add(GrafikKopfumfangButton);
		EingabeBox.add(MessdatumHbA1cPanel);
		SpeichernLoeschenButtonPanel.add(ZurueckButtonPanel);
		SpeichernLoeschenButtonPanel.add(WertSpeichernButtonPanel);
		//SpeichernLoeschenButtonPanel.add(Box.createRigidArea(new Dimension(60,0)));
		SpeichernLoeschenButtonPanel.add(LoeschenButtonPanel);
		SpeichernLoeschenButtonPanel.add(AbbrechenButtonPanel);
		SpeichernLoeschenButtonPanel.add(VorButtonPanel);
		EingabeBox.add(SpeichernLoeschenButtonPanel);
		pack();	
		
		class WertSpeichernButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				char geschlecht = 'm';
				String vorname = "";
				String nachname = "";
				String geburtsdatum = "";
				String messdatum = "01.01.2000";
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
				try {
					nuechternbz = Integer.valueOf(NuechternBZField.getText()).intValue();
					if(nuechternbz == 0) nuechternbz = 9999;
				}
					catch(Exception ex) {
						nuechternbz = 9999;
					}
				try {
					bmizehntel 
					  = (int)((Float.valueOf(BMIField.getText().replace(',','.')).floatValue() * 10)+0.5);
					if(bmizehntel == 0) bmizehntel = 9999;
				}
					catch(Exception ex) {
						bmizehntel = 9999;
					}
				try {
					hba1czehntel  
					  = (int)((Float.valueOf(HbA1cField.getText().replace(',','.')).floatValue() * 10)+0.5);
					if(hba1czehntel == 0) hba1czehntel = 9999;
				}
					catch(Exception ex) {
					hba1czehntel = 9999;
				}
				
				Iterator<GlucMesspunkt> itMesspunkt = aktuellerPatient.PatientenMesspunkte.iterator();
				boolean bestehenderMesspunktmodifiziert = false;
				while(itMesspunkt.hasNext()) {
					GlucMesspunkt punktimIterator = (GlucMesspunkt) itMesspunkt.next();
					if(punktimIterator.MessDatum.equals(MessdatumField.getText())) {
						
						punktimIterator.MessNuechternBZ = nuechternbz;
						punktimIterator.MessBMIZehntel = bmizehntel;	 
						punktimIterator.MessHbA1cZehntel = hba1czehntel;	
						
						bestehenderMesspunktmodifiziert = true;
					}
					if(punktimIterator.MessDatum.equals("")) {
						itMesspunkt.remove();                        //defekte Einträge löschen
					}
				} 
				if(bestehenderMesspunktmodifiziert == false) {
					aktuellerPatient.PatientenMesspunkte.add(new GlucMesspunkt(
							MessdatumField.getText(), nuechternbz, bmizehntel, hba1czehntel));
				}
				
				Collections.sort(aktuellerPatient.PatientenMesspunkte);

				Iterator<GlucPatient> DBit = GlucMain.PatDB.iterator();
				while(DBit.hasNext()) {
					GlucPatient itPatient = (GlucPatient) DBit.next();
					if((itPatient.PatientenNachname.equals(aktuellerPatient.PatientenNachname)) &
							(itPatient.PatientenNachname.equals(aktuellerPatient.PatientenNachname)) &
							(itPatient.PatientenGeburtsdatum.equals(aktuellerPatient.PatientenGeburtsdatum))) {
						DBit.remove();       		                      //aktuellen Patienten aus PatDB entfernen...
					}
				} 
				GlucMain.PatDB.add(aktuellerPatient);	  //und mit neuem Messpunkt wieder hinzufügen

				File speicherDatei = new File("PatDB.gt");
				try {
					FileOutputStream fos = new FileOutputStream(speicherDatei, false); 
					//alte PatDB.gt soll überschrieben werden, daher "false"
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(GlucMain.PatDB);
					oos.flush();
					oos.close();
					JOptionPane.showMessageDialog(null,	"Wert gespeichert");
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null,	"Speichern nicht erfolgreich!");
				}
			}			
		}
		
		class LoeschenButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Iterator<GlucMesspunkt> itMesspunkt = aktuellerPatient.PatientenMesspunkte.iterator();
				while(itMesspunkt.hasNext()) {
					GlucMesspunkt punktimIterator = (GlucMesspunkt) itMesspunkt.next();
				
					if(punktimIterator.MessDatum.equals(MessdatumField.getText())) {
						itMesspunkt.remove();
						JOptionPane.showMessageDialog(null,	"Wert gelöscht");
					}
					Collections.sort(aktuellerPatient.PatientenMesspunkte);
				} 
				
				
				Iterator<GlucPatient> DBit = GlucMain.PatDB.iterator();
				while(DBit.hasNext()) {
					GlucPatient itPatient = (GlucPatient) DBit.next();
					if((itPatient.PatientenNachname.equals(aktuellerPatient.PatientenNachname)) &
							(itPatient.PatientenNachname.equals(aktuellerPatient.PatientenNachname)) &
							(itPatient.PatientenGeburtsdatum.equals(aktuellerPatient.PatientenGeburtsdatum))) {
						DBit.remove();       		                      //aktuellen Patienten aus PatDB entfernen...
					}
				} 
				GlucMain.PatDB.add(aktuellerPatient);	  //und ohne gelöschten Messpunkt wieder hinzufügen

				File speicherDatei = new File("PatDB.gt");
				try {
					FileOutputStream fos = new FileOutputStream(speicherDatei, false); 
					//alte PatDB.gt soll überschrieben werden, daher "false"
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(GlucMain.PatDB);
					oos.flush();
					oos.close();
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null,	"Fehler beim Speichern der Patientendatenbank!");
				}

				NuechternBZField.setText("0");
				BMIField.setText("0");
				HbA1cField.setText("0");
				MessdatumField.setText("");
			}
		}

		class AbbrechenButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GlucWerteForm.this.dispose();			
			}
		}

		class ZurueckButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				if(aktuellermesspunkt > 0) {
					aktuellermesspunkt--;
					NuechternBZField.setText(""+aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessNuechternBZ);
					BMIField.setText(""+(float)aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessBMIZehntel/10);
					HbA1cField.setText(""+(float)aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessHbA1cZehntel/10);
					MessdatumField.setText(aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessDatum);							
				}
			}
		}
		
		class VorButtonItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				
				
				if(aktuellerPatient.PatientenMesspunkte.size() > aktuellermesspunkt + 1) {
					aktuellermesspunkt++;
					NuechternBZField.setText(""+aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessNuechternBZ);
					BMIField.setText(""+(float)aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessBMIZehntel/10);
					HbA1cField.setText(""+(float)aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessHbA1cZehntel/10);
					MessdatumField.setText(""+aktuellerPatient.PatientenMesspunkte.get(aktuellermesspunkt).MessDatum);							
				}
				else {
					aktuellermesspunkt = aktuellerPatient.PatientenMesspunkte.size();
					NuechternBZField.setText("0");
					BMIField.setText("0");
					HbA1cField.setText("0");
					MessdatumField.setText("");								
				}
			}
		}
		
		class GrafikGroesseButtonListener implements ActionListener {
			Graphics g;
			public void actionPerformed(ActionEvent e) {
				GlucMain.welcheGrafik = GlucMain.Grafikart.NUECHTERNBZ_VERLAUF;
				GlucMain.MesspunktefuerGrafik = aktuellerPatient.PatientenMesspunkte;
				GlucGrafik BildschirmGrafik = new GlucGrafik();
			}
		}
		
		class GrafikGewichtButtonListener implements ActionListener {
			Graphics g;
			public void actionPerformed(ActionEvent e) {
				GlucMain.welcheGrafik = GlucMain.Grafikart.BMI_VERLAUF;
				GlucMain.MesspunktefuerGrafik = aktuellerPatient.PatientenMesspunkte;
				GlucGrafik BildschirmGrafik = new GlucGrafik();
			}
		}

		class GrafikKUButtonListener implements ActionListener {
			Graphics g;
			public void actionPerformed(ActionEvent e) {
				GlucMain.welcheGrafik = GlucMain.Grafikart.HBA1C_VERLAUF;
				GlucMain.MesspunktefuerGrafik = aktuellerPatient.PatientenMesspunkte;
				GlucGrafik BildschirmGrafik = new GlucGrafik();
			}
		}


		WertSpeichernButton.addActionListener(new WertSpeichernButtonItemListener());
		LoeschenButton.addActionListener(new LoeschenButtonItemListener());
		AbbrechenButton.addActionListener(new AbbrechenButtonItemListener());
		ZurueckButton.addActionListener(new ZurueckButtonItemListener());
		VorButton.addActionListener(new VorButtonItemListener());
		
		GrafikGroesseButton.addActionListener(new GrafikGroesseButtonListener());
		GrafikGewichtButton.addActionListener(new GrafikGewichtButtonListener());
		GrafikKopfumfangButton.addActionListener(new GrafikKUButtonListener());


	}    //Konstruktor zu Ende

	
}