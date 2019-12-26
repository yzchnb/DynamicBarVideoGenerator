package com.yzchnb.dynamicbarvideogenerator.DataProcessor;

import com.yzchnb.dynamicbarvideogenerator.ConfigurationEntity.GeneratorConfiguration;
import com.yzchnb.dynamicbarvideogenerator.ConfigurationEntity.UserInputConfiguration;
import com.yzchnb.dynamicbarvideogenerator.GeneratorUtils.UtilEntity.Line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.yzchnb.dynamicbarvideogenerator.DataProcessor.LineProvider.boxLine;

public class ParamsChecker {
    public static void checkParams(GeneratorConfiguration generatorConfiguration, File csvFile) throws Exception{
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            long linesCount = bufferedReader.lines().count();
            if(linesCount <= 2){
                throw new Exception("数据太少！");
            }
            bufferedReader = new BufferedReader(new FileReader(csvFile));
            String firstLine = bufferedReader.readLine();
            String[] splitedfirstLine = firstLine.split(",");

            UserInputConfiguration userInputConfiguration = generatorConfiguration.getUserInputConfiguration();
            int DPS = userInputConfiguration.getDPS();
            int FPS = userInputConfiguration.getFPS();
            if(DPS % FPS != 0 && FPS % DPS != 0){
                throw new Exception("DPS 与 FPS 不是整数倍关系！");
            }

            if(splitedfirstLine.length <= 1){
                throw new Exception("输入文件格式不对：列太少");
            }

            ArrayList<String> types = new ArrayList<>(splitedfirstLine.length - 1);
            for (int i = 1; i < splitedfirstLine.length; i++) {
                types.add(splitedfirstLine[i]);
            }

            Line lastLine = null;
            Long lastDuration = null;
            while(true){
                String lineStr = bufferedReader.readLine();
                if(lineStr == null){
                    break;
                }
                Line newLine = boxLine(lineStr, types);
                if(lastLine != null){
                    if(lastDuration != null){
                        long newDuration = Duration.between(lastLine.getLocalDateTime(), newLine.getLocalDateTime()).toNanos();
                        if(newDuration != lastDuration){
                            throw new Exception("时间间隔不同！");
                        }
                        lastDuration = newDuration;
                    }else{
                        lastDuration = Duration.between(lastLine.getLocalDateTime(), newLine.getLocalDateTime()).toNanos();
                    }
                    lastLine = newLine;
                }else{
                    lastLine = boxLine(lineStr, types);
                }

            }

        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

}
