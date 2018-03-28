package model;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;


import lib.Request;
import lib.Response;

public class RequestsProcessor extends Thread{

	private volatile ArrayList<Request> queue;
	private Semaphore sem; 
	public RequestsProcessor()
	{
		sem = new Semaphore(1, true);
		queue = new ArrayList<Request>();
	}
	
	public void enqueueRequest(Request req)
	{
		this.queue.add(req);
	}
	private Request dequeueRequest()
	{
		return this.queue.remove(0);
	}
	
	
	public void run()
	{
		while(true)
		{
			try 
			{
				sem.acquire();
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
			if(queue.size() > 0)
			{
				Request request = dequeueRequest();
				try
				{
					Response response = new Response(request.getUrl(), request.getCookies());
					response.sendResponse(request.getSocket());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			sem.release();
		}
	}
	public Semaphore getSemaphore()
	{
		return this.sem;
	}
}
