package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeerLoader implements CommandLineRunner {

    @Autowired
    private BeerRepository beerRepository;


    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if (beerRepository.count() == 0) {
            beerRepository.save(Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle("IPA")
                    .quantityToBrew(200)
                    .upc(33701000001l)
                    .price(new BigDecimal(57))
                    .build());
            beerRepository.save(Beer.builder()
                    .beerName("Lucky Jack")
                    .beerStyle("APA")
                    .quantityToBrew(200)
                    .upc(33701000002l)
                    .price(new BigDecimal(39))
                    .build());
            beerRepository.save(Beer.builder()
                    .beerName("Easy")
                    .beerStyle("NEIPA")
                    .quantityToBrew(200)
                    .upc(33701000003l)
                    .price(new BigDecimal(43))
                    .build());
        }
        System.out.println("Loaded Beers: "+beerRepository.count());
    }
}
