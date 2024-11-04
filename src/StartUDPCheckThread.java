public class StartUDPCheckThread implements Runnable {
    private final ReceiverViewModelUdp receiver_udp;
    private final TcpSocketConnection tcpConnection;
    

    public StartUDPCheckThread(ReceiverViewModelUdp receiver_udp, TcpSocketConnection tcpConnection) {
        this.receiver_udp = receiver_udp;
        this.tcpConnection = tcpConnection;
        
    }

    @Override
    public void run() {
        while (true) {
            
        	try {

                // ���ο� �޽����� ���ŵ� ���
                System.out.println("StartUDPCheckThread is awake");
                
                //byte�迭�� Ack�޽����� �����Եȴ�. sendAckMessage�� �����ǵǾ�����
                tcpConnection.sendAckMessage(ReceiverViewModelUdp.checkNewMessage);
                
                printByteArrayAsBinary(ReceiverViewModelUdp.checkNewMessage);
                
                System.out.println( " is transmitted");
                System.out.println("(1) UDP Message state: " + receiver_udp.hasNewMessage());

                // �÷��� �ʱ�ȭ
                receiver_udp.resetNewMessageFlag();
                System.out.println("(2) UDP Message state: " + receiver_udp.hasNewMessage());
                
                
                // ������ �ð� ���� ���
                long interval = 5000;
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // �����尡 �ߴܵǾ��� �� ���� ó��
                System.out.println("Thread was interrupted");
                break;
            }
        }
    }
    public static void printByteArrayAsBinary(byte[] byteArray) {
        for (byte b : byteArray) {
            // �� ����Ʈ�� 0�� 1�� ��ȯ
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.println(binaryString); // ��ȯ�� ������ ���
        }
    }
}