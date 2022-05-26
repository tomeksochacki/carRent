package pl.camp.it.car.rent;

import pl.camp.it.car.rent.database.Database;
import pl.camp.it.car.rent.gui.GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int loginCounter = 0;
        while (true) {
            String login, password;
            try {
                System.out.println("Podaj login:");
                login = reader.readLine();
                System.out.println("Podaj haslo:");
                password = reader.readLine();
            } catch (IOException e) {
                System.out.println("Wczytywanie danych nieudane !!");
                continue;
            }

            boolean authResult = database.authenticate(login, password);
            if (authResult) {
                break;
            } else {
                System.out.println("Niepoprawne dane !!");
                loginCounter++;
            }

            if (loginCounter >= 3) {
                System.out.println("Logowanie nieudane !!");
                System.exit(0);
            }
        }

        boolean flag = true;
        while (flag) {
            try {
                GUI.getInstance().showMainMenu();
                switch (reader.readLine()) {
                    case "1":
                        GUI.getInstance().showAllVehicles(database.getVehicles());
                        break;
                    case "2":
                        System.out.println("Podaj numer rejestracyjny pojazd:");
                        GUI.getInstance().showRentResult(database.rentVehicle(reader.readLine()));
                        break;
                    case "3":
                        System.out.println("Podaj numer rejestracyjny pojazdu:");
                        GUI.getInstance().showReturnResult(database.returnVehicle(reader.readLine()));
                        break;
                    case "4":
                        flag = false;
                        Database.getInstance().writeDataToFile();
                        break;
                    default:
                        System.out.println("Wybór nieprawidłowy !!");
                }
            } catch (IOException e) {
                System.out.println("Wczytywanie danych nieudane !!");
            }
        }
    }
}
