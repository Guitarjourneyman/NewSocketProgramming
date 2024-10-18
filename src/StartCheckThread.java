public class StartCheckThread implements Runnable {
    private final ReceiverViewModelUdp receiver_udp;
    private final TcpSocketConnection tcpConnection;
    private final Server_Tcp receiver_tcp;
    /*
     * UDP �޽����� �������� �� TCP�� ���ڸ޽����� �۽��ϴ� ���� 
     * 
     * */
    
    public StartCheckThread(ReceiverViewModelUdp receiver_udp,Server_Tcp receiver_tcp,TcpSocketConnection tcpConnection) {
        this.receiver_udp = receiver_udp;
        this.tcpConnection = tcpConnection;
        this.receiver_tcp = receiver_tcp;
    }

    @Override
    public void run() {
        while (true) {
            if (receiver_udp.hasNewMessage()) { //UDP Broad ���Ű˻� 
                
            System.out.println("UDP broadcast message is sent");
            
            tcpConnection.sendEchoMessage("Echo message "+receiver_udp.receivedMessageNum+"  "  );
            
            // �÷��� �ʱ�ȭ
            receiver_udp.resetNewMessageFlag();    
            }/*
            else if(receiver_tcp.hasNewEchoMessage()) { //���ڸ޽����� �˻� 
            	
            	System.out.println("TCP Echo message is got");
            	NewSocket.clients_tcp.set(tcpConnection.permanent_id,true);
            	if(NewSocket.clients_tcp.get(tcpConnection.permanent_id) == true)
                	System.out.println("Client Num: "+tcpConnection.permanent_id+" Changed index value TRUE");
            	receiver_tcp.resetNewEchoMessageFlag();
            }*/
            	
            	
            /*
            try {
                Thread.sleep(1); // 1ms���� ���� ���θ� Ȯ�� 
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }*/
        }
    }
}
