import java.util.Arrays;
import javax.swing.*;
import java.awt.*;

public class StartTCPCheck  {
    private final Server_Tcp serverTcp;
    private final TcpConnectionAccepter.ClientHandler handler;
    
    volatile boolean runningFlag = true;
    
    public StartTCPCheck(Server_Tcp serverTCP, TcpConnectionAccepter.ClientHandler handler) {
        this.serverTcp = serverTCP;
        this.handler = handler;
    }

    public void startChecking() {
        

                
                
                int totalBits = serverTcp.array_index * 8 - serverTcp.ignored_bits; // üũ�� �� ��Ʈ ��
            	int totalBytes = (totalBits + 7) / 8; // �� ����Ʈ ��, 8�� ���� �������� ������ +1

            	// ���ο� �迭�� ���� checkNewMessage�� �� ����Ʈ�� �������� shift
            	byte[] shiftedMessage = new byte[totalBytes];
            	for (int i = 0; i < totalBytes; i++) {
            	    // �ش� ����Ʈ�� �������� ignored_bits ��ŭ ����Ʈ
            	    int index = i < serverTcp.array_index ? i : serverTcp.array_index - 1; // ������ ����Ʈ�� �ε����� ����
            	    // �������� ����Ʈ �� ������ ��Ʈ�� 1�� ä��
            	    shiftedMessage[i] = (byte) ((serverTcp.checkNewMessage[index] << serverTcp.ignored_bits) |
            	        ((1 << serverTcp.ignored_bits) - 1)); // ������ ��Ʈ�� ��� 1�� ä���
            	}

            	// ��� ��Ʈ�� 1���� Ȯ��
            	boolean allBitsOne = true;
            	for (byte b : shiftedMessage) {
            	    if (b != (byte) 0xFF) { // ���� �� ����Ʈ�� 0xFF�� �ƴ϶��
            	        allBitsOne = false;
            	        break;
            	    }
            	}

            	if (allBitsOne) {
            	    // �ε��� ��ȣ�� �ش��ϴ� client Ŭ���� �迭 ȣ��
            	    ClientInfo clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
            	    // ���� true set���� ���Ƿ� false set ����
            	    clientinfo.setNewMsg(true);
            	    System.out.println("Client Num: " + handler.permanent_id + " Changed index value TRUE");
            	}
            	
            	
           
        }
    
    public void stopChecking() {
    	//�ش� �ε����� true�� �����Ͽ� ������� ���α׷��� �۵��ϵ�����
    	
    	 //�ε��� ��ȣ�� �ش��ϴ� clientŬ���� �迭 ȣ�� �� ������� false, ���ο� �޽��� true ����
        ClientInfo clientinfo = TcpConnectionManager.getClient(handler.permanent_id);
        System.out.println("Client : "+clientinfo.getIp()+" is disconnected");
        clientinfo.setNewMsg(true);
    	clientinfo.setConnected(false);
    	    	
    }
    
    
}
