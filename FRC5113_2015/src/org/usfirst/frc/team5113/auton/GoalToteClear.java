package org.usfirst.frc.team5113.auton;

import org.usfirst.frc.team5113.auton.GoalToteStep.State;
import org.usfirst.frc.team5113.comms.IRISComms;

public class GoalToteClear extends ActionGoal
{
	public enum State 
	{
		ROTATEFROMWITHINAUTOZONE, DOWNTOGRABTOTE, TOTEUP, ROTATE, MOVEAWAYFROMTOTE, QUIT, INTOZONE, DROPTOTEINTOAUTOZONE
	}
	
	public State state = State.DOWNTOGRABTOTE;
	
	private boolean pause = false;
	
	private long timer;
	private double startAngle;

	@Override
	public void update()
	{
		
		float[] ytotedat = IRISComms.GetInstance().getToteDataFromString();
		
		float[] center = new float[] {
				ytotedat[0] + (ytotedat[2] / 2f),
				ytotedat[1] + (ytotedat[3] / 2f)};
				
		float[] centerImage = new float[] {320, 240};
		//SIZE_640x480
		 
		 
		
		//System.out.println("TEST: " + center[0] + ", " + center[1] + ", ");
		
		switch(state)
		{
		case DOWNTOGRABTOTE:
			
			//System.out.println("TOTE DOWN");
			
			if(controller.elevToPoint(75) && !pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}
			
			if(pause && (System.currentTimeMillis() - timer > 500))
			{
				pause = false;
				state = State.TOTEUP;				
			}
			
			break;
			
		case TOTEUP:
			//System.out.println("TOTE UP");
			
			if(controller.elevToPoint(500) && !pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}
			
			if(pause && System.currentTimeMillis() - timer > 250)
			{
				pause = false;
				state = State.ROTATE;
			}
			
			 break;
			 
		case ROTATE:
			
			if(!pause)
			{
				startAngle = controller.getAngle();
				pause = true;
			}
			
			if(pause && startAngle - controller.getAngle() >= 90)
			{
				controller.stop();
				pause = false;
				state = State.INTOZONE;
			}
			controller.rotCW(0.3f);
			break;
			
		case INTOZONE:
			
			if(!pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}
			else
			{
				if(System.currentTimeMillis() - timer > 500)
				{
					pause = false;
					state = State.ROTATEFROMWITHINAUTOZONE;
					controller.stop();
				}
				else
				controller.forward(.4f);
			}
			
			break;
			
		case ROTATEFROMWITHINAUTOZONE:
			
			if(!pause)
			{
				startAngle = controller.getAngle();
				pause = true;
			}
			
			if(pause && startAngle - controller.getAngle() >= 180)
			{
				controller.stop();
				pause = false;
				state = State.DROPTOTEINTOAUTOZONE;
			}
			controller.rotCW(0.3f);
			break;
			
		case DROPTOTEINTOAUTOZONE:
			
			//System.out.println("TOTE");
			
			if(controller.elevToPoint(65) && !pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}			
			else if(pause && (System.currentTimeMillis() - timer > 250))
			{
				pause = false;
				state = State.MOVEAWAYFROMTOTE;				
			}
			
			break;
			
			
		case MOVEAWAYFROMTOTE:
			
			if(!pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}
			else
			{
				if(System.currentTimeMillis() - timer > 1000)
				{
					pause = false;
					state = State.QUIT;
				}
				controller.back(0.2f);
			}
			
			break;

		
		case QUIT:
			controller.stop();
			break;
			
		}
		
	}
}
