package org;

public class MuseumDemo {
    public static void main(String[] args) {
        museumdoor m1=new museumdoor(497);
        System.out.println("当前游客人数"+m1.getNumber());
        for(int i=1;i<10;i++){
            Thread t=new museumThread(m1);
            t.setName("游客"+i+"号");
            t.start();
        }
    }
}

class museumdoor{
    //博物馆最大人数
   static final int MAXNUMBER=500;
   //博物馆当前人数
   private int  number;
    //door=false表示门正在被使用，door=true表示可以进入
    private boolean door;
    public int getNumber() {
        return number;
    }
   public museumdoor(int number){
       this.number=number;
       door=true;
   }

    /**
     * synchronized线程排队,找一个共享对象的对象锁
     * synchronized出现在实例方法上，锁的是this
     */
   //进门
    public synchronized void indoor(){
        if(number>=MAXNUMBER){
            System.out.println("当前博物馆参观人数已满");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       while(door){
//           Thread.currentThread().interrupt();
           door=false;
           System.out.println(Thread.currentThread().getName()+"正在进门");
           try {
               Thread.sleep(2000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           System.out.println(Thread.currentThread().getName()+"已经进门");
           number++;
       }
       door=true;

   }
   //参观
    public void visit(){
        System.out.println(Thread.currentThread().getName()+"正在参观");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    //出门
    public synchronized void outdoor(){
        notifyAll();
        while(door){
            door=false;
            System.out.println(Thread.currentThread().getName()+"正在出门");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"已经出门");
            number--;
        }
        door=true;
    }



}

class museumThread extends Thread{
    //多个线程间共享一个门的实例变量
    private museumdoor m;
    public museumThread(museumdoor m){
        this.m=m;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m.indoor();
        m.visit();
        m.outdoor();
    }
}
