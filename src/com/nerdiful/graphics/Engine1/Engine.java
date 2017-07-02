package com.nerdiful.graphics.Engine1;

import com.nerdiful.graphics.Engine1.core.*;

public class Engine extends Thread
{
	private boolean isRunning = true;
	private Render render;
	private Update update;
	
	private int fpsTarget = 1;
	private int fpsNum = 10;
	private int[] fps = new int[fpsNum];
	private int fpsCount = 0;
	
	public Engine()
	{
		render = new Render();
		update = new Update();
	}
	
	public void run()
	{
		long startTime;
		long runTime;
		double residualTime = 0;
		
		while(isRunning)
		{
			startTime = System.nanoTime();
			update.update();
			render.render();
			runTime = System.nanoTime() - startTime;
			
			if(runTime < 1e9/fpsTarget)
			{
				Double sleepTime = (1e9/fpsTarget - runTime) / 1e6;
				
				residualTime += sleepTime % 1;
				sleepTime -= sleepTime % 1;
				
				if(residualTime > 1)
				{
					sleepTime++;
					residualTime--;
				}
				
				try
				{
					sleep(sleepTime.longValue());
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					
					System.out.println("\n     Run time: " + runTime / 1e6);
					System.out.println("   Sleep time: " + sleepTime);
					System.out.println("Residual Time: " + residualTime);
					
				}
			}
			
			fps(startTime);
		}
	}
	
	private void fps(long startTime)
	{
		fpsCount %= fpsNum;
		fps[fpsCount] = (int)(System.nanoTime() - startTime);
		
		if(fps[fps.length - 1] != 0)
		{
			int totalTime = 0;
			for(int i=0; i<fps.length; i++)
			{
				totalTime += fps[i];
			}
			System.out.printf("          FPS: %.2f\n", 1/((totalTime/fps.length)/1e9));
		}
		fpsCount++;
	}
	
	public static void main(String[] args)
	{
		Engine engine = new Engine();
		engine.start();
	}
}
