package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";
    public static final String BEER_4_UPC = "0234892394829";
    public static final String BEER_5_UPC = "0824289340283";

    @Autowired
    private BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (beerRepository.count() == 0) {
            log.debug("initialize beer table");
            loadBeerObjects();
        }
    }

    private void loadBeerObjects() {
        beerRepository.save(Beer.builder()
                .beerName("'Mango Bobs")
                .beerStyle("IPA")
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal(12.95))
                .upc("0631234200036")
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Galaxy Cat")
                .beerStyle("PALE_ALE")
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal(12.95))
                .upc("0631234300019")
                .build());

        beerRepository.save(Beer.builder()
                .beerName("Pinball Porter")
                .beerStyle("PORTER")
                .minOnHand(12)
                .quantityToBrew(200)
                .price(new BigDecimal(12.95))
                .upc("0083783375213")
                .build());
        beerRepository.save(Beer.builder()
                .beerName("Lucky Jack")
                .beerStyle("APA")
                .minOnHand(24)
                .quantityToBrew(500)
                .price(new BigDecimal(30))
                .upc("0234892394829")
                .build());
        beerRepository.save(Beer.builder()
                .beerName("Tasty Juice")
                .beerStyle("NEIPA")
                .minOnHand(6)
                .quantityToBrew(120)
                .price(new BigDecimal(79))
                .upc("0824289340283")
                .build());

    }
}
