package org.usfirst.frc.team5113.drive;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;

public class AngleManager
{
	// Accelerometer
	//ADXL345_I2C accel;	//Ari confirmed that we do in fact use I2C
	//ADXL345_I2C.AllAxes accelerations;
	BuiltInAccelerometer accel;

	// Gyro
	Gyro gyro;

	private static AngleManager angleManagerInstance;

	public static AngleManager getInstance()
	{
		return angleManagerInstance;
	}

	public static void setup()
	{
		angleManagerInstance = new AngleManager();
		angleManagerInstance.init();
	}
	
	
	//The ADXL345 starts in a power saving mode. You must turn it on by writing 0x08 to POWER_CTL (0x2D) before it will do anything interesting.
	public void init()
	{
		//accel = new ADXL345_I2C(Port.kMXP, Range.k4G);
		accel = new BuiltInAccelerometer();
		System.out.println("Test for toString of Accelerometer: " + accel.toString());
		System.out.println("Test for toString(NO toString) of Accelerometer: " + accel);
		
		gyro = new Gyro(1);
		gyro.initGyro();
		System.out.println("Gyro is now inited\t" + gyro.getAngle());
		
		
	}

	public void update()
	{
		gyro.updateTable();
		accel.updateTable();
	}
	
	//double[] temp = accelVals();
    //return (.98 * gyroVals()) + (.2 * temp[0]);
	//pitch = atan2(accYval, accZval)+PI
	//roll = atan2(accXval, accZval)+PI
			
	public double roll()//X vals and Z vals
	{
		double[] temp = accelVals();
		return Math.atan2(temp[1], temp[2]) + Math.PI;
	}
	
	public double pitch()//Y vals and Z vals
	{
		double[] temp = accelVals();
		return Math.atan2(temp[0], temp[2]);
	}

	/***
	 * @return double[] {x, y, z}
	 */
	public double[] accelVals()
	{
		double[] vals = new double[3];
		
		if(accel != null)
		{
//			vals[0] = accel.getAcceleration(ADXL345_I2C.Axes.kX);
//			vals[1] = accel.getAcceleration(ADXL345_I2C.Axes.kY);
//			vals[2] = accel.getAcceleration(ADXL345_I2C.Axes.kZ);
			
			vals[0] = accel.getX();
			vals[1] = accel.getY();
			vals[2] = accel.getZ();
			
			//if(accel.getX() != 0  || accel.getY() != 0 || accel.getZ() != 0)
				//System.out.println("Accelerometer vals: " + vals[0] + "\t" + vals[1] + "\t" + vals[2]);
	
		
			return vals;
		}
		else
		{
			System.out.println("The Accelerometer was null! yay!");
			return vals;
		}
	}
	
	public double gyroVals()
	{
		if(gyro != null)
			return gyro.getAngle();
		else
			return -5000;
	}
	
	public double currAngle()
	{
		return gyroVals();
	}
}