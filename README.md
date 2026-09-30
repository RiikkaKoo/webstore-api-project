# VERKKOKAUPPA TIETOKANTA & API HARJOITUSTYÖ

## ALOITUS

## API-PÄÄTEPISTEET



Base URL:

http://localhost:8090/api

### Asiakkaat (Customers)

| Metodi   | Päätepiste                | Kuvaus                            |
|----------|---------------------------|-----------------------------------|
| `GET`    | `/customers`              | Hakee kaikki asiakkaat            |
| `GET`    | `/customers/{id}`         | Hakee asiakkaan ID:n perusteella  |
| `GET`    | `/customers/{id}/address` | Hakee asiakkaan osoitteet         |
| `GET`    | `/customers/{id}/orders`  | Hakee asiakkaan tilaukset         |
| `POST`   | `/customers`              | Lisää uuden asiakkaan             |
| `POST`   | `/customers/{id}/address` | Lisää asiakkaalle uuden osoitteen |
| `PUT`    | `/customers/{id}`         | Päivittää asiakkaan tiedot        |
| `PUT`    | `/customers/{id}/address` | Päivittää asiakkaan osoitteen     |
| `DELETE` | `/customers/{id}`         | Poistaa asiakkaan                 |
| `DELETE` | `/customers/{id}/address` | Poistaa asiakkaan osoitteen       |

---
####  **GET** **`/customers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Esimerkkivastaus:

```json
{
  "customer_address": [
    {
      "city": "New Lisa",
      "country": "Armenia",
      "id": 67,
      "postal_code": "84756",
      "street_address": "6147 Brandy Rue Suite 122"
    }
  ],
  "email": "sandralawson67@example.com",
  "first_name": "Derek",
  "id": 67,
  "last_name": "Berg",
  "phone": "(414)595-0874x52504"
}
```

---
### Tilaukset (Orders)

| Metodi   | Päätepiste                              | Kuvaus                           |
|----------|-----------------------------------------|----------------------------------|
| `GET`    | `/orders`                               | Hakee kaikki tilaukset           |
| `GET`    | `/orders/{id}`                          | Hakee tilauksen ID:n perusteella |
| `GET`    | `/orders/{id]/items`                    | Hakee tilauksen tuotteet         |
| `POST`   | `/orders`                               | Lisää uuden tilauksen            |
| `POST`   | `/orders/{id}/items/add/{productId}`    | Lisää uuden tuotteen tilaukseen  |
| `PUT`    | `/orders/{id}`                          | Päivittää tilauksen tiedot       |
| `DELETE` | `/products/{id}`                        | Poistaa tilauksen                |
| `DELETE` | `/orders/{id}/items/remove/{productId}` | Poistaa tuotteen tilauksesta     |


### Tuotteet (Products)

| Metodi   | Päätepiste           | Kuvaus                                             |
|----------|----------------------|----------------------------------------------------|
| `GET`    | `/products`          | Hakee kaikki tuotteet                              |
| `GET`    | `/products/{id}`     | Hakee tuotteen ID:n perusteella                    |
| `GET`    | `/products/archived` | Hakee valikoimasta poistetut tuotteet              |
| `POST`   | `/products`          | Lisää uuden tuotteen                               |
| `PUT`    | `/products/{id}`     | Päivittää tuotteen tiedot                          |
| `DELETE` | `/products/{id}`     | Arkistoi tuotteen, eli päivittää sen arkistoiduksi |


## TIETOKANNAN OMINAISUUDET

### Indeksit

Indekseille on pyritty tehostamaa tauluhin kohdistuvia hakuja. Tähän tietokantaan on toteutettu seuraavat indeksit:

- **idx_customeraddresses_country**
    - Kohdistuu `customeraddresses`-taulun `country`-kenttään. API:ssa haetaan tietoa raportteja varten osoitteiden maan perusteella, joten indeksillä pyritään tehostamaan näitä hakuja.

- **idx_customers_lastname**
    - Kohdistuu `customers`-taulun `last_name`-kenttään. Indeksillä tehostetaan hakuja, joissa asiakkaita haetaan sukunimen perusteella.

- **idx_orders_orderdate**
    - Kohdistuu `orders`-taulun `order_date`-kenttään. Tilauksia haetaan ja suodatetaan tilauspäivämäärien mukaan, joten indeksillä pyritään tehostamaan näitä operaatioita.

- **idx_orders_status**
    - Kohdistuu `orders`-taulun `status`-kenttään. API suodattaa tilauksia niiden tilan perusteella useammassa kohdassa, joten indeksillä pyritään tehostamaan näitä hakuja.

- **idx_products_price**
    - Kohdistuu `products`-taulun `price`-kenttään. Tuotteita suodatetaan niiden hintojen perusteella, joten indeksin tarkoitus on tehostaa näitä hakuja.

- **idx_products_stock**
    - Kohdistuu `products`-taulun `stock_quantity`-kenttään. Raportteja varten tuotteita haetaan niiden varastomäärän perusteella, joten indeksillä pyritään tehostamaan näitä hakuja.

- **idx_supplieraddresses_country**
    - Kohdistuu `supplieraddresses`-taulun `country`-kenttään. Raportteja varten haetaan toimittajia heidän sijaintimaidensa perusteella, joten indeksillä pyritään tehostamaan suodatusta.



### Transaktiot

### Näkymät

Tietokantaan on toteutettu näkymiä kahta eri tarkoitusta varten. Ensimmäiset näkymät on luotu keräämään koostetietoa. 
Toiset näkymät kokoavat yhteen eri taulujen tietoja, jotta niitä voidaan helposti käyttää näkymän kautta. 
Näkymiin kohdistetaan vain SELECT-kyselyitä tässä API:ssa, eli kaikki muokkaukset tehdään suoraan oikeisiin tauluihin, sillä
näkymät ovat joko koostetietoa ja/tai ne on luotu useamman taulun tiedoista, mikä tekee päivittämisestä näkymän kautta hankalaa tai jopa mahdotonta.

Tietokantaan on toteutettu seuraavat näkymät:

- **orders_stats**
  - Kerää yhteen koostetietoa orders-, customeraddresses- ja orderitems-tauluista.
  - Laskee kaikkien tilausten lukumäärät ja tuotot maakohtaisesti. Mukaan ei lasketa peruutettuja tilauksia.
  - Näkymästä haetaan tiedot raportteja varten. Raportin voi nähdä oman päätepisteen kautta.

- **order_items**
  - Kerää yhteen tietoa orders-, orderitems- ja products-tauluista.
  - Näkymässä on sarakkeet tilauksen ID:lle, asiakkaan ID:lle, tuotteen ID:lle, tuotteen nimelle, ostetun tuotteen kappalemäärälle ja -hinnalle sekä kokonaishinnalle.
  - Näkymästä voidaan hakea helposti hakea oleellinen tieto tilauksen kaikista tuotteista. GET-metodien JSON-vastaukset ovat yksinkertaisempia ja siistimpiä, 
  kun tieto haetaan näkymästä, sen sijaan, että backend puolella palautettaisiin useita OrderItem-entiteettejä.

- **stock_suppliers**
  - Kerää yhteen tietoa products-, suppliers- ja supplieraddresses-tauluista.
  - Näkymässä on sarakkeet tuotteen ID:lle, tuotteen nimelle, tuotteen lukumäärälle, eli varastolle, toimittajan ID:lle sekä toimittajan yhteys- ja osoitetiedoille.
  - Näkymästä voidaan hakea mm. tietoa toimittajan tuotteista ja vähissä olevan tuotteen toimittajan tiedot. Näkymää käytetään raportteja varten, joita voi tarkastella päätepisteiden kautta.

### Liipaisimet

Tietokannan eheydestä huolehditaan pääasiassa eheysrajoitteiden avulla, eli eri ON DELETE- ja ON UPDATE-määritelmät tietokannan taulujen viiteavainten määrrittelyn yhteydessä.
Backend:n ja frontend:n vastuulla on huolehtia tietyistä operaatioista, jotta tietokanta pysyy muokattavana ja ymmärrettävämpänä. Nämä seikat vähentävät liipaisinten tarvetta tässä tietokannassa.

Tietokantaan on kuitenkin toteutettu yksi liipaisin:

- **validate_stock_quantity**
  - Laukaisu tapahtuu ennen products-tauluun kohdistuvaa päivitysoperaatiota.
  - Liipaisin tarkistaa, että products-taulun stock_quantity sarakkeeseen ei yritetä syöttää negatiivista arvoa. 
  Jos syötettävä arvo on negatiivinen, liipaisin heittää virhekoodin.

### Tapahtumat

Tietokantaan on toteutettu yksi ajastettu tapahtuma: 

- **log_daily_sales**
  - Tapahtuu pävittäin vuorokaudenvaihteessa, eli klo. 00.00.
  - Lisää daily_sales-tauluun raportin päivämäärän, päivän tuotot ja tilausten lukumäärän. Ei laske mukaan peruutettuja tilauksia.

### Temporaaliominaisuudet

Tässä tietokannassa käytetään pääasiassa kirjausaikaan perustuvaa aikaulottuvuutta. Tiettyjen taulujen muutokset kirjataan ylös, milloin taulun tietuetta on muokattu.
Näin voidaan tarkastella, mitä arvoja tietueella on aikaisemmin ollut tietyllä valillä, ja milloin muutoksia on tapahtunut. Kirjausajan aikaulottuvuus on käytössä
products-, orders-, customers- ja suppliers-tauluissa. Aikaulottuvuus on otettu käyttöön lisäämällä WITH SYSTEM VERSIONING -määritelmä taulujen perään.

(Kelpoisuusaika?)

### Tietoturva

Tietokannan skeemaa, eli rakennetta, voi muokata vain se henkilö jolla on tiedossa administrator-käyttäjän salasana. Käyttäjälle pystyy kirjautumaan vain tietystä
IP-osoitteesta, eli vain tietyn verkon sisältä. Tämä estää ulkoiset kirjautumiset ja lisää tietokannan tietoturvaa. Verkkosivuston kautta pystyy tekemään vain 
tietokannan taulujen tietueihin kohdistuvia luku-, lisäys-, päivitys- ja poisto-operaatioita. Koska tämä API on tällä hetkellä suunniteltu käytettäväksi vain admin-
käyttäjien sovelluksen yhteydessä, käyttäjien oikeuksia ei tarkisteta päivitys- ja poisto-operaatioiden yhteydessä. Tulevaisuudessa tähän API:iin ja tietokantaan tulisi lisätä
jokin tunnistus tapa, jolla voidaan erottaa verkkokaupan ylläpitäjät, eli admin käyttäjät, ja asiakkaat toisistaan, jotta voidaan estää ei sallittuja operaatioita.

Tietokannan varmuuskopiointisuunnitelma:

- **Mitä:** Täysi varmuuskopiointi: koko tietokanta Ja inkrementaalinen varmuuskopiointi: muutoksen täyden kopioinnin jälkeen

- **Milloin:** Täysi varmuuskopiointi maanantai yöllä (1.00 – 3.00) ja inkrementaalinen varmuuskopiointi päivittäin

- **Miten:** Lämmin varmuuskopiointi, eli tietokannasta voi lukea tietoja, mutta tietoja ei voi muokata.

    - Täysi varmuuskopiointi: Looginen varmuuskopio: mysqldump-komennolla tuotetaan tietokantaskripti, jossa on tietokannan rakenne ja kaikki tietueet.

    - Inkrementaalinen varmuuskopiointi: Päivän aikana tapahtuvat muutokset kirjataan ylös lokiin

- **Minne:** Varmuuskopiot otetaan talteen erilliselle serverille, mikä parantaa tietoturvaa. Varmuuskopioita säilytetään kolme viikkoa. Sen jälkeen täysi varmuuskopio ja siihen liittyvät inkrementaaliset varmuuskopiot poistetaan.