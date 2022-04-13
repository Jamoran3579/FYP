package com.example.coinify;

import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Analyzer {

     File picture_file;

    public Mat detect_coins() {

        OpenCVLoader.initDebug();

        Mat coins = Imgcodecs.imread(picture_file.getAbsolutePath());
        Mat gray = new Mat();
        Mat img = new Mat();
        Mat circles = new Mat();
        Mat coins_copy = new Mat();

        Imgproc.cvtColor(coins, gray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.medianBlur(gray, img,7);
        Imgproc.HoughCircles(img, circles, Imgproc.HOUGH_GRADIENT, 0.8, 50, 110, 100, 70, 500);

        coins.copyTo(coins_copy);

        for (int i = 0; i < circles.cols(); i++ ) {
            double[] data = circles.get(0, i);
            Point center = new Point(data[0], data[1]);
            int radius = (int) data[2];

            Imgproc.circle(coins_copy, center, radius, new Scalar(0, 255, 0), 4);

        }

        Imgcodecs.imwrite(picture_file.getAbsolutePath(), coins_copy);

        return circles;
    }

    public Double calculate_amount() {

        Map<String, ArrayList<HashMap<String, Double>>>coins = new HashMap<>();

        HashMap<String, Double>value = new HashMap<>();
        value.put("Value", 1.0);
        HashMap<String, Double>radius = new HashMap<>();
        value.put("Radius", 16.2);
        HashMap<String, Double>ratio = new HashMap<>();
        value.put("Ratio", 1.0);
        HashMap<String, Double>count = new HashMap<>();
        value.put("Count", 0.0);
        coins.put("0.01 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("0.01 EUR").add(value);
        coins.get("0.01 EUR").add(radius);
        coins.get("0.01 EUR").add(ratio);
        coins.get("0.01 EUR").add(count);


        value.put("Value", 2.0);
        value.put("Radius", 18.75);
        value.put("Ratio", 1.154);
        value.put("Count", 0.0);
        coins.put("0.02 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("0.02 EUR").add(value);
        coins.get("0.02 EUR").add(radius);
        coins.get("0.02 EUR").add(ratio);
        coins.get("0.02 EUR").add(count);


        value.put("Value", 5.0);
        value.put("Radius", 21.25);
        value.put("Ratio", 1.308);
        value.put("Count", 0.0);
        coins.put("0.05 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("0.05 EUR").add(value);
        coins.get("0.05 EUR").add(radius);
        coins.get("0.05 EUR").add(ratio);
        coins.get("0.05 EUR").add(count);


        value.put("Value", 10.0);
        value.put("Radius", 19.75);
        value.put("Ratio", 1.215);
        value.put("Count", 0.0);
        coins.put("0.10 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("0.10 EUR").add(value);
        coins.get("0.10 EUR").add(radius);
        coins.get("0.10 EUR").add(ratio);
        coins.get("0.10 EUR").add(count);


        value.put("Value", 20.0);
        value.put("Radius", 22.25);
        value.put("Ratio", 1.369);
        value.put("Count", 0.0);
        coins.put("0.20 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("0.20 EUR").add(value);
        coins.get("0.20 EUR").add(radius);
        coins.get("0.20 EUR").add(ratio);
        coins.get("0.20 EUR").add(count);


        value.put("Value", 50.0);
        value.put("Radius", 24.25);
        value.put("Ratio", 1.492);
        value.put("Count", 0.0);
        coins.put("0.50 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("0.50 EUR").add(value);
        coins.get("0.50 EUR").add(radius);
        coins.get("0.50 EUR").add(ratio);
        coins.get("0.50 EUR").add(count);


        value.put("Value", 100.0);
        value.put("Radius", 23.25);
        value.put("Ratio", 1.431);
        value.put("Count", 0.0);
        coins.put("1 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("1 EUR").add(value);
        coins.get("1 EUR").add(radius);
        coins.get("1 EUR").add(ratio);
        coins.get("1 EUR").add(count);


        value.put("Value", 200.0);
        value.put("Radius", 25.75);
        value.put("Ratio", 1.585);
        value.put("Count", 0.0);
        coins.put("2 EUR", new ArrayList<HashMap<String, Double>>());
        coins.get("2 EUR").add(value);
        coins.get("2 EUR").add(radius);
        coins.get("2 EUR").add(ratio);
        coins.get("2 EUR").add(count);

        Mat circles = detect_coins();

        Double radi[];
        Point centers[];

        radi = new Double[circles.cols()];
        centers = new Point[circles.cols()];

        for (int i = 0; i < circles.cols(); i++ ) {
            double[] data = circles.get(0, i);
            centers[i] = new Point(data[0], data[1]);
            radi[i] =  data[2];
        }

        Arrays.sort(radi);
        Double smallest = radi[0];
        float tolerance = 0.03F;
        double total_amount = 0.0;
        Double ratio_to_check;
        Double current_value;

        Mat coins_circled = Imgcodecs.imread(picture_file.getAbsolutePath());

        Log.i("Coins","Contents of coins.get(\"0.01 EUR\")" + coins.get("0.01 EUR"));
        Log.i("Coins","Contents of coins.get(\"0.01 EUR\").get(0).get(\"Ratio\")" + coins.get("0.01 EUR").get(0).get("Ratio"));

        Log.i("Contents of circles.cols()", Integer.toString(circles.cols()));
        for (int i = 0; i < circles.cols(); i++ ) {
            double[] data = circles.get(0, i);
            Point current_center = new Point(data[0], data[1]);
            Double current_radius =  data[2];

            ratio_to_check = current_radius/smallest;

            Log.i("Ratio to check: ", String.valueOf(ratio_to_check));

            if (Math.abs(ratio_to_check - coins.get("0.01 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("0.01 EUR").get(0).get("Value");
                coins.get("0.01 EUR").get(0).put("Count", coins.get("0.01 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("0.01 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center, Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("0.02 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("0.02 EUR").get(0).get("Value");
                coins.get("0.02 EUR").get(0).put("Count", coins.get("0.02 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("0.02 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("0.05 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("0.05 EUR").get(0).get("Value");
                coins.get("0.05 EUR").get(0).put("Count", coins.get("0.05 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("0.05 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("0.10 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("0.10 EUR").get(0).get("Value");
                coins.get("0.10 EUR").get(0).put("Count", coins.get("0.10 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("0.10 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("0.20 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("0.20 EUR").get(0).get("Value");
                coins.get("0.20 EUR").get(0).put("Count", coins.get("0.20 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("0.20 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("0.50 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("0.50 EUR").get(0).get("Value");
                coins.get("0.50 EUR").get(0).put("Count", coins.get("0.50 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("0.50 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("1 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("1 EUR").get(0).get("Value");
                coins.get("1 EUR").get(0).put("Count", coins.get("1 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("1 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }
            else if (Math.abs(ratio_to_check - coins.get("2 EUR").get(0).get("Ratio")) <= tolerance) {
                current_value = coins.get("2 EUR").get(0).get("Value");
                coins.get("2 EUR").get(0).put("Count", coins.get("2 EUR").get(0).get("Count") + 1F);
                total_amount = total_amount + coins.get("2 EUR").get(0).get("Value");
                Imgproc.putText(coins_circled,String.valueOf(current_value/100),current_center,Imgproc.FONT_HERSHEY_SIMPLEX,3, new Scalar(0, 0, 0),4);
            }

        }

        Imgcodecs.imwrite(picture_file.getAbsolutePath(), coins_circled);

        return total_amount;

    }
}
