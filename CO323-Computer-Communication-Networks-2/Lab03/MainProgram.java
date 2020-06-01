import java.util.Queue;

public class MainProgram {
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */
    public static void main(String[] args) {


        int timeToLiveForExperiment;//=15;
        int totalPackets;//=1000;
        int packetsPerSecond;//=100;
        boolean randomPacketGeneration;//=false;
        int queueCapacity;//=20;
        int bucketCapacity;//=10;
        int tokensGeneratedPerSec;//=200;
        int packetSizeKiloBits=1;

        try{
            timeToLiveForExperiment=Integer.parseInt(args[0]);
            totalPackets=Integer.parseInt(args[1]);
            packetsPerSecond=Integer.parseInt(args[2]);
            randomPacketGeneration=Boolean.parseBoolean(args[3]);
            queueCapacity=Integer.parseInt(args[4]);
            bucketCapacity=Integer.parseInt(args[5]);
            tokensGeneratedPerSec=Integer.parseInt(args[6]);

        }
        catch (Exception e){
            System.out.println("Usage : ");
            System.out.println("java MainProgram TTL totPKT PKTps ran Qcap BKTcap TKNps");
            return;
        }


        Clock clock=new Clock(timeToLiveForExperiment);

        PacketQueue routerRecievingQueue=new PacketQueue(queueCapacity);
        PacketGenerator packetGenerator=new PacketGenerator(totalPackets,
                packetsPerSecond,randomPacketGeneration,routerRecievingQueue,packetSizeKiloBits);

        TokenBucket tokenBucket=new TokenBucket(tokensGeneratedPerSec,bucketCapacity);
        Router router=new Router(routerRecievingQueue,tokenBucket);

        clock.addListener(routerRecievingQueue);
        /*The queue actually doesnt need a timer, but here it is used to print the status messages*/
        clock.addListener(packetGenerator);
        clock.addListener(tokenBucket);
        clock.addListener(router);


        IO.printImportant("Finished setting up.\nStarting clock");
        clock.start();
        IO.printImportant("End of experiment");
        packetGenerator.printReport();
        router.printReport();



    }
}
