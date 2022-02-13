package com.spacetime.spacetime2;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com", "org.n3r.idworker"})
//@ComponentScan("com")
@MapperScan("com.mapper")
@SpringBootApplication
public class Spacetime2Application {

    public static void main(String[] args) {
        SpringApplication.run(Spacetime2Application.class, args);
    }

}
