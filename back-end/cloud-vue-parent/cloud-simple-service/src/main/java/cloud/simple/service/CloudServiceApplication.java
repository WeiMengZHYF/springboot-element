package cloud.simple.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


//@EnableDiscoveryClient
@SpringBootApplication
//@EnableEurekaClient
@MapperScan(basePackages={"cloud.simple.service.dao"})
public class CloudServiceApplication {

    private static Logger logger = LoggerFactory.getLogger(CloudServiceApplication.class);
	
    public static void main(String[] args) {

        SpringApplication.run(CloudServiceApplication.class, args);
        logger.info("cloud service application start successfully------------------");
    }

}
