package model;

import java.io.IOException;
import java.util.ArrayList;

import exceptions.NotFoundException;

import lib.Request;
import lib.Response;

public class RequestsProcessor {

	private ArrayList<Request> queue;
	
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
	
	
	void run() throws IOException, NotFoundException
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
					
				}
			}
		}
	}
}
