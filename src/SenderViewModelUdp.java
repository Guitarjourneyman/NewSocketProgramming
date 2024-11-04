import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SenderViewModelUdp {
    private static final int PORT = 1996;

    public void startSend(String serverIP, int messageNum) {
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            System.out.println("UDP is connected.");

            //ũ�⸦ �����Ͽ� ���ӵ� "A" ���� ����
            int string_size = 1400; 
            
            StringBuilder messageBuilder = new StringBuilder(string_size);
            for (int i = 0; i < string_size; i++) {
                messageBuilder.append('A');
            }
            String message = String.valueOf(messageNum) + messageBuilder.toString();

            // �޽����� ����Ʈ �迭�� ��ȯ
            byte[] messageBytes = message.getBytes();

            // DatagramPacket ���� (60KB)
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, serverAddress, PORT);

            // ��Ŷ ����
            socket.send(packet);

            System.out.println(string_size+"KB Message is sent in one packet");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
    
    
}
