public class StartTCPCheckThread implements Runnable {
    private final Server_Tcp serverTcp;
    private final TcpConnectionAccepter.ClientHandler handler;
    
    volatile boolean runningFlag = true;
    
    public StartTCPCheckThread(Server_Tcp serverTCP, TcpConnectionAccepter.ClientHandler handler) {
        this.serverTcp = serverTCP;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (runningFlag) {
            synchronized (serverTcp) {
                while (!serverTcp.hasNewEchoMessage()) {
                    try {
                        System.out.println("StartTCPCheckThread���� wait() ��...");
                        serverTcp.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("TCP Ack message is got");
                
                //�ε��� ��ȣ�� �ش��ϴ� clientŬ���� �迭 ȣ��
                ClientInfo clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
                clientinfo.setNewMsg(true);
                //Ŭ���̾�Ʈ�κ��� ���� ����Ʈ �迭 ���
                System.out.println(Server_Tcp.checkNewMessage);
            	if(clientinfo.getNewMsg() == true)
                	System.out.println("Client Num: "+handler.permanent_id+" Changed index value TRUE");
                

                // �÷��� �ʱ�ȭ
                serverTcp.resetNewEchoMessageFlag();
            }

            }
        }
    
    public void stopThread() {
    	//�ش� �ε����� true�� �����Ͽ� ������� ���α׷��� �۵��ϵ�����
    	
    	 //�ε��� ��ȣ�� �ش��ϴ� clientŬ���� �迭 ȣ�� �� ������� false, ���ο� �޽��� true ����
        ClientInfo clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
        System.out.println("Client : "+clientinfo.getIp()+" is disconnected");
        clientinfo.setNewMsg(true);
    	clientinfo.setConnected(false);
    	runningFlag = false;    	
    }
    
}
