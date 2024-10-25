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
            synchronized (receiver_udp) { // receiver_udp�� ���� ����ȭ : ����ȭ ���� ���� �� tcp �۽Ű� udp ������ ������ �߻�����
                while (!receiver_udp.hasNewMessage()) { // �޽����� ���� ��� ���
                    try {
                        receiver_udp.wait(); // ���
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // ���ο� �޽����� ���ŵ� ���
                System.out.println("UDP broadcast message is sent");
                //Ack�޽����� ����
                tcpConnection.sendAckMessage("Ack message " + receiver_udp.receivedMessageNum + "  ");
                
                System.out.println("(1) UDP Message state: " + receiver_udp.hasNewMessage());
                

                // �÷��� �ʱ�ȭ
                receiver_udp.resetNewMessageFlag();
                System.out.println("(2) UDP Message state: " + receiver_udp.hasNewMessage());
            }
        }
    }
}
