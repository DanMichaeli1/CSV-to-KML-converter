import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.GroundOverlay;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LatLonBox;
import de.micromata.opengis.kml.v_2_2_0.LookAt;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import de.micromata.opengis.kml.v_2_2_0.Kml;	

public class kml {

	public static void makeKmlFile(ArrayList<String[]> toDisplay, String path){// זאת הפונקציה מהתיקייה של הק.מ.ל אם תצטרך עוד הסברים תקשר אליי.

		Kml kml = new Kml();
		Document doc = kml.createAndSetDocument();
		String time;
		for (int i = 0; i < toDisplay.size(); i++) {
			time = convertTimeFormat(toDisplay.get(i)[0]);
			TimeStamp ts = new TimeStamp();
			ts.setWhen(time);
			doc.createAndAddPlacemark().withName(toDisplay.get(i)[8]).withOpen(Boolean.TRUE).withTimePrimitive(ts)
			.withDescription("mac: "+toDisplay.get(i)[7]+" freq: "+toDisplay.get(i)[9]+" signal: "+toDisplay.get(i)[6])
			.createAndSetPoint().addToCoordinates(toDisplay.get(i)[3]+", "+toDisplay.get(i)[2]);
		}
		try {
			kml.marshal(new File(path));
		} catch (IOException ex) {
			System.out.print("Error reading file\n" + ex);
			System.exit(2);
		}
	}

	//ONEPLUS A3003
	private static String convertTimeFormat(String oldTimeFormat) { /// סעיף מספר 7
		String[] dateTime = oldTimeFormat.split(" ");
		//System.out.println(dateTime[0] + 'T' + dateTime[1]);
		return dateTime[0] + 'T' + dateTime[1];


	}

}
