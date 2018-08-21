function setHeaderWithUserName() {
    let xhttp = new XMLHttpRequest();
    
    xhttp.onreadystatechange = () => {
        if (xhttp.readyState == 4 & xhttp.status == 200) {
            let json = JSON.parse(xhttp.responseText);
            if (json) {
                let header = document.createElement("h1");
                header.textContent = "Welcome " + json.firstName + "!";
                document.getElementsByClassName("container")[0].appendChild(header);
            }
        }
    };

    xhttp.open("GET", "/userData", true);
    xhttp.send(null);
}