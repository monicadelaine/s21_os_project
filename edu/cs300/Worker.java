package edu.cs300;

import java.util.concurrent.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class Worker extends Thread{

  ArrayBlockingQueue<AtBatPitchResults> incomingStats;
  ArrayBlockingQueue outgoingResults;
  Integer id;
  String name; 



  public Worker(Integer id,ArrayBlockingQueue<AtBatPitchResults> incomingStats, ArrayBlockingQueue outgoingResults, String playerName){
    this.incomingStats=incomingStats;
    this.outgoingResults=outgoingResults;
    this.id=id;
    this.name=playerName;//put name of player here

  }

  public void run() {
    DebugLog.log("Player Tracker-"+this.id+" ("+this.name+") thread started ...");
    int count = 0;
    while (true){
      try {
        count++;//not neeed for anything...
        AtBatPitchResults atBat=(AtBatPitchResults)this.incomingStats.take();
        DebugLog.log("Worker-" + this.id + " " + atBat.pitchResults);
          
        //final message from tracker, used to exit out of infinite while loop
        if(atBat.gameOver()) {
            DebugLog.log("signal'd game over");
            
            //print player stats report; must be done from threads
            
            //Write game stats to player history file <player_id.txt>
            //Must be done concurrently from thread; append, don't rewrite
            
            //send game stats back then exit
            this.outgoingResults.put("Format class with data needed for stats msg");
            return;
        } 
        
        System.out.println("Game not ending...process pitch results");     
                                     
          
      } catch(InterruptedException e){
        System.err.println(e.getMessage());
      }
    }
  }
    
}
