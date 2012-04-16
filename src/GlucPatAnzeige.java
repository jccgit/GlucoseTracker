import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import javax.swing.*;

public class GlucPatAnzeige extends JFrame {

	private JList PatientenListe;
	private DefaultListModel PatDatenModell;
	private JPanel LadenLoeschenAbbrechenPanel;
	private JButton LadenButton;
	private JButton LoeschenButton;
	private JButton SchliessenButton;
	
	public GlucPatAnzeige() {
		this.setTitle("Patienten in Datenbank");
		setLocation(300,200);
		Box PatAnzeigeBox = Box.createVerticalBox();
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		this.setVisible(true);
	
		String[] anzeigeArray = new String[GlucMain.PatDB.size()];

		//Panels anordnen:		
		PatientenListe = new JList();
		PatDatenModell = new DefaultListModel();
		Iterator<GlucPatient> DBit = GlucMain.PatDB.iterator();
		int i = 0;
		while(DBit.hasNext()) {
			GlucPatient zwischenPat = (GlucPatient) DBit.next();
			anzeigeArray[i] = zwischenPat.PatientenNachname + ", " + 
					zwischenPat.PatientenVorname;
			i++;
		}
		java.util.Arrays.sort(anzeigeArray);
		PatientenListe.setModel(PatDatenModell);
		PatientenListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		for(int j=0; j<anzeigeArray.length; j++) {
			PatDatenModell.add(j, anzeigeArray[j]);
		}
		PatientenListe.setVisibleRowCount(10);
		PatAnzeigeBox.add(new JScrollPane(PatientenListe));
		LadenButton = new JButton("Laden");
		LoeschenButton = new JButton("Löschen");
		SchliessenButton = new JButton("Schließen");
		LadenLoeschenAbbrechenPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		LadenLoeschenAbbrechenPanel.add(LadenButton);
		LadenLoeschenAbbrechenPanel.add(LoeschenButton);
		LadenLoeschenAbbrechenPanel.add(SchliessenButton);
		PatAnzeigeBox.add(LadenLoeschenAbbrechenPanel);
		this.add(PatAnzeigeBox);
		pack();
		
		class LadenButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				int gewaehlt = PatientenListe.getSelectedIndex();
				if(gewaehlt == -1) {
					JOptionPane.showMessageDialog(null,	"Bitte wählen Sie einen Datensatz aus.");
				}
				else {
					GlucMain.AktuellerPatientensatz = (String) PatientenListe.getSelectedValue();
					JOptionPane.showMessageDialog(null, "" + GlucMain.AktuellerPatientensatz + " geladen!");
					GlucPatAnzeige.this.dispose();
					GlucWerteForm WerteFenster = new GlucWerteForm();
				}
			}
		}	

		class LoeschenButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Iterator<GlucPatient> Loeschenit = GlucMain.PatDB.iterator();
				int gewaehlt = PatientenListe.getSelectedIndex();
				if(gewaehlt == -1) {
					JOptionPane.showMessageDialog(null,	"Bitte wählen Sie einen Datensatz aus.");
				}
				else {
					while(Loeschenit.hasNext()) {
						GlucPatient zwischenPat = (GlucPatient) Loeschenit.next();
						if(PatientenListe.getSelectedValue().equals(zwischenPat.PatientenNachname + ", " + 
								zwischenPat.PatientenVorname)) {					
							int ok = JOptionPane.showConfirmDialog(null, "Sind Sie sicher, dass Sie " 
									+ PatientenListe.getSelectedValue() + " löschen möchten?",
									"Datensatz löschen", JOptionPane.YES_NO_OPTION);
							if(ok == JOptionPane.YES_OPTION) {
								Loeschenit.remove();
								JOptionPane.showMessageDialog(null, "" + PatientenListe.getSelectedValue() + " gelöscht!");
								if(PatientenListe.getSelectedValue().equals(GlucMain.AktuellerPatientensatz)) {
									GlucMain.AktuellerPatientensatz = "";
								}
								PatDatenModell.removeElement(PatientenListe.getSelectedValue());
								pack();
							}
							break;
						}
					}
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
						JOptionPane.showMessageDialog(null,	"Speichern nicht erfolgreich!");
					}
				}
			}
		}	

		class SchliessenButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GlucPatAnzeige.this.dispose();
			}
		}	
		LadenButton.addActionListener(new LadenButtonListener());
		LoeschenButton.addActionListener(new LoeschenButtonListener());
		SchliessenButton.addActionListener(new SchliessenButtonListener());
	}
	
}
