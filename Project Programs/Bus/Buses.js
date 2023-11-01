const xhr=new XMLHttpRequest();
xhr.open("GET","/https://api.opentripplanner.org/v2/routers/default/plan?fromPlace="+ start +"&toPlace=" +destination +"&mode=bus");
xhr.setRequestHeader("Content-Type","application/json");
xhr.onload=function(){
    if(xhr.status=== 200){
        const busesavailable=JSON.parse(xhr.responseText);
        const table=document.createElement("table");
        const row=document.createElement("tr");
        const cell1=document.createElement("th");
        cell1.textContent="Bus Name";
        row.appendChild(cell1);
        const cell2=document.createElement("th");
        cell2.textContent="Fare";
        row.appendChild(cell2);
        const cell3=document.createElement("th");
        cell3.textContent="Arrival Time";
        row.appendChild(cell3);
        const cell4=document.createElement("th");
        cell4.textContent="Departure Time";
        row.appendChild(cell4);  
        table.appendChild(row);
        for(const bus of busesavailable){
            const bodyrow=document.createElement("tr");
            const cell1=document.createElement("td");
            cell1.textContent=bus.getName();
            bodyrow.appendChild(cell1);
            const cell2=document.createElement("td");
            cell2.textContent=bus.getFare();
            bodyrow.appendChild(cell2);
            const cell3=document.createElement("td");
            cell3.textContent=bus.getArrivalTime();
            bodyrow.appendChild(cell3);
            const cell4=document.createElement("td");
            cell4.textContent=bus.getDepartureTime();
            bodyrow.appendChild(cell4);
            table.appendChild(bodyrow);
        } 
        document.body.appendChild(table);
    }
};
xhr.send();