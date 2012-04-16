import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class GlucBMIRechner extends JFrame {

	public JTextField AusgabeTextField = new JTextField("             ");
	public JSpinner groesseSpinner;
	public JSpinner gewichtSpinner;

	public GlucBMIRechner() {
		this.setTitle("BMI-Rechner");
		setLocation(300,200);
		Box BMIBox = Box.createVerticalBox();
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		this.setVisible(true);	
		JButton BerechneButton = new JButton("Berechnen");
		
		SpinnerModel groesseModel = new SpinnerNumberModel(170, 40, 220, 1);
		SpinnerModel gewichtModel = new SpinnerNumberModel(70, 1, 120, 1);
		
		JPanel EingabenPanel = new JPanel();
		JPanel AusgabePanel = new JPanel();
		JPanel BerechnePanel = new JPanel();

		groesseSpinner = new JSpinner(groesseModel);
		gewichtSpinner = new JSpinner(gewichtModel);
		
		EingabenPanel.add(new JLabel("Größe: "));
		EingabenPanel.add(groesseSpinner);
		EingabenPanel.add(new JLabel("cm"));
		EingabenPanel.add(Box.createRigidArea(new Dimension(40,1)));
		EingabenPanel.add(new JLabel("Gewicht: "));
		EingabenPanel.add(gewichtSpinner);
		EingabenPanel.add(new JLabel("kg"));
		AusgabePanel.add(new JLabel("BMI: "));
		AusgabePanel.add(AusgabeTextField);
		AusgabePanel.add(new JLabel("kg/m²"));
		BerechnePanel.add(BerechneButton);
		BMIBox.add(EingabenPanel);
		BMIBox.add(BerechnePanel);
		BMIBox.add(AusgabePanel);
		this.add(BMIBox);
		pack();
		
		class BerechneButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				float groesse = (Integer) groesseSpinner.getValue();
				float gewicht = (Integer) gewichtSpinner.getValue();
				float bmi = (gewicht / ((groesse / 100) * (groesse / 100))); 
				AusgabeTextField.setText(new DecimalFormat("#.##").format(bmi));
				GlucMain.PatBMIZehntel = (int)(bmi*10+0.5);
				}

		}	

		BerechneButton.addActionListener(new BerechneButtonListener());

	}

}
