package org.usfirst.frc.team5113.comms;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.vision.USBCamera;

//"Host" robot side code for creating and recieving data from the network tables of IRIS.

public class IRISComms {
	
	private String tableName = "IRISTable";
	private NetworkTable table;

	public IRISComms()
	{
		init();
	}
	
	private void init()
	{
		table = NetworkTable.getTable(tableName);
		//CameraServer.getInstance().startAutomaticCapture(new USBCamera("cam0"));
		//CameraServer.getInstance().startAutomaticCapture(new USBCamera("cam1"));
		
		//camera.setQuality(50);
		//camera.startAutomaticCapture("cam0");		//CameraServer.getInstance().startAutomaticCapture(new USBCamera("cam3"));
		

	}
	
	public double stupidTestPleaseIgnore()
	{
		//System.out.println("Gotten Testval : " + table.getNumber("testValue", 0));
		return table.getNumber("testValue", 0);
	}

}
