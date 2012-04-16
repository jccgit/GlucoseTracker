import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;

public class GlucGrafik extends JFrame implements Printable {

	private JPanel GrafikPanel;
	
	public static BufferedImage DruckImage;
	
	private JMenuBar MenueLeiste;
	private JMenu DruckenMenu;
	private JMenu SchliessenMenu;
	private JMenuItem WerteDruckenMenuItem;
	private JMenuItem GrafikDruckenMenuItem;
	private JMenuItem SchliessenMenuItem;
	
	private Color Gelb = new Color(255,150,0);
	private Color Rot = Color.red;
	private Color Dunkelgruen = new Color(0,150,0);
	private Color Hellgruen = new Color(100,255,100);
	private Color Blau = Color.blue;
	private Color Weiss = Color.white;
	private Color Schwarz = Color.black;
	private Color Grau = Color.gray;
	
	private String PatPrintName = ""; 

	private DateFormat fDatum;
	private Calendar Kalender;  
	private Date erstesDatum;
	private Date letztesDatum;
	
	public GlucGrafik() {
		super("Grafik - GlucoseTracker " + GlucMain.VersionsString);
		
		this.setLocationRelativeTo(rootPane);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		fDatum =  new SimpleDateFormat("dd.MM.yyyy");
		Kalender = Calendar.getInstance();
		erstesDatum = Kalender.getTime();
		letztesDatum = Kalender.getTime();

		MenueLeiste = new JMenuBar();
		DruckenMenu = new JMenu("Drucken");
		SchliessenMenu = new JMenu("Schließen");
		WerteDruckenMenuItem = new JMenuItem("Werte drucken");
		GrafikDruckenMenuItem = new JMenuItem("Grafik drucken");
		SchliessenMenuItem = new JMenuItem("Grafik schließen");
		
		//GrafikPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		GrafikPanel = new JPanel();
		GrafikPanel.setLayout(new BorderLayout());
		GrafikPanel.setBackground(new Color(200, 230, 255));
		
		DruckenMenu.add(WerteDruckenMenuItem);
		DruckenMenu.add(GrafikDruckenMenuItem);
		SchliessenMenu.add(SchliessenMenuItem);
		MenueLeiste.add(DruckenMenu);
		MenueLeiste.add(SchliessenMenu);
		
		this.setJMenuBar(MenueLeiste);
		this.add(GrafikPanel);
		this.setVisible(true);
		this.setSize(1200,800);
		this.setLocation(50,80);
		
		class GrafikDruckenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GlucGrafik.this.druckeGrafikFrame();
			}
		}
	
		class SchliessenMenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GlucGrafik.this.dispose();
			}
		}	
		GrafikDruckenMenuItem.addActionListener(new GrafikDruckenMenuItemListener());
		WerteDruckenMenuItem.addActionListener(new GlucPrinter());
		SchliessenMenuItem.addActionListener(new SchliessenMenuItemListener());

	}
	
	public int findeX(int zeit) {
		return(zeit * 40);
	}
	
	public int findeYNuechternBZ(int nuechternbz) {
		return(600-nuechternbz*5);
	}

	public int findeYBMI(int bmizehntel) {
		return(600-bmizehntel);
	}
	
	public int findeYHbA1c(int hba1czehntel) {
		return(600-hba1czehntel*5);			
	}

	public int berechneAlter(GregorianCalendar geburtsdatum, GregorianCalendar messdatum) {
		int alter = 0;
		alter = (messdatum.get(Calendar.YEAR) - geburtsdatum.get(Calendar.YEAR)) * 12;
		if(messdatum.get(Calendar.MONTH) > geburtsdatum.get(Calendar.MONTH)) {
			alter = alter + messdatum.get(Calendar.MONTH) - geburtsdatum.get(Calendar.MONTH);
		}
		if(messdatum.get(Calendar.MONTH) < geburtsdatum.get(Calendar.MONTH)) {
			alter = alter - (geburtsdatum.get(Calendar.MONTH) - messdatum.get(Calendar.MONTH));
		}
		return alter;
	}
	

	public void zeichneKoordinatenNuechternBZ(Graphics g, Boolean drucken) {
		Graphics2D g2d = (Graphics2D) g;
		if(drucken == false) {
			g2d.setColor(Weiss);
			g2d.fillRect(50,80,1100,700);
			g2d.setColor(Blau);
			g2d.drawRect(50,80,1100,700);
		}

		g2d.translate(this.getInsets().top + 100,100);

		//Achse und Hilfsachsen für Zeit zeichnen (in Y-Richtung):
		g2d.setColor(Schwarz);
		g2d.drawLine(0,600,1000,600);	
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		for(int i=1; i<=24; i++) {
			g2d.setColor(Schwarz);
			g2d.drawLine(findeX(i),600,findeX(i),610);
			g2d.setColor(Grau);
			g2d.drawLine(findeX(i),600,findeX(i),0);
			g2d.setColor(Schwarz);
			g2d.drawString(""+i, findeX(i)-5,630);
		}
		g2d.drawString("Tage", 980,630);

		
		//Achse und Hilfsachsen für Nüchtern-BZ zeichnen (in X-Richtung):
		g2d.drawLine(0,0,0,600);
		g2d.setColor(Schwarz);
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		for(int i=1; i<120; i++) {
			if(i%10<0.001) {
				g2d.setColor(Schwarz);
				g2d.drawLine(0, findeYNuechternBZ(i),-5,findeYNuechternBZ(i));
				g2d.setColor(Grau);
				g2d.drawLine(0, findeYNuechternBZ(i),1000,findeYNuechternBZ(i));
				g2d.setColor(Schwarz);
				g2d.drawString(""+i, -35, findeYNuechternBZ(i));
			}
			g2d.drawString("mg/dl", -65,-5);
		}	
	}
	
	public void zeichneKoordinatenBMI(Graphics g, Boolean drucken) {
		Graphics2D g2d = (Graphics2D) g;
		if(drucken == false) {
			g2d.setColor(Weiss);
			g2d.fillRect(50,80,1100,700);
			g2d.setColor(Blau);
			g2d.drawRect(50,80,1100,700);
		}

		g2d.translate(this.getInsets().top + 100,100);

		//Achse und Hilfsachsen für Tage zeichnen (in Y-Richtung):
		g2d.setColor(Schwarz);
		g2d.drawLine(0,600,1000,600);	
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		for(int i=1; i<=24; i++) {
			g2d.setColor(Schwarz);
			g2d.drawLine(findeX(i),600,findeX(i),610);
			g2d.setColor(Grau);
			g2d.drawLine(findeX(i),600,findeX(i),0);
			g2d.setColor(Schwarz);
			g2d.drawString(""+i, findeX(i)-5,630);
		}
		g2d.drawString("Tage", 980,630);
		
		//Achse und Hilfsachsen für BMI zeichnen (in X-Richtung):
		g2d.drawLine(0,0,0,600);
		g2d.setColor(Schwarz);
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		for(int i=1; i<600; i++) {
			if(i%100<0.001) {
				g2d.setColor(Schwarz);
				g2d.drawLine(0, findeYBMI(i),-5,findeYBMI(i));
				g2d.setColor(Grau);
				g2d.drawLine(0, findeYBMI(i),1000,findeYBMI(i));
				g2d.setColor(Schwarz);
				g2d.drawString(""+i/10, -50, findeYBMI(i));
			}
			g2d.drawString("kg/m²", -65,-5);
		}	
	}

	public void zeichneKoordinatenHbA1c(Graphics g, Boolean drucken) {
		Graphics2D g2d = (Graphics2D) g;
		if(drucken == false) {
			g2d.setColor(Weiss);
			g2d.fillRect(50,80,1100,700);
			g2d.setColor(Blau);
			g2d.drawRect(50,80,1100,700);
		}

		g2d.translate(this.getInsets().top + 100,100);

		//Achse und Hilfsachsen für Tage zeichnen (in Y-Richtung):
		g2d.setColor(Schwarz);
		g2d.drawLine(0,600,1000,600);	
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		for(int i=1; i<=24; i++) {
			g2d.setColor(Schwarz);
			g2d.drawLine(findeX(i),600,findeX(i),610);
			g2d.setColor(Grau);
			g2d.drawLine(findeX(i),600,findeX(i),0);
			g2d.setColor(Schwarz);
			g2d.drawString(""+i, findeX(i)-5,630);
		}
		g2d.drawString("Tage", 980,630);
		
		//Achse und Hilfsachsen für HbA1c zeichnen (in X-Richtung):
		g2d.drawLine(0,0,0,600);
		g2d.setColor(Schwarz);
		g2d.setFont(new Font("Arial", Font.PLAIN, 16));
		for(int i=1; i<120; i++) {
			if(i%10<0.001) {
				g2d.setColor(Schwarz);
				g2d.drawLine(0, findeYHbA1c(i),-5,findeYHbA1c(i));
				g2d.setColor(Grau);
				g2d.drawLine(0, findeYHbA1c(i),1000,findeYHbA1c(i));
				g2d.setColor(Schwarz);
				g2d.drawString(""+i/10, -35, findeYHbA1c(i));
			}
			g2d.drawString("%", -65,-5);
		}	
	}

	public void zeichnePatWertNuechternBZ(int tage, int nuechternbz, Graphics g, Boolean drucken) {
		
		//Der Wert "drucken" wird z.Zt. nicht gebraucht, evtl. später, wenn zwischen
		//Ausgabe für den Bildschirm und Ausgabe zum Drucken unterschieden werden muss
		//(wie in der Methode zeichneKoordinatenGroesse)
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Schwarz);
		g2d.fillOval(findeX(tage)-5, 
				findeYNuechternBZ(nuechternbz)-5, 10,10);
	}
	
	public void zeichnePatWertBMI(int tage, int bmizehntel, Graphics g, Boolean drucken) {
		
		//Der Wert "drucken" wird z.Zt. nicht gebraucht, evtl. später, wenn zwischen
		//Ausgabe für den Bildschirm und Ausgabe zum Drucken unterschieden werden muss
		//(wie in der Methode zeichneKoordinatenGroesse)
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Schwarz);
		g2d.fillOval(findeX(tage)-5, 
				findeYBMI(bmizehntel)-5, 10,10);
	}

	public void zeichnePatWertHbA1c(int tage, int hba1czehntel, Graphics g, Boolean drucken) {
		
		//Der Wert "drucken" wird z.Zt. nicht gebraucht, evtl. später, wenn zwischen
		//Ausgabe für den Bildschirm und Ausgabe zum Drucken unterschieden werden muss
		//(wie in der Methode zeichneKoordinatenGroesse)
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Schwarz);
		g2d.fillOval(findeX(tage)-5, 
				findeYHbA1c(hba1czehntel)-5, 10, 10);
	}

	public void paint(Graphics g) {
		super.paint(g);
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ) {
			zeichneKoordinatenNuechternBZ(g, false);
			zeichnePatWertNuechternBZ(0, GlucMain.PatNuechternBZ, g, false);			
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI) {
			zeichneKoordinatenBMI(g, false);
			zeichnePatWertBMI(0, GlucMain.PatBMIZehntel, g, false);			
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C) {
			zeichneKoordinatenHbA1c(g, false);
			zeichnePatWertHbA1c(0, GlucMain.PatHbA1cZehntel, g, false);			
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ_VERLAUF) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calGeburt = new GregorianCalendar(); 
			GregorianCalendar calMess = new GregorianCalendar(); 
			Date geburtsDatum = new Date();
			Date messDatum = new Date();
			int alter = 0;
			
			try {
				geburtsDatum = sdf.parse(GlucMain.PatGeburtsdatum);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Ungültiges Alter!");
			}
			calGeburt.setTime(geburtsDatum);

			zeichneKoordinatenNuechternBZ(g, false);
			GlucMain.MesspunktefuerGrafik.trimToSize();
			if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
				for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
					try {
						messDatum = sdf.parse(GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Ungültiges Messdatum: " +
								GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					calMess.setTime(messDatum);
					
					alter = berechneAlter(calGeburt, calMess);
					
					zeichnePatWertNuechternBZ(alter, GlucMain.MesspunktefuerGrafik.get(i).MessNuechternBZ, g, false);	
				}
				GlucMain.PatAlter = alter;
			}
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI_VERLAUF) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calGeburt = new GregorianCalendar(); 
			GregorianCalendar calMess = new GregorianCalendar(); 
			Date geburtsDatum = new Date();
			Date messDatum = new Date();
			int alter = 0;
			
			try {
				geburtsDatum = sdf.parse(GlucMain.PatGeburtsdatum);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Ungültiges Alter!");
			}
			calGeburt.setTime(geburtsDatum);

			zeichneKoordinatenBMI(g, false);
			GlucMain.MesspunktefuerGrafik.trimToSize();
			if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
				for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
					try {
						messDatum = sdf.parse(GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Ungültiges Messdatum: " +
								GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					calMess.setTime(messDatum);
					
					alter = berechneAlter(calGeburt, calMess);

					zeichnePatWertBMI(alter, GlucMain.MesspunktefuerGrafik.get(i).MessBMIZehntel, g, false);
				}
				GlucMain.PatAlter = alter;
			}
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C_VERLAUF) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calGeburt = new GregorianCalendar(); 
			GregorianCalendar calMess = new GregorianCalendar(); 
			Date geburtsDatum = new Date();
			Date messDatum = new Date();
			int alter = 0;
			
			try {
				geburtsDatum = sdf.parse(GlucMain.PatGeburtsdatum);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Ungültiges Alter!");
			}
			calGeburt.setTime(geburtsDatum);

			zeichneKoordinatenHbA1c(g, false);
			GlucMain.MesspunktefuerGrafik.trimToSize();
			if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
				for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
					try {
						messDatum = sdf.parse(GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Ungültiges Messdatum: " +
							GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					calMess.setTime(messDatum);
					alter = berechneAlter(calGeburt, calMess);

					zeichnePatWertHbA1c(alter, GlucMain.MesspunktefuerGrafik.get(i).MessHbA1cZehntel,
							g, false);
				}
				GlucMain.PatAlter = alter;
			}
		}
	}
	
	public int print(Graphics g, PageFormat pf, int page) throws
    PrinterException {

		Graphics2D g2d = (Graphics2D) g;
		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

		//hier beginnt die Ausgabe:
		
		//Text drucken:
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ) {
			GlucPrinter TextzurGrafik = new GlucPrinter();
			TextzurGrafik.PatPrintName = PatPrintName;
			TextzurGrafik.TextDrucken(g2d, true);
			TextzurGrafik.AktuellenWertDrucken(g2d, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI) {
			GlucPrinter TextzurGrafik = new GlucPrinter();
			TextzurGrafik.PatPrintName = PatPrintName;
			TextzurGrafik.TextDrucken(g2d, true);
			TextzurGrafik.AktuellenWertDrucken(g2d, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C) {
			GlucPrinter TextzurGrafik = new GlucPrinter();
			TextzurGrafik.PatPrintName = PatPrintName;
			TextzurGrafik.TextDrucken(g2d, true);
			TextzurGrafik.AktuellenWertDrucken(g2d, true);
		}
		
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ_VERLAUF) {
			GlucPrinter TextzurGrafik = new GlucPrinter();
			TextzurGrafik.PatPrintName = PatPrintName;
			TextzurGrafik.TextDrucken(g2d, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI_VERLAUF) {
			GlucPrinter TextzurGrafik = new GlucPrinter();
			TextzurGrafik.PatPrintName = PatPrintName;
			TextzurGrafik.TextDrucken(g2d, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C_VERLAUF) {
			GlucPrinter TextzurGrafik = new GlucPrinter();
			TextzurGrafik.PatPrintName = PatPrintName;
			TextzurGrafik.TextDrucken(g2d, true);
		}
		
	
		//Grafik drucken:
		g2d.translate(0,250);
		g2d.scale(0.45,0.45);
		//Stroke alterStroke = g2d.getStroke();			//falls für die Druckausgabe die
		//g2d.setStroke(new BasicStroke(2));			//Liniendicke verändert werden soll
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ) {
			zeichneKoordinatenNuechternBZ(g2d, true);
			zeichnePatWertNuechternBZ(0, GlucMain.PatNuechternBZ, g2d, true);
			}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI) {
			zeichneKoordinatenBMI(g2d, true);
			zeichnePatWertBMI(0, GlucMain.PatBMIZehntel, g2d, true);
			}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C) {
			zeichneKoordinatenHbA1c(g2d, true);
			zeichnePatWertHbA1c(0, GlucMain.PatHbA1cZehntel, g2d, true);
			}
		
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ_VERLAUF) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calGeburt = new GregorianCalendar(); 
			GregorianCalendar calMess = new GregorianCalendar(); 
			Date geburtsDatum = new Date();
			Date messDatum = new Date();
			int alter = 0;
			
			try {
				geburtsDatum = sdf.parse(GlucMain.PatGeburtsdatum);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Ungültiges Alter!");
			}
			calGeburt.setTime(geburtsDatum);

			zeichneKoordinatenNuechternBZ(g2d, true);
			GlucMain.MesspunktefuerGrafik.trimToSize();
			if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
				for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
					try {
						messDatum = sdf.parse(GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Ungültiges Messdatum!");
					}
					calMess.setTime(messDatum);
					alter = berechneAlter(calGeburt, calMess);

					/*alter = (calMess.get(Calendar.YEAR) - calGeburt.get(Calendar.YEAR)) * 12;
					if(calMess.get(Calendar.MONTH) > calGeburt.get(Calendar.MONTH)) {
						alter = alter + calMess.get(Calendar.MONTH) - calGeburt.get(Calendar.MONTH);
					}
					if(calMess.get(Calendar.MONTH) < calGeburt.get(Calendar.MONTH)) {
						alter = alter - calMess.get(Calendar.MONTH) + calGeburt.get(Calendar.MONTH);
					}*/
					zeichnePatWertNuechternBZ(alter, GlucMain.MesspunktefuerGrafik.get(i).MessNuechternBZ, g2d, true);	
				}
			}
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI_VERLAUF) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calGeburt = new GregorianCalendar(); 
			GregorianCalendar calMess = new GregorianCalendar(); 
			Date geburtsDatum = new Date();
			Date messDatum = new Date();
			int alter = 0;
			
			try {
				geburtsDatum = sdf.parse(GlucMain.PatGeburtsdatum);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Ungültiges Alter!");
			}
			calGeburt.setTime(geburtsDatum);

			zeichneKoordinatenBMI(g2d, true);
			GlucMain.MesspunktefuerGrafik.trimToSize();
			if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
				for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
					try {
						messDatum = sdf.parse(GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Ungültiges Messdatum!");
					}
					calMess.setTime(messDatum);
					alter = berechneAlter(calGeburt, calMess);

					/*alter = (calMess.get(Calendar.YEAR) - calGeburt.get(Calendar.YEAR)) * 12;
					if(calMess.get(Calendar.MONTH) > calGeburt.get(Calendar.MONTH)) {
						alter = alter + calMess.get(Calendar.MONTH) - calGeburt.get(Calendar.MONTH);
					}
					if(calMess.get(Calendar.MONTH) < calGeburt.get(Calendar.MONTH)) {
						alter = alter - calMess.get(Calendar.MONTH) + calGeburt.get(Calendar.MONTH);
					}*/
					zeichnePatWertBMI(alter, GlucMain.MesspunktefuerGrafik.get(i).MessBMIZehntel, g2d, true);
				}
			}
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C_VERLAUF) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calGeburt = new GregorianCalendar(); 
			GregorianCalendar calMess = new GregorianCalendar(); 
			Date geburtsDatum = new Date();
			Date messDatum = new Date();
			int alter = 0;
			
			try {
				geburtsDatum = sdf.parse(GlucMain.PatGeburtsdatum);
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Ungültiges Alter!");
			}
			calGeburt.setTime(geburtsDatum);

			zeichneKoordinatenHbA1c(g2d, true);
			GlucMain.MesspunktefuerGrafik.trimToSize();
			if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
				for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
					try {
						messDatum = sdf.parse(GlucMain.MesspunktefuerGrafik.get(i).MessDatum);
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Ungültiges Messdatum!");
					}
					calMess.setTime(messDatum);
					alter = berechneAlter(calGeburt, calMess);

					zeichnePatWertHbA1c(alter, GlucMain.MesspunktefuerGrafik.get(i).MessHbA1cZehntel * 10,
							g2d, true);
				}
			}
		}


		//g2d.setStroke(alterStroke);				
		g2d.scale(1/0.45,1/0.45);				//Werte zurücksetzen
		g2d.translate(0,-250);   				//       "

		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;		
	}
	
	public void druckeGrafikFrame() {	
		String InputString;
		if((GlucMain.PatNachname == "") & (GlucMain.PatVorname == "")) {
			InputString = JOptionPane.showInputDialog(null, "Bitte den Namen des Patienten eingeben: ");
		}
		else InputString = GlucMain.PatNachname + ", " + GlucMain.PatVorname;
		if(InputString != null) {
			PatPrintName = InputString;
			if(GlucMain.PatNachname == "") {
				GlucMain.PatNachname = PatPrintName;
			}
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintable(this);
			boolean ok = job.printDialog();
			super.repaint();		
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					JOptionPane.showMessageDialog(null, "Drucken nicht erfolgreich!");
				}
				finally {
					super.repaint();
					}
			}
		}
		
	}

}
