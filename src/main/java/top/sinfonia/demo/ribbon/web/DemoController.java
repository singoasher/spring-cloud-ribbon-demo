package top.sinfonia.demo.ribbon.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <br>
 * <b>Project:</b> spring-cloud-ribbon-demo<br>
 * <b>Date:</b> 2017/11/1 14:24<br>
 * <b>Author:</b> Asher<br>
 */
@RestController
public class DemoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);
    private static final String VIRTUAL_HOST_NAME_DEMOSERVICE = "demoservice";
    private static final String VIRTUAL_HOST_NAME_ASHMAN = "ashman";

    @Autowired
    @LoadBalanced
    private RestTemplate loadBalanced;
    @Autowired

    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/demo/ip")
    public ResponseEntity<String> demoIp() {
        String result = loadBalanced.getForObject("http://" + VIRTUAL_HOST_NAME_DEMOSERVICE + "/ip", String.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/demo/ip/10088")
    public ResponseEntity<String> demoIp10088() {
        String result = restTemplate.getForObject("http://sinfonia.top:10088/ip", String.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/demo/info")
    public ResponseEntity<String> demoInfo() {
        String result = loadBalanced.getForObject("http://" + VIRTUAL_HOST_NAME_DEMOSERVICE + "/info", String.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/demo/instance")
    public ResponseEntity<ServiceInstance> logDemoInstance() {
        ServiceInstance serviceInstance = loadBalancerClient.choose(VIRTUAL_HOST_NAME_DEMOSERVICE);
        LOGGER.info("ServiceId: {}; Host: {}; Port: {}",
                serviceInstance.getServiceId(),
                serviceInstance.getHost(),
                serviceInstance.getPort()
        );
        return ResponseEntity.ok(serviceInstance);
    }

    @GetMapping("/ashman/list")
    public ResponseEntity<String> ashmanList() {
        String result = loadBalanced.getForObject("http://" + VIRTUAL_HOST_NAME_ASHMAN + "/v1/ashman", String.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/ashman/instance")
    public ResponseEntity<ServiceInstance> logAshmanInstance() {
        ServiceInstance serviceInstance = loadBalancerClient.choose(VIRTUAL_HOST_NAME_ASHMAN);
        LOGGER.info("ServiceId: {}; Host: {}; Port: {}",
                serviceInstance.getServiceId(),
                serviceInstance.getHost(),
                serviceInstance.getPort()
        );
        return ResponseEntity.ok(serviceInstance);
    }
}
