import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * 
 * @author Dan Michaeli, Dor Levi, Yarden Mizrahi
 *
 */
public class ScannerFunctions{

	/**
	 * 
	 * @param directoryName directory to search csv files
	 * @return list with all the csv files paths
	 */
	public static ArrayList<String> getAllcsvFileListFromFolder(String directoryName){

		ArrayList<String> fileList = new ArrayList<String>();
		File directory = new File(directoryName);

		//get all the files from a directory

		if (!directory.isDirectory()) {
			return fileList;
		}
		File[] fList = directory.listFiles();

		for (File file : fList){

			if (file.isFile()){

				if(file.getAbsolutePath().endsWith(".csv")){
					fileList.add(file.getAbsolutePath());
					System.out.println("Fetching data from: "+file.getAbsolutePath());
				}

			} else if (file.isDirectory()){

				fileList.addAll(getAllcsvFileListFromFolder(file.getAbsolutePath()));

			}

		}
		return fileList;

	}

	/**
	 * 
	 * @param fileName
	 * @param wifiList
	 * 
	 */
	private static void readFileAndAddToList(String fileName, LinkedList<WiFiLinkedList> wifiList ) {

		try {
			FileReader Fr = new FileReader(fileName);
			BufferedReader BR= new BufferedReader(Fr); 
			int count=0;
			WiFiLinkedList wll = new WiFiLinkedList();
			String Line = BR.readLine();
			String[] firstLine = Line.split(",");
			String id = "";
			while(Line != null){
				if(count==0){
					if(!Line.contains("WigleWifi")){
						System.out.println("Error: File must be from WigleWifi ("+fileName+").");
						break;
					}
					else id = firstLine[2].substring(6);
				}
				count++;
				if(count>2){
					String[] arr = Line.split(",");

					//Creating WIFILIST
					String time = arr[3];
					double alt = Double.parseDouble(arr[8]);
					double lat =  Double.parseDouble(arr[6]);
					double lon = Double.parseDouble(arr[7]);
					if (wifiList.size() == 0){
						wll = new WiFiLinkedList(lat, lon, alt, time, id);
						wifiList.add(wll);
					}

					//System.out.println(Arrays.toString(arr));

					//Creating WiFi
					String SSID = arr[1],MAC = arr[0];
					double freq = Double.parseDouble(arr[4]) , signal = Double.parseDouble(arr[5]);
					WiFi wf = new WiFi(SSID,MAC ,freq ,signal);

					if(!wll.IsBelong(lat, lon, time)){
						wll = new WiFiLinkedList(lat, lon, alt, time, id);
						wifiList.add(wll);		//adding WiFiLinkedList to LinkedList (ans)
					}
					wll.add(wf);		//adding WiFi to WiFiLinkedList
				}
				Line = BR.readLine();		//next line
			}
			BR.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param csvFile
	 * @param pathToWriteKML
	 * @param startTime
	 * @param endTime
	 */
	public static void filtercsvFileByTime(String csvFile, String pathToWriteKML, String startTime, String endTime ){
		int lineYear, lineMonth, lineDay;
		int startYear, startMonth, startDay;
		int endYear, endMonth, endDay;
		startYear = Integer.parseInt(startTime.substring(0, 4));
		startMonth = Integer.parseInt(startTime.substring(5, 7));
		startDay = Integer.parseInt(startTime.substring(8, 10));
		endYear = Integer.parseInt(endTime.substring(0, 4));
		endMonth = Integer.parseInt(endTime.substring(5, 7));
		endDay = Integer.parseInt(endTime.substring(8, 10));

		try {
			FileReader fr = new FileReader(csvFile);
			BufferedReader br= new BufferedReader(fr); 
			ArrayList<String> filteredCSV = new ArrayList<String>();
			String brFirstLine = br.readLine(); //skipping first line
			String brLine = br.readLine();
			String[] line = brLine.split(",");
			String timeColumn = line[0];
			String hmsStart = startTime.substring(11, timeColumn.length()); //hms = hour, minutes, seconds
			String hmsEnd = endTime.substring(11, timeColumn.length());
			String lineDate = "";
			LocalTime lineTime = null;

			// filtering the csv file
			while(brLine != null){
				//getting line's time data
				lineTime = LocalTime.parse(brLine.substring(11, timeColumn.length()));
				lineDate = brLine.substring(0, 10);
				lineYear = Integer.parseInt(lineDate.substring(0, 4));
				lineMonth = Integer.parseInt(lineDate.substring(5, 7));
				lineDay = Integer.parseInt(lineDate.substring(8, 10));
				if(lineYear>=startYear && lineYear<=endYear && lineMonth>=startMonth && lineMonth<=endMonth && lineDay>startDay) {
					hmsStart = "00:00:00";
				}
				if(lineYear>=startYear && lineYear<=endYear && lineMonth>=startMonth && lineMonth<=endMonth && lineDay<endDay) {
					hmsEnd = "23:59:59";
				}
				// checking if the line's time is corresponding to the given time filter
				if(lineYear>=startYear && lineYear<=endYear && lineMonth>=startMonth && lineMonth<=endMonth && lineDay>=startDay && lineDay<=endDay) {
					if(lineTime.isAfter(LocalTime.parse(hmsStart)) && lineTime.isBefore(LocalTime.parse(hmsEnd)))
					{
						filteredCSV.add(brLine);
						brLine = br.readLine();
					}
					else brLine = br.readLine();
				}
				else brLine = br.readLine();
			}
			fr.close();
			br.close();

			ArrayList<String[]> filtered = new ArrayList<String[]>();
			for(int i = 0; i < filteredCSV.size(); i++)
			{
				filtered.add(filteredCSV.get(i).split(","));
			}
			kml.makeKmlFile(filtered, pathToWriteKML);
			//printToKML(filtered, pathToWriteKML); // creating the kml file
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void filtercsvFileByID(String csvFile, String pathToWriteKML, String ID) {

		try {
			FileReader fr = new FileReader(csvFile);
			BufferedReader br= new BufferedReader(fr); 
			ArrayList<String> filteredCSV = new ArrayList<String>();
			String brFirstLine = br.readLine(); // skipping first line
			String brLine = br.readLine();

			// filtering the csv file
			while(brLine != null){
				String[] line = brLine.split(",");
				if(line[1].equals(ID))
				{
					filteredCSV.add(brLine);
					brLine = br.readLine();
				}
				else brLine = br.readLine();
			}

			fr.close();
			br.close();

			ArrayList<String[]> filtered = new ArrayList<String[]>();
			for(int i = 0; i < filteredCSV.size(); i++)
			{
				filtered.add(filteredCSV.get(i).split(","));
			}

			printToKML(filtered, pathToWriteKML);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void filtercsvFileByGPS(String csvFile, String pathToWriteKML, double lonStart, double latStart, double lonEnd, double latEnd){
		// filter rectangle coordinates
		double lineLon, lineLat;
		double xTopRight = Math.max(lonStart, lonEnd);
		double yTopRight = Math.max(latStart, latEnd);
		double xBottomLeft = Math.min(lonStart, lonEnd);
		double yBottomLeft = Math.min(latStart, latEnd);
		double xBottomRight = xTopRight;
		double yBottomRight = yBottomLeft;
		double xTopLeft = xBottomLeft;
		double yTopLeft = yTopRight;

		try {

			FileReader fr = new FileReader(csvFile);
			BufferedReader br= new BufferedReader(fr); 
			ArrayList<String> filteredCSV = new ArrayList<String>();
			String brFirstLine = br.readLine(); // skipping first line
			String brLine = br.readLine();

			// filtering the csv file
			while(brLine != null){
				String[] line = brLine.split(",");
				lineLat = Double.parseDouble(line[2]);
				lineLon= Double.parseDouble(line[3]);
				if(lineLat<yTopRight && lineLat>=yBottomRight && lineLon<xTopRight && lineLon>=xTopLeft)
				{
					filteredCSV.add(brLine);
					brLine = br.readLine();
				}
				else brLine = br.readLine();
			}

			fr.close();
			br.close();

			ArrayList<String[]> filtered = new ArrayList<String[]>();
			for(int i = 0; i < filteredCSV.size(); i++)
			{
				filtered.add(filteredCSV.get(i).split(","));
			}

			double[] rectTop = {xTopLeft, yTopLeft, xTopRight, yTopRight}; 
			double[] rectBot = {xBottomLeft, yBottomLeft, xBottomRight, yBottomRight};

			printToKmlByGPS(filtered, pathToWriteKML, rectTop, rectBot);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param lon = Placemark longitude
	 * @param lat = Placemark latitude
	 * @param pointName = Placemark name
	 * @param Desc = Placemark description
	 * @return string
	 */
	private static String kmlPlacemarkGenerator(String lon,String lat,String pointName, String desc){// adds a placemark (with description)
		if(pointName.indexOf('&')>=0)
		{
			pointName = pointName.replaceAll("&", "&amp;");
		}
		if(desc.indexOf('&')>=0)
		{
			desc = desc.replaceAll("&", "&amp;");
		}

		String all = "<Placemark>\n           <name>"+pointName+"</name>\n           <description>"+ desc+"</description>\n "
				+ "          <styleUrl>#red</styleUrl>\n           <Point>\n                   "
				+ "         <coordinates>";
		all+= lon+","+lat+"</coordinates>\n           </Point>\n       </Placemark>\n\n		";


		return all;

	}

	private static String addFilteringArea(double[] rectTop, double[] rectBot, String kml){//adds the rectangle of the filtering area
		//rectTop = {xTopLeft, yTopLeft, xTopRight, yTopRight}
		//rectBot = {xBottomLeft, yBottomLeft, xBottomRight, yBottomRight}
//<altitudeMode>relativeToGround</altitudeMode>
		kml+="<Placemark>\n      <name>Filtered Area</name>\n      <styleUrl>#msn_ylw-pushpin</styleUrl>\n "
				+ "     <Polygon>\n        <extrude>1</extrude>\n          		<tessellate>1</tessellate>\n"
				+ "				\n 				<outerBoundaryIs>\n"
				+ "					<LinearRing>\n           "
				+ " <coordinates>\n              " 
				+rectTop[0]+","+rectTop[1]+",50\n              "
				+rectTop[2]+","+rectTop[3]+",50\n              "
				+rectBot[2]+","+rectBot[3]+",50\n              "
				+rectBot[0]+","+rectBot[1]+",50\n              "
				+rectTop[0]+","+rectTop[1]+",50\n              "
				+"</coordinates>\n       "
				+ "   </LinearRing>\n        </outerBoundaryIs>\n      </Polygon>\n    </Placemark>";
		return kml;
	}

	private static void swap(WiFi[] arr, int i, int j){
		WiFi temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	/**
	 * 
	 * @param list = list of lines from the csv file
	 * @param path = path to create the kml file
	 * @param rectTop = coordinates for the upper points of the rectangle
	 * @param rectBot = coordinates for the lower points of the rectangle
	 */
	private static void printToKmlByGPS(ArrayList<String[]> list, String path, double[] rectTop, double[] rectBot)
	{
		HashMap<Integer, String> mac = new HashMap<>(); //creating a list of mac
		for(int i = 0; i < list.size(); i++)
		{
			mac.put(i, list.get(i)[7]);
		}

		String temp = "";
		String[] lineMark = {"#","","","","","","-200"}; // used to mark lines that won't get added to the kml file
		for(int i = 0; i < list.size(); i++)
		{
			temp = mac.get(i);
			for(int j = i + 1; j < list.size(); j++)
			{
				if(temp.equals(mac.get(j)) && i!=j) // if the mac address of this line was found in another line
				{
					if(Double.parseDouble(list.get(j)[6])<Double.parseDouble(list.get(i)[6])) // replace the line with the lower signal with lineMark 
					{
						list.set(j, lineMark);
					}
					else list.set(i, lineMark);
				}
			}
		}
		PrintWriter pw = null;
		try {
			pw  = new PrintWriter(new File(path));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		String kmlHeader = " <kml xmlns=\"http://www.opengis.net/kml/2.2\">\n    <Document>\n       <name>Wifi Scanner.kml</name> <open>1</open>\n "
				+ "      <Style id=\"red\">\n      <IconStyle>\n        <Icon>\n"
				+ "          <href>http://maps.google.com/mapfiles/ms/icons/red-dot.png</href>\n        </Icon>\n      </IconStyle>\n    </Style>\n<Style id=\"Magnifier\">\n "
				+ "     <IconStyle>\n        <Icon>\n "
				+ "         <href>https://images.vexels.com/media/users/3/132064/isolated/preview/27a9fb54f687667ecfab8f20afa58bbb-search-businessman-circle-icon-by-vexels.png</href>\n"
				+ "        </Icon>\n      </IconStyle>\n    </Style><Style id=\"exampleStyleDocument\">           <LabelStyle>\n           <color>ff0000cc</color>\n           </LabelStyle>\n"
				+ "         </Style>\n\n       <Style id=\"transBluePoly\">\n      <LineStyle>\n        <width>1.5</width>\n      </LineStyle>\n      <PolyStyle>\n        <color>7dff0000</color>\n "
				+ "     </PolyStyle>\n    </Style> <Folder><name>Wifi Networks</name>";

		builder.append(kmlHeader);

		String polygonHeader = "<name>Untitled Polygon.kml</name>\n" + "	<Style id=\"sh_ylw-pushpin\">\n" + "		<IconStyle>\n" + "			<scale>1.1</scale>\n" + "			<Icon>\n" + 
				"				<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\n" + "			</Icon>\n" + "			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\n"
				+ "		</IconStyle>\n" + "		<LineStyle>\n" + 
				"			<color>ff000000</color>\n" + "			<width>3</width>\n" + "		</LineStyle>\n" + "		<PolyStyle>\n" + "			<color>66ffff55</color>\n" + 
				"		</PolyStyle>\n" + "	</Style>\n" + "	<Style id=\"sn_ylw-pushpin\">\n" + "		<IconStyle>\n" + "			<scale>1.3</scale>\n" + "			<Icon>\n" + 
				"				<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\n" + "			</Icon>\n" + "			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>\n"
				+ "		</IconStyle>\n" + "		<LineStyle>\n" + 
				"			<color>ff000000</color>\n" + "			<width>3</width>\n" + "		</LineStyle>\n" + "		<PolyStyle>\n" + "			<color>66ffff55</color>\n" + "		</PolyStyle>\n" + 
				"	</Style>\n" + "	<StyleMap id=\"msn_ylw-pushpin\">\n" + "		<Pair>\n" + "			<key>normal</key>\n" + "			<styleUrl>#sn_ylw-pushpin</styleUrl>\n" + 
				"		</Pair>\n" + 				"		<Pair>\n" + "			<key>highlight</key>\n" + "			<styleUrl>#sh_ylw-pushpin</styleUrl>\n" +"		</Pair>\n" + 	"	</StyleMap>";

		// add the filtering area to the kml file
		builder.append(polygonHeader);
		String polygon = addFilteringArea(rectTop, rectBot, path); 
		builder.append(polygon);									

		// writing the kml file
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i)!=lineMark) // if the line was not marked add its data to the kml
			{
				String desc = "Wifi SSID: "+list.get(i)[8]+"\nMac: "+list.get(i)[7]+"\nSignal strength: "+list.get(i)[6]
						+"\nTime: "+list.get(i)[0]+"\nChannel: "+list.get(i)[9]
								+"\nNumber of additional wifi connections: "+list.get(i)[5];
				builder.append(kmlPlacemarkGenerator(list.get(i)[3], list.get(i)[2], list.get(i)[8], desc));
			}
			if(i+1==list.size()){
				builder.append("</Folder>\n</Document>\n</kml>");
				pw.write(builder.toString());
				pw.close();
			}
		}
	}

	private static void printToKML(ArrayList<String[]> list, String path) {
		HashMap<Integer, String> mac = new HashMap<>(); //creating a list of mac
		for(int i = 0; i < list.size(); i++)
		{
			mac.put(i, list.get(i)[7]);
		}

		String temp = "";
		String[] lineMark = {"#","","","","","","-200"}; // used to mark lines that won't get added to the kml file
		for(int i = 0; i < list.size(); i++)
		{
			temp = mac.get(i);
			for(int j = i + 1; j < list.size(); j++)
			{
				if(temp.equals(mac.get(j)) && i!=j)
				{
					if(Double.parseDouble(list.get(j)[6])<Double.parseDouble(list.get(i)[6]))
					{
						list.set(j, lineMark);
					}
					else list.set(i, lineMark);
				}
			}
		}
		PrintWriter pw = null;
		try {
			pw  = new PrintWriter(new File(path));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		//http://www.freepngimg.com/download/wifi/4-2-wi-fi-png-images.png
		String kmlHeader = " <kml xmlns=\"http://www.opengis.net/kml/2.2\">\n    <Document>\n       <name>Wifi Scanner.kml</name> <open>1</open>\n "
				+ "      <Style id=\"red\">\n      <IconStyle>\n        <Icon>\n"
				+ "          <href>http://maps.google.com/mapfiles/ms/icons/red-dot.png</href>\n        </Icon>\n      </IconStyle>\n    </Style>\n<Style id=\"Magnifier\">\n      <IconStyle>\n        <Icon>\n          <href>https://images.vexels.com/media/users/3/132064/isolated/preview/27a9fb54f687667ecfab8f20afa58bbb-search-businessman-circle-icon-by-vexels.png</href>\n        </Icon>\n      </IconStyle>\n    </Style><Style id=\"exampleStyleDocument\">           <LabelStyle>\n           <color>ff0000cc</color>\n           </LabelStyle>\n         </Style>\n\n       <Style id=\"transBluePoly\">\n      <LineStyle>\n        <width>1.5</width>\n      </LineStyle>\n      <PolyStyle>\n        <color>7dff0000</color>\n      </PolyStyle>\n    </Style> <Folder><name>Wifi Networks</name>";
		builder.append(kmlHeader);
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i)!=lineMark)
			{
				String desc = "Wifi SSID: "+list.get(i)[8]+"\nMac: "+list.get(i)[7]+"\nSignal strength: "+list.get(i)[6]
						+"\nTime: "+list.get(i)[0]+"\nChannel: "+list.get(i)[9]
								+"\nNumber of additional wifi connections: "+list.get(i)[5]
										+"\nCapturing device: "+list.get(i)[1];
				builder.append(kmlPlacemarkGenerator(list.get(i)[3], list.get(i)[2], list.get(i)[8], desc));
			}
			if(i+1==list.size()){
				builder.append("</Folder>\n</Document>\n</kml>");
				pw.write(builder.toString());
				pw.close();
			}
		}
	}

	/**
	 * 
	 * @param CSVFile
	 * @param wifiList
	 */
	private static void printCSVFromWiFiLinkedList(String CSVFile, LinkedList<WiFiLinkedList> wifiList)
	{
		PrintWriter pw = null;
		try {
			pw  = new PrintWriter(new File(CSVFile));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		String ColumnNamesList = "TIME,ID,LAT,LON,ALT,WIFI NETWORK,SIGNAL1,MAC1,SSID1,FREQNCY1,SIGNAL2,MAC2,SSID2,FREQNCY2,SIGNAL3,MAC3,SSID3,FREQNCY3,SIGNAL4,MAC4,SSID4,FREQNCY4,SIGNAL5,MAC5,SSID5,FREQNCY5,SIGNAL6,MAC6,SSID6,FREQNCY6,SIGNAL7,MAC7,SSID7,FREQNCY7,SIGNAL8,MAC8,SSID8,FREQNCY8,SIGNAL9,MAC9,SSID9,FREQNCY9,SIGNAL10,MAC10,SSID10,FREQNCY10";
		for (int i = 0; i < wifiList.size(); i++) {
			if (i==0){
				builder.append(ColumnNamesList +"\n");}
			else{ builder.append(wifiList.get(i));
			builder.append('\n');}
			if(i+1==wifiList.size()){
				pw.write(builder.toString());
				pw.close();
			}
		}
		int slashIndex = 0;
		int count = 0;
		String s = "";
		String slash = "\\";
		while(!s.equals(slash)  || s.equals("/"))
		{
			s = CSVFile.substring(slashIndex, slashIndex+1);
			slashIndex = CSVFile.length() - count - 1;
			count++;
		}
		System.out.println("Created "+CSVFile.substring(slashIndex + 2, CSVFile.length())+" successfuly");
	}

	private static WiFi[] Best10WIFI(WiFi[] ans) {
		boolean flag= true;
		for (int i = 0; i < ans.length && flag; i++) {
			flag=false;
			for (int j = 0; j < ans.length-1; j++) {
				if(ans[j].getSignal()<ans[j+1].getSignal() ){
					flag=true;
					swap(ans,j,j+1);
				}
			}
		}
		WiFi [] first10= new WiFi[Math.min(10,ans.length)];
		for (int i = 0; i < first10.length; i++) {
			first10[i]=ans[i];
		}
		return first10;
	}

	/**
	 * 
	 * @param directoryName directory to collect all csv files from
	 * @param csvWritePath path to write the merged csv file
	 */
	public static void getAllcsvFilesFromFolderAndAddtoOneCSVTable(String directoryName, String csvWritePath)
	{
		ArrayList<String> fileList = getAllcsvFileListFromFolder(directoryName);
		LinkedList<WiFiLinkedList> wifiList = new LinkedList<WiFiLinkedList>();
		for (String csvFileName : fileList) {
			readFileAndAddToList(csvFileName, wifiList);
		}

		for (int i = 0; i < wifiList.size(); i++) {
			WiFi[] result = Best10WIFI(wifiList.get(i).getArrWiFi());
			wifiList.get(i).setWiFiList(result);    
		}
		printCSVFromWiFiLinkedList(csvWritePath, wifiList);
	}

	// ***** MAIN PROGRAM *****
	public static void run()
	{
		boolean exit = false;
		while(!exit) {
			System.out.println("Select a folder to scan for csv files: ");
			Scanner folderScanner = new Scanner(System.in);
			String folder = folderScanner.nextLine();
			System.out.println("Enter path to write the CSV file (with the file's name): ");
			Scanner csvScanner = new Scanner(System.in);
			String csvWriteFolder = csvScanner.nextLine();

			getAllcsvFilesFromFolderAndAddtoOneCSVTable(folder, csvWriteFolder);

			System.out.println("Create a KML file Sorted by (1)Time, (2)GPS, (3)ID: ");
			Scanner sort = new Scanner(System.in);
			int option = Integer.parseInt(sort.nextLine());

			switch(option)
			{
			case 1: { // filter by time
				System.out.println("Filter by time syntax:\nStart time: year(xxxx):month(xx):day(xx):hr(xx):min(xx):sec(xx)"
						+ " \nEnd time: year(xxxx):month(xx):day(xx):hr(xx):min(xx):sec(xx)");
				System.out.println("Enter path to write the KML file (with the file's name): ");
				Scanner kmlPathScan = new Scanner(System.in);
				String kmlPath = kmlPathScan.nextLine();
				System.out.println("Start time: ");
				Scanner startTimeScan = new Scanner(System.in);
				String startTime = startTimeScan.nextLine();
				System.out.println("End time: ");
				Scanner endTimeScan = new Scanner(System.in);
				String endTime = endTimeScan.nextLine();
				filtercsvFileByTime(csvWriteFolder, kmlPath,  startTime, endTime);
				System.out.println("Success!");
				kmlPathScan.close();
				startTimeScan.close();
				endTimeScan.close();
				break;
			}
			case 2: { // filter by GPS
				System.out.println("Filter by GPS syntax:\nStart lat (bottom right corner of the filtering rectangle): 32.xxxxxx\nStart lon (bottom right corner of the filtering rectangle): 35.xxxxxx"
						+ "\nEnd lat (top left corner of the filtering rectangle): 32.xxxxxx \nEnd lon (top left corner of the filtering rectangle): 35.xxxxxx");
				System.out.println("\nEnter path to write the KML file (with the file's name): ");
				Scanner kmlPathScan = new Scanner(System.in);
				String kmlPath = kmlPathScan.nextLine();
				System.out.println("Start lat: ");
				Scanner startLatScan = new Scanner(System.in);
				double startLat = startLatScan.nextDouble();
				System.out.println("Start lon: ");
				Scanner startLonScan = new Scanner(System.in);
				double startLon = startLonScan.nextDouble();
				System.out.println("End lat: ");
				Scanner endLatScan = new Scanner(System.in);
				double endLat = endLatScan.nextDouble();
				System.out.println("End lon: ");
				Scanner endLonScan = new Scanner(System.in);
				double endLon = endLonScan.nextDouble();
				filtercsvFileByGPS(csvWriteFolder, kmlPath, startLon, startLat,
						endLon, endLat);

				kmlPathScan.close();
				startLatScan.close();
				endLatScan.close();
				startLatScan.close();
				startLonScan.close();
				endLonScan.close();
				System.out.println("Success!");
				break;
			}
			case 3: { // filter by ID
				System.out.println("Enter path to write the KML file (with the file's name): ");
				Scanner kmlPathScan = new Scanner(System.in);
				String kmlPath = kmlPathScan.nextLine();
				System.out.println("Device ID: ");
				Scanner id = new Scanner(System.in);
				filtercsvFileByID(csvWriteFolder, kmlPath, id.nextLine());
				kmlPathScan.close();
				id.close();
				System.out.println("Success!");
				break;
			}
			}
			sort.close();
			csvScanner.close();
			folderScanner.close();
			System.out.println("Input 'c' to continue or 'x' to exit...");
			Scanner input = new Scanner(System.in);
			while(!input.hasNextLine()){
				if(input.hasNextLine()){
					if(input.nextLine()=="x"){
						exit = true;
						break;
					}
					if(input.nextLine()=="c"){
						exit = false;
						break;
					}
				}
			}
		}
		System.out.println("done!");
	}

	// ***** TESTER *****
	public static void runTest(){
		String folder = "C:\\Users\\Dan\\Desktop\\New folder\\27.10";
		//String folder = "C:\\Users\\USER\\Desktop\\data\\New folder";
		String csvWritePath = "C:\\Users\\Dan\\Desktop\\Test\\merged csv file.csv";
		/*		String kmlFolder="C:\\Users\\USER\\Desktop\\New folder\\BookTime.kml";
				    2017:11:12:13:06:42
				 	2017:11:12:13:09:42
		start
		<coordinates>34.97470633453521,32.06320051865906,0</coordinates>
		
		end
		<coordinates>34.87623703518137,32.11926397274331,0</coordinates>
		*/
		getAllcsvFilesFromFolderAndAddtoOneCSVTable(folder, csvWritePath);

		System.out.println("Create a KML file Sorted by (1)Time, (2)GPS, (3)ID: ");
		Scanner sort = new Scanner(System.in);
		int option = Integer.parseInt(sort.nextLine());

		switch(option)
		{
		case 1: {
			System.out.println("Filter by time syntax:\nStart time: year(xxxx):month(xx):day(xx):hr(xx):min(xx):sec(xx)"
					+ " \nEnd time: year(xxxx):month(xx):day(xx):hr(xx):min(xx):sec(xx)");
			System.out.println("Enter path to write the KML file: ");
			String kmlPath = "C:\\Users\\Dan\\Desktop\\Test\\BookTime_TEST2.kml";
			String startTime = "2017:11:19:23:03:57";
			String endTime = "2017:11:20:00:55:57";
			filtercsvFileByTime(csvWritePath, kmlPath,  startTime, endTime);
			System.out.println("Success!");
			break;
		}
		case 2: {
			System.out.println("Filter by GPS syntax:\nStart lon: 35.xxxxxx, Start lat: 32.xxxxxx\nEnd lon: 35.xxxxxx, End lat: 32.xxxxxx");
			String kmlPath = "C:\\Users\\Dan\\Desktop\\Test\\BookGPS_TEST.kml";
			double startLon = 35.20889839056406;
			double endLon = 35.21127107088187;
			double startLat = 32.1037564590792;
			double endLat = 32.105012886431425;

			filtercsvFileByGPS(csvWritePath, kmlPath, startLon, startLat,
					endLon, endLat);

			System.out.println("Success!");
			break;
		}
		case 3: {
			System.out.println("Enter path to write the KML file: ");
			String kmlPath = "C:\\Users\\Dan\\Desktop\\Test\\BookID_TEST.kml";
			String ID = "SM-G935F";
			filtercsvFileByID(csvWritePath, kmlPath, ID);
			break;
		}
		}
		sort.close();

		System.out.println("done!");
	}
	public static void main(String[] args) {

		/*
		String folder = "C:\\Users\\USER\\Desktop\\data\\New folder";
		String csvWritePath = "C:\\Users\\USER\\Desktop\\data\\New folder\\merged csv file.csv";
		String kmlFolder="C:\\Users\\Dan\\Desktop\\New folder\\BookTime.kml";
		2017:11:12:13:06:42
		2017:11:12:13:09:42 
		2017:11:19:23:03:57
		2017:11:20:01:11:15
		Start lon: 
			35.20889839056406
		End lon: 
			35.21127107088187
		Start lat: 
			32.1037564590792
		End lat: 
			32.105012886431425
		 */
		//run();
		runTest();

	}
}
