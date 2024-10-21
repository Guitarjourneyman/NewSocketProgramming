public class StartTCPCheckThread implements Runnable {
    private final Server_Tcp receiver_tcp;
    private final TcpConnectionAccepter.ClientHandler tcpHandler;
    /*
     * UDP �޽����� �������� �� TCP�� ���ڸ޽����� �۽��ϴ� ���� 
     * 
     * */
    
    public StartTCPCheckThread(Server_Tcp server_tcp,TcpConnectionAccepter.ClientHandler tcpHandler) {
        this.receiver_tcp = server_tcp;
        this.tcpHandler = tcpHandler;
    }

    @Override
    public void run() {
        while (true) {
            synchronized(receiver_tcp) {
            	while(!receiver_tcp.hasNewEchoMessage()) {
            		try {
            			System.out.println("Waiting in StartTCPCheckThread...");
            			receiver_tcp.wait();						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	System.out.println("TCP Echo message is got");
            	
            	NewSocket.clients_tcp.set(tcpHandler.permanentId,true);
            	if(NewSocket.clients_tcp.get(tcpHandler.permanentId) == true)
                	System.out.println("Client Num: "+tcpHandler.permanentId+" Changed index value TRUE");
                	
            	//�÷��� �ʱ�
            	receiver_tcp.resetNewEchoMessageFlag();
            }
            
            	
            	
        }
    }
}
