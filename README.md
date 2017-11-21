# CSV to KML converter

מגישים:
דור לוי – 203288139	
ירדן מזרחי – 311332183
דן מיכאלי – 308542232

תיאור כללי:
התוכנית מקבלת תיקייה ומחפשת קבצי csv המכילים נתונים מאפלקציית WigleWifi.
לאחר מכן התוכנית ממיינת, מסדרת ומאחדת את המידע שמתקבל מקבצי ה- csv  השונים.
התוכנית ממיינת את הנתוננים לפי בקשת המשתמש   ( ID, GPS, Time ) ולאחר מכן 
מיצאת קובץ Kml המאפשר לראות את הנקודות על גבי מפה ב- Google maps.

פירוט מחלקות ומתודות:
1)	:ScannerFunctions 

•	getAllcsvFileListFromFolder: מקבלת תיקייה ומחפשת את כל הקבמי csv  ומחזירה רשימה עם הכתובות שלהן.

•	readFileAndAddToList: הפונקציה קוראת את קבצי הcsv   מוודא שהקובץ יובא מאפילקציית WigleWifi  ויוצרת WifiList.

•	printToKml: עובר על קובץ הcsv  שסיננו ובודק אם יש את אותו mac  כמה פעמים ומסנן ומוסיף את הרשת החזקה יותר קובץ kml  הסופי.

•	filtercsvFileByGPS: מקבלת קובץ csv  וממיינת לפי נתונים GPS .

•	KmlPlacemarkGenerator:  מוסיף placemark ל kml.

•	Swap: הפונקציה הרגילה.

•	printCSVFromWifiLinkedList: עובר על הסטרינגים , שורה שורה מתוך קובץ הcsv  ומסדר לפי הדרישות של המטלה בתוך WifiList.

•	printCSVFromArrayList: מקבל ArrayList  בשם WifiList  וכותב את הנתונים שלו בתוך CSV.

•	Beas10Wifi:מקבל מערך של Wifi  ממיין אותו ומחזיר מערך עם 10 הWifi  עם הסיגנל החזק ביותר.

•	getAllcsvFilesFromFolderAndAddToOneCSVTable: מקבל כמה קבצי CSV מאחד את הנתונים שלהם ומכניס אותם לתוך WifiList.

•	run: מקבל נתונים מהמשתמש ומפכיל בעצם את כל התכונה ולבסוף פותח את קובץ הkml  שייצרנו ופותח אותו בGoogle Maps.

•	filtercsvFileByTime : מקבלת קובץ Csv  מתוכנה שלנו , יש להכניס לו זמן התחלתי וזמן סופי והמתודה מחזירה קובץ kml  שמכיל את כל נקודות   ה WIFI בזמן הנתון.



2)	Wifi:

•	WiFi: בנאי שמקבל mac, id, frequency, signal  מנקודת Wifi הנתונה.

•	getSingal: מחזיר את העוצמה של נקודת הWifi    הנתונה.

•	toString: מדפיס את הנתונים של הנקודה.


3)	WifiLinkedList:

•	WifiLinkedList: בנאי שמקבל  את הנתונים: LAT, LON, ALT, Time, ID.

•	add: מוסיף אובייקט נוסף מסוג WIFI   לWiFiLinkedList.

•	IsBelong: בודק אם יש נק' WIFI דומות. אם יש הוא מאחד אותן.

•	getArrWifi: מחזיר מערך של WIFI

•	setWifiList: מכניס למערך של WIFI

•	toString: מדפיס נתונים של ID, LAT, ALT, LON, LLWF


