package model;

import java.util.ArrayList;

import lib.Request;

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
	private Request dequeueRequest(Request req)
	{
		return this.queue.remove(0);
	}
	
	
	void run()
	{
		while(true)
		{
			if(queue.size() > 0)
			{
				// TODO: Generate Response
			}
		}
	}
}
