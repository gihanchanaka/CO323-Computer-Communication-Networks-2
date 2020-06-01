import javax.security.auth.callback.PasswordCallback;

public class PacketGenerator implements ClockTickListener {
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */
    long milliSeconds;
    int remainingPackets,averageRate;
    boolean random;
    PacketQueue sendTo;
    int nextPacketID;
    int packetSizeKiloBits;
    PacketGenerator(int TotalPackets,int packetsPerSecond,boolean random,PacketQueue sendTo,int packetSizeKiloBits){
        this.milliSeconds=0;
        this.remainingPackets=TotalPackets;
        this.averageRate= packetsPerSecond;
        this.random=random;
        this.sendTo=sendTo;
        this.nextPacketID=0;
        this.packetSizeKiloBits=packetSizeKiloBits;
    }

    public int getRemainingPackets(){
        return this.remainingPackets;
    }

    public boolean packetsFinished(){
        return getRemainingPackets()==0;
    }

    public void printReport(){
        IO.printTimeDeviceAnd(this.milliSeconds,"PKT-GEN","TOTAL PACKETS GENERATED = "+nextPacketID);
    }




    public void fireForClockTick() {

        milliSeconds++;

        if(!packetsFinished()){

            if(!random){
                if((milliSeconds%(1000/averageRate))==0){
                    IO.printTimeDeviceAnd(this.milliSeconds,
                            "PKT-GEN","Created packet "+nextPacketID);
                    sendTo.put(new Packet(nextPacketID,packetSizeKiloBits));
                    remainingPackets--;
                    nextPacketID++;
                }
            }
        }


    }
}
