package org.shakh.covid19.commandline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shakh.covid19.client.error.Covid19DataNotFound;
import org.shakh.covid19.commandline.dto.Command;
import org.shakh.covid19.commandline.util.ConsoleTemplate;
import org.shakh.covid19.service.CountryCovid19InfoService;
import org.shakh.covid19.service.dto.CountryCovid19InfoDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInputReader implements CommandLineRunner {

    private final CountryCovid19InfoService countryCovidInfoService;
    private final static String ENTER_COUNTRY = "Enter country name:";

    @Override
    public void run(final String... args) {
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            System.out.println(ENTER_COUNTRY);
            input = sc.nextLine();

            log.debug("The name of the country ({}) is entered.", input);

            try {
                CountryCovid19InfoDto covidInfo = countryCovidInfoService.getCovid19Info(input);
                if (covidInfo != null) {
                    String resultCovidInfo = ConsoleTemplate.getCovid19InfoForCountry(covidInfo);
                    log.debug("Received information about covid19. {}", resultCovidInfo);
                    System.out.println(resultCovidInfo);
                }
            } catch (Covid19DataNotFound exception) {
                System.out.println("Data not found!");
            } catch (Exception exception) {
                System.out.println("Something went wrong!");
            }
        } while (!Command.EXIT.getName().equalsIgnoreCase(input));
        sc.close();
    }

}