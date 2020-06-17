package com.teofilo.jobs_crawler.util;

public class Semaphore {

	private boolean lock = false;

	public synchronized void acquire() throws InterruptedException{
		while (this.lock) {
			System.out.println("=========================> Waiting...: " + Thread.currentThread().getName());
			wait();
			System.out.println("=========================> Notified: ");
		}
		this.lock = true;
	}

	public synchronized void release()  {
		System.out.println("=========================> Release: " + Thread.currentThread().getName());
		this.lock = false;
		this.notifyAll();
	}

}
