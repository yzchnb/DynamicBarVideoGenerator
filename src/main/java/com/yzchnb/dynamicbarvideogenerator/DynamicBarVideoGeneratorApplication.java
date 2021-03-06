package com.yzchnb.dynamicbarvideogenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DynamicBarVideoGeneratorApplication {

    private static boolean memoryFull = false;

    public static boolean isMemoryFull(){
        return memoryFull;
    }

    public static void setMemoryFull(boolean b){
        memoryFull = b;
    }

    public static void main(String[] args) {
        //SpringApplicationBuilder builder = new SpringApplicationBuilder(DynamicBarVideoGeneratorApplication.class);
        //builder.headless(false).run(args);
        SpringApplication.run(DynamicBarVideoGeneratorApplication.class, args);
    }
}
