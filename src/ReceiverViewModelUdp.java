import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JTextArea;

public class ReceiverViewModelUdp {

    private static final int PORT = 1996;
    private static final int BUFFER_SIZE = 1024;
    private JTextArea receivedMessagesArea;  // GUI�� receive message â
    private static int receive_message_num = 0;
    private volatile boolean newMessageReceived_udp = false;
    public int receivedMessageNum; 
    private static int checkSerial; //���� UDP �޽����� �� ��° �޽������� ����
    
    public static byte[] checkNewMessage;
    
    //���� UDP �޽����� �� ��° �޽������� �����ϰ� �ִ� checkSerial ������ ����ϴ� �޼ҵ�
    public int Print_checkSerial() {
    	return checkSerial;
    }
    // �����ڿ��� JTextArea ���� ����
    public ReceiverViewModelUdp(JTextArea receivedMessagesArea) {
        this.receivedMessagesArea = receivedMessagesArea;
    }
    
    public void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewMessage() {
        return newMessageReceived_udp;
    }
    
    public void resetNewMessageFlag() {
        newMessageReceived_udp = false;
    }
    public static String extractLeadingNumbers(String input) {
        // ���Խ��� ����� ���ڿ��� �պκп��� ���ڸ� ����
        StringBuilder numberStr = new StringBuilder();
        
        // ���ڿ��� �ϳ��� �˻��ؼ� ������ ��츸 numberStr�� �߰�
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                numberStr.append(c);
            } else {
                // ���ڰ� �ƴ� ���ڸ� ������ �ݺ��� �����ϰ� ���� �κи� ��ȯ
                break;
            }
        }
        
        // ���ڰ� ������ ���ڿ� ���·� ��ȯ�ϰ�, ������ null ��ȯ
        return numberStr.length() > 0 ? numberStr.toString() : null;
    }

    public void startServer() {
        DatagramSocket socket = null;
        //���Ƿ� 2����Ʈ�� �迭����
        checkNewMessage = new byte[2];
        
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Server started on port " + PORT + ". Waiting for messages...");

            // ���� ������ �޽��� ��� ����
            while (true) {
                try {
                    // ���� ����
                    byte[] buffer = new byte[BUFFER_SIZE];

                    // ������ ��Ŷ ����
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    // ������ ����
                    socket.receive(receivePacket);

                    // ���ŵ� ������ ó��
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                    // �۽��� IP �ּ� ��������
                    InetAddress senderAddress = receivePacket.getAddress();
                    String senderIP = senderAddress.getHostAddress();

                    // ���� �ð��� hh:mm:ss.SSS �������� ��������
                    String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());

                    // �޽����� �պκ� 10���ڸ� �߶� ǥ��
                    String truncatedMessage = receivedMessage.length() > 10
                            ? receivedMessage.substring(0, 10)
                            : receivedMessage;

                    // ���� �޽��� GUI�� ǥ��
                    receive_message_num++;
                    
                    
                    
                    receivedMessagesArea.append("[" + receive_message_num + "] Received UDP message from " + senderIP + ": " + truncatedMessage + " [" + timeStamp + "]\n");                    
                    System.out.println("I got Message: " + truncatedMessage);

                    // ���ڿ��� ������ ��ȯ
                    String numberStr = extractLeadingNumbers(truncatedMessage);
                    
                    
                    if (numberStr != null) {
                        
                    	int number = Integer.parseInt(numberStr);
                    	//change 1bit by 1bit
                    	toggleNewMsgBit();
                    	
                        System.out.println("Extracted number: " + number);
                        
                        
                       
                        
                        
                    } 
                    else {
                        System.out.println("No leading numbers found.");
                    }

                   

                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in received message: " + e.getMessage());
                } catch (SocketException e) {
                    System.out.println("Socket error occurred: " + e.getMessage());
                    break; // ���Ͽ� ������ ����� ������ Ż���Ͽ� ������ �ߴ��մϴ�.
                } catch (SecurityException e) {
                    System.out.println("Security exception: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal argument: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error while receiving data: " + e.getMessage());
                    e.printStackTrace(); // �߰����� ���� �α׸� ����Ͽ� ������ �� ��Ȯ�� �ľ��� �� �ְ� �մϴ�.
                }
            }
        } catch (SocketException e) {
            System.out.println("Failed to bind UDP socket to port " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Server startup error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("UDP Server socket closed.");
            }
        }
    }
    
    public String startConnect_to_tcp() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("UDP Server started on port " + PORT + ". Waiting for connect to tcp...");
            
            // ���� ����
            byte[] buffer = new byte[BUFFER_SIZE];

            // ������ ��Ŷ ����
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            // ������ ���� (�޽����� �� ������ ���)
            socket.receive(receivePacket);

            // �۽��� IP �ּ� ��������
            InetAddress senderAddress = receivePacket.getAddress();
            String senderIP = senderAddress.getHostAddress();

            // IP �ּҰ� ��ȿ�ϸ� ��ȯ, ��ȿ���� ������ null ��ȯ
            if (senderIP != null && !senderIP.isEmpty()) {
                System.out.println("This is serverIP: " + senderIP);
                return senderIP;
            } else {
                System.out.println("No server IP");
                return null;
            }
        } catch (SocketException e) {
            System.out.println("Failed to bind UDP socket to port " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Server startup error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // �޽����� ������ �� ������ �ݾ� �� �̻� �޽����� ���� �ʵ��� ��.
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("UDP Server socket closed.");
            }
        }
        return null; // �޽����� �������� ���߰ų� ���� �߻� �� null ��ȯ
    }
    
 // checkNewMessage �迭�� ��Ʈ�� ���������� 0���� 1�� �ٲٴ� �޼ҵ�
    public void toggleNewMsgBit() {
        boolean allBitsOne = true; // ��� ��Ʈ�� 1���� Ȯ���ϴ� �÷���

        // ��� ��Ʈ�� 1���� �˻�
        for (byte b : checkNewMessage) {
            if (b != (byte) 0xFF) { // ���� �� ����Ʈ�� 0xFF(11111111)�� �ƴ϶��
                allBitsOne = false;
                break;
            }
        }

        if (allBitsOne) {
            // ��� ��Ʈ�� 1�� ��� ��� ��Ʈ�� 0���� �ʱ�ȭ
            Arrays.fill(checkNewMessage, (byte) 0);
        } else {
            // ��� ��Ʈ�� 1�� �ƴ϶�� ù ��° 0�� ��Ʈ�� 1�� ����
            for (int i = 0; i < checkNewMessage.length; i++) {
                for (int bit = 0; bit < 8; bit++) {
                    // �� ����Ʈ�� Ư�� ��Ʈ�� 0���� Ȯ��
                    if ((checkNewMessage[i] & (1 << bit)) == 0) {
                    	checkNewMessage[i] |= (1 << bit); // �ش� ��Ʈ�� 1�� ����
                    	System.out.println("["+i+"]byte Array [" +bit +"] bit changes its value 1");
                        return; // ��Ʈ�� �ϳ��� 1�� �ٲٰ� �޼ҵ� ����
                    }
                }
            }
        }
    }


}
