package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvailityInsurance {
    public static void main(String[] args) throws IOException {
        // provide complete path for parent directory

        String mainDir = "/Users/kirani/Documents/TestWorkspace/TestProject/src/TestData/";

        List<File> csvFiles = getallfiles(mainDir);

        for (File file: csvFiles) {
            List<Avality> avality = readFromCsv(file.getAbsolutePath());

            for (Avality b : avality) {
                File csvOutputFile = new File(mainDir + "//" + b.getInsuranceCompany());
                PrintWriter pw = null;
                if (csvOutputFile.exists()) {
                    pw = new PrintWriter(new FileOutputStream(csvOutputFile, true));
                }
                else {
                    pw = new PrintWriter(csvOutputFile);
                }
                pw.append(b.toString()+"\n");
                pw.close();

            }
        }
    }// Main ends here


    public static List<File> getallfiles(String directoryName) {

        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        System.out.println("ResultList is " + resultList);
        for (File file : fList) {
            if (file.isFile()) {
                System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(getallfiles(file.getAbsolutePath()));
            }
        }
        resultList = resultList.parallelStream().filter(c -> c.getName().contains("csv")).collect(Collectors.toList());
        return resultList;
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data).collect(Collectors.joining(","));
    }

    private static Avality createAvality(String[] metadata) {
        String userId = metadata[0];
        String firstName = metadata[1];
        String lastName = metadata[2];
        String version = metadata[3];
        String insuranceCompany = metadata[4];

        // create and return Avality of this metadata
        return new Avality(userId, firstName, lastName, version, insuranceCompany);
    }

    private static List<Avality> readFromCsv(String fileName) {

        List<Avality> avalityFile = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String headerLine = br.readLine();
            String line  = br.readLine();

            // loop until all lines are read
            while (line != null) {
                String[] attributes = line.split(",");

                Avality avality = createAvality(attributes);

                // adding book into ArrayList
                avalityFile.add(avality);

                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return avalityFile;
    }
}

class Avality {

    private String userId;
    private String firstName;
    private String lastName;
    private String version;
    private String insuranceCompany;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public Avality(String userId, String firstName, String lastName, String version, String insuranceCompany) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.version = version;
        this.insuranceCompany = insuranceCompany;
    }

    @Override
    public String toString() {
        return userId + "," + firstName + "," + lastName + "," + version + "," + insuranceCompany;
    }

}
