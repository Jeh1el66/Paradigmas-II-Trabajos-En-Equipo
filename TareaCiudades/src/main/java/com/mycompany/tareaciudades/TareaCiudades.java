package com.mycompany.tareaciudades;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class TareaCiudades {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduce la ruta del archivo CSV:");
        String filePath = scanner.nextLine();

        LinkedHashSet<String> cityTemperatures = new LinkedHashSet<>();
        try {
            loadCSVData(filePath, cityTemperatures);
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo CSV: " + e.getMessage());
            return; // Termina la ejecución del programa si hay un error
        }

        boolean running = true;

        while (running) {
            System.out.println("Elige una opción:");
            System.out.println("1. Mostrar todas las ciudades y temperaturas");
            System.out.println("2. Buscar ciudad por nombre");
            System.out.println("3. Salir");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    showAllCities(cityTemperatures);
                    break;
                case 2:
                    searchCityByName(cityTemperatures);
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        }

        scanner.close();
    }

    private static void loadCSVData(String filePath, LinkedHashSet<String> cityTemperatures) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             Scanner scanner = new Scanner(fis)) {

            if (!scanner.hasNextLine()) {
                throw new IOException("El archivo CSV está vacío");
            }

            // Saltar las primeras dos líneas
            if (scanner.hasNextLine()) scanner.nextLine();
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    cityTemperatures.add(line);
                }
            }
        }
    }

    private static void showAllCities(LinkedHashSet<String> cityTemperatures) {
        System.out.println("Ciudades y temperaturas:");
        for (String data : cityTemperatures) {
            String[] parts = data.split(";");
            String city = parts[0];
            String temperature = parts[1];
            System.out.println("Ciudad: " + city + ", " +"Temperatura: "+ temperature + "°C");
        }
    }

    private static void searchCityByName(LinkedHashSet<String> cityTemperatures) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduzca el nombre de la ciudad: ");
        String cityName = scanner.nextLine();
        boolean found = false;

        for (String data : cityTemperatures) {
            String[] parts = data.split(";");
            String city = parts[0];
            String temperature = parts[1];
            if (city.equalsIgnoreCase(cityName)) {
                System.out.println("La temperatura de " + city + " es de " + temperature + "°C");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("La ciudad especificada no se encontró en la lista.");
        }
    }
}
