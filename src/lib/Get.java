package lib;

import java.net.Socket;
import java.util.ArrayList;

import exceptions.BadRequestException;

public class Get extends Request{

	
	public Get(String request, Socket socket, String server_root) throws BadRequestException {
		super(request, socket, server_root);
		/*parses the get parameters*/
		String[] params_path = this.getUrl().split("\\?");
		if(params_path.length != 2)
		{
			this.setMethod_parameters(null);
		}
		else
		{
			this.setUrl(params_path[0]);
			ArrayList<Parameter> method_parameters = new ArrayList<>();
			String[] params = params_path[1].split("\\&");
			for(int i = 0; i < params.length; i++)
			{
				String[] fields = params[i].split("\\=");
				if(fields.length != 2)
				{
					method_parameters.add(new Parameter(fields[0], ""));
				}
				else
				{
					method_parameters.add(new Parameter(fields[0], fields[1]));
				}

			}
			this.setMethod_parameters(method_parameters);
		}
	}

	public Object CreateResponse()
	{
		return null;
	}
}
