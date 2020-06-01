public class Packet {
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */

    int sizeKiloBits;
    int packetID;
    /*
        Note: The packet size is given in kilo bits for the convenience of
        calculation when it comes to transmission
     */
    Packet(int id,int sizeKiloBits){
        this.packetID=id;
        this.sizeKiloBits=sizeKiloBits;
    }
}
