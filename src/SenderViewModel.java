import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SenderViewModel {
    
	private Socket socket;
	private PrintWriter out = null;
	
	// Socket�� �Ķ���ͷ� �޴� ������
	public SenderViewModel(Socket socket) {
		this.socket = socket;
	}

	// TCP �޽����� �����ϴ� �޼���
	public void sendMessage_tcp() {
		try {
			// PrintWriter�� ����Ͽ� ������ �޽����� ����
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

			// 60KB�� ���ӵ� "A" ���� ����
			StringBuilder messageBuilder = new StringBuilder(61440);
			for (int i = 0; i < 61440; i++) {
				messageBuilder.append('A');
			}

			// ���� �ð��� hh:mm:ss.SSS �������� ��������
			String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
			String message = messageBuilder.toString();

			// �޽��� ���� + Ÿ�ӽ����� �߰�
			out.println("From Window " + "[" + timeStamp + "]" + message);
			System.out.println("60KB�� ���ӵ� 'A' �޽����� ������ �����߽��ϴ�.");

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
