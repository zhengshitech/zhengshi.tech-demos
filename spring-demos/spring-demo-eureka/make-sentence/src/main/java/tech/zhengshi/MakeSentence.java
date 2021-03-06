package tech.zhengshi;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import tech.zhengshi.webapi.SubjectClient;

import java.net.URI;

/**
 * Created by zhengqiang on 2017/1/10 15:47.
 * desc:
 */
@RestController
public class MakeSentence {

    //    @Autowired
//    DiscoveryClient client;
    @Autowired
    LoadBalancerClient client;

    @Autowired
    SubjectClient subjectClient;


    @RequestMapping("/sentence")
//    @HystrixCommand(fallbackMethod="getFallbackSubject")
    public String getSentence() {
        String subject = getWord("zhengshitech-eureka-client-subject");
        String action = getWord("zhengshitech-eureka-client-verb");
        String sentence = subject + " " + action + ".";
        System.out.println(sentence);
        return sentence;//大小写不区分
    }


    public static final String getFallbackSubject(){
        return "make sentence error!";
    }

//
//    public String getWord(String service) {
//        List<ServiceInstance> list = client.getInstances(service);
//        if (list != null && list.size() > 0) {
//            ServiceInstance serviceInstance = list.get(0);
//            URI uri = serviceInstance.getUri();
//            if (uri != null) {
//                String forObject = (new RestTemplate()).getForObject(uri, String.class);
//                return forObject;
//            }
//        }
//        return null;
//    }



    @HystrixCommand(fallbackMethod="getFallbackSubject")
    @RequestMapping("/sentenceV2")
    public String sentenceV2() {
        String subject = subjectClient.getWord();
        String action = getWord("zhengshitech-eureka-client-verb");
        String sentence = subject + " " + action + ".";
        System.out.println(sentence);
        return sentence;//大小写不区分
    }


    public String getWord(String service) {
            ServiceInstance serviceInstance = client.choose(service);
            URI uri = serviceInstance.getUri();
            if (uri != null) {
                String forObject = (new RestTemplate()).getForObject(uri, String.class);
                return forObject;
            }
            return "##### ERR MSG ####";
    }
}