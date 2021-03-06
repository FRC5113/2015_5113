package org.usfirst.frc.team5113.auton;

public class GoalToteStep extends ActionGoal
{
	public enum State 
	{
		WAIT, ROTATEFROMWITHINAUTOZONE, DOWNTOGRABTOTE, TOTEUP, ROTATE, MOVEAWAYFROMTOTE, QUIT, OVERSTAIRS, OVERSTAIRS2,  DROPTOTEINTOAUTOZONE
	}
	
	public State state = State.DOWNTOGRABTOTE;
	//public State state = State.ROTATEFROMWITHINAUTOZONE;
	
	private boolean pause = false;
	
	private long timer;
	private double startAngle;

	@Override
	public void update()
	{
		
//		float[] ytotedat = IRISComms.GetInstance().getToteDataFromString();
//		
//		float[] center = new float[] {
//				ytotedat[0] + (ytotedat[2] / 2f),
//				ytotedat[1] + (ytotedat[3] / 2f)};
//				
//		float[] centerImage = new float[] {320, 240};
		//SIZE_640x480
		 
		 
		
		//System.out.println("TEST: " + center[0] + ", " + center[1] + ", ");
		
		switch(state)
		{
		case DOWNTOGRABTOTE:			
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
			
			if(controller.elevToPoint(700) && !pause)
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
			
			if(pause && controller.getAngle() - startAngle >= 75)
			{
				controller.stop();
				pause = false;
				state = State.OVERSTAIRS;
			}
			
			
			//System.err.println("Angle for boolean(1st): " + (controller.getAngle() - startAngle));
			controller.rotCW(0.3f);
			
			break;
			
			case OVERSTAIRS:
			
			if(!pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}
			else
			{
				if(System.currentTimeMillis() - timer > 250)
				{
					pause = false;
					state = State.OVERSTAIRS2;
					controller.stop();
				}
				else
					controller.forward(.5f);
			}
			
			break;
		
		case OVERSTAIRS2:
			
			if(Math.abs(controller.pitch()) < 10)
			{
				controller.stop();
				state = State.ROTATEFROMWITHINAUTOZONE;
			}
			else
				controller.forward(0.9f);
			
			break;
			
		case ROTATEFROMWITHINAUTOZONE:
			
			if(!pause)
			{
				startAngle = controller.getAngle();
				pause = true;
			}
			
			if(pause && controller.getAngle() - startAngle >= 75)
			{
				controller.stop();
				pause = false;
				state = State.WAIT;
			}
			
			
			//System.err.println("Angle for boolean(2nd): " + (controller.getAngle() - startAngle));
			controller.rotCW(0.3f);
			
			break;
			
		case WAIT:
			
			controller.stop();
			
			if(!pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}
			
			if(pause && System.currentTimeMillis() - timer > 1000)
			{
				controller.stop();
				pause = false;
				state = State.DROPTOTEINTOAUTOZONE;
			}
			
		case DROPTOTEINTOAUTOZONE:
			
			//System.out.println("TOTE");
			
			if(controller.elevToPoint(65) && !pause)
			{
				timer = System.currentTimeMillis();
				pause = true;
			}			
			else if(pause && (System.currentTimeMillis() - timer > 500))
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
				if(System.currentTimeMillis() - timer > 3000)
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
