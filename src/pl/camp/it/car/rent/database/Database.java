package pl.camp.it.car.rent.database;

import org.apache.commons.codec.digest.DigestUtils;
import pl.camp.it.car.rent.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private static Database instance = new Database();
    private final String pathToDbFile = "carRentDb.txt";

    private Database() {
        this.loadDataFromFile();
    }

    public static Database getInstance() {
        if(Database.instance == null) {
            Database.instance = new Database();
        }
        return Database.instance;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public boolean rentVehicle(String plate) {
        Vehicle vehicle = findVehicle(plate);
        if(vehicle != null && !vehicle.isRent()) {
            vehicle.setRent(true);
            return true;
        }
        return false;
    }

    public boolean returnVehicle(String plate) {
        Vehicle vehicle = findVehicle(plate);
        if(vehicle != null && vehicle.isRent()) {
            vehicle.setRent(false);
            return true;
        }
        return false;
    }

    private Vehicle findVehicle(String plate) {
        for(Vehicle vehicle : this.vehicles) {
            if (vehicle.getPlate().equals(plate)) {
                return vehicle;
            }
        }
        return null;
    }

    public boolean authenticate(String login, String password) {
        for(User currentUser : this.users) {
            if(currentUser.getLogin().equals(login) && currentUser.getPassword().equals(DigestUtils.md5Hex(password))) {
                return true;
            }
        }

        return false;
    }

    public void listUsers() {
        for(User user : this.users) {
            System.out.println(user.getLogin() + " - " + user.getPassword());
        }
    }

    public void authenticate2(String login, String password) {
        for(User currentUser : this.users) {
            if(currentUser.getLogin().equals(login) && currentUser.getPassword().equals(DigestUtils.md5Hex(password))) {
                return;
            }
        }
        throw new NullPointerException();
    }

    public void writeDataToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.pathToDbFile));

            for(Vehicle vehicle : this.vehicles) {
                writer.append(vehicle.convertToDbRecord());
                writer.newLine();
            }

            for(User user : this.users) {
                writer.append(user.convertToDbRecord());
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.pathToDbFile));

            String record;
            while((record = reader.readLine()) != null) {
                String[] recordArray = record.split(";");
                switch (recordArray[0]) {
                    case "Car":
                        this.vehicles.add(
                                new Car(recordArray[1], recordArray[2],
                                Integer.parseInt(recordArray[3]),
                                Integer.parseInt(recordArray[4]),
                                Double.parseDouble(recordArray[5]),
                                recordArray[6], Boolean.parseBoolean(recordArray[7]))
                        );
                        break;
                    case "Bus":
                        this.vehicles.add(
                                new Bus(recordArray[1], recordArray[2],
                                        Integer.parseInt(recordArray[3]),
                                        Integer.parseInt(recordArray[4]),
                                        Double.parseDouble(recordArray[5]),
                                        recordArray[6], Boolean.parseBoolean(recordArray[7]),
                                        Integer.parseInt(recordArray[8]), Boolean.parseBoolean(recordArray[9]))
                        );
                        break;
                    case "Motorcycle":
                        this.vehicles.add(
                                new Motorcycle(recordArray[1], recordArray[2],
                                        Integer.parseInt(recordArray[3]),
                                        Integer.parseInt(recordArray[4]),
                                        Double.parseDouble(recordArray[5]),
                                        recordArray[6], Boolean.parseBoolean(recordArray[7]),
                                        Boolean.parseBoolean(recordArray[8]), recordArray[9])
                        );
                        break;
                    case "User":
                        this.users.add(
                                new User(recordArray[1], recordArray[2])
                        );
                        break;

                    default:
                        break;
                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
            System.out.println("Wczytywanie bazy danych nieudane !!");
            System.exit(0);
        }
    }
}
