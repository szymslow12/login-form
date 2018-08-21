function setHeaderWithUserName() {
    let xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = () => {
        if (this.readyState == 4 & this.status == 200) {
            let json = JSON.parse(this.responseText);
            if (json) {
                let header = document.getElementsByTagName("h1")[0];
                header.textContent = "Welcome " + json.fistName + "!";
            }
        }
    };

    xhttp.open("GET", "/userData", true);
    xhttp.send(null);
}