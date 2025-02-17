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
            synchronized (receiver_udp) { // receiver_udp에 대한 동기화 : 동기화 하지 않을 시 tcp 송신과 udp 수신의 딜레이 발생가능
                while (!receiver_udp.hasNewMessage()) { // 메시지가 없을 경우 대기
                    try {
                        receiver_udp.wait(); // 대기
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 새로운 메시지가 수신된 경우
                System.out.println("UDP broadcast message is sent");
                //Ack메시지를 보냄
                tcpConnection.sendAckMessage("Ack message " + receiver_udp.receivedMessageNum + "  ");
                
                System.out.println("(1) UDP Message state: " + receiver_udp.hasNewMessage());
                

                // 플래그 초기화
                receiver_udp.resetNewMessageFlag();
                System.out.println("(2) UDP Message state: " + receiver_udp.hasNewMessage());
            }
        }
    }
}
