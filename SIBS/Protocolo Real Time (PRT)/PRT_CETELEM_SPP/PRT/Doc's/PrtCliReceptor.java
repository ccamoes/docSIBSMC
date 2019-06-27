package sibs.deswin.prt.clt;

import sibs.deswin.lib.com.PersClientConnection;
import sibs.deswin.lib.com.SocketIO;
import sibs.deswin.lib.util.LogStream;

/**
 * Classe que demonsta a utilização do cliente receptor no PRT.
 * 
 * Apenas faz echo das mensagens que recebe, deve servir de guia para as
 * implementações especificas dos clientes.
 */
public class PrtCliReceptor {
	private static final long waitTime = 0L;

	public static void main(String args[]) {
		PersClientConnection clt = new PersClientConnection();
		try {
			clt.setApplName("sibs.deswin.prt.PrtSrvGateway");
			clt.setHostName(args[0]);
			clt.setHostPort(Integer.parseInt(args[1]));
			clt.setParameter("PRTSES", args[2]);
			clt.setLogStream(new LogStream());
			clt.setDebug(2);
			System.out.println("A Fazer o Call!");

			SocketIO sio = clt.connect((SocketIO) null);
			while (true) {
				echoRequest(sio);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	private static void echoRequest(SocketIO sio) throws Exception {
		String str = sio.readString();
		System.out.println("Pedido:" + str.substring(12));
		/*
		 * Sleep de waitTime ms, apenas para teste de timeout, em produção não
		 * faz sentido
		 */
		if (waitTime > 0L) {
			Thread.sleep(waitTime);
		}
		sio.write(str);
		System.out.println("Respos:" + str.substring(12));
	}
}
