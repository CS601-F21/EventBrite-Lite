/**
 * Author : Shubham Pareek
 * Purpose : Component for the EventBlock
 */
const EventBlocks = (props) => {
  const events = props.events;
  const setEvents = props.setEvents;

  //function which makes the api call everytime the user wants to buy a ticket
  function buyTicket() {
    //getting the sessionId from the localStorage
    const sessionId = localStorage.getItem("sessionid");
    const eventId = props.id;
    fetch(
      "http://localhost:8080/purchaseticket?sessionid=" +
        sessionId +
        "&eventid=" +
        eventId,
      {
        method: "POST",
        // mode: "no-cors",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Credentials": "true",
        },
      }
    )
      .then((res) => res.json())
      .then((json) => {
        let res = json[0];
        //letting the user know whether the purchase was sucessful or not
        if (res["ok"] === "true") {
          alert("Purchase Sucessful");
          window.location.reload();
        } else {
          localStorage.clear();
          alert("Purchase Unsucessful " + res["message"]);
          window.location.reload();
        }
      });
  }

  //function which will execute if the user wants to transfer the ticket
  function transferTicket(event) {
    event.preventDefault();
    //getting the email id of the person to whom the ticket is to be transferred
    const email = document.getElementById("transferTicket").value;
    //getting the sessionId
    const sessionId = props.sessionId;
    //getting the eventId
    const eventId = props.id;
    console.log("Received id " + eventId + " sending to " + email);
    /**
     * /transferticket?sessionid={sessionid}
     *          {
     *              to : {email address of the user who the ticket is being transferred to}
     *              eventId : {id of the event}
     *          }
     */
    fetch("http://localhost:8080/transferticket?sessionid=" + sessionId, {
      method: "POST",
      // mode: "no-cors",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "http://localhost:8080/search",
        "Access-Control-Allow-Credentials": "true",
      },
      body: JSON.stringify({
        to: email,
        eventId: eventId,
      }),
    })
      .then((res) => res.json())
      .then((json) => {
        let res = json[0];

        //letting the user know whether the purchase was sucesfull or not
        if (res["ok"] == "true") {
          window.location.reload();
        } else {
          alert("Transfer failed : " + res["message"]);
          window.location.reload();
        }
      });
  }

  //The HTML of the event block is different based on which page the user is on
  if (props.purchasingTicket) {
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
            <button
              className="butTicketButton"
              onClick={buyTicket}
              id={props.id}
            >
              Buy Ticket
            </button>
          </span>
        </div>
      </div>
    );
  } else {
    return (
      <div className="eventBlock userEventBlock" id={props.id}>
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
        <div className="transferTicket">
          <form onSubmit={transferTicket}>
            <label for="transferTicket" className="transferTicketLabel">
              Transfer Ticket To (Email of receipient) :
              <input
                type="email"
                id="transferTicket"
                name="transferTicket"
                className="transferTicket"
                required
              />
            </label>
            <input
              className="transferSubmitButton"
              type="submit"
              value="Submit"
            />
          </form>
        </div>
      </div>
    );
  }
};

export default EventBlocks;
