import java.util.Iterator;
import java.util.LinkedList;

public class Clock {
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */
    LinkedList<ClockTickListener> listeners;
    int timeOut;
    Clock(int maxSeconds){
        this.listeners=new LinkedList<ClockTickListener>();
        timeOut=maxSeconds;
    }

    public void addListener(ClockTickListener cl){
        listeners.add(cl);
    }

    public void fire(){
        Iterator it=listeners.iterator();
        while(it.hasNext()){
            try {
                ClockTickListener cl = (ClockTickListener) it.next();
                cl.fireForClockTick();
            }
            catch (Exception e){
                //
            }
        }
    }

    public void start(){
        for(int x=0;x<timeOut;x++){
            for(int y=0;y<1000;y++){
                fire();
            }
        }
    }

}
