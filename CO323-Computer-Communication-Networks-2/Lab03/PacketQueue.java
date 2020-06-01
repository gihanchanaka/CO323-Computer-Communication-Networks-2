import java.util.LinkedList;
import java.util.Queue;

public class PacketQueue implements ClockTickListener{
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */
    long millis;
    int holdingCapacity;
    LinkedList<Packet> queue=new LinkedList<Packet>();
    PacketQueue(int holdingCapacity){
        millis=0;
        this.holdingCapacity=holdingCapacity;

    }

    public void fireForClockTick() {
        millis++;
    }

    public boolean put(Packet p){
        if(queue.size()<holdingCapacity){
            IO.printTimeDeviceAnd(this.millis,"QUEUE","Enqueud packet "+p.packetID);
            queue.add(p);
            return true;
        }
        else{
            IO.printTimeDeviceAnd(this.millis,"QUEUE","Dropped packet "+p.packetID);
            return false;
        }
    }

    public boolean isEmpty(){
        return queue.size()==0;
    }

    public Packet pop(){
        return queue.pop();
    }
}
