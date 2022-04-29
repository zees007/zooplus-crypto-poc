# zooplus-crypto-poc

Tect Stack used: spring boot, HTML, CSS, JavaScript
In Memory H2 db
GeoIP DB for country


My Assumptions: 

1. I've used coin maker api to get the all avaiable crypto currencies.
2. I've used GeoLite2 database (GeoLite2-Country.mmdb) from MaxMind to fetch the country and ISO Code of country from IP address.
3. Coin maker api has feature to provide each country currency symbol and sign. I fetched these info and stored in in-memory db.
4. When we select crypto currency from dropdown with IP address, I called coin maker api with crypto currency code which return below data.

<img width="631" alt="image" src="https://user-images.githubusercontent.com/41251660/165867509-2e588790-7cf3-4c5a-8308-5dec97645242.png">



