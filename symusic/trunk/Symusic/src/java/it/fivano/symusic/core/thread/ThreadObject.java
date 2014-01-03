package it.fivano.symusic.core.thread;



import it.fivano.symusic.MyLogger;
import it.fivano.symusic.SymusicUtility;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadObject {

	private AtomicInteger numFinishThreads;
	private AtomicInteger numActiveThreads;
	private int maxThreads;
	protected Object monitor;
	protected Long idUser;
	
	protected static final long THREAD_TIMEOUT = 60000;
	
	public ThreadObject(int maxThreads, Object monitor) {
		this.maxThreads = maxThreads;
		this.monitor = monitor;
		this.numActiveThreads = new AtomicInteger();
		this.numFinishThreads = new AtomicInteger();
	}
	
	public void increment() {
		synchronized (monitor) {
			numActiveThreads.incrementAndGet();
		}
	}
	
	public void decrement() {
		
		synchronized (monitor) {
			numActiveThreads.decrementAndGet();
			numFinishThreads.incrementAndGet();
			
			monitor.notifyAll();
		}
	}
	

	public Object getMonitor() {
		return monitor;
	}
	
	public int getNumFinishThreads() {
		synchronized (monitor) {
			return numFinishThreads.get();
		}
	}
	
	private int getNumActiveThread() {
		synchronized (monitor) {
			return numActiveThreads.get();
		}
	}

	public void canThreadStart(MyLogger logger, Thread t) {
		// SE IL NUMERO DI THREAD ATTIVI SUPERA IL MASSIMO CONSENTITO
		// ALLORA IL THREAD CORRENTE RIMANE IN ATTESA PER QUALCHE MS
		try {
			long initTimeSleep = System.currentTimeMillis();
			int cSleep = 1;
//			int ticket = 0;
			logger.debug("[ACTIVE_THREAD="+this.getNumActiveThread()+" - FINISH_THREAD="+numFinishThreads);
			while(this.getNumActiveThread() >= maxThreads) {
				if(cSleep==1) {
					// QUESTO LOG VIENE STAMPATO IN REAL TIME, SENZA SYNC
					logger.debug("THREAD '"+t.getName()+"' IN ATTESA - Ci sono "+this.getNumActiveThread()+" thread attivi su un massimo di "+maxThreads);
//					ticket = getWaitTicket();
				}
				cSleep++;
				SymusicUtility.sleepRandom(100); // 100ms + delta (0-300ms)
				if(System.currentTimeMillis()-initTimeSleep>THREAD_TIMEOUT){
					logger.warn("Il THREAD '"+t.getName()+"' e' rimasto in attesa per troppo tempo... ora viene sbloccato!");
					break; // TROPPO TEMPO IN ATTESA, LO PROCESSIAMO LO STESSO
				}

			}
			this.increment(); // ORA IL THREAD FA PARTE DI QUELLI ATTIVI
			
		} catch (Exception e) {
			
		}
	}
	
	/**
	private int getWaitTicket() {
		return waitTicket.incrementAndGet();
	}
	
	private boolean isYourTicket(int ticket) {
		synchronized (monitor) {
			if(ticket <= lastTicket) { // SE HA PERSO IL TURNO, HA LA PRECEDENZA
				System.out.println("TICKET="+ticket+" - LAST="+lastTicket+"   OK");
				return true; // E' IL TUO TURNO
			} else if(ticket >= lastTicket+1 && ticket <= lastTicket+3) { // I SUCCESSIVI 3 TICKET POSSON ANDARE
				lastTicket = ticket;
				System.out.println("TICKET="+ticket+" - LAST="+lastTicket+"   OK");
				return true; // E' IL TUO TURNO
			} else {
				System.out.println("TICKET="+ticket+" - LAST="+lastTicket+"   NOK");
				return false; // ANCORA NO
			}
		}
	}
	*/

}
