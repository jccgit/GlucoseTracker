import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import java.awt.print.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GlucPrinter implements Printable, ActionListener {
	
	public String PatPrintName = ""; 
	public Date heutigesDatum;
	public DateFormat heutigesDatumFormatiert;
	private Font normalFont = new Font("Helvetica", Font.PLAIN, 12);
	private Font fettFont = new Font("Helvetica", Font.BOLD, 12);
	private Font kursivFont = new Font("Helvetica", Font.ITALIC, 10);
	
	public void TextDrucken(Graphics g, Boolean grafikvorhanden) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.translate(30,0);
		heutigesDatum = new Date();
		heutigesDatumFormatiert = DateFormat.getDateInstance(DateFormat.MEDIUM);
		g.setFont(kursivFont);
		g.drawString("GlucoseTracker "+ GlucMain.VersionsString, 50, 90);
		g.drawString("Report vom " + heutigesDatumFormatiert.format(heutigesDatum), 350, 90);
		g.drawLine(50, 100, 460, 100);
		g.setFont(fettFont);
		g.drawString(PatPrintName, 50, 130);
		g.setFont(normalFont);
		g.drawString("Geburtsdatum:", 50, 160);
		g.drawString("" + GlucMain.PatGeburtsdatum, 200, 160);
		g.drawString("Geschlecht: " + GlucMain.PatGeschlecht, 350, 160);
		g2d.translate(-30,0);
	}
	
	public void VerlaufText(Graphics g, ArrayList<GlucMesspunkt> messpunkte) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.translate(30,0);
		g.drawString("Geburtsdatum: " + GlucMain.PatGeburtsdatum, 50, 190);
		g.setFont(fettFont);
		g.drawString("Bisheriger Wachstumsverlauf" , 50, 220);
		g.setFont(normalFont);
		g.drawString("Datum:", 50, 250);
		g.drawString("Größe:", 150, 250);
		g.drawString("Gewicht:", 250, 250);
		g.drawString("Kopfumfang:", 350, 250);
		
		GlucMain.MesspunktefuerGrafik.trimToSize();
		if(GlucMain.MesspunktefuerGrafik.isEmpty() == false) {
			for(int i=0; i < GlucMain.MesspunktefuerGrafik.size(); i++) {
				g.drawString(GlucMain.MesspunktefuerGrafik.get(i).MessDatum, 50, 280 + (i*30));
				g.drawString("" + GlucMain.MesspunktefuerGrafik.get(i).MessNuechternBZ, 150, 280 + (i*30));
				g.drawString("" + GlucMain.MesspunktefuerGrafik.get(i).MessBMIZehntel, 250, 280 + (i*30));
				g.drawString("" + GlucMain.MesspunktefuerGrafik.get(i).MessHbA1cZehntel, 350, 280 + (i*30));
			}
		}
		g2d.translate(-30,0);
	}

	
	public void AktuellenWertDrucken(Graphics g, Boolean grafikvorhanden) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.translate(30,0);
		g.drawString("Nüchtern-BZ:" , 50, 190);
		g.drawString("" + GlucMain.PatNuechternBZ + " mg/dl", 200, 190);
		g.drawString("BMI:", 50, 220);
		g.drawString("" + GlucMain.PatBMIZehntel/10 + " kg/m²", 200, 220);
		g.drawString("HbA1c:", 50, 250);
		g.drawString("" + GlucMain.PatHbA1cZehntel/10 + "%", 200, 250);
		g2d.translate(-30,0);
	}

	
	public int print(Graphics g, PageFormat pf, int page) throws
    PrinterException {

		if (page > 0) { /* We have only one page, and 'page' is zero-based */
			return NO_SUCH_PAGE;
		}

		//hier beginnt die Ausgabe:
		this.TextDrucken(g, false);
		//Text drucken:
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ) {
			this.AktuellenWertDrucken(g, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI) {
			this.AktuellenWertDrucken(g, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C) {
			this.AktuellenWertDrucken(g, true);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.NUECHTERNBZ_VERLAUF) {
			this.VerlaufText(g, GlucMain.MesspunktefuerGrafik);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.BMI_VERLAUF) {
			this.VerlaufText(g, GlucMain.MesspunktefuerGrafik);
		}
		if(GlucMain.welcheGrafik == GlucMain.Grafikart.HBA1C_VERLAUF) {
			this.VerlaufText(g, GlucMain.MesspunktefuerGrafik);
		}

		/* tell the caller that this page is part of the printed document */
		return PAGE_EXISTS;		
	}

	public void actionPerformed(ActionEvent e) {
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
			if (ok) {
				try {
					job.print();
				} catch (PrinterException ex) {
					JOptionPane.showMessageDialog(null, "Drucken nicht erfolgreich!");
				}
			}
		}
	}
}