package com.chong.girl.control;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSSigner;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class VideoController {
    @Autowired
    private HttpServletRequest request;
    String secretId = "AKIDlLhneG7iZBYfXqomMgla9T6UcU6StS5N";
    String secretKey = "B7niOoysWv5zIMBl5A0bw1sUrQlskNmx";


    @GetMapping("/video")
    public String uploadPage(Model model) {
        String appId = "1256321894";




        return "VideoTest";
    }

    @RequestMapping("/getsign")
    @ResponseBody
    public String getTencentSign(@RequestBody Map<String,String> map){
        COSSigner signer = new COSSigner();
        Date expirationTime = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Map<String,String> headerMap = new TreeMap<>();
        Map <String,String>paramMap = new TreeMap<>();
        String hello = signer.buildAuthorizationStr(HttpMethodName.PUT, map.get("pathname"),
                cred, expirationTime);
        return hello;
    }

    @RequestMapping("/geturl")
    @ResponseBody
    public String getTencentUpURL(@RequestBody String path){

        URL url = getUrl(path,System.currentTimeMillis());
        return url.toString();
    }

    Long now = 0L;
    URL l;
    private URL getUrl(String path,Long time) {


        if (time>now+10 * 60 * 1000){
            now = time+ 10 * 60 * 1000;
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
            COSClient cosclient = new COSClient(cred, clientConfig);
            String bucketName = "girl-1256321894";
            Date expirationTime = new Date(time+ 10 * 60 * 1000);
            l =  cosclient.generatePresignedUrl(bucketName, path, expirationTime, HttpMethodName.PUT);
        }

        return  l;
    }


}
  /* qiniu
     String accessKey = "LtbI54tq_oh44hsD2NffbsOMTQ5n3rWIjoPKAmCg";
        String secretKey = "-V7t5BJUVdgySTmt6JZ8NIya4b87VRtm60GePP-k";
        String bucket = "girl";
        String resourceKey = System.currentTimeMillis()+".jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket,resourceKey);
        System.out.println(upToken);
        String url = request.getRequestURL().toString();
        System.out.println(url);
        model.addAttribute("resourceKey",resourceKey);
        model.addAttribute("token", upToken);
     */