function loadJSON(callback) {   

    let xobj = new XMLHttpRequest();
        xobj.overrideMimeType("application/json");
    xobj.open('GET', 'data.json', true);
    xobj.onreadystatechange = function () {
          if (xobj.readyState == 4 && xobj.status == "200") {
            callback(xobj.responseText);
          }
    };
    xobj.send(null);  
}


function init() {
    loadJSON(function(response) {

        let actual_JSON = JSON.parse(response);

        let points = document.getElementById("driver-points");

        actual_JSON["races"].forEach(race => {
            let new_th = document.createElement("th");
            new_th.textContent = race;
            new_th.className = "gp";
            points.before(new_th);
        });

        let driver_data = document.getElementById("driver-data");

        actual_JSON["drivers"].forEach(driver => {//Tableau des pilotes
            let new_tr = document.createElement("tr");

            let position = document.createElement("th");
            position.scope = "row";
            position.textContent = driver["position"];

            let name = document.createElement("td");
            name.textContent = driver["name"];
            name.className = "score";

            let teamname = document.createElement("td");
            teamname.textContent = driver["teamname"];

            new_tr.append(position);
            new_tr.append(teamname);
            new_tr.append(name);

            for (let i = 0; i < driver["results"].length; i++) {
                let res = document.createElement("td");
                res.textContent = driver["results"][i];
                switch (driver["positions"][i]) {
                    case 1:
                        res.className = "first-place sp";
                        break;
                    case 2:
                        res.className = "second-place sp";
                        break;
                    case 3:
                        res.className = "third-place sp";
                        break;
                    case -1:
                        res.className = "dns sp";
                        break;
                }
                new_tr.append(res);
              }

            let points = document.createElement("td");
            points.textContent = driver["totalpoints"];
            points.className = "score";
            new_tr.append(points);

            driver_data.append(new_tr);
        });

        let team_data = document.getElementById("team-data");

        actual_JSON["teams"].forEach(team => {//Tableau des equipes


            let new_tr = document.createElement("tr");
            switch (team["position"]) {
                case 1:
                    new_tr.className = "first-place";
                    break;
                case 2:
                    new_tr.className = "second-place";
                    break;
                case 3:
                    new_tr.className = "third-place";
                    break;
            }

            let position = document.createElement("th");
            position.scope = "row";
            position.textContent = team["position"];

            let name = document.createElement("td");
            name.textContent = team["name"];
            name.className = "score";

            let victories = document.createElement("td");
            victories.textContent = team["victories"];

            let podiums = document.createElement("td");
            podiums.textContent = team["podiums"];

            let points = document.createElement("td");
            points.textContent = team["points"];
            points.className = "score";

            new_tr.append(position);
            new_tr.append(name);
            new_tr.append(victories);
            new_tr.append(podiums);
            new_tr.append(points);

            team_data.append(new_tr);
        });

        //date 

        let jsondate = actual_JSON["date"];

        let dates = document.querySelectorAll(".updated");
        dates.forEach(date => {
            date.textContent = jsondate;
        });

    });
}

init();
