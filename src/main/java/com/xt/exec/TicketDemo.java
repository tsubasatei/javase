package com.xt.exec;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Ticket{
    private int number = 100;
    private Lock lock = new ReentrantLock();

    public void sell() {
        lock.lock();
        try {
            while (number > 0) {
                // 暂停一会儿线程
                System.out.println(Thread.currentThread().getName() + "窗口，剩余票数：" + number);
                number--;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
public class TicketDemo {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(() -> {
            // 暂停一会儿线程
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            ticket.sell();
        }, "A").start();
        new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            ticket.sell();
        }, "B").start();
        new Thread(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            ticket.sell();
        }, "C").start();
    }
}
