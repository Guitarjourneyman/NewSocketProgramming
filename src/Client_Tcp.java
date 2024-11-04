import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client_Tcp {
    
	private Socket socket;
	private PrintWriter out = null;
	
	// Socket�� �Ķ���ͷ� �޴� ������
	public Client_Tcp(Socket socket) {
		this.socket = socket;
	}

	// TCP �޽����� �����ϴ� �޼���
	public void sendMessage_tcp(String message) {
		try {
			// PrintWriter�� ����Ͽ� ������ �޽����� ����
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			// ���� �޽��� ���� 
			

			// ���� �ð��� hh:mm:ss.SSS �������� ��������
			String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
			

			// �޽��� ���� + Ÿ�ӽ����� �߰�
			out.println("From Window " + "[" + timeStamp + "]" + message);
			System.out.println(message+ "Echo message gets sent");

		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
