/* Movie.java
==============
PURPOSE:   
AUTHOR:Jainlin Luo
CREATED: 2017-09-23
UPDATED: 
*/
package luojianl;

import java.util.ArrayList;

public class Movie {
    private String title;
    private int year;
    private int runningTime;
    private ArrayList<String> genres;
    public Movie(){}
    public Movie(String title,int year,int runningTime, ArrayList<String> genres){
        setTitle(title);
        setRunTime(runningTime);
        setYear(year);
        this.genres=genres;
    }
    private void setTitle(String title){
        this.title=title;
    }
    private void  setYear(int year){
        this.year=year;
    }
    private void setRunTime(int runningTime){
        this.runningTime=runningTime;
    }
    public String getTitle(){
        return title;
    }
    public int getYear(){
        return year;
    }
    public int getRunningTime(){
        return runningTime;
    }
    
}
