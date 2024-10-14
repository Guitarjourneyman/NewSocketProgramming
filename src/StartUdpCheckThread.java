public class StartUdpCheckThread implements Runnable {
    private final ReceiverViewModelUdp receiver_udp;
    private final TcpSocketConnection tcpConnection;

    public StartUdpCheckThread(ReceiverViewModelUdp receiver_udp, TcpSocketConnection tcpConnection) {
        this.receiver_udp = receiver_udp;
        this.tcpConnection = tcpConnection;
    }

    @Override
    public void run() {
        while (true) {
            if (receiver_udp.hasNewMessage()) {
                
            System.out.println("UDP broadcast message is sent");
            NewSocket.clients_tcp.set(tcpConnection.perminent_id, true); //�޽����� �޾����� ���ſ��� true�� ����
            if(NewSocket.clients_tcp.get(tcpConnection.perminent_id) == true)
            	System.out.println("Client Num: "+tcpConnection.perminent_id+" Changed index value TRUE");
                // ���� ���� �÷��� �ʱ�ȭ
                receiver_udp.resetNewMessageFlag();
            }

            try {
                Thread.sleep(10); // 10ms���� ���� ���θ� Ȯ��
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
