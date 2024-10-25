import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class NewSocket extends JFrame {

    private JTextArea receivedMessagesArea;
    private JTextArea sendMessageArea;
    private JTextArea consoleArea;
    private JButton connection_Button_TCP;
    
    private JButton sendButton_UDP;
    private JButton sendStopButton_UDP;
    private JButton accept_Button_TCP;
    
    private JButton receiveButton_UDP;
    private JButton clearReceiveButton;
    private JButton clearSendButton;
    
    private ReceiverViewModelUdp receiver_udp;
    private TcpSocketConnection tcp_connection;
    private SenderViewModelUdp sender_udp;
    private JTextField inputIp_1;
    private JTextField inputIp_2;
    private JTextField inputIp_udpBroad;
    private int sentMessageCount = 0;       // �޽����� ��ȣ 
    private int sentMessageCount_actual = 0; //���� ���� �޽��� ī��
    private int tcpSocketNum =0; //TCP Socket�� ��ȣ
    private Timer udpTimer;                 // UDP ������ ���� Ÿ�̸�
    public static ArrayList<Boolean> clients_tcp;   //���ڸ޽����� �޾Ҵ� �� Ȯ���ϴ� �������迭 
    public static int clients_tcp_index = 0; // ���ڸ޽����� �迭�� �ε���
    
    
    
    public NewSocket() {
    	//���ڸ޽��� �迭 �ʱ�ȭ
    	clients_tcp = new ArrayList<>();
    	clients_tcp.add(false);
    	
        // GUI �⺻ ����
        setTitle("P2P UCP Broadcast");
        setSize(1300, 600); // ũ�⸦ ���� �� �÷���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ���� �޽��� ���� (�� ���� ��ġ)
        receivedMessagesArea = new JTextArea();
        receivedMessagesArea.setEditable(false);
        receivedMessagesArea.setLineWrap(true);
        receivedMessagesArea.setWrapStyleWord(true);
        JScrollPane receivedScrollPane = new JScrollPane(receivedMessagesArea);
        receivedScrollPane.setBorder(BorderFactory.createTitledBorder("Received Messages"));

        // Clear ��ư �߰� (���� �޽���)
        clearReceiveButton = new JButton("Clear Received Messages");
        clearReceiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receivedMessagesArea.setText(""); // ���� �޽��� â�� �ؽ�Ʈ �ʱ�ȭ
                if (receiver_udp != null) {
                    receiver_udp.reset_message_num(); // ���� �޽��� ī���� �ʱ�ȭ
                }
            }
        });

        // ���� �޽��� �Է� ���� (�߰��� ��ġ)
        sendMessageArea = new JTextArea();
        sendMessageArea.setLineWrap(true);
        sendMessageArea.setWrapStyleWord(true);
        JScrollPane sendScrollPane = new JScrollPane(sendMessageArea);
        sendScrollPane.setBorder(BorderFactory.createTitledBorder("Send Message"));

        // Clear ��ư �߰� (���� �޽���)
        clearSendButton = new JButton("Clear Send Messages");
        clearSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageArea.setText(""); // ���� �޽��� â�� �ؽ�Ʈ �ʱ�ȭ
                sentMessageCount = 0;        // ���� �޽��� ī���� �ʱ�ȭ
            }
        });

        // �ܼ� ���� (�� �Ʒ��� ��ġ)
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));

        // ��ư ����
        connection_Button_TCP = new JButton("Connection TCP Socket");
        sendButton_UDP = new JButton("Send UDP Message");
        accept_Button_TCP = new JButton("Wait for TCP");
        receiveButton_UDP = new JButton("Wait for UDP");
        sendStopButton_UDP = new JButton("Stop UDP Msg");
        
        
        //ù��° TCP ������ IP �Է� �ʵ�
        inputIp_1 = new JTextField("192.167.11.36", 15);
        inputIp_udpBroad = new JTextField("192.168.223.255",15);//192.168.223.255, 192.168.0.255
        //�ι�° TCP ������ IP �Է� �ʵ�
        inputIp_2 = new JTextField("192.167.11.22", 15);
        
        
        // ��ư�� �ؽ�Ʈ �ʵ带 ���� �г�
        JPanel buttonPanel_main = new JPanel(new FlowLayout());
        JPanel buttonPanel_1 = new JPanel(new FlowLayout());
        JPanel buttonPanel_2 = new JPanel(new FlowLayout());
        JPanel buttonPanel_3 = new JPanel(new FlowLayout());
        JPanel buttonPanel_4 = new JPanel(new FlowLayout());
        JPanel buttonPanel_5 = new JPanel(new FlowLayout());
        
        //���ο� ��ư�� ���� �г� ����
        JPanel buttonSmallPanel = new JPanel(new BorderLayout());
        
        buttonPanel_1.add(new JLabel("Client1 IP:"));
        buttonPanel_1.add(inputIp_1);
        buttonSmallPanel.add(buttonPanel_1,BorderLayout.NORTH);
        
        buttonPanel_2.add(new JLabel("Client2 IP:"));
        buttonPanel_2.add(inputIp_2);
        buttonSmallPanel.add(buttonPanel_2,BorderLayout.SOUTH);
        buttonPanel_main.add(buttonSmallPanel);
        
        buttonPanel_3.add(new JLabel("Broad IP:"));
        buttonPanel_3.add(inputIp_udpBroad);
        buttonPanel_main.add(buttonPanel_3);
        
        
        
        buttonPanel_4.add(connection_Button_TCP);
        buttonPanel_4.add(accept_Button_TCP);
        buttonPanel_main.add(buttonPanel_4);
        
        buttonPanel_5.add(sendButton_UDP);
        buttonPanel_5.add(receiveButton_UDP);
        buttonPanel_main.add(buttonPanel_5);
        
        buttonPanel_main.add(sendStopButton_UDP);

        // ���� ���̾ƿ� ����
        setLayout(new BorderLayout());

        // ���� �޽��� + Clear ��ư�� ���� �г�
        JPanel receivedPanel = new JPanel(new BorderLayout());
        receivedPanel.add(receivedScrollPane, BorderLayout.CENTER);
        receivedPanel.add(clearReceiveButton, BorderLayout.SOUTH);

        // ���� �޽��� + Clear ��ư�� ���� �г�
        JPanel sendPanel = new JPanel(new BorderLayout());
        sendPanel.add(sendScrollPane, BorderLayout.CENTER);
        sendPanel.add(clearSendButton, BorderLayout.SOUTH);

        // Center Panel: ��� â�� ������ ũ��� �����ϱ� ���� GridLayout
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));  // 3 rows, 1 column
        centerPanel.add(receivedPanel);  // ���� �޽��� â + Clear ��ư
        centerPanel.add(sendPanel);      // ���� �޽��� â + Clear ��ư
        centerPanel.add(consoleScrollPane);   // �ܼ� â

        add(centerPanel, BorderLayout.CENTER);      // �߾ӿ� 3���� â�� ���� ũ��� ��ġ
        add(buttonPanel_main, BorderLayout.SOUTH);        // �ϴܿ� ��ư �г� ��ġ
        
        
        
        // ���� ��ư �̺�Ʈ ó��
        connection_Button_TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp_connection = new TcpSocketConnection();
                String serverIP = inputIp_1.getText();
                tcp_connection.startClient(serverIP);
                if(tcpSocketNum != 0 ) consoleArea.append("["+tcpSocketNum+"]Client: "+serverIP+"�� TCP ���ϰ� ����Ǿ����ϴ�. \n");
                else if(tcpSocketNum == 0 ) {
                	consoleArea.append("["+tcpSocketNum+"]Client: "+serverIP+"�� TCP ���ϰ� ����Ǿ����ϴ�. \n");
                	tcpSocketNum++;
                	}
                
                
                
            }
        });
        //�ش� ��ư�� ������ �����ư�� ���� ���Ͽ����� ������ 
     // TCP ���� ��ư �̺�Ʈ ó��
        accept_Button_TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                TcpConnectionAccepter tcp_accepter = new TcpConnectionAccepter();
                tcp_accepter.startServer(receivedMessagesArea,consoleArea);
                if(tcpSocketNum != 0 ) consoleArea.append("["+tcpSocketNum+"] TCP ���ϰ� ����Ǿ����ϴ�. \n");
                else if(tcpSocketNum == 0 ) {
                	consoleArea.append("["+tcpSocketNum+"] TCP ���ϰ� ����Ǿ����ϴ�. \n");
                	tcpSocketNum++;
                	}
                
            }
        });

        sendButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//receiver_tcp = tcp_connection.receiverViewModel_tcp(); //ReceiverViewModel�� �ν��Ͻ��� �޾ƿ�
                if (udpTimer != null) {
                    udpTimer.cancel();  // Ÿ�̸� ���� (������ ���� ���̾��ٸ�)
                }
                
                sender_udp = new SenderViewModelUdp();
                String serverIP = inputIp_udpBroad.getText();

                udpTimer = new Timer();
                udpTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                    	// �ֱ������� Ŭ���̾�Ʈ ���� üũ
                        if (checkAllClientsTrue(clients_tcp)) {
                            
                            consoleArea.append("��� Ŭ���̾�Ʈ�κ��� "+"[" + sentMessageCount + "]�� ���� �޽����� �޾����Ƿ� ��ε�ĳ��Ʈ ����\n");
                            sentMessageCount++; // ���� �޽��� ī��Ʈ ����
                            
                            clients_tcp.replaceAll(element -> false); //���ڸ޽��� ���ſ��� �ʱ�ȭ 

                            return; // ���� ���� �� ����
                        }
                       if (sentMessageCount == 0) sentMessageCount++; // ù �޽��� �߼۶��� ī��Ʈ ���� 
                       
                       
                        sender_udp.startSend(serverIP,sentMessageCount);   // 50ms���� UDP �޽��� ����
                        
                        // sendMessageArea�� ������ �޽��� �߰�
                        sentMessageCount_actual++;
                        sendMessageArea.append("[" + sentMessageCount_actual +"][" +sentMessageCount + "] UDP�� ���۵� �޽���: 'A' * 1400 bytes\n");
                        
                        consoleArea.append("UDP�� �޽����� ���۵Ǿ����ϴ�.\n");
                    }
                }, 0, 50); // 50ms �������� ����
            }
        });
        // UDP ���� ���� ��ư
        sendStopButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (udpTimer != null) {
                    udpTimer.cancel();  // Ÿ�̸� ����
                    udpTimer = null;    // Ÿ�̸� ��ü�� null�� �����Ͽ� ���� �ʱ�ȭ
                    consoleArea.append("UDP �޽��� ������ �����Ǿ����ϴ�.\n");
                }
            }
        });
        receiveButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiver_udp = new ReceiverViewModelUdp(receivedMessagesArea);  // receivedMessagesArea ����
                new Thread(() -> receiver_udp.startServer()).start();
                consoleArea.append("UDP ���� ��� ��...\n");
                
            }
        });
    }
    
 // Ŭ���̾�Ʈ �߰� �޼���
    public static void addClient() {
        clients_tcp.add(false);  // ���ο� Ŭ���̾�Ʈ�� �߰� (�⺻�� false)
    }
    
    public static boolean checkAllClientsTrue(ArrayList<Boolean> booleanlist) {
    	for (Boolean value: booleanlist) {
    		if(!value) return false; //�ϳ��� false�� ������ false ��
    	}
    	return true;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewSocket().setVisible(true);
            }
        });
    }
}
