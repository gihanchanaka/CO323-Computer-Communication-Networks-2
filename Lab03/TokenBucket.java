import java.util.LinkedList;
import java.util.Random;

public class TokenBucket implements ClockTickListener {
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */

    int tokensPerSec,maxCapacity;
    long milliSeconds;
    LinkedList<Token> bucket;
    TokenBucket(int tokensPerSecond,int maxCapacity){
        this.tokensPerSec=tokensPerSecond;
        this.maxCapacity=maxCapacity;
        this.milliSeconds=0;
        bucket=new LinkedList<Token>();
    }




    public void put(Token t){
        if(bucket.size()<maxCapacity){
            bucket.add(t);
            IO.printTimeDeviceAnd(this.milliSeconds,"TKN-BKT","Added to bucket. Now capacity="+bucket.size());
        }
        else{
            IO.printTimeDeviceAnd(this.milliSeconds,"TKN-BKT","Dropped the token. Total tokens in bucket="+bucket.size());

        }
    }


    public void fireForClockTick() {
        milliSeconds++;
        if(milliSeconds%(1000/tokensPerSec)==0){
            IO.printTimeDeviceAnd(this.milliSeconds,"TKN-GEN","Generated a token. Total tokens in bucket="+bucket.size());
            put(new Token());

        }
    }

    public boolean isEmpty(){
        return bucket.isEmpty();
    }

    public void popToken(){
        IO.printTimeDeviceAnd(this.milliSeconds,"TKN-BKT","Used a token. Total="+this.bucket.size());
        bucket.pop();
    }
}
