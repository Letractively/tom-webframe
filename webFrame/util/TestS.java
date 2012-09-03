package webFrame.util;

public class TestS extends Thread{

	private TcpC tcpC;
	public String str;

	public TestS(TcpC tcpC,String str) {
		this.str = str;
		this.tcpC = tcpC;
	}

	@Override
	public void run() {
		try{
			while(true){
				System.out.println(tcpC.recv(1024)); ;
			}
		}catch(Exception e){
			e.getStackTrace();
		}finally{
			tcpC.close();
		}
	}
	
	public static void main(String[] args) {
		TcpS tcps = new TcpS(8000);
		boolean flag = true;
		TcpC tcpC  = null;
		while (flag) {
			tcpC = new TcpC(tcps.getClient());
			new TestS(tcpC, "服务端一").start();

			tcpC.send("我很好");
		}
		tcpC.send("我很好");
//		new TestS(new TcpC(tcps.getClient()), "服务端一").start();

//		new TestS(new TcpC(tcps.getClient()),"服务端二").start();
//		new TestS(new TcpC(tcps.getClient()),"服务端三").start();
		
	}
}
