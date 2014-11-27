package com.example.wanderxx.neurosky;

import java.util.Random;

/**
 * Created by nitin on 10/18/14.
 */
public class NeuroskyUtility {

   private static NeuroskyUtility neuroskyUtility=null;
   private static Random random=null;
   private static final double randomValue=0.5;
   private static double theta=45.00;
   private static final double theatMAX=361;

    private double getRadians(double theta){

        return (theta* Math.PI)/180.00;
    }

   private NeuroskyUtility()
    {
        random= new Random();
    }

    public static NeuroskyUtility getInstance()
    {
        if(null !=neuroskyUtility)
            return neuroskyUtility;
        else
            return new NeuroskyUtility();
    }
    public  int[] randNeuroskyData()
    {
        int data[]=new int[3];
        for(int i=0;i<data.length;i++) {
            double sinValue = Math.sin(getRadians(theta));
            theta *= 2.2;
            theta %= theatMAX;
            data[i] = Math.abs((int) Math.round((sinValue * 100)));
        }
        return data;
    }

}
