import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

public class GlucMesspunkt implements Comparable<GlucMesspunkt>, Serializable  {

	public String MessDatum;
	public int MessNuechternBZ;
	public int MessBMIZehntel;
	public int MessHbA1cZehntel;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

	@Override
	public int compareTo(GlucMesspunkt mp) {
		try {
			return (sdf.parse(MessDatum).compareTo(sdf.parse(mp.MessDatum)));
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Ungültiges Datum: " + MessDatum + ", " + mp.MessDatum);
			return 0;
		}
    }
	
	public GlucMesspunkt() {
		MessDatum = "01.01.2000";
		MessNuechternBZ = 0;
		MessBMIZehntel = 0;
		MessHbA1cZehntel = 0;	
	}
	
	public GlucMesspunkt(String datum, int nuechternbz, int bmizehntel, int hba1czehntel) {
		MessDatum = datum;
		MessNuechternBZ = nuechternbz;
		MessBMIZehntel = bmizehntel;
		MessHbA1cZehntel = hba1czehntel;	
	}

}
