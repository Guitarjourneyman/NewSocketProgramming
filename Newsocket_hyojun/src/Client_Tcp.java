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
        try {
            // PrintWriter �ʱ�ȭ
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TCP �޽����� �����ϴ� �޼���
    public void sendMessage_tcp(String message) {
        try {
            // ���� �ð��� hh:mm:ss.SSS �������� ��������
            String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());

            // �޽��� ���� + Ÿ�ӽ����� �߰�
            out.println(message + " From Window " + "[" + timeStamp + "]");
            System.out.println(message + " Ack message gets sent");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ���� ���� �޼���
    public void closeSocket() {
        try {
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("TCP socket is closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
