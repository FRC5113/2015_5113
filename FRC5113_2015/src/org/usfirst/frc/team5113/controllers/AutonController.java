package org.usfirst.frc.team5113.controllers;

import org.usfirst.frc.team5113.auton.ActionGoal;
import org.usfirst.frc.team5113.auton.GoalNothing;
import org.usfirst.frc.team5113.auton.GoalToteClear;
import org.usfirst.frc.team5113.auton.GoalToteStep;
import org.usfirst.frc.team5113.comms.IRISComms;
import org.usfirst.frc.team5113.drive.AngleManager;
import org.usfirst.frc.team5113.drive.CANManager;

public class AutonController extends DriveController
{
	AngleManager angleMesure = new AngleManager();
	
	private float mag;
	private float dir;
	private float rot;
	private float elev;
	private float elevToPoint;
	private double lastElevatorHeight = 0;
	
	public ActionGoal autonGoal;
	
	//private String choice = "Tote_Clear";
	private String choice = "NONE";

	@Override
	public void init()
	{
		mag = dir = rot = elev = elevToPoint = 0;
		setChoice();
		
		
		switch(choice)
		{
		case "":
			autonGoal = new GoalNothing();
			break;
		case "NONE":
			autonGoal = new GoalNothing();
			break;
		case "TOTE_STEP":
			autonGoal = new GoalToteStep();
			break;
		case "TOTE_CLEAR":
			autonGoal = new GoalToteClear();
			break;
		}
		
		
  		autonGoal.controller = this;		
	}
	
	private void setChoice()
	{
		choice = IRISComms.GetInstance().getMiscData("AutonPreset");
	}

	@Override
	public void update(CANManager dr)
	{		
		double tempDir = 90 - dir;
  		dr.mecanumDrive2(mag, tempDir, rot);
  		
  		if(elev == 0 && elevToPoint != 0)
  		{
  			dr.elevatorMovementLimited((int) elevToPoint);
  			elev = 0;
  		}
  		else
  		{
  			dr.elevatorMovement(elev);
  			elevToPoint = 0; 
  		}
  		
  		lastElevatorHeight = dr.elevatorHeight();
  		
  		AngleManager.getInstance().update();
  		autonGoal.update();
  		
  		System.out.println(choice);
  		
	}
	
	public void mecan(float mag, float dir, float rot)
	{
		this.mag = mag;
		this.dir = dir;
		this.rot = rot;
	}

	public void forward(float speed)
	{
		mag = speed;
		dir = 0;
		rot = 0;
	}
	
	public void left(float speed)
	{
		mag = speed;
		dir = 270;
		rot = 0;
	}
	
	public void right(float speed)
	{
		mag = speed;
		dir = 90;
		rot = 0;
	}
	
	public void back(float speed)
	{
		mag = speed;
		dir = 180;
		rot = 0;
	}
	
	public void rotCCW(float speed)
	{
		mag = 0;
		dir = 0;
		rot = speed;
	}
	
	public void rotCW(float speed)
	{
		mag = 0;
		dir = 0;
		rot = -speed;
	}
	
	public boolean elevToPoint(float point)
	{
		boolean bool = elevatorOnPoint();
		
		if(!bool)
			elevToPoint = point;
		else
			elevToPoint = 0;
		
		return bool;
	}
	
	private boolean elevatorOnPoint()
	{
		float elevatorGoalHeight = elevToPoint;
		if(elevatorGoalHeight > (lastElevatorHeight + 20))
		{
			return false;
		}
		else if(elevatorGoalHeight < (lastElevatorHeight - 20))
		{
			return false;
		}
		return true;
	}
	
	public void elevUp(float speed)
	{
		elev = speed;
	}
	
	public void elevDown(float speed)
	{
		elev = speed;
	}
	
	public double getAngle()
  	{
  		return AngleManager.getInstance().currAngle();
  	}
  	
  	public double roll()
  	{
  		return AngleManager.getInstance().roll();
  	}
  	
  	public double pitch()
  	{
  		return AngleManager.getInstance().pitch();
  	}
  	
  	public double[] getAllAngleInfo()
  	{
  		double[] angles = new double[6];
  		double[] accelValues = angleMesure.accelVals();
  		
  		angles[0] = accelValues[0];
  		angles[1] = accelValues[1];
  		angles[2] = accelValues[2];
  		angles[3] = angleMesure.gyroVals();
  		angles[4] = angleMesure.roll();
  		angles[5] = angleMesure.pitch();
  		
  		return angles;
  	}
	
	public void stop()
	{
		mag = dir = rot = elev = elevToPoint = 0;
	}
	
	public void orbit()
	{
		mag = 0.8f;
		dir = 90;
		rot = 0.2f;
	}
}