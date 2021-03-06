package org.usfirst.frc.team5113.robot;

import org.usfirst.frc.team5113.comms.IRISComms;
import org.usfirst.frc.team5113.controllers.AutonController;
import org.usfirst.frc.team5113.controllers.DriveController;
import org.usfirst.frc.team5113.controllers.JoystickController;
import org.usfirst.frc.team5113.drive.AngleManager;
import org.usfirst.frc.team5113.drive.CANManager;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{

	private CANManager motorManagers;// this gives us access to the Drive class
	private DriveController controller;
	private AutonController autonControll;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit()
	{
		IRISComms.init();	//init() automatically creates and runs thread, so no need to call update method here
		controller = new JoystickController();
		controller.init();
		motorManagers = new CANManager();
		motorManagers.init();
		AngleManager.setup();
	}
	
	public void disabledPeriodic()
	{
	
	}
	
	public void autonomousInit()
	{
		autonControll = new AutonController();
		autonControll.init();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic()
	{
			autonControll.update(motorManagers);
			AngleManager.getInstance().update();
			
//			System.out.println();
//			System.err.println();
//			System.err.println("Angle?: " + AngleManager.getInstance().currAngle());
//			System.err.println("Gyro?: " + AngleManager.getInstance().gyroVals());
//			System.err.println("Roll?: " + AngleManager.getInstance().roll());
			//System.err.println("Pitch?: " + AngleManager.getInstance().pitch());
	}
	

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic()
	{
			controller.update(motorManagers);
			//AngleManager.getInstance().update();
			//System.err.println("The string potentiometer height is: " + motorManagers.elevatorHeight());//1427 for highest to lift tote above other tote
		
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic()
	{

	}

}
