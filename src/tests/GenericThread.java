package tests;

import java.util.concurrent.Callable;

public class GenericThread extends Thread {
	Callable<?> func;
	
	public GenericThread(Callable<?> func)
	{
		this.func = func;
	}
	
	public void run()
	{
		try {
			this.func.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
