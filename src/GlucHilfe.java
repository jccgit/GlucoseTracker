import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class GlucHilfe extends JFrame implements ActionListener {
	
	private JEditorPane hilfeEditorPane;
	private URL hilfeURL;
	
	public GlucHilfe(URL hilfURL) {
		super("Hilfe - GlucoseTracker " + GlucMain.VersionsString);
		this.setLocationRelativeTo(rootPane);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		hilfeURL = hilfURL;
		
		hilfeEditorPane = new JEditorPane();
		hilfeEditorPane.setEditable(false);
		try {
			hilfeEditorPane.setPage(hilfeURL);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		hilfeEditorPane.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent ev) {
				try {
					if(ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						hilfeEditorPane.setPage(ev.getURL());
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
					
				}
			}
		});
		
		getContentPane().add(new JScrollPane(hilfeEditorPane));

		addButtons();

		this.setVisible(true);
		this.setLocation(200,200);
		this.setSize(700,700);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String strAction = e.getActionCommand();
		URL tempURL;
		
		try {
			if(strAction == "Inhalt") {
				JOptionPane.showMessageDialog(null, hilfeURL);
				tempURL = hilfeEditorPane.getPage();
				hilfeEditorPane.setPage(hilfeURL);
				hilfeEditorPane.setCaretPosition(0);
			}
			if(strAction == "Schließen") {
				GlucHilfe.this.dispose();
			}		
		} catch (IOException ex) {ex.printStackTrace();}
	}
	
	private void addButtons() {
		/*JButton inhaltButton = new JButton("Inhalt");
		inhaltButton.addActionListener(this);*/
		//wird erst gebraucht, wenn die Hilfedatei unübersichtlich wird
		
		JButton schliessenButton = new JButton("Schließen");
		schliessenButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		//buttonPanel.add(inhaltButton);
		buttonPanel.add(schliessenButton);

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

}
