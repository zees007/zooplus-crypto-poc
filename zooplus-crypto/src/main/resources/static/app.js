

window.addEventListener('load', function() {
    listOfCurrencies();
})

const listOfCurrencies = async () => {
    const currencyList = fetch("http://localhost:8080/cryptoCurrencies").then(res => res.json())
    const flatMap = fetch("http://localhost:8080/getFlatmap");
    const list = await currencyList;
    console.log("flatMap", flatMap);
    let dropdown = document.getElementById("cryptoSelect");
    for (let i = 0; i < list.length; i++){
        dropdown[dropdown.length] = new Option(list[i].cryptoCurrencyName, list[i].symbol);
    }

};


const selectedCurrency = async () => {
    const cryptoSelect = document.getElementById("cryptoSelect");
    let ipAddress = document.getElementById("ipAddress")

    console.log("ip Address", ipAddress.value);

    const getSelectedCurrency = fetch("http://localhost:8080/getSelectedCrypto?" + new URLSearchParams({
        symbol: cryptoSelect.value,
        ipAddress: ipAddress.value
    }))
        .then(response => response.json());
    const currDetails =  await getSelectedCurrency;
    const para = document.createElement("p");
    const node = document.createTextNode("Current Unit price of " + currDetails.cryptoCurrencyName + " is "  + currDetails.sign + " " + currDetails.price);
    para.appendChild(node);
    const element = document.getElementById("displayId");
    element.appendChild(para);


}

function getCurrencyDetails() {
    selectedCurrency();
}



