package sibs.deswin.prt.clt;

import sibs.deswin.lib.com.IPersComConsumer;
import sibs.deswin.lib.com.PersClientConnection;
import sibs.deswin.lib.com.PersConnection;
import sibs.deswin.lib.com.SocketIO;
import sibs.deswin.lib.io.ByteBuffer;
import sibs.deswin.lib.util.LogStream;

public class PrtCliEmissor implements IPersComConsumer {
	private int _nseq = 0;

	private int _tpMsg = 0x01;

	private int _tmOut = 0x02;

	private int _mxPed = 1;

	public void setTpMsg(int tpMsg) {
		_tpMsg = tpMsg;
	}

	public void setMxPed(int mxPed) {
		_mxPed = mxPed;
	}

	public void setTimeOut(int tmOut) {
		_tmOut = tmOut;
	}

	public void dspMessage(SocketIO sio, ByteBuffer bb) {
		try {
			int ofs = bb.getOffset();
			byte[] buf = bb.getBuffer();
			if (0x05 == buf[ofs + 3]) {
				// ERRO VINDO DO PRT
			} else if (0x00 == buf[ofs + 3]) {
				if ((_tpMsg == 0x01) || (_tpMsg == 0x02)) {
					buf[ofs] = 0x00;
					sio.write(buf, ofs, 16);
				}
				formPedido(sio);
			} else {
				formPedido(sio);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showConnectionStatus(PersConnection conn, SocketIO _sio,
			int pevt) {
		if (PCON_CONNECT == pevt) {
			try {
				if (_mxPed > 0) {
					_nseq = 0;
					formPedido(_sio);
				}
			} catch (Exception e) {
			}
		}
	}

	public void formPedido(SocketIO sio) throws Exception {

		int i = ++_nseq;
		if (i > _mxPed) {
			if (_mxPed > 0) {
				sio.close();
			}
			return;
		}

		StringBuffer sbf = new StringBuffer();
		sbf.append((char) 0x01); // D.original
		sbf.append((char) (i / 256)); // Token 1
		sbf.append((char) (i % 256)); // Token 2
		sbf.append((char) _tpMsg); // Tipo Msg
		sbf.append((char) (_tmOut / 256)); // Token 1
		sbf.append((char) (_tmOut % 256)); // Token 1
		sbf.append("SIBSThequickbrownfoxjumpsoverthelazydog1234567890");
		sbf.append(i); // Token 1
		sio.write(sbf.toString());
	}

	public static void main(String args[]) {
		PrtCliEmissor prtemi = new PrtCliEmissor();
		PersClientConnection clt = new PersClientConnection();
		try {
			clt.setApplName(args[0]);
			clt.setHostName(args[1]);
			clt.setHostPort(Integer.parseInt(args[2]));
			clt.setParameter("PRTSES", args[3]);
			if (args.length > 4) {
				prtemi.setMxPed(Integer.parseInt(args[4]));
			}
			clt.setLogStream(new LogStream());
			clt.setDebug(2);
			System.out.println("A Fazer o Call!");
			clt.connect(prtemi, 1000L);
			while (true) {
				Thread.sleep(5000);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
