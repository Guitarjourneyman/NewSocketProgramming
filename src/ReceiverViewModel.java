import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiverViewModel {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private static final int PORT = 1995;
    private volatile boolean newEchoReceived_tcp = false; // ���� �޽��� ���� ����

    public boolean hasNewEchoMessage() {
        return newEchoReceived_tcp;
    }

    public void resetNewEchoMessageFlag() {
        newEchoReceived_tcp = false;
    }

    public void startServer() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection...");
            socket = serverSocket.accept();
            System.out.println("Connected completely.");

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            String clientIP = socket.getInetAddress().getHostAddress();
            String hostIP = InetAddress.getLocalHost().getHostAddress();

            // Ŭ���̾�Ʈ���� ������ �����ϸ鼭 �޽����� ���������� ����
            while (!socket.isClosed()) {
                try {
                    Object receivedObject = in.readObject();
                    if (receivedObject instanceof String) {
                        String receivedMessage = (String) receivedObject;

                        // ���� �ð��� hh:mm:ss.SSS �������� ��������
                        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());

                        System.out.println("���ŵ� �޽��� from " + clientIP + ": " + receivedMessage + " [" + timeStamp + "]");

                        // ���ο� ���� �޽����� ���ŵǾ����� ǥ��
                        newEchoReceived_tcp = true;

                        out.flush(); 
                    }
                } catch (EOFException e) {
                    System.out.println("Client Connection is disconnected.");
                    break; // Ŭ���̾�Ʈ�� ������ �������� �� while ������ Ż��
                } catch (SocketException e) {
                    System.out.println("Connection gets reset: " + e.getMessage());
                    break; // ������ ���µǾ��� �� while ������ Ż��
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break; // ��Ÿ ������ �߻����� �� while ������ Ż��
                }
            }
        } catch (BindException e) {
            System.out.println("The port is already used: " + e.getMessage());
            try {
                Thread.sleep(1000); // 1�� ���
                serverSocket = new ServerSocket(PORT); // �ٽ� �õ�
            } catch (InterruptedException | IOException ie) {
                ie.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
                if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
