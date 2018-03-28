package model;

import java.util.ArrayList;


import lib.Request;
import lib.Response;

public class RequestsProcessor extends Thread{

	private volatile ArrayList<Request> queue;
	
	public RequestsProcessor()
	{
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
		}
	}
}
