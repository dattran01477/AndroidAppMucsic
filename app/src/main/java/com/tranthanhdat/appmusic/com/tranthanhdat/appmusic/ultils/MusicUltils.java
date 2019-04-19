package com.tranthanhdat.appmusic.com.tranthanhdat.appmusic.ultils;

public class MusicUltils {
    public static final  int MAX_PROGRESSS=10000;

    public String milliSecondToTime(long miliseconds){
        //chuyen milisecond to time. ex:1200->0:2:00
        String finalTimeString="";
        String secondString="";

        int hours=(int) (miliseconds/(1000*60*60));
        int minutes=(int) (miliseconds%(1000*60*60))/(1000*60);
        int seconds=(int) ((miliseconds%(1000*60*60))%(1000*60)/1000);

        if(hours>0){
            finalTimeString=hours+":";
        }
        if(seconds<0){
            secondString="0"+seconds;
        }else{
            secondString=""+seconds;
        }

        finalTimeString=finalTimeString+minutes+":"+secondString;


        return finalTimeString;
    }

    public int getProgressSeekBar(long currrentDuration,long totalDuration){
        //progress tuong ung voi bai nhac dang phat. ex:bai nhac co tong thoi gian la 1200 milisecond va dang phat toi 600 millisecond
        //thi progress la (600/1200)*Max_progress

        Double progress=(double) 0;
        progress=(((double)currrentDuration)/totalDuration)*MAX_PROGRESSS;

        return progress.intValue();
    }
    public int progressToTime(int progress, int totalDuration){
        //tra ve currentDuration khi progress thay doi, lam nguoc lai vi du tren. *Luu y khi tra ve nho nhan cho 1000.

        int currentDuration=0;
        totalDuration=(int) (totalDuration/1000);
        currentDuration=(int)((double)progress/MAX_PROGRESSS)*totalDuration;

        return currentDuration*1000;
    }
}
