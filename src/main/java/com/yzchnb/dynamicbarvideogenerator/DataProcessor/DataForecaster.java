package com.yzchnb.dynamicbarvideogenerator.DataProcessor;

import com.yzchnb.dynamicbarvideogenerator.Entity.GeneratorEntity.Line;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DataForecaster {
    public static ArrayList<Line> doPredict(List<Line> lines, double proportion){
        assert proportion < 1;
        ArrayList<Line> newLines = new ArrayList<>(lines);

        Line fLine = newLines.get(0);
        Line lLine = newLines.get(newLines.size() - 1);
        int length = newLines.size();
        LocalDate lLineTime = lLine.getLocalDate();
        int same_time_count=0;
        int different_count=0;
        String last_day=null;
        for(int i=0;i<newLines.size();++i){
            String now_time=newLines.get(i).getLocalDate().toString();
            if(!now_time.equals(last_day)){
                ++different_count;
                last_day=now_time;
            }
        }
        same_time_count=newLines.size()/different_count;

        long sepDays = (lLine.getLocalDate().toEpochDay() - fLine.getLocalDate().toEpochDay()) / different_count;
        HashMap<String, Double> fLineType2Value = fLine.getType2Value();
        HashMap<String, Double> lLineType2Value = lLine.getType2Value();
        HashMap<String, Double> type2Grad = new HashMap<>();
        fLineType2Value.forEach((k, v) -> type2Grad.put(k, (lLineType2Value.get(k) - v) / (1.0 * length)));

        for (int i = 0; i < length * proportion; i++){
            Line newLine = new Line();

            newLine.setType2Value((HashMap<String, Double>) lLineType2Value.clone());

            newLine.getType2Value().forEach((k, v) -> {
                Random r = new Random();
                double ratio = (r.nextInt(60) - 30) / 100.0;
                Double newValue = v + (int)((1 + ratio) * type2Grad.get(k));
                newLine.getType2Value().put(k, newValue);
            });

            lLineType2Value.clear();
            lLineType2Value.putAll(newLine.getType2Value());
            if(i%same_time_count==0) {
                lLineTime = lLineTime.plusDays(sepDays);
            }
            newLine.setLocalDate(lLineTime);
            newLine.setTimeFormat(Line.TimeFormat.YYYY_MM_DD);
            newLines.add(newLine);
        }
        return newLines;
    }
}
