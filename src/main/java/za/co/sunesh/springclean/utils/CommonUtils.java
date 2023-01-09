package za.co.sunesh.springclean.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.FileCopyUtils;
import za.co.sunesh.springclean.pojos.Address;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CommonUtils {

    @Async
    public static String readFileFromResource(String filename) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return asString(resourceLoader.getResource("classpath:"+filename));
    }

    public static String asString(Resource resource) {
        try(Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static List<Address> jsonStringToAddressList(String data) {
        Address[] address = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            address = mapper.readValue(data, Address[].class);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException("unable to convert Json to Object: "+jpe.getMessage());
        }
        return Arrays.asList(address);
    }

    public static boolean isInteger(String s) {
        if(s == null || s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i), 10) < 0) return false;
        }
        return true;
    }
}
