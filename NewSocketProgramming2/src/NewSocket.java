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
    private TcpConnectionAccepter tcp_accepter;
    
    private JTextField inputIp;
    private JTextField inputIp_udpBroad;
    private int sentMessageCount = 0;       // ���� �޽��� ī����
    private Timer udpTimer;                 // UDP ������ ���� Ÿ�̸�
    public static ArrayList<Boolean> clients_tcp;   //���ڸ޽����� �޾Ҵ� �� Ȯ���ϴ� �������迭 
    public static ArrayList<Integer> clients_receivedEcho;
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
        sendStopButton_UDP = new JButton("Stop UDP");
        
        // IP �Է� �ʵ�
        inputIp = new JTextField("172.30.1.76", 15);
        inputIp_udpBroad = new JTextField("192.167.11.255",15);//192.168.223.255, 192.168.0.255
        // ��ư�� �ؽ�Ʈ �ʵ带 ���� �г�
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(new JLabel("Client IP:"));
        buttonPanel.add(inputIp);
        buttonPanel.add(new JLabel("Broad IP:"));
        buttonPanel.add(inputIp_udpBroad);
        buttonPanel.add(connection_Button_TCP);
        buttonPanel.add(accept_Button_TCP);
        
        buttonPanel.add(sendButton_UDP);
        buttonPanel.add(receiveButton_UDP);
        buttonPanel.add(sendStopButton_UDP);
        

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
        add(buttonPanel, BorderLayout.SOUTH);        // �ϴܿ� ��ư �г� ��ġ
        
        
        
        // ���� ��ư �̺�Ʈ ó��
        connection_Button_TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp_connection = new TcpSocketConnection();
                String serverIP = inputIp.getText();
                tcp_connection.startClient(serverIP);
                consoleArea.append("Client: "+serverIP+"�� TCP ���ϰ� ����Ǿ����ϴ�. \n");
                
            }
        });
        //�ش� ��ư�� ������ �����ư�� ���� ���Ͽ����� ������ 
     // TCP ���� ��ư �̺�Ʈ ó��
        accept_Button_TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                tcp_accepter = new TcpConnectionAccepter();
                tcp_accepter.startServer(receivedMessagesArea);
                consoleArea.append("TCP ���� ����Ϸ� \n");
                System.out.println("TCP Socket is connnected");
                
                
                
              
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
                            
                            consoleArea.append("��� Ŭ���̾�Ʈ�κ��� ["+sentMessageCount+ "]�� ���� �޽����� �޾����Ƿ� ��ε�ĳ��Ʈ ����\n");
                            // ���� ���� �÷��� �ʱ�ȭ
                            setAllClientsFalse(clients_tcp);
                            
                            sentMessageCount++; // ���� �޽��� ī��Ʈ ����
                            
                            return; // ���� ���� �� ����
                        }
                        if(sentMessageCount == 0 ) sentMessageCount++; 
                        
                        sender_udp.startClient(serverIP,sentMessageCount);   // 50ms���� UDP �޽��� ����
                        
                        // sendMessageArea�� ������ �޽��� �߰�
                        
                        sendMessageArea.append("[" + sentMessageCount + "] UDP�� ���۵� �޽���: 'A' * 30 bytes\n");
                        
                        consoleArea.append("UDP�� �޽����� ���۵Ǿ����ϴ�.\n");
                    }
                }, 0, 1000); // 1000ms �������� ����
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
    public static void setAllClientsFalse(ArrayList<Boolean> booleanList) {
        for (int i = 0; i < booleanList.size(); i++) {
            booleanList.set(i, false);
        }
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
