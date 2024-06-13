package selobank;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class Database {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Random random = new Random();

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public int generateUniqueID() {
        int id;
        do {
            id = random.nextInt(9000000) + 1000000;
        } while (accountExists(id));
        return id;
    }

    public boolean accountExists(int id) {
        for (Account account : accounts) {
            if (account.getHesapID() == id) {
                return true;
            }
        }
        return false;
    }

    public Account getAccountByID(int id) {
        for (Account account : accounts) {
            if (account.getHesapID() == id) {
                return account;
            }
        }
        return null;
    }

    public boolean tcNoExists(long tcNo) {
        for (Account account : accounts) {
            if (account.getTcNo() == tcNo) {
                return true;
            }
        }
        return false;
    }

    public void saveDataToFile(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Account account : accounts) {
                writer.print(account.getIsim() + "," +
                             account.getSoyIsim() + "," +
                             account.getHesapID() + "," +
                             account.getTcNo() + "," +
                             account.getBakiye() + "," +
                             account.getSifre());
                for (String islem : account.getİslemGecmisiList()) {
                    writer.print("," + islem);
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDataFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String isim = parts[0];
                    String soyIsim = parts[1];
                    int hesapID = Integer.parseInt(parts[2]);
                    long tcNo = Long.parseLong(parts[3]);
                    double bakiye = Double.parseDouble(parts[4]);
                    int sifre = Integer.parseInt(parts[5]);
                    Account account = new Account(isim, soyIsim, hesapID, tcNo, bakiye, sifre);
                    for (int i = 6; i < parts.length; i++) {
                        account.işlemEkle(parts[i]);
                    }
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
