package edu.cs300;

import java.util.concurrent.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class PlayerStatsTracker {
    
    //HashMap<Integer,ArrayBlockingQueue> players;
    ArrayBlockingQueue<String> resultsOutputArray;
    ArrayBlockingQueue<AtBatPitchResults> queue16;
    ArrayBlockingQueue<AtBatPitchResults> queue33;
    int playerCount=0;
    
    
    public PlayerStatsTracker(){
        //this.players = new HashMap<Integer,ArrayBlockingQueue>();
        
        this.resultsOutputArray = new ArrayBlockingQueue<String>(30);
        try {
            
            queue16 = new  ArrayBlockingQueue<AtBatPitchResults>(10);
            new Worker(16,queue16, this.resultsOutputArray, "Bailey Hemphill").start();
            playerCount++;
            
            queue33 = new  ArrayBlockingQueue<AtBatPitchResults>(10);
            new Worker(33,queue33, this.resultsOutputArray, "Claire Jenkins").start();
            playerCount++;
            
 
           File file = new File ("players.txt");//finish processing of players and players history here

        } catch (Exception ex) {
        //} catch (FileNotFoundException ex) {
          System.err.println("FileNotFoundException triggered:"+ex);
        }
    }
    
  public static void main(String[] args) throws FileNotFoundException {
      
      
      PlayerStatsTracker tracker=new PlayerStatsTracker();
      try {
        tracker.run();//Not a thread; run() is just convenient naming
      } catch   (InterruptedException e){
          System.err.println("InterruptedException:"+e);
      }
  }
    
 void run() throws InterruptedException {

        while (true) {
            //process incoming pitch results
            //Here are some test pitch results
            queue16.put(new AtBatPitchResults(16,"BKBK1"));
            queue33.put(new AtBatPitchResults(33,"SKF1"));
            queue16.put(new AtBatPitchResults(16,"BBKBO"));
            
            //Consider using map to track the queues that map to each player's thread
            //this.players.get(16).put(new AtBatPitchResults(16,"BKBK1"));
            //this.players.get(33).put(new AtBatPitchResults(33,"SKF1"));

            break;//break when sentinel atBatPitchResult received
        }

        AtBatPitchResults sentinel=new AtBatPitchResults();
        queue16.put(sentinel);
        queue33.put(sentinel);

        //wait for all threads to complete
        int count=1;
        while (count <=playerCount ){
            String gameStatsMsg= resultsOutputArray.take();
            
            DebugLog.log(count+" stats to format/send:"+gameStatsMsg);

            //test message; map fields from resultsOutputArray to this message
            MessageJNI.writePlayerFinalStatsMsg( 16, 1, 1, "Temp name", 1, 2, 3, 4, 5, 6, .999,.500);
            count++;
                               
        }
    }
  
}
