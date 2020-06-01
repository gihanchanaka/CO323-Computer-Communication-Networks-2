public class Router implements ClockTickListener{
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */

    PacketQueue recievingQueue;
    TokenBucket tokenBucket;
    long milliSeconds;
    int forwardedPacketCount;
    Router(PacketQueue recievingQueue,TokenBucket tokenBucket){
        this.recievingQueue=recievingQueue;
        this.tokenBucket=tokenBucket;
        this.milliSeconds=0;
        this.forwardedPacketCount=0;
    }

    public void forward(Packet p){
        IO.printTimeDeviceAnd(this.milliSeconds,"ROUTER","Forwarded the packet "+p.packetID);
        forwardedPacketCount++;
    }

    public void fireForClockTick() {
        milliSeconds++;
        while(!tokenBucket.isEmpty() && !recievingQueue.isEmpty()){
            tokenBucket.popToken();
            forward(recievingQueue.pop());
        }

    }

    public void printReport(){
        IO.printTimeDeviceAnd(milliSeconds,"ROUTER","Forwarded total packets = "+forwardedPacketCount);
    }
}
