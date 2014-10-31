package org.example;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="MainServlet", urlPatterns={"/"})
public class MainServlet extends HttpServlet {

    final static String randomNumUrl = "http://randnum.herokuapp.com/";
    final static String randomWordUrl = "http://random-word.herokuapp.com/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpClient client = new HttpClient();

        GetMethod getRandomNum = new GetMethod(randomNumUrl);

        String output = "";

        try {
            client.executeMethod(getRandomNum);
            int numWords = Integer.parseInt(getRandomNum.getResponseBodyAsString());

            for(int i = 0; i < numWords; i++) {

                GetMethod getRandomWord = new GetMethod(randomWordUrl);

                try {

                    client.executeMethod(getRandomWord);
                    output += getRandomWord.getResponseBodyAsString() + " ";

                } catch (HttpException e) {
                    System.err.println("Fatal protocol violation: " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.err.println("Fatal transport error: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    getRandomWord.releaseConnection();
                }

            }

        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            getRandomNum.releaseConnection();
        }

        ServletOutputStream out = resp.getOutputStream();
        out.write(output.getBytes());
        out.flush();
        out.close();
    }

}
