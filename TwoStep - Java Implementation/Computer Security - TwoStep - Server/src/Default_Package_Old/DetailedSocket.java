package Default_Package_Old;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
	
public class DetailedSocket {
		private Socket socket;
		private ObjectOutputStream out = null;
		private ObjectInputStream in = null;
		
		public DetailedSocket(Socket socket) 
		{
			this.socket = socket;
		}
		public void createInputOutput() throws IOException
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
		}
		
		public Object readObject() throws ClassNotFoundException, IOException
		{
			return in.readObject();
		}
		
		public void writeObject(Object obj) throws IOException 
		{
			out.writeObject(obj);
			out.flush();
		}
		
		public boolean isInitialized()
		{
			return ((in != null) && (out != null));
		}
		
		public void close() throws IOException
		{
			out.close();
			in.close();
			socket.close();
		}
		
//		@Override
//		public void finalize() throws IOException
//		{
//			close();
//			out = null;
//			in = null;;
//			socket = null;
//		}

}
