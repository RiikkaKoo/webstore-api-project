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

### Liipaisimet

### Tapahtumat

### Temporaaliominaisuudet

### Tietoturva