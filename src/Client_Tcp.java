import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client_Tcp {
    
    private Socket socket;
    private PrintWriter out = null;

    // Socket을 파라미터로 받는 생성자
    public Client_Tcp(Socket socket) {
        this.socket = socket;
        try {
            // PrintWriter 초기화
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TCP 메시지를 전송하는 메서드
    public void sendMessage_tcp(String message) {
        try {
            // 현재 시간을 hh:mm:ss.SSS 형식으로 가져오기
            String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());

            // 메시지 전송 + 타임스탬프 추가
            out.println(message + " From Window " + "[" + timeStamp + "]");
            System.out.println(message + " Echo message gets sent");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 소켓 종료 메서드
    public void closeSocket() {
        try {
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("TCP socket is closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
