import java.io.Serializable;
import java.util.ArrayList;


public class GlucPatient implements Serializable {

	public String PatientenVorname;
	public String PatientenNachname;
	public String PatientenGeburtsdatum;
	public char PatientenGeschlecht;
	public ArrayList<GlucMesspunkt> PatientenMesspunkte;
	
	private GlucMesspunkt neuerMesspunkt;
	
	public GlucPatient() {
		PatientenVorname = "";
		PatientenNachname = "";
		PatientenGeburtsdatum = "01.01.2000";
		PatientenGeschlecht = 'm';
		PatientenMesspunkte = new ArrayList<GlucMesspunkt>();
	}
	
	public GlucPatient(String vorname, String nachname, String geburtsdatum, 
			char geschlecht) {
		PatientenVorname = vorname;
		PatientenNachname = nachname;
		PatientenGeburtsdatum = geburtsdatum;
		PatientenGeschlecht = geschlecht;
		PatientenMesspunkte = new ArrayList<GlucMesspunkt>();
	}
	

}
