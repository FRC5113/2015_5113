package org.usfirst.frc.team5113.drive;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

/**
 * @author Lemons
 * This class is responsible for managing the movement and CAN controls of the elevator and drive train.
 */
public class MotorManager
{
	//Drive train motors as CAN devices
	private CANTalon fl;
	private CANTalon fr;
	private CANTalon bl;
	private CANTalon br;
	
	//Elevator CAN
	private CANTalon elevator;
	
	private int elevatorGoalHeight = 1500;
	private long lastTick = System.currentTimeMillis();
	
	//Elevator sensors
	DigitalInput limitHigh;
	DigitalInput limitLow;
	AnalogInput stringPot;
	
	//Time to stop motors with if no signal found
	private float safetyExpiration = 0.1f;
	
	//PID Controls
	double p = 0.1;
	double i = 0.001;
	double d = 1;
	double f = 0.0001;
	int izone = 100;
	double ramprate = 36;
	int profile = 0;
	
	final double TOL = 0.05;
	
	
	public void init()
	{
		//Initialize and set to CAN IDs.
		//We can remap the IDs from within the web browser at roboRIO-5113.local
		
		fl = new CANTalon (2);
		//fl.changeControlMode(CANTalon.ControlMode.PercentVbus);
		//fl.setSafetyEnabled(true);
		//fl.setExpiration(safetyExpiration);
		//fl.setPID(p, i, d, f, izone, ramprate, profile);
		fl.set(0);
		
		fr = new CANTalon (5);
		//fr.changeControlMode(CANTalon.ControlMode.PercentVbus);
		//fr.setSafetyEnabled(true);
		//fr.setExpiration(safetyExpiration);
		//fr.setPID(p, i, d, f, izone, ramprate, profile);
		fr.set(0);
		
		bl = new CANTalon (1);
		//bl.changeControlMode(CANTalon.ControlMode.PercentVbus);
		//bl.setSafetyEnabled(true);
		//bl.setExpiration(safetyExpiration);
		//bl.setPID(p, i, d, f, izone, ramprate, profile);
		bl.set(0);
		
		br = new CANTalon (3);
		//br.changeControlMode(CANTalon.ControlMode.PercentVbus);
		//br.setSafetyEnabled(true);
		//br.setExpiration(safetyExpiration);
		//br.setPID(p, i, d, f, izone, ramprate, profile);
		br.set(0);
		
		
		elevator = new CANTalon(4);
		//elevator.changeControlMode(CANTalon.ControlMode.Speed);
		//elevator.setSafetyEnabled(true);
		//elevator.setExpiration(safetyExpiration);
		//elevator.setPID(p, i, d, f, izone, ramprate, profile);
		elevator.set(0);
		
		limitHigh = new DigitalInput(9);
		limitLow = new DigitalInput(8);
		stringPot = new AnalogInput(0);
		
		elevatorGoalHeight = 1500;
		
		//startEncoder();
	}
	
	public double elevatorHeight()
	{
		return stringPot.getValue();
	}
	
	public void elevatorMovement(double speed)
	{	
		if(speed > 0 && !limitLow.get())
		{
			speed = 0;
		}
		if(speed < 0 && !limitHigh.get())
		{
			speed = 0;
		}
		elevator.set(speed);
	}
	
	public void elevatorMovementLimited(int goal)
	{		
		System.out.println("L: " + limitLow.get() + ", H: " + limitHigh.get() + ", S: " + stringPot.getValue() + "G: " + elevatorGoalHeight);
		
		elevatorGoalHeight = goal;
		
		float speedOut = 0;
		
		////
		if(elevatorGoalHeight > (elevatorHeight() + 250))
		{
			speedOut = -0.45f;
		}
		else if(elevatorGoalHeight > (elevatorHeight() + 125))
		{
			speedOut = -0.3f;
		}
		else if(elevatorGoalHeight > (elevatorHeight() + 20))
		{
			speedOut = -0.15f;
		}
		////
		if(elevatorGoalHeight < (elevatorHeight() - 250))
		{
			speedOut = 0.45f;
		}
		else if(elevatorGoalHeight < (elevatorHeight() - 125))
		{
			speedOut = 0.3f;
		}
		else if(elevatorGoalHeight < (elevatorHeight() - 20))
		{
			speedOut = 0.15f;
		}

		lastTick = System.currentTimeMillis();
		
		System.out.println(speedOut);
		
		elevator.set(speedOut);
		
	}

	//Controls the drive train
	public void mecanumDrive(double magnitude, double angle, double rotation)
	{			
		//Makes sure that magnitude fits into the range [0, 0.99] as expected. Hardware errors can otherwise cause small movement changes.
		magnitude = Math.min(Math.abs(magnitude), 0.99);

		//As the mecanum drive is X-Shaped, we must adjust to be at 45* angles.
		float newDirection = (float) (angle + 45);
		newDirection = (float) (newDirection * Math.PI) / 180f;
		float cosine, sine;
		cosine = (float) Math.cos(newDirection);
		sine = (float) Math.sin(newDirection);
		
		float frontLeftPower = (float) -(sine * magnitude + rotation);//+
		float frontRightPower = (float) (cosine * magnitude - rotation);//-
		float backLeftPower = (float) -(cosine * magnitude + rotation);//+
		float backRightPower = (float) (sine * magnitude - rotation);//-

		
		bl.set(backLeftPower);
		br.set(backRightPower);
		fl.set(frontLeftPower);
		fr.set(frontRightPower);
	}
	

}