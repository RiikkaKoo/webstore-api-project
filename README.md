# VERKKOKAUPPA TIETOKANTA & API HARJOITUSTYÖ

Tämä on repositorio Tietokantaratkaisut-kurssilla tehdylle tietokanta- ja API-projektille. 
Projektissa tehtiin toteutettiin tietokanta ja backend-palvelu kuvitteeliselle verkkokaupalle käyttäen SQL- ja Java-koodikieliä. 
Tietokanta on suunniteltu käyttettäväksi MariaDB tietokannanhallintajärjestelmällä, ja backend REST API-taustapalvelu on toteutettu Spring Boot frameworkilla.

REST API-taustapalvelu on tarkoitettu verkkokaupan työntekijöiden ja ylläpitäjien käyttöön eikä niinkään itse verkkokaupan API:ksi. Se on siis suunniteltu siten,
että verkkokaupalla olisi erillinen sovellus/verkkosivu, josta vain työntekijät pääisisivät tekemään kutsuja taustapalveluun. Tässä projektissa ei siis tarkemmin
määritetä autentikointi- ja autorisointitoimintoja API-pyynnöille backendissa, vaikka ne lopulliseen järjestelmään toteutettaisiin.

Koska REST API-taustapalvelu on tarkoitettu verkkokaupan työntekijöille, on taustapalvelun kautta mahdollista nähdä muokata tietokannan dataa sekä joidenkin taulujen
kohdalla myös poistaa tietoa. Haettu tieto on myös yksityiskohtaisempaa, eli mm. haetun tuotteen mukana tulee tietoa myös sen toimittajasta. 
Työntekijöille siis tarjotaan enemmän vapauksia tietokannan tiedon käsittelyyn.

Tämä dokumentti keroo, miten tietokantajärjestelmän voi ottaa käyttöön, ja esittelee taustapalvelun API-päätepisteet sekä tietokannanominaisuudet. 

**SISÄLTÖ** </br>

- [ALOITUS](#aloitus)
- [API PÄÄTEPISTEET](#api-päätepisteet)
- [TIETOKANNAN OMINAISUUDET](#tietokannan-ominaisuudet)
  - [Indeksit](#indeksit)
  - [Transkatiot](#transaktiot)
  - [Näkymät](#näkymät)
  - [Liipaisimet](#liipaisimet)
  - [Tapahtumat](#tapahtumat)
  - [Temporaaliominaisuudet](#temporaaliominaisuudet)
  - [Tietoturva](#tietoturva)

---

## ALOITUS

---

### Tietokanta

Tietokanta on suunniteltu käytettäväksi MariaDB-tietokannanhallintajärjestelmällä. 
Kansiossa, jonka nimi on `sql`, on tietokannan luontiin tarvittavat sql-tiedostot.

Tietokanta luodaan kansiosta löytyvällä sql-skriptillä. Tiedoston nimi on `web_store_script.sql`. Suorittamalla skriptin
MariaDB:ssä tietokannan taulut ja muut rakenteet luodaan.

Luo tietokantakäyttäjä, jolla on pääsy `web_store` tietokantaan, käyttäen `web_store_user.sql`-tiedoston komentoja, tai kopiomalla ne suoraan tästä:
```sql
CREATE USER 'webstore'@'localhost' IDENTIFIED BY 'webstore_password';

GRANT SELECT, INSERT, UPDATE, DELETE ON web_store.* TO
    'webstore'@'localhost';

FLUSH PRIVILEGES;
```
Kansiossa on myös tiedosto, jossa on mallidataa tietokannan täyttöön, jos haluaa kokeilla ja testata tietokannan toimintaa. 
Mallidata on `seed_data_compacted_v2.sql`-tiedostossa.

Sql-tiedostoja voi suorittaa suoraan MariaDB:n kautta `SOURCE`-komennolla:
```sql
SOURCE C:/filepath/web_store_script.sql;
SOURCE C:/filepath/web_store_user.sql;
SOURCE C:/filepath/seed_data_compacted_v2.sql;
```
Tallenna tiedostot omalle koneelle, ja muuta tiedoston osoite oikeaksi.

---

### REST API-taustapalvelu

Kopio GitHub-repositorio omalle koneellesi:
```
git clone https://github.com/RiikkaKoo/webstore-api-project.git
```

Suorita `WebstoreApplication`-luokan koodi. Tämä käynnistää taustapalvelun.

API-päätepisteiden GET-pyyntöjä voi testa selaimen kautta osoitteessta http://localhost:8090/api, mutta POST-, UPDATE- ja DELETE-pyyntöjä varten tarvitaan
frontend-sovellus, jota ei ole tässä projektissa toteutettu. Näitä komentoja voi kuitenkin testata `test`-kansion `APItests.http`-tiedoston tai `controller`-paketin luokkien kautta. 
Controller-luokkien metodien yhteydessä on maapallokuvake, joka luo http-pyynnön, jonka voi suorittaa.

---

## API-PÄÄTEPISTEET

---

Base URL: </br>
http://localhost:8090/api

Eri päätepisteet:

- [Asiakkaat (Customers)](#asiakkaat-customers)
- [Tilaukset (Orders)](#tilaukset-orders)
- [Tuotteet (Products)](#tuotteet-products)
- [Kategoriat (Categories)](#kategoriat-categories)
- [Toimittajat (Suppliers)](#toimittajat-suppliers)
- [Raportit (Reports)](#raportit-reports)

---

### Asiakkaat (Customers)

| Metodi   | Päätepiste                 | Kuvaus                                                                          |
|----------|----------------------------|---------------------------------------------------------------------------------|
| `GET`    | `/customers`               | Hakee kaikki asiakkaat                                                          |
| `GET`    | `/customers/{id}`          | Hakee asiakkaan ID:n perusteella                                                |
| `GET`    | `/customers/email/{email}` | Hakee asiakkaan sähköpostiosoitteen perusteella                                 |
| `GET`    | `/customers/{id}/address`  | Hakee asiakkaan osoitteen                                                       |
| `GET`    | `/customers/{id}/orders`   | Hakee asiakkaan tilaukset                                                       |
| `POST`   | `/customers`               | Lisää uuden asiakkaan                                                           |
| `POST`   | `/customers/{id}/address`  | Lisää asiakkaalle uuden osoitteen                                               |
| `PUT`    | `/customers/{id}`          | Päivittää asiakkaan tiedot                                                      |
| `PUT`    | `/customers/{id}/address`  | Päivittää asiakkaan osoitteen                                                   |
| `DELETE` | `/customers/{id}`          | Muuttaa asiakkaan anonyymiksi, eli piilottaa henkilötiedot ja poistaa osoitteen |
| `DELETE` | `/customers/{id}/address`  | Poistaa asiakkaan osoitteen                                                     |

---
####  **GET** **`/customers`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "email": "vsmith1@example.com",
    "first_name": "Dustin",
    "id": 1,
    "last_name": "Carey",
    "phone": "001-789-824-7188x591"
  },
  {
    "email": "nunezmary2@example.net",
    "first_name": "Jeanette",
    "id": 2,
    "last_name": "Hendrix",
    "phone": "(941)966-5615"
  },
  {
    "email": "patrick873@example.org",
    "first_name": "William",
    "id": 3,
    "last_name": "Wilson",
    "phone": "001-788-255-6483"
  },
  {
    "email": "karen885@example.com",
    "first_name": "Ronald",
    "id": 5,
    "last_name": "Collins",
    "phone": "001-402-488-3385"
  }
]
```

####  **GET** **`/customers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "email": "annaho21@example.org",
  "first_name": "Joan",
  "id": 21,
  "last_name": "Perry",
  "phone": "514.295.7841x474"
}
```

####  **GET** **`/customers/email/{email}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus                     |
|--------|--------|----------------------------|
| email  | String | Asiakkaan sähköpostiosoite |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "email": "annaho21@example.org",
  "first_name": "Joan",
  "id": 21,
  "last_name": "Perry",
  "phone": "514.295.7841x474"
}
```

####  **GET** **`/customers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "city": "Josephfort",
  "country": "Guinea",
  "customer": {
    "email": "annaho21@example.org",
    "first_name": "Joan",
    "id": 21,
    "last_name": "Perry",
    "phone": "514.295.7841x474"
  },
  "id": 21,
  "postal_code": "59018",
  "street_address": "57445 Stevenson Corner Apt. 108"
}
```

####  **GET** **`/customers/{id}/orders`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "customer": {
      "email": "alexanderreed8@example.com",
      "first_name": "Matthew",
      "id": 8,
      "last_name": "Torres",
      "phone": "001-853-538-4101"
    },
    "final_pickup_date": "2024-09-30",
    "id": 5,
    "order_date": "2025-03-15T05:12:21",
    "pickup_location": "WebStore OY Stockholm",
    "status": "NEW"
  },
  {
    "delivery_date": "2022-11-23T04:18:35",
    "street_address": "68623 Connor Turnpike Suite 553",
    "postal_code": "06952",
    "city": "Deborahville",
    "country": "Peru",
    "customer": {
      "email": "alexanderreed8@example.com",
      "first_name": "Matthew",
      "id": 8,
      "last_name": "Torres",
      "phone": "001-853-538-4101"
    },
    "id": 18,
    "order_date": "2024-06-10T03:45:51",
    "status": "NEW"
  }
]
```

####  **POST** **`/customers`**

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
"first_name": "Jane",
"last_name": "Doe",
"email": "janedoe@example.com",
"phone": "0201122233"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "email": "janedoe@example.com",
  "first_name": "Jane",
  "id": 51,
  "last_name": "Doe",
  "phone": "0201122233"
}
```

####  **POST** **`/customers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "street_address": "Streetaddress 1A",
  "city": "Cityname",
  "postal_code": "01200",
  "country": "Finland"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "city": "Cityname",
  "country": "Finland",
  "customer": {
    "email": "janedoe@example.com",
    "first_name": "Jane",
    "id": 51,
    "last_name": "Doe",
    "phone": "0201122233"
  },
  "id": 51,
  "postal_code": "01200",
  "street_address": "Streetaddress 1A"
}
```

####  **PUT** **`/customers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "first_name": "Jane",
  "last_name": "Doe",
  "email": "janedoe2@example.com",
  "phone": "0501122233"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "email": "janedoe2@example.com",
  "first_name": "Jane",
  "id": 51,
  "last_name": "Doe",
  "phone": "0501122233"
}
```

####  **PUT** **`/customers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "street_address": "Newstreetaddress 1A",
  "city": "Newcityname",
  "postal_code": "01200",
  "country": "Finland"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "city": "Newcityname",
  "country": "Finland",
  "customer": {
    "email": "janedoe2@example.com",
    "first_name": "Jane",
    "id": 51,
    "last_name": "Doe",
    "phone": "0501122233"
  },
  "id": 51,
  "postal_code": "01200",
  "street_address": "Newstreetaddress 1A"
}
```

####  **DELETE** **`/customers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

"SOFT DELETE: Customer with ID 51 was successfully set anonymous"

####  **DELETE** **`/customers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Asiakkaan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

"Customer Address for Customer with ID 50 was successfully deleted"

---
### Tilaukset (Orders)

| Metodi   | Päätepiste                              | Kuvaus                             |
|----------|-----------------------------------------|------------------------------------|
| `GET`    | `/orders`                               | Hakee kaikki tilaukset             |
| `GET`    | `/orders/pickup`                        | Hakee kaikki noutotilaukset        |
| `GET`    | `/orders/delivery`                      | Hakee kaikki toimitustilaukset     |
| `GET`    | `/orders/{id}`                          | Hakee tilauksen ID:n perusteella   |
| `GET`    | `/orders/{id]/items`                    | Hakee tilauksen tuotteet           |
| `POST`   | `/orders/pickup`                        | Lisää uuden noutotilauksen         |
| `POST`   | `/orders/delivery`                      | Lisää uuden toimitustilauksen      |
| `POST`   | `/orders/{id}/items/add`                | Lisää uuden tuotteen tilaukseen    |
| `PUT`    | `/orders/pickup/{id}`                   | Päivittää noutotilauksen tiedot    |
| `PUT`    | `/orders/delivery/{id}`                 | Päivittää toimitustilauksen tiedot |
| `DELETE` | `/orders/{id}/items/remove/{productId}` | Poistaa tuotteen tilauksesta       |
---

####  **GET** **`/orders`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "customer": {
      "email": "ejones9@example.net",
      "first_name": "Mary",
      "id": 9,
      "last_name": "Proctor",
      "phone": "(414)630-3080x76624"
    },
    "final_pickup_date": "2023-06-05",
    "id": 14,
    "order_date": "2024-07-08T11:53:45",
    "pickup_location": "WebStore OY Tokyo",
    "status": "RETRIEVED"
  },
  {
    "customer": {
      "email": "bryanmorris33@example.com",
      "first_name": "Amy",
      "id": 33,
      "last_name": "Hawkins",
      "phone": "907.782.4059"
    },
    "final_pickup_date": "2024-10-09",
    "id": 15,
    "order_date": "2024-10-19T20:36:45",
    "pickup_location": "WebStore OY Vantaa",
    "status": "NEW"
  },
  {
    "delivery_date": "2023-07-14T09:24:17",
    "street_address": "8845 King Mount",
    "postal_code": "41053",
    "city": "Rickyville",
    "country": "Vietnam",
    "customer": {
      "email": "solisjohn15@example.net",
      "first_name": "Christina",
      "id": 15,
      "last_name": "Young",
      "phone": "807-999-7446x417"
    },
    "id": 16,
    "order_date": "2025-10-19T08:04:23",
    "status": "NEW"
  }
]
```

####  **GET** **`/orders/pickup`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "customer": {
      "email": "tyler5423@example.com",
      "first_name": "Nathan",
      "id": 23,
      "last_name": "Rivera",
      "phone": "001-764-469-6295x1564"
    },
    "final_pickup_date": "2024-07-26",
    "id": 6,
    "order_date": "2024-07-12T08:15:54",
    "pickup_location": "WebStore OY Vantaa",
    "status": "CANCELLED"
  },
  {
    "customer": {
      "email": "patricia2241@example.org",
      "first_name": "Brittany",
      "id": 41,
      "last_name": "Hubbard",
      "phone": "+1-392-890-1611"
    },
    "final_pickup_date": "2025-01-27",
    "id": 7,
    "order_date": "2024-07-19T06:43:17",
    "pickup_location": "WebStore OY Stockholm",
    "status": "NEW"
  },
  {
    "customer": {
      "email": "solisjohn15@example.net",
      "first_name": "Christina",
      "id": 15,
      "last_name": "Young",
      "phone": "807-999-7446x417"
    },
    "final_pickup_date": "2024-07-24",
    "id": 8,
    "order_date": "2024-07-10T16:10:17",
    "pickup_location": "WebStore OY Vantaa",
    "status": "NEW"
  }
]
```

####  **GET** **`/orders/delivery`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "delivery_date": "2025-11-14T09:24:17",
    "street_address": "8845 King Mount",
    "postal_code": "41053",
    "city": "Rickyville",
    "country": "Vietnam",
    "customer": {
      "email": "solisjohn15@example.net",
      "first_name": "Christina",
      "id": 15,
      "last_name": "Young",
      "phone": "807-999-7446x417"
    },
    "id": 16,
    "order_date": "2025-10-19T08:04:23",
    "status": "NEW"
  },
  {
    "delivery_date": null,
    "street_address": "21169 Bryan Avenue",
    "postal_code": "94651",
    "city": "Port Stevenfort",
    "country": "Bermuda",
    "customer": {
      "email": "awilliams11@example.org",
      "first_name": "Sandra",
      "id": 11,
      "last_name": "Rodriguez",
      "phone": "+1-719-491-6695x5048"
    },
    "id": 17,
    "order_date": "2024-04-25T18:42:59",
    "status": "CANCELLED"
  },
  {
    "delivery_date": "2024-11-23T04:18:35",
    "street_address": "68623 Connor Turnpike Suite 553",
    "postal_code": "06952",
    "city": "Deborahville",
    "country": "Peru",
    "customer": {
      "email": "alexanderreed8@example.com",
      "first_name": "Matthew",
      "id": 8,
      "last_name": "Torres",
      "phone": "001-853-538-4101"
    },
    "id": 18,
    "order_date": "2024-06-10T03:45:51",
    "status": "NEW"
  }
]
```

####  **GET** **`/orders/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Tilauksen ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "delivery_date": "2023-01-07T21:06:44",
  "street_address": "073 Daniel Square Suite 059",
  "postal_code": "71498",
  "city": "South Walterview",
  "country": "Liechtenstein",
  "customer": {
    "email": "blevinskevin43@example.com",
    "first_name": "Connie",
    "id": 43,
    "last_name": "Lewis",
    "phone": "(531)850-0145x9660"
  },
  "id": 20,
  "order_date": "2024-12-06T02:58:44",
  "status": "NEW"
}
```

####  **GET** **`/orders/{id}/items`**

Parametrit

| Kenttä | Tyyppi | Kuvaus       |
|--------|--------|--------------|
| id     | Number | Tilauksen ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "orderItemsViewId": {
      "productId": 40,
      "orderId": 9
    },
    "customerId": 10,
    "product_name": "Swift Planet 313",
    "quantity": 3,
    "unit_price": 430.15,
    "total": 1290.45
  },
  {
    "orderItemsViewId": {
      "productId": 42,
      "orderId": 9
    },
    "customerId": 10,
    "product_name": "Eco Board 659",
    "quantity": 1,
    "unit_price": 308.89,
    "total": 308.89
  },
  {
    "orderItemsViewId": {
      "productId": 78,
      "orderId": 9
    },
    "customerId": 10,
    "product_name": "Happy Plug 714",
    "quantity": 5,
    "unit_price": 578.4,
    "total": 2892
  }
]
```
####  **POST** **`/orders/pickup`**

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "pickup_location": "Webstore OY Vantaa",
  "customer": {
    "id": 2
  },
  "items": [
    {"product_id": 6, "quantity": 4},
    {"product_id": 23, "quantity": 1}
  ]
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "customer": {
    "email": null,
    "first_name": null,
    "id": 2,
    "last_name": null,
    "phone": null
  },
  "final_pickup_date": "2026-10-28",
  "pickup_location": "Webstore OY Vantaa",
  "items": [
    {
      "product_id": 6,
      "quantity": 4
    },
    {
      "product_id": 23,
      "quantity": 1
    }
  ]
}
```
####  **POST** **`/orders/delivery`**

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "street_address": "Streetname 1B",
  "country": "Cityname",
  "postal_code": "11111",
  "city": "Country",
  "customer" : {
    "id" : 1
  },
  "items": [
    {"product_id": 4, "quantity": 3},
    {"product_id": 10, "quantity": 2}
  ]
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "customer": {
    "email": null,
    "first_name": null,
    "id": 1,
    "last_name": null,
    "phone": null
  },
  "street_address": "Streetname 1B",
  "postal_code": "11111",
  "city": "Country",
  "country": "Cityname",
  "items": [
    {
      "product_id": 4,
      "quantity": 3
    },
    {
      "product_id": 10,
      "quantity": 2
    }
  ]
}
```

####  **POST** **`/orders/{id}/items/add`**

Parametrit

| Kenttä    | Tyyppi | Kuvaus       |
|-----------|--------|--------------|
| id        | Number | Tilauksen ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "product_id": 1,
  "quantity": 9
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "order_id": 20,
  "product_id": 1,
  "product_name": "Super Bug 360",
  "quantity": 9,
  "unit_price": 22.22
}
```

####  **PUT** **`/orders/delivery/{id}`**

Parametrit

| Kenttä    | Tyyppi | Kuvaus       |
|-----------|--------|--------------|
| id        | Number | Tilauksen ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "street_address": "Streetname 1B",
  "country": "Cityname",
  "postal_code": "11111",
  "city": "Country",
  "delivery_date": "2026-10-12T17:45:00",
  "status": "DELIVERED"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "delivery_date": "2026-10-12T17:45:00",
  "street_address": "Streetname 1B",
  "postal_code": "11111",
  "city": "Country",
  "country": "Cityname",
  "customer": {
    "email": "rjarvis7@example.net",
    "first_name": "Alexa",
    "id": 7,
    "last_name": "Wallace",
    "phone": "(359)458-7733x38686"
  },
  "id": 39,
  "order_date": "2023-11-23T08:49:24",
  "status": "DELIVERED"
}
```

####  **PUT** **`/orders/pickup/{id}`**

Parametrit

| Kenttä    | Tyyppi | Kuvaus       |
|-----------|--------|--------------|
| id        | Number | Tilauksen ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "final_pickup_date": "2026-10-30",
  "pickup_location": "Webstore OY Helsinki",
  "status": "RETRIEVED"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "customer": {
    "email": "nunezmary2@example.net",
    "first_name": "Jeanette",
    "id": 2,
    "last_name": "Hendrix",
    "phone": "(941)966-5615"
  },
  "final_pickup_date": "2026-10-30",
  "id": 45,
  "order_date": "2026-10-14T17:33:11",
  "pickup_location": "Webstore OY Helsinki",
  "status": "RETRIEVED"
}
```

####  **DELETE** **`/orders/{id}/items/remove/{productId}`**

Parametrit

| Kenttä    | Tyyppi | Kuvaus       |
|-----------|--------|--------------|
| id        | Number | Tilauksen ID |
| productId | Number | Tuotteen ID  |

Onnistunut pyyntö (200) - Esimerkkivastaus:

"Product with ID 1 was successfully removed from order with ID 20"

---
### Tuotteet (Products)

| Metodi   | Päätepiste                           | Kuvaus                                                                                      |
|----------|--------------------------------------|---------------------------------------------------------------------------------------------|
| `GET`    | `/products`                          | Hakee kaikki tuotteet                                                                       |
| `GET`    | `/products/{id}`                     | Hakee tuotteen ID:n perusteella                                                             |
| `GET`    | `/products/search/{key}`             | Hakee kaikki tuotteet joiden nimessä tai kuvauksessa esiintyy annettu hakusana              |
| `GET`    | `/products/inactive`                 | Hakee valikoimasta poistetut tuotteet                                                       |
| `GET`    | `/products/lowStock/{max}`           | Hakee kaikkai tuotteen joiden kappalemäärä on yhtäsuuri tai pienempi kuin annettu lukumäärä |
| `POST`   | `/products`                          | Lisää uuden tuotteen                                                                        |
| `PUT`    | `/products/{id}`                     | Päivittää tuotteen tiedot                                                                   |
| `PUT`    | `/products/price/increase/{percent}` | Nostaa kaikkien tuotteiden hintoja annetulla prosenttimäärällä                              |
| `DELETE` | `/products/{id}`                     | Arkistoi tuotteen, eli päivittää sen arkistoiduksi                                          |
---

####  **GET** **`/products`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "active": true,
    "category": {
      "description": "Kodin ja arjen tarvikkeet, kodintekstiilit",
      "id": 2,
      "name": "Kodin tarvikkeet"
    },
    "description": "Her fall move current him.",
    "id": 1,
    "name": "Super Bug 360",
    "price": 24.44,
    "stock_quantity": 127,
    "supplier": {
      "contact_name": "Mats Karlsson",
      "email": "mats.karlsson@alandiapharma.ax",
      "id": 35,
      "name": "Alandia Pharma",
      "phone": "0458542541"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Urheiluvälineet, pelit ja vapaa-ajan tuotteet",
      "id": 10,
      "name": "Urheilu & vapaa-aika"
    },
    "description": "Score million throw thing instead ball line think.",
    "id": 2,
    "name": "Happy Pack 382",
    "price": 600.69,
    "stock_quantity": 408,
    "supplier": {
      "contact_name": "Raimo Tuuri",
      "email": "raimo.tuuri@teollisuusliike.fi",
      "id": 16,
      "name": "Teollisuusliike Oy",
      "phone": "0453452214"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Vaatteet ja asusteet aikuisille ja lapsille",
      "id": 3,
      "name": "Vaatteet"
    },
    "description": "Ask decide need next very capital.",
    "id": 3,
    "name": "Quantum Widget 457",
    "price": 934.63,
    "stock_quantity": 313,
    "supplier": {
      "contact_name": "Jens Nilsson",
      "email": "jens.nilsson@scanbrew.se",
      "id": 17,
      "name": "ScanBrew AB",
      "phone": "0408119456"
    }
  }
]
```

####  **GET** **`/products/{id}`**

Parametrit

| Kenttä    | Tyyppi | Kuvaus      |
|-----------|--------|-------------|
| id        | Number | Tuotteen ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "active": true,
  "category": {
    "description": "Urheiluvälineet, pelit ja vapaa-ajan tuotteet",
    "id": 10,
    "name": "Urheilu & vapaa-aika"
  },
  "description": "Notice show send responsibility.",
  "id": 67,
  "name": "Lucky Blade 598",
  "price": 1030.46,
  "stock_quantity": 371,
  "supplier": {
    "contact_name": "Steve Thompson",
    "email": "steve.thompson@windycityparts.us",
    "id": 61,
    "name": "Windy City Parts",
    "phone": "001312774512"
  }
}
```

####  **GET** **`/products/search/{key}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus               |
|--------|--------|----------------------|
| key    | String | Hakusana/-merkkijono |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "active": true,
    "category": {
      "description": "Sähkölaitteet, komponentit ja elektroniikkatuotteet",
      "id": 1,
      "name": "Elektroniikka"
    },
    "description": "Fast through glass tree five full eight bar court.",
    "id": 103,
    "name": "Zippy Key 687",
    "price": 988.96,
    "stock_quantity": 48,
    "supplier": {
      "contact_name": "Olga Belova",
      "email": "olga.belova@easternchem.ru",
      "id": 12,
      "name": "Eastern Chemicals",
      "phone": "0509998765"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Urheiluvälineet, pelit ja vapaa-ajan tuotteet",
      "id": 10,
      "name": "Urheilu & vapaa-aika"
    },
    "description": "Total system write consider heart glass.",
    "id": 107,
    "name": "Cool Kit 315",
    "price": 907.98,
    "stock_quantity": 45,
    "supplier": {
      "contact_name": "Raimo Tuuri",
      "email": "raimo.tuuri@teollisuusliike.fi",
      "id": 16,
      "name": "Teollisuusliike Oy",
      "phone": "0453452214"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Puutarhan ja pihan tuotteet sekä välineet",
      "id": 8,
      "name": "Puutarha & piha"
    },
    "description": "Really car reduce response laugh relationship feel address miss glass.",
    "id": 149,
    "name": "Wave Bug 811",
    "price": 615.89,
    "stock_quantity": 60,
    "supplier": {
      "contact_name": "Mikkel Sørensen",
      "email": "mikkel.sorensen@openscandinavia.dk",
      "id": 71,
      "name": "Open Scandinavia",
      "phone": "0452348899"
    }
  }
]
```

####  **GET** **`/products/inactive`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "active": false,
    "category": {
      "description": "Urheiluvälineet, pelit ja vapaa-ajan tuotteet",
      "id": 10,
      "name": "Urheilu & vapaa-aika"
    },
    "description": "Begin thus begin consider between better someone use.",
    "id": 45,
    "name": "Ultra Bottle 524",
    "price": 311.65,
    "stock_quantity": 11,
    "supplier": {
      "contact_name": "Anna Fernanda López",
      "email": "anna.lopez@publiprint.es",
      "id": 36,
      "name": "PubliPrint",
      "phone": "0521122334"
    }
  },
  {
    "active": false,
    "category": {
      "description": "Käsityökalut, sähkökoneet ja remointitarvikkeet",
      "id": 5,
      "name": "Työkalut"
    },
    "description": "Remember boy approach camera fund whether such.",
    "id": 77,
    "name": "Spark Carrier 878",
    "price": 500.32,
    "stock_quantity": 120,
    "supplier": {
      "contact_name": "Henrik Rossi",
      "email": "henrik.rossi@veistopartners.fi",
      "id": 40,
      "name": "Veisto Partners",
      "phone": "0412200099"
    }
  }
]
```

####  **GET** **`/products/lowStock/{max}`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "active": true,
    "category": {
      "description": "Vaatteet ja asusteet aikuisille ja lapsille",
      "id": 3,
      "name": "Vaatteet"
    },
    "description": "Event stuff forget increase site ahead part particular miss.",
    "id": 902,
    "name": "Flex Shell 334",
    "price": 55.77,
    "stock_quantity": 0,
    "supplier": {
      "contact_name": "Toomas Mill",
      "email": "toomas.mill@tallinnakaubad.ee",
      "id": 78,
      "name": "Tallinna Kaubad",
      "phone": "0448704543"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Puutarhan ja pihan tuotteet sekä välineet",
      "id": 8,
      "name": "Puutarha & piha"
    },
    "description": "Single him threat rate.",
    "id": 260,
    "name": "Nifty Mat 756",
    "price": 547.11,
    "stock_quantity": 1,
    "supplier": {
      "contact_name": "Pekka Helminen",
      "email": "pekka.helminen@hameenpaperi.fi",
      "id": 59,
      "name": "Hämeen Paperi",
      "phone": "0448765432"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Sähkölaitteet, komponentit ja elektroniikkatuotteet",
      "id": 1,
      "name": "Elektroniikka"
    },
    "description": "Bill language clear material room forget.",
    "id": 702,
    "name": "Green Station 670",
    "price": 136.38,
    "stock_quantity": 1,
    "supplier": {
      "contact_name": "Hiroshi Tanaka",
      "email": "hiroshi.tanaka@nipponsupplies.jp",
      "id": 18,
      "name": "Nippon Supplies",
      "phone": "0708462179"
    }
  }
]
```

####  **POST** **`/products`**

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "name": "New Product",
  "description": "This is a description",
  "stock_quantity": 80,
  "price": 332.44,
  "active": true,
  "category": {
    "id": 10
  },
  "supplier": {
    "id": 60
  }
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "active": true,
  "category": {
    "description": "Urheiluvälineet, pelit ja vapaa-ajan tuotteet",
    "id": 10,
    "name": "Urheilu & vapaa-aika"
  },
  "description": "This is a description",
  "id": 1003,
  "name": "New Product",
  "price": 332.44,
  "stock_quantity": 80,
  "supplier": {
    "contact_name": "Sanne de Vries",
    "email": "sanne.devries@sunbeauty.nl",
    "id": 60,
    "name": "SunBeauty NL",
    "phone": "0312424242"
  }
}
```

####  **PUT** **`/products/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus      |
|--------|--------|-------------|
| id     | Number | Tuotteen ID |


Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "name": "New Product",
  "description": "This is an updated description",
  "stock_quantity": 40,
  "price": 340.44,
  "active": false,
  "category": {
    "id": 9
  },
  "supplier": {
    "id": 60
  }
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "active": false,
  "category": {
    "description": "Kirjat, paperitarvikkeet ja toimistotuotteet",
    "id": 9,
    "name": "Kirjat & toimisto"
  },
  "description": "This is an updated description",
  "id": 45,
  "name": "New Product",
  "price": 340.44,
  "stock_quantity": 40,
  "supplier": {
    "contact_name": "Sanne de Vries",
    "email": "sanne.devries@sunbeauty.nl",
    "id": 60,
    "name": "SunBeauty NL",
    "phone": "0312424242"
  }
}
```

####  **PUT** **`/products/price/increase/{percent}`**

Parametrit

| Kenttä      | Tyyppi   | Kuvaus                          |
|-------------|----------|---------------------------------|
| percent     | Number   | Hinnankorotuksen prosenttimäärä |


Onnistunut pyyntö (200) - Esimerkkivastaus:

"Price increased by 5.0% for 1003 products."


####  **DELETE** **`/products/{id}`**

Parametrit

| Kenttä | Tyyppi   | Kuvaus      |
|--------|----------|-------------|
| id     | Number   | Tuotteen ID |


Onnistunut pyyntö (200) - Esimerkkivastaus:

"SOFT DELETE: Product with ID 1003 was successfully set inactive"

---
### Kategoriat (Categories)

| Metodi   | Päätepiste                        | Kuvaus                                  |
|----------|-----------------------------------|-----------------------------------------|
| `GET`    | `/categories`                     | Hakee kaikki tuotekategoriat            |
| `GET`    | `/categories/{id}`                | Hakee tuotekategorian ID:n perusteella  |
| `GET`    | `/categories/{id}/products`       | Hakee kaikki tuotteet tuotekategoriassa |
| `POST`   | `/categories`                     | Lisää uuden tuotekategorian             |
| `PUT`    | `/categories/{id}`                | Päivittää tuotekategorian tiedot        |
| `DELETE` | `/categories/{id}`                | Poistaa tuotekategorian                 |
---

####  **GET** **`/categories`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "description": "Sähkölaitteet, komponentit ja elektroniikkatuotteet",
    "id": 1,
    "name": "Elektroniikka"
  },
  {
    "description": "Kodin ja arjen tarvikkeet, kodintekstiilit",
    "id": 2,
    "name": "Kodin tarvikkeet"
  },
  {
    "description": "Vaatteet ja asusteet aikuisille ja lapsille",
    "id": 3,
    "name": "Vaatteet"
  },
  {
    "description": "Ruoka-aineet ja elintarvikkeet",
    "id": 4,
    "name": "Elintarvikkeet"
  },
  {
    "description": "Käsityökalut, sähkökoneet ja remointitarvikkeet",
    "id": 5,
    "name": "Työkalut"
  },
  {
    "description": "Matkapuhelimet, kuulokkeet ja mobiilitarvikkeet",
    "id": 6,
    "name": "Puhelimet & lisävarusteet"
  },
  {
    "description": "Sisustustuotteet ja huonekalut koteihin ja toimistoihin",
    "id": 7,
    "name": "Huonekalut"
  },
  {
    "description": "Puutarhan ja pihan tuotteet sekä välineet",
    "id": 8,
    "name": "Puutarha & piha"
  },
  {
    "description": "Kirjat, paperitarvikkeet ja toimistotuotteet",
    "id": 9,
    "name": "Kirjat & toimisto"
  },
  {
    "description": "Urheiluvälineet, pelit ja vapaa-ajan tuotteet",
    "id": 10,
    "name": "Urheilu & vapaa-aika"
  }
]
```

####  **GET** **`/categories/{id}`**

Parametrit

| Kenttä | Tyyppi   | Kuvaus        |
|--------|----------|---------------|
| id     | Number   | Kategorian ID |


Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "description": "Ruoka-aineet ja elintarvikkeet",
  "id": 4,
  "name": "Elintarvikkeet"
}
```

####  **GET** **`/categories/{id}/products`**

Parametrit

| Kenttä | Tyyppi   | Kuvaus        |
|--------|----------|---------------|
| id     | Number   | Kategorian ID |


Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "active": true,
    "category": {
      "description": "Ruoka-aineet ja elintarvikkeet",
      "id": 4,
      "name": "Elintarvikkeet"
    },
    "description": "Man space low indicate next throw happen per evening.",
    "id": 16,
    "name": "Amazing Lamp 880",
    "price": 979.36,
    "stock_quantity": 500,
    "supplier": {
      "contact_name": "Logan Clark",
      "email": "logan.clark@arcticsport.ca",
      "id": 41,
      "name": "Arctic Sport Inc",
      "phone": "0456677889"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Ruoka-aineet ja elintarvikkeet",
      "id": 4,
      "name": "Elintarvikkeet"
    },
    "description": "Soldier by so commercial name there idea make.",
    "id": 36,
    "name": "Bold Planet 415",
    "price": 837.45,
    "stock_quantity": 448,
    "supplier": {
      "contact_name": "Carlos Silva",
      "email": "carlos.silva@madeiradecor.pt",
      "id": 58,
      "name": "MadeiraDecor",
      "phone": "0512345678"
    }
  },
  {
    "active": true,
    "category": {
      "description": "Ruoka-aineet ja elintarvikkeet",
      "id": 4,
      "name": "Elintarvikkeet"
    },
    "description": "Effect campaign military whatever effort gas past early range eye throw.",
    "id": 56,
    "name": "Green Doodle 484",
    "price": 237.57,
    "stock_quantity": 56,
    "supplier": {
      "contact_name": "Santiago Costa",
      "email": "santiago.costa@atlanticsails.pt",
      "id": 34,
      "name": "Atlantic Sails",
      "phone": "0511122334"
    }
  }
]
```

####  **POST** **`/categories`**

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "name": "Category name",
  "description": "This is the description for this category"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "description": "This is the description for this category",
  "id": 11,
  "name": "Category name"
}
```

####  **PUT** **`/categories/{id}`**

Parametrit

| Kenttä | Tyyppi   | Kuvaus        |
|--------|----------|---------------|
| id     | Number   | Kategorian ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "name": "Updated Category name",
  "description": "This is an updated description for this category"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "description": "This is an updated description for this category",
  "id": 11,
  "name": "Updated Category name"
}
```

####  **DELETE** **`/categories/{id}`**

Parametrit

| Kenttä | Tyyppi   | Kuvaus        |
|--------|----------|---------------|
| id     | Number   | Kategorian ID |


Onnistunut pyyntö (200) - Esimerkkivastaus:

"Category with ID 11 was successfully deleted"


---

### Toimittajat (Suppliers)

| Metodi   | Päätepiste                 | Kuvaus                                                                          |
|----------|----------------------------|---------------------------------------------------------------------------------|
| `GET`    | `/suppliers`               | Hakee kaikki tavarantoimittajat                                                 |
| `GET`    | `/suppliers/address`       | Hakee kaikkien tavarantoimittajien osoitteet                                    |
| `GET`    | `/suppliers/{id}`          | Hakee tavarantoimittajan ID:n perusteella                                       |
| `GET`    | `/suppliers/email/{email}` | Hakee tavarantoimittajan sähköpostiosoitteen perusteella                        |
| `GET`    | `/suppliers/{id}/address`  | Hakee tavarantoimittajan osoitteen                                              |
| `POST`   | `/suppliers`               | Lisää uuden asiakkaan                                                           |
| `POST`   | `/suppliers/{id}/address`  | Lisää asiakkaalle uuden osoitteen                                               |
| `PUT`    | `/suppliers/{id}`          | Päivittää asiakkaan tiedot                                                      |
| `PUT`    | `/suppliers/{id}/address`  | Päivittää asiakkaan osoitteen                                                   |
| `DELETE` | `/suppliers/{id}`          | Muuttaa asiakkaan anonyymiksi, eli piilottaa henkilötiedot ja poistaa osoitteen |
| `DELETE` | `/suppliers/{id}/address`  | Poistaa asiakkaan osoitteen                                                     |

---

####  **GET** **`/suppliers`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "contact_name": "Mia Manninen",
    "email": "mia.manninen@polarelec.fi",
    "id": 1,
    "name": "Polar Electronics Oy",
    "phone": "0401001000"
  },
  {
    "contact_name": "Samira Morgan",
    "email": "samira.morgan@ecotech.com",
    "id": 2,
    "name": "EcoTech Ltd",
    "phone": "0507236312"
  },
  {
    "contact_name": "Joni Mylläri",
    "email": "joni.myllari@viherpalvelut.fi",
    "id": 3,
    "name": "Suomen Viherpalvelut",
    "phone": "0415317895"
  },
  {
    "contact_name": "Lars Johansen",
    "email": "lars.johansen@nordicsteel.se",
    "id": 4,
    "name": "Nordic Steel AB",
    "phone": "0405668899"
  },
  {
    "contact_name": "Ling Chen",
    "email": "ling.chen@globalparts.cn",
    "id": 5,
    "name": "Global Parts Asia",
    "phone": "0800555366"
  }
]
```

####  **GET** **`/suppliers/address`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "supplier": {
      "contact_name": "Mia Manninen",
      "email": "mia.manninen@polarelec.fi",
      "id": 1,
      "name": "Polar Electronics Oy",
      "phone": "0401001000"
    },
    "street_address": "Kairatie 5",
    "postal_code": "96400",
    "city": "Rovaniemi",
    "country": "Suomi",
    "id": 1
  },
  {
    "supplier": {
      "contact_name": "Samira Morgan",
      "email": "samira.morgan@ecotech.com",
      "id": 2,
      "name": "EcoTech Ltd",
      "phone": "0507236312"
    },
    "street_address": "Greenway Ave 22",
    "postal_code": "E15 4PH",
    "city": "London",
    "country": "United Kingdom",
    "id": 2
  },
  {
    "supplier": {
      "contact_name": "Joni Mylläri",
      "email": "joni.myllari@viherpalvelut.fi",
      "id": 3,
      "name": "Suomen Viherpalvelut",
      "phone": "0415317895"
    },
    "street_address": "Lehmustie 10",
    "postal_code": "80100",
    "city": "Joensuu",
    "country": "Suomi",
    "id": 3
  },
  {
    "supplier": {
      "contact_name": "Lars Johansen",
      "email": "lars.johansen@nordicsteel.se",
      "id": 4,
      "name": "Nordic Steel AB",
      "phone": "0405668899"
    },
    "street_address": "Storgatan 1",
    "postal_code": "211 41",
    "city": "Malmö",
    "country": "Sverige",
    "id": 4
  },
  {
    "supplier": {
      "contact_name": "Ling Chen",
      "email": "ling.chen@globalparts.cn",
      "id": 5,
      "name": "Global Parts Asia",
      "phone": "0800555366"
    },
    "street_address": "Qingdao Road 101",
    "postal_code": "266000",
    "city": "Qingdao",
    "country": "China",
    "id": 5
  }
]
```

####  **GET** **`/suppliers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "contact_name": "Mats Karlsson",
  "email": "mats.karlsson@alandiapharma.ax",
  "id": 35,
  "name": "Alandia Pharma",
  "phone": "0458542541"
}
```

####  **GET** **`/suppliers/email/{email}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus                       |
|--------|--------|------------------------------|
| email  | String | Toimittajan sähköpostiosoite |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "contact_name": "Mats Karlsson",
  "email": "mats.karlsson@alandiapharma.ax",
  "id": 35,
  "name": "Alandia Pharma",
  "phone": "0458542541"
}
```

####  **GET** **`/suppliers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "supplier": {
    "contact_name": "Mats Karlsson",
    "email": "mats.karlsson@alandiapharma.ax",
    "id": 35,
    "name": "Alandia Pharma",
    "phone": "0458542541"
  },
  "street_address": "Comercial 89",
  "postal_code": "03901",
  "city": "Mexico City",
  "country": "Mexico",
  "id": 35
}
```

####  **POST** **`/suppliers`**

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "name": "Suppliers Oy",
  "email": "suppiersoy@email.com",
  "phone": "02013456789",
  "contact_name": "Susan Supplier"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "contact_name": "Susan Supplier",
  "email": "suppiersoy@email.com",
  "id": 101,
  "name": "Suppliers Oy",
  "phone": "02013456789"
}
```

####  **POST** **`/suppliers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "street_address": "Streetname 1C",
  "city": "Cityname",
  "postal_code": "13131",
  "country": "Country"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "supplier": {
    "contact_name": "Susan Supplier",
    "email": "newsuppiersoy@email.com",
    "id": 101,
    "name": "New Suppliers Oy",
    "phone": "02013456789"
  },
  "street_address": "Streetname 1C",
  "postal_code": "13131",
  "city": "Cityname",
  "country": "Country",
  "id": 101
}
```

####  **PUT** **`/suppliers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "name": "New Suppliers Oy",
  "email": "newsuppiersoy@email.com",
  "phone": "02013456789",
  "contact_name": "Susan Supplier"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "contact_name": "Susan Supplier",
  "email": "newsuppiersoy@email.com",
  "id": 101,
  "name": "New Suppliers Oy",
  "phone": "02013456789"
}
```

####  **PUT** **`/suppliers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Pyynnön sisältö (RequestBody) - Esimerkki:

```json
{
  "street_address": "Newstreetname 1C",
  "city": "Newcityname",
  "postal_code": "13111",
  "country": "Country"
}
```

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "supplier": {
    "contact_name": "Susan Supplier",
    "email": "newsuppiersoy@email.com",
    "id": 101,
    "name": "New Suppliers Oy",
    "phone": "02013456789"
  },
  "street_address": "Newstreetname 1C",
  "postal_code": "13111",
  "city": "Newcityname",
  "country": "Country",
  "id": 101
}
```

####  **DELETE** **`/suppliers/{id}`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

"Supplier with ID 35 was successfully deleted"

####  **DELETE** **`/suppliers/{id}/address`**

Parametrit

| Kenttä | Tyyppi | Kuvaus         |
|--------|--------|----------------|
| id     | Number | Toimittajan ID |

Onnistunut pyyntö (200) - Esimerkkivastaus:

"Supplier Address for Supplier with ID 36 was successfully deleted"

---

### Raportit (Reports)

| Metodi   | Päätepiste                         | Kuvaus                                                                                                                                                  |
|----------|------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|
| `GET`    | `/reports/country/sales`           | Hakee raportin, joka näyttää toimitusten lukumäärät ja tuotot maakohtaisesti                                                                            |
| `GET`    | `/reports/country/{country}/sales` | Hakee raportin, joka näyttää toimitusten lukumäärät ja tuotot annetulle maalle                                                                          |
| `GET`    | `/reports/dilysales`               | Hakee raportin, joka näyttää päivittäiset tuotot                                                                                                        |
| `GET`    | `/reports/dilysales`               | Hakee raportin, joka näyttää päivittäiset tuotot                                                                                                        |
| `GET`    | `/stock/suppliers`                 | Hakee raportin, jossa näkyy tuotteiden varastotiedot ja niiden toimittajien tiedot                                                                      |
| `GET`    | `/stock/suppliers/{supplierId}`    | Hakee raportin, jossa näkyy annetun toimittajan tiedot ja heidän tuotteidensa varastotiedot                                                             |
| `GET`    | `/stock/{threshold}/suppliers`     | Hakee raportin, jossa näkyy niiden tuotteiden, <br/> joita on varastossa yhtäpaljon tai alle annettu määrä, varastotiedot ja niiden toimittajien tiedot |

---

####  **GET** **`/reports/country/sales`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "country": "South Africa",
    "sales": 14328.5,
    "delivery_orders": 1
  },
  {
    "country": "Equatorial Guinea",
    "sales": 11423.83,
    "delivery_orders": 1
  },
  {
    "country": "Liechtenstein",
    "sales": 10955.97,
    "delivery_orders": 1
  },
  {
    "country": "Cayman Islands",
    "sales": 10498.85,
    "delivery_orders": 1
  },
  {
    "country": "Cuba",
    "sales": 9203.53,
    "delivery_orders": 1
  },
  {
    "country": "Zimbabwe",
    "sales": 9190.28,
    "delivery_orders": 2
  },
  {
    "country": "Tanzania",
    "sales": 9108.08,
    "delivery_orders": 1
  }
]
```

####  **GET** **`/reports/country/{country}/sales`**

Parametrit

| Kenttä  | Tyyppi | Kuvaus            |
|---------|--------|-------------------|
| country | String | Maan/valtion nimi |


Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
{
  "country": "sweden",
  "sales": 6946.47,
  "delivery_orders": 1
}
```

####  **GET** **`/reports/dailysales`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "id": 1,
    "report_date": "2026-10-12",
    "orders_total": 12,
    "sales_total": 12570.57
  },
  {
    "id": 2,
    "report_date": "2026-10-13",
    "orders_total": 16,
    "sales_total": 16349.21
  },
  {
    "id": 3,
    "report_date": "2026-10-14",
    "orders_total": 8,
    "sales_total": 8764.93
  }
]
```

####  **GET** **`/reports/stock/suppliers`**

Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "product_id": 111,
    "product_name": "Nifty Kit 939",
    "stock_quantity": 237,
    "supplier_id": 1,
    "supplier": "Polar Electronics Oy",
    "contact_name": "Mia Manninen",
    "phone": "0401001000",
    "email": "mia.manninen@polarelec.fi",
    "street_address": "Kairatie 5",
    "city": "Rovaniemi",
    "country": "Suomi"
  },
  {
    "product_id": 668,
    "product_name": "Magic Bit 144",
    "stock_quantity": 180,
    "supplier_id": 2,
    "supplier": "EcoTech Ltd",
    "contact_name": "Samira Morgan",
    "phone": "0507236312",
    "email": "samira.morgan@ecotech.com",
    "street_address": "Greenway Ave 22",
    "city": "London",
    "country": "United Kingdom"
  },
  {
    "product_id": 182,
    "product_name": "Solar Plug 493",
    "stock_quantity": 304,
    "supplier_id": 4,
    "supplier": "Nordic Steel AB",
    "contact_name": "Lars Johansen",
    "phone": "0405668899",
    "email": "lars.johansen@nordicsteel.se",
    "street_address": "Storgatan 1",
    "city": "Malmö",
    "country": "Sverige"
  },
  {
    "product_id": 326,
    "product_name": "Atomic Gadget 689",
    "stock_quantity": 425,
    "supplier_id": 6,
    "supplier": "Lapland Crafts Ltd",
    "contact_name": "Sanni Jokela",
    "phone": "0502101234",
    "email": "sanni.jokela@laplandcrafts.fi",
    "street_address": "Lapintie 8",
    "city": "Rovaniemi",
    "country": "Suomi"
  }
]
```

####  **GET** **`/reports/stock/suppliers/{supplierId}`**

Parametrit

| Kenttä     | Tyyppi | Kuvaus         |
|------------|--------|----------------|
| supplierId | Number | Toimittajan ID |


Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "product_id": 386,
    "product_name": "Nimble Bit 743",
    "stock_quantity": 326,
    "supplier_id": 15,
    "supplier": "GadgetZone",
    "contact_name": "Jessica Turner",
    "phone": "0415947221",
    "email": "jessica.turner@gadgetzone.us",
    "street_address": "Broadway 250",
    "city": "New York",
    "country": "USA"
  },
  {
    "product_id": 578,
    "product_name": "Super Clip 551",
    "stock_quantity": 23,
    "supplier_id": 15,
    "supplier": "GadgetZone",
    "contact_name": "Jessica Turner",
    "phone": "0415947221",
    "email": "jessica.turner@gadgetzone.us",
    "street_address": "Broadway 250",
    "city": "New York",
    "country": "USA"
  },
  {
    "product_id": 613,
    "product_name": "Prime Frame 132",
    "stock_quantity": 329,
    "supplier_id": 15,
    "supplier": "GadgetZone",
    "contact_name": "Jessica Turner",
    "phone": "0415947221",
    "email": "jessica.turner@gadgetzone.us",
    "street_address": "Broadway 250",
    "city": "New York",
    "country": "USA"
  },
  {
    "product_id": 691,
    "product_name": "Chic Rocket 492",
    "stock_quantity": 281,
    "supplier_id": 15,
    "supplier": "GadgetZone",
    "contact_name": "Jessica Turner",
    "phone": "0415947221",
    "email": "jessica.turner@gadgetzone.us",
    "street_address": "Broadway 250",
    "city": "New York",
    "country": "USA"
  }
]
```

####  **GET** **`/reports/stock/{threshold}/suppliers`**

Parametrit

| Kenttä     | Tyyppi  | Kuvaus                          |
|------------|---------|---------------------------------|
| threshold  | Number  | Tavaroiden kapplamäärän yläraja |


Onnistunut pyyntö (200) - Esimerkkivastaus:

```json
[
  {
    "product_id": 29,
    "product_name": "Amazing Blob 125",
    "stock_quantity": 3,
    "supplier_id": 7,
    "supplier": "Red Maple Supplies",
    "contact_name": "Daniel Cooper",
    "phone": "0445879630",
    "email": "daniel.cooper@redmaple.ca",
    "street_address": "Maple Street 587",
    "city": "Toronto",
    "country": "Canada"
  },
  {
    "product_id": 702,
    "product_name": "Green Station 670",
    "stock_quantity": 1,
    "supplier_id": 18,
    "supplier": "Nippon Supplies",
    "contact_name": "Hiroshi Tanaka",
    "phone": "0708462179",
    "email": "hiroshi.tanaka@nipponsupplies.jp",
    "street_address": "Chuo Dori 1-4",
    "city": "Tokyo",
    "country": "Japan"
  },
  {
    "product_id": 656,
    "product_name": "Flex Plug 168",
    "stock_quantity": 5,
    "supplier_id": 22,
    "supplier": "Järvi Fisheries",
    "contact_name": "Paavo Moilanen",
    "phone": "0505324421",
    "email": "paavo.moilanen@jarvifisheries.fi",
    "street_address": "Kalasatamantie 2",
    "city": "Helsinki",
    "country": "Suomi"
  },
  {
    "product_id": 852,
    "product_name": "Lucky Handle 263",
    "stock_quantity": 2,
    "supplier_id": 40,
    "supplier": "Veisto Partners",
    "contact_name": "Henrik Rossi",
    "phone": "0412200099",
    "email": "henrik.rossi@veistopartners.fi",
    "street_address": "Narva mnt 5",
    "city": "Tallinn",
    "country": "Estonia"
  }
]
```


---

## TIETOKANNAN OMINAISUUDET

---

### Indeksit

Indekseille on pyritty tehostamaa tauluhin kohdistuvia hakuja. Tähän tietokantaan on toteutettu seuraavat indeksit:

- **idx_customeraddresses_country**
    - Kohdistuu `customeraddresses`-taulun `country`-kenttään. API:ssa haetaan tietoa raportteja varten osoitteiden maan perusteella, joten indeksillä pyritään tehostamaan näitä hakuja.

- **idx_customers_lastname**
    - Kohdistuu `customers`-taulun `last_name`-kenttään. Indeksillä tehostetaan hakuja, joissa asiakkaita haetaan sukunimen perusteella.

- **idx_orders_orderdate**
    - Kohdistuu `delivery_orders`-taulun `order_date`-kenttään. Tilauksia haetaan ja suodatetaan tilauspäivämäärien mukaan, joten indeksillä pyritään tehostamaan näitä operaatioita.

- **idx_orders_status**
    - Kohdistuu `delivery_orders`-taulun `status`-kenttään. API suodattaa tilauksia niiden tilan perusteella useammassa kohdassa, joten indeksillä pyritään tehostamaan näitä hakuja.

- **idx_products_price**
    - Kohdistuu `products`-taulun `price`-kenttään. Tuotteita suodatetaan niiden hintojen perusteella, joten indeksin tarkoitus on tehostaa näitä hakuja.

- **idx_products_stock**
    - Kohdistuu `products`-taulun `stock_quantity`-kenttään. Raportteja varten tuotteita haetaan niiden varastomäärän perusteella, joten indeksillä pyritään tehostamaan näitä hakuja.

- **idx_supplieraddresses_country**
    - Kohdistuu `supplieraddresses`-taulun `country`-kenttään. Raportteja varten haetaan toimittajia heidän sijaintimaidensa perusteella, joten indeksillä pyritään tehostamaan suodatusta.

---

### Transaktiot

Tietokantajärjestelmä sisältää transaktioita erityisesti products-tauluun kohdistuvien pyyntöjen yhteydessä. 
Transaktiolla varmistetaan, että peräkkäiset operaatiot toteutetaan oikein ja kokonaisuudessaan ilman muiden operaatioiden tai transaktioiden häirintää. 
Transaktiolla ja lukoilla voidaan mm. varmistaa, etteivät muut transaktiot pääse lukemaan tai muokkamaan saman tuotteen tietoja samaan aikaan.
Mikäli jokin transaktion operaatio epäonnistuu muut transaktion operaatiot peruutetaan, ettei tietokantaan jää virheellistä dataa.

Järjestelmä käyttää transaktioita erityisesti seuraavissa tilanteissa:

- Uuden tilauksen luonti. Tämä transaktio sisältää vähintää kolme muokkausoperaatiota: tietueen lisääminen orders-tauluun, tuotteen kappalemäärän pienentäminen products-taulussa ja uuden tietueen lisääminen orderitems-tauluun.
- Yksittäisen tuotteen lisääminen tilaukseen. Tämä transaktio sisältää kaksi muokkausoperaatiota: tuotteen kappalemäärän pienentäminen products-taulussa ja uuden tietueen lisääminen orderitems-tauluun.
- Tuotteen varasto päivitys. Tämä transaktio sisältää vain yhden operaation: tuotteen kappalemäärän muuttaminen products-taulussa. Transaktiolla ja lukoilla halutaan kuitenkin varmistaa, etteivät muut operaatiot lue tai päivitä tuotteen tietoja samaan aikaan.

Transaktiot on toteutettu taustapalvelussa @Transactional-annotaatioiden ja @Lock-annotaatioiden, jotka asettavat kirjoituslukkoja tietokannassa, avulla.

---

### Näkymät

Tietokantaan on toteutettu näkymiä kahta eri tarkoitusta varten. Ensimmäiset näkymät on luotu keräämään koostetietoa. 
Toiset näkymät kokoavat yhteen eri taulujen tietoja, jotta niitä voidaan helposti käyttää näkymän kautta. 
Näkymiin kohdistetaan vain SELECT-kyselyitä tässä API:ssa, eli kaikki muokkaukset tehdään suoraan oikeisiin tauluihin, sillä
näkymät ovat joko koostetietoa ja/tai ne on luotu useamman taulun tiedoista, mikä tekee päivittämisestä näkymän kautta hankalaa tai jopa mahdotonta.

Tietokantaan on toteutettu seuraavat näkymät:

- **orders_stats**
  - Kerää yhteen koostetietoa delivery_orders-, orderitems- ja order-tauluista.
  - Laskee kaikkien toimitustilausten lukumäärät ja tuotot maakohtaisesti. Mukaan ei lasketa peruutettuja tilauksia.
  - Näkymästä haetaan tiedot raportteja varten. Raportin voi nähdä oman päätepisteen kautta.

- **order_items**
  - Kerää yhteen tietoa delivery_orders-, orderitems- ja products-tauluista.
  - Näkymässä on sarakkeet tilauksen ID:lle, asiakkaan ID:lle, tuotteen ID:lle, tuotteen nimelle, ostetun tuotteen kappalemäärälle ja -hinnalle sekä kokonaishinnalle.
  - Näkymästä voidaan hakea helposti hakea oleellinen tieto tilauksen kaikista tuotteista. GET-metodien JSON-vastaukset ovat yksinkertaisempia ja siistimpiä, 
  kun tieto haetaan näkymästä, sen sijaan, että backend puolella palautettaisiin useita OrderItem-entiteettejä.

- **stock_suppliers**
  - Kerää yhteen tietoa products-, suppliers- ja supplieraddresses-tauluista.
  - Näkymässä on sarakkeet tuotteen ID:lle, tuotteen nimelle, tuotteen lukumäärälle, eli varastolle, toimittajan ID:lle sekä toimittajan yhteys- ja osoitetiedoille.
  - Näkymästä voidaan hakea mm. tietoa toimittajan tuotteista ja vähissä olevan tuotteen toimittajan tiedot. Näkymää käytetään raportteja varten, joita voi tarkastella päätepisteiden kautta.

---

### Liipaisimet

Tietokannan eheydestä huolehditaan pääasiassa eheysrajoitteiden avulla, eli eri ON DELETE- ja ON UPDATE-määritelmät tietokannan taulujen viiteavainten määrrittelyn yhteydessä.
Backend:n ja frontend:n vastuulla on huolehtia tietyistä operaatioista, jotta tietokanta pysyy muokattavana ja ymmärrettävämpänä. Nämä seikat vähentävät liipaisinten tarvetta tässä tietokannassa.

Tietokantaan on kuitenkin toteutettu yksi liipaisin:

- **validate_stock_quantity**
  - Laukaisu tapahtuu ennen products-tauluun kohdistuvaa päivitysoperaatiota.
  - Liipaisin tarkistaa, että products-taulun stock_quantity sarakkeeseen ei yritetä syöttää negatiivista arvoa. 
  Jos syötettävä arvo on negatiivinen, liipaisin heittää virhekoodin.

---

### Tapahtumat

Tietokantaan on toteutettu yksi ajastettu tapahtuma: 

- **log_daily_sales**
  - Tapahtuu pävittäin vuorokaudenvaihteessa, eli klo. 00.00.
  - Lisää daily_sales-tauluun raportin päivämäärän, päivän tuotot ja tilausten lukumäärän. Ei laske mukaan peruutettuja tilauksia.

---

### Temporaaliominaisuudet

Tässä tietokannassa käytetään pääasiassa kirjausaikaan perustuvaa aikaulottuvuutta. Tiettyjen taulujen muutokset kirjataan ylös, milloin taulun tietuetta on muokattu.
Näin voidaan tarkastella, mitä arvoja tietueella on aikaisemmin ollut tietyllä valillä, ja milloin muutoksia on tapahtunut. Kirjausajan aikaulottuvuus on käytössä
products-, delivery_orders-, customers- ja suppliers-tauluissa. Aikaulottuvuus on otettu käyttöön lisäämällä WITH SYSTEM VERSIONING -määritelmä taulujen perään.

---

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