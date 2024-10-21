package com.example.tec.Service;

import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.script.ReactiveScriptExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import static java.lang.Math.abs;

@Service
public class CaptchaService {



    public ResponseEntity<Object> getCaptcha() throws IOException {
        RedissonClient redissonClient = Redisson.create();
        Random random = new Random();

        RList<Integer> captchaList = redissonClient.getList("captchaList");

        Instant now = Instant.now();
        long currentTimeSeconds = now.getEpochSecond();

        String currentTimeString = Long.toString(currentTimeSeconds+ random.nextInt());

        int hashCode = currentTimeString.hashCode();

        captchaList.add(abs(hashCode));


        BufferedImage image = createImageWithNumber(abs(hashCode));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/png");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    private BufferedImage createImageWithNumber(int number) {
        int width = 300;
        int height = 150;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.setColor(Color.BLACK);

        String numberStr = String.valueOf(number);

        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(numberStr)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();

        g2d.drawString(numberStr, x, y);

        g2d.dispose();

        return image;
    }



}
