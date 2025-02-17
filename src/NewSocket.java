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
    private int sentMessageCount = 0;       // 메시지의 번호 
    private int sentMessageCount_actual = 0; //실제 전송 메시지 카운
    private int tcpSocketNum =0; //TCP Socket의 번호
    private Timer udpTimer;                 // UDP 전송을 위한 타이머
    public static ArrayList<Boolean> clients_tcp;   //에코메시지를 받았는 지 확인하는 이진수배열 
    public static int clients_tcp_index = 0; // 에코메시지의 배열의 인덱스
    
    
    
    public NewSocket() {
    	//에코메시지 배열 초기화
    	clients_tcp = new ArrayList<>();
    	clients_tcp.add(false);
    	
        // GUI 기본 설정
        setTitle("P2P UCP BroadcastFinal");
        setSize(1300, 600); // 크기를 조금 더 늘려줌
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 수신 메시지 영역 (맨 위에 위치)
        receivedMessagesArea = new JTextArea();
        receivedMessagesArea.setEditable(false);
        receivedMessagesArea.setLineWrap(true);
        receivedMessagesArea.setWrapStyleWord(true);
        JScrollPane receivedScrollPane = new JScrollPane(receivedMessagesArea);
        receivedScrollPane.setBorder(BorderFactory.createTitledBorder("Received Messages"));

        // Clear 버튼 추가 (수신 메시지)
        clearReceiveButton = new JButton("Clear Received Messages");
        clearReceiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receivedMessagesArea.setText(""); // 수신 메시지 창의 텍스트 초기화
                if (receiver_udp != null) {
                    receiver_udp.reset_message_num(); // 수신 메시지 카운터 초기화
                }
            }
        });

        // 전송 메시지 입력 영역 (중간에 위치)
        sendMessageArea = new JTextArea();
        sendMessageArea.setLineWrap(true);
        sendMessageArea.setWrapStyleWord(true);
        JScrollPane sendScrollPane = new JScrollPane(sendMessageArea);
        sendScrollPane.setBorder(BorderFactory.createTitledBorder("Send Message"));

        // Clear 버튼 추가 (전송 메시지)
        clearSendButton = new JButton("Clear Send Messages");
        clearSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageArea.setText(""); // 전송 메시지 창의 텍스트 초기화
                sentMessageCount = 0;        // 전송 메시지 카운트 초기화
                sentMessageCount_actual = 0; // 실제 전송된 메시지 카운트 초기화
            }
        });

        // 콘솔 영역 (맨 아래에 위치)
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));

        // 버튼 생성
        connection_Button_TCP = new JButton("Connection TCP Socket");
        sendButton_UDP = new JButton("Send UDP Message");
        accept_Button_TCP = new JButton("Wait for TCP");
        receiveButton_UDP = new JButton("Wait for UDP");
        sendStopButton_UDP = new JButton("Stop UDP Msg");
        
        
        //첫번째 TCP 소켓의 IP 입력 필드
        inputIp_1 = new JTextField("192.167.11.36", 15);
        inputIp_udpBroad = new JTextField("192.167.11.255",15);//192.168.223.255, 192.168.0.255
        //두번째 TCP 소켓의 IP 입력 필드
        inputIp_2 = new JTextField("192.167.11.22", 15);
        
        
        // 버튼과 텍스트 필드를 담을 패널
        JPanel buttonPanel_main = new JPanel(new FlowLayout());
        JPanel buttonPanel_1 = new JPanel(new FlowLayout());
        JPanel buttonPanel_2 = new JPanel(new FlowLayout());
        JPanel buttonPanel_3 = new JPanel(new FlowLayout());
        JPanel buttonPanel_4 = new JPanel(new FlowLayout());
        JPanel buttonPanel_5 = new JPanel(new FlowLayout());
        
        //새로운 버튼을 위한 패널 설정
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

        // 메인 레이아웃 설정
        setLayout(new BorderLayout());

        // 수신 메시지 + Clear 버튼을 위한 패널
        JPanel receivedPanel = new JPanel(new BorderLayout());
        receivedPanel.add(receivedScrollPane, BorderLayout.CENTER);
        receivedPanel.add(clearReceiveButton, BorderLayout.SOUTH);

        // 전송 메시지 + Clear 버튼을 위한 패널
        JPanel sendPanel = new JPanel(new BorderLayout());
        sendPanel.add(sendScrollPane, BorderLayout.CENTER);
        sendPanel.add(clearSendButton, BorderLayout.SOUTH);

        // Center Panel: 모든 창을 동일한 크기로 설정하기 위한 GridLayout
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));  // 3 rows, 1 column
        centerPanel.add(receivedPanel);  // 수신 메시지 창 + Clear 버튼
        centerPanel.add(sendPanel);      // 전송 메시지 창 + Clear 버튼
        centerPanel.add(consoleScrollPane);   // 콘솔 창

        add(centerPanel, BorderLayout.CENTER);      // 중앙에 3개의 창을 같은 크기로 배치
        add(buttonPanel_main, BorderLayout.SOUTH);        // 하단에 버튼 패널 배치
        
        
        
        // 연결 버튼 이벤트 처리
        connection_Button_TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tcp_connection = new TcpSocketConnection();
                String serverIP = inputIp_1.getText();
                tcp_connection.startClient(serverIP);
                if(tcpSocketNum != 0 ) {
                	consoleArea.append("Connection버튼 \n");
                	consoleArea.append("["+tcpSocketNum+"]Client: "+serverIP+"가 TCP 소켓과 연결되었습니다. \n");
                	}
                else if(tcpSocketNum == 0 ) {
                	consoleArea.append("Connection버튼 \n");
                	consoleArea.append("["+tcpSocketNum+"]Client: "+serverIP+"가 TCP 소켓과 연결되었습니다. \n");
                	tcpSocketNum++;
                	}
                
                
                
            }
        });
        //해당 버튼을 눌러야 연결버튼을 통한 소켓연결이 가능함 
     // TCP 수신 버튼 이벤트 처리
        accept_Button_TCP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                TcpConnectionAccepter tcp_accepter = new TcpConnectionAccepter();
                tcp_accepter.startServer(receivedMessagesArea,consoleArea);
                if(tcpSocketNum != 0 ) {
                	consoleArea.append("Wait for TCP버튼 \n");
                	consoleArea.append("["+tcpSocketNum+"] TCP 소켓과 연결되었습니다. \n");
                	}
                else if(tcpSocketNum == 0 ) {
                	consoleArea.append("Wait for TCP버튼 \n");
                	consoleArea.append("["+tcpSocketNum+"] TCP 소켓과 연결되었습니다. \n");
                	tcpSocketNum++;
                	}
                
            }
        });

        sendButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//receiver_tcp = tcp_connection.receiverViewModel_tcp(); //ReceiverViewModel의 인스턴스를 받아옴
                if (udpTimer != null) {
                    udpTimer.cancel();  // 타이머 중지 (이전에 동작 중이었다면)
                }
                
                sender_udp = new SenderViewModelUdp();
                String serverIP = inputIp_udpBroad.getText();

                udpTimer = new Timer();
                udpTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                    	// 주기적으로 클라이언트 응답 체크
                        if (checkAllClientsTrue(clients_tcp)) {
                            
                            consoleArea.append("모든 클라이언트로부터 "+"[" + sentMessageCount + "]의 에코 메시지를 받았으므로 브로드캐스트 중지\n");
                            sentMessageCount++; // 전송 메시지 카운트 증가
                            
                            clients_tcp.replaceAll(element -> false); //에코메시지 수신여부 초기화 

                            return; // 전송 중지 후 종료
                        }
                       if (sentMessageCount == 0) sentMessageCount++; // 첫 메시지 발송때만 카운트 증가 
                       
                       
                        sender_udp.startSend(serverIP,sentMessageCount);   // 50ms마다 UDP 메시지 전송
                        
                        // sendMessageArea에 보내는 메시지 추가
                        sentMessageCount_actual++;
                        sendMessageArea.append("[" + sentMessageCount_actual +"][" +sentMessageCount + "] UDP로 전송된 메시지: 'A' * 1400 bytes\n");
                        
                        consoleArea.append("UDP로 메시지가 전송되었습니다.\n");
                    }
                }, 0, 50); // 50ms 간격으로 실행
            }
        });
        // UDP 전송 중지 버튼
        sendStopButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (udpTimer != null) {
                    udpTimer.cancel();  // 타이머 중지
                    udpTimer = null;    // 타이머 객체를 null로 설정하여 상태 초기화
                    consoleArea.append("UDP 메시지 전송이 중지되었습니다.\n");
                }
            }
        });
        receiveButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiver_udp = new ReceiverViewModelUdp(receivedMessagesArea);  // receivedMessagesArea 전달
                new Thread(() -> receiver_udp.startServer()).start();
                consoleArea.append("UDP 수신 대기 중...\n");
                
                //UDP Broad메시지를 수신하였지 체크하는 스레드 생성 
                StartUDPCheckThread udpCheckThread = new StartUDPCheckThread(receiver_udp,tcp_connection);
                Thread udpCheck = new Thread(udpCheckThread);
                udpCheck.start();
            }
        });
    }
    
 // 클라이언트 추가 메서드
    public static void addClient() {
        clients_tcp.add(false);  // 새로운 클라이언트를 추가 (기본값 false)
    }
    
    public static boolean checkAllClientsTrue(ArrayList<Boolean> booleanlist) {
    	for (Boolean value: booleanlist) {
    		if(!value) return false; //하나라도 false가 있으면 false 변
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
