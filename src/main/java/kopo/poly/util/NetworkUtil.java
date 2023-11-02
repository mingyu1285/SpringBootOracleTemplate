package kopo.poly.util;

import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NetworkUtil {


    /**
     * Get 방식으로 OpenAPI 호출하기(전송할 헤더값이 존재하지 않는 경우 사용)
     *
     * @param apiUrl
     */
    public static String get(String apiUrl){
        return get(apiUrl, null);
    }


    /**
     * Get방식으로 OpenAPI 호출하기
     * 네이버 API 전송 소스 참고하여 구현
     * @param apiUrl 호출할 OpenAPI URL 주소
     * @param requestHeaders 전송하고 싶은 헤더 정보
     */
    public static String get(String apiUrl, @Nullable Map<String, String> requestHeaders){

        HttpURLConnection con =connect(apiUrl);

        try{
            con.setRequestMethod("GET");

            //전송할 헤더 값이 존재하면, 헤더 추가하기
            if (requestHeaders != null){
                for(Map.Entry<String, String> header : requestHeaders.entrySet()){
                    con.setRequestProperty(header.getKey(), header.getValue());

                }
            }
            //API 호출 후, 결과 받기
            int responseCode = con.getResponseCode();

            //API 호출 성공하면
            if (responseCode == HttpURLConnection.HTTP_OK){
                return readBody(con.getInputStream()); //성공 결과 값을 문자열로 변환
            } else{
                return  readBody(con.getInputStream()); //실행 결과 값을 문자열로 변환
            }
        } catch (IOException e){
            throw new RuntimeException("API 요청 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    /**OpenURL에 접속하기
     * 이 함수는 NetworkUtil에서만 사용하기에 접근 제한자를 private으로 선언함
     * 외부 자바 파일에서 호출 불가함
     *
     * @param apiUrl 호출할 OpenAPI URL 주소
     *
     */
    private static HttpURLConnection connect(String apiUrl){
        try{
            URL url = new URL(apiUrl);

            return (HttpURLConnection) url.openConnection();
        }catch (MalformedURLException e){
            throw new RuntimeException("API URL이 잘못되었습니다. : "+ apiUrl, e);
        }catch (IOException e){
            throw new RuntimeException("연결이 실패했습니다. : "+ apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)){
            StringBuilder responseBody = new StringBuilder();

            String line;
            while((line = lineReader.readLine()) != null){
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e){
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
