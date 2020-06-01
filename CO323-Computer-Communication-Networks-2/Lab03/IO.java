public class IO {
    /*
        CO323 Lab 03
        Token Bucket Algorithm
        E/14/158 gihanchanaka@gmail.com
        05/05/2018
     */
    public static void printImportant(String s){
        System.out.println("********************");
        System.out.println(s);
        System.out.println("********************");
    }

    public static void print(String s){
        System.out.println(s);
    }

    public static void printTime(long milliSeconds){
        long seconds=milliSeconds/1000;
        long min=seconds/60;

        System.out.print((min)+":"+(seconds%60)+":"+(milliSeconds%1000));
    }

    public static void printTimeAnd(long milliSeconds,String s){
        printTime(milliSeconds);
        System.out.println("\t"+s);
    }

    public static void printTimeDeviceAnd(long milliSeconds,String device,String s){
        printTimeAnd(milliSeconds,device+"\t:"+s);
    }

}
