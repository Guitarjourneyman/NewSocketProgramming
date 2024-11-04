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
                System.out.println(ReceiverViewModelUdp.checkNewMessage +" is transmitted");
                System.out.println("(1) UDP Message state: " + receiver_udp.hasNewMessage());

                // �÷��� �ʱ�ȭ
                receiver_udp.resetNewMessageFlag();
                System.out.println("(2) UDP Message state: " + receiver_udp.hasNewMessage());
                
                
                // ������ �ð� ���� ���
                long interval = 50;
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // �����尡 �ߴܵǾ��� �� ���� ó��
                System.out.println("Thread was interrupted");
                break;
            }
        }
    }
}