# Wifi Scanner

������:
��� ��� � 203288139	
���� ����� � 311332183
�� ������ � 308542232

����� ����:
������� ����� ������ ������ ���� csv ������� ������ ��������� WigleWifi.
���� ��� ������� ������, ����� ������ �� ����� ������ ����� �- csv  ������.
������� ������ �� �������� ��� ���� ������   ( ID, GPS, Time ) ����� ��� 
����� ���� Kml ������ ����� �� ������� �� ��� ��� �- Google maps.

����� ������ �������:
1)	:ScannerFunctions 
�	getAllcsvFileListFromFolder: ����� ������ ������ �� �� ����� csv  ������� ����� �� ������� ����.
�	readFileAndAddToList: �������� ����� �� ���� �csv   ����� ������ ���� ���������� WigleWifi  ������ WifiList.
�	printToKml: ���� �� ���� �csv  ������ ����� �� �� �� ���� mac  ��� ����� ����� ������ �� ���� ����� ���� ���� kml  �����.
�	filtercsvFileByGPS: ����� ���� csv  ������� ��� ������ GPS .
�	KmlPlacemarkGenerator:  ����� placemark � kml.
�	Swap: �������� ������.
�	printCSVFromWifiLinkedList: ���� �� ��������� , ���� ���� ���� ���� �csv  ����� ��� ������� �� ����� ���� WifiList.
�	printCSVFromArrayList: ���� ArrayList  ��� WifiList  ����� �� ������� ��� ���� CSV.
�	Beas10Wifi:���� ���� �� Wifi  ����� ���� ������ ���� �� 10 �Wifi  �� ������ ���� �����.
�	getAllcsvFilesFromFolderAndAddToOneCSVTable: ���� ��� ���� CSV ���� �� ������� ���� ������ ���� ���� WifiList.
�	run: ���� ������ ������� ������ ���� �� �� ������ ������ ���� �� ���� �kml  ������� ����� ���� �Google Maps.
�	filtercsvFileByTime : ����� ���� Csv  ������ ���� , �� ������ �� ��� ������ ���� ���� ������� ������ ���� kml  ����� �� �� ������   � WIFI ���� �����.


2)	Wifi:
�	WiFi: ���� ����� mac, id, frequency, signal  ������ Wifi ������.
�	getSingal: ����� �� ������ �� ����� �Wifi    ������.
�	toString: ����� �� ������� �� ������.

3)	WifiLinkedList:
�	WifiLinkedList: ���� �����  �� �������: LAT, LON, ALT, Time, ID.
�	add: ����� ������� ���� ���� WIFI   �WiFiLinkedList.
�	IsBelong: ���� �� �� ��' WIFI �����. �� �� ��� ���� ����.
�	getArrWifi: ����� ���� �� WIFI
�	setWifiList: ����� ����� �� WIFI
�	toString: ����� ������ �� ID, LAT, ALT, LON, LLWF

