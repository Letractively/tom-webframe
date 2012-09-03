package webFrame.util;

import java.util.HashMap;
import java.util.Map;

public class TestC extends Thread {
	public static Map<Thread, Object> threadMap = new HashMap<Thread, Object>();

	private TcpC tcpC;
	public String str;

	public TestC(TcpC tcpC, String str) {
		this.str = str;
		this.tcpC = tcpC;
	}

	@Override
	public void run() {
		tcpC.send("hello-->"+ Thread.currentThread());
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
	
	public static TcpC startservice(){
		TcpC tcpC = null;
		if(threadMap.containsKey(Thread.currentThread())){
			tcpC = (TcpC) threadMap.get(Thread.currentThread());
		} else{
			tcpC=  new TcpC("127.0.0.1", 8000);
			threadMap.put(Thread.currentThread(), tcpC);
			new TestC(tcpC,"客户端一").start();
		}
		return tcpC;
	}
	
	public static void change(user u){
		u.setName("潘明光");
		u.setSex("男");
		
	}

	public static void main(String[] args) {
		user u = new user();
		change(u);
		System.out.println(u);
		
		
//		TcpC tcpc = startservice();
//		startservice();
//		tcpc.send("ni hao-->"+Thread.currentThread());
//		tcpc.send("ni hao-->"+Thread.currentThread());

	
//		new TestC(new TcpC("127.0.0.1", 8000),"客户端二").start();
//		new TestC(new TcpC("127.0.0.1", 8000),"客户端三").start();
	}
}
class user {
	private String name;
	private String sex;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSex() {
		return sex;
	}
	@Override
	public String toString() {
		return "user [name=" + name + ", sex=" + sex + "]";
	}
	
}
