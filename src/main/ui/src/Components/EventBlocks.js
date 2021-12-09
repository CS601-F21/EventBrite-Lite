const EventBlocks = (props) => {
  const events = props.events;
  const setEvents = props.setEvents;
  function buyTicket () {
      const sessionId = localStorage.getItem("sessionid")
      const eventId = props.id;
      fetch("http://localhost:8080/purchaseticket?sessionid="+sessionId+"&eventid="+eventId, {
      method: "POST",
      // mode: "no-cors",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "Access-Control-Allow-Credentials": "true"

      },
    })
      .then((res) => res.json())
      .then((json) => {
        let res = json[0];
        if (res['ok'] === 'true'){
          alert("Purchase Sucessfull")
        } else {
          localStorage.clear();
          alert("Purchase Unsucessful " + res['message'])
          window.location.reload();
        }
      });    
  }


  return (
    <div className="eventBlock" id={props.id}>
      <div className="eventNameAndCapacity">
        <div className="eventName">
          <h1 className="eventNameText">{props.name}</h1>
        </div>
        <div className="eventCapacity">
          <h3 className="eventCapacityText">
            {props.attending}/{props.capacity} tickets booked
          </h3>
        </div>
      </div>
      <div className="eventDate">
        Date : <span>{props.date}</span>
      </div>
      <div className="eventLocation">
        Location : <span>{props.location}</span>
      </div>
      <div className="eventOrganizer">
        Organized By : <span>{props.organizer}</span>
      </div>
      <div className="eventPrice">
        <span>Price : ${props.price}</span>
        <span className="buyButton">
          <button className = "butTicketButton" onClick = {buyTicket} id = {props.id}>Buy Ticket</button>
        </span>
      </div>
    </div>
  );
};

export default EventBlocks;
