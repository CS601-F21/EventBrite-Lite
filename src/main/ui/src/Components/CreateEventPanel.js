const CreateEventPanel = (props) => {
  function createEvent(event) {
    event.preventDefault();
    const eventName = document.getElementById("eventNameInput").value;
    const eventLocation = document.getElementById("eventLocationInput").value;
    const eventCapacity = document.getElementById("eventCapacityInput").value;
    const eventPrice = document.getElementById("eventPriceInput").value;
    const eventDate = document.getElementById("eventDateInput").value;
    let userAttending = document.getElementById("eventAttendingInput").checked;
    const sessionId = localStorage.getItem("sessionid");
    // console.log(`event name ${eventName} eventLoc ${eventLocation} eventCap ${eventCapacity} eventPrice ${eventPrice} eventDate ${eventDate} userAttending ${userAttending}`);

    fetch("http://localhost:8080/createevent?sessionid=" + sessionId, {
      method: "POST",
      // mode: "no-cors",
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "http://localhost:8080/search",
        "Access-Control-Allow-Credentials": "true",
      },
      body: JSON.stringify({
        name: eventName,
        location: eventLocation,
        attending: userAttending,
        capacity: eventCapacity,
        price: eventPrice,
        date: eventDate,
      }),
    })
      .then((res) => res.json())
      .then((json) => {
        let res = json[0];
        if (res["ok"] == "true") {
          window.location.reload();
        } else {
          alert("Event not created " + res["message"]);
        }
      });
  }
  return (
    <div className="createEventPane">
      <h3 className="createEventText">Create Event</h3>
      <form onSubmit={createEvent} autocomplete="off">
        <label for="eventName" className="newEventLabel">
          Event name :
        </label>
        <br />
        <input
          type="text"
          id="eventNameInput"
          name="eventName"
          placeholder="Enter Name"
          className="newEventInput"
          required
        />
        <br />
        <label for="eventLocation" className="newEventLabel">
          Location :
        </label>
        <br />
        <input
          type="text"
          id="eventLocationInput"
          name="eventLocation"
          placeholder="Enter Location"
          className="newEventInput"
          required
        />
        <br />
        <label for="eventCapacity" className="newEventLabel">
          Capacity :
        </label>
        <br />
        <input
          type="number"
          id="eventCapacityInput"
          name="eventCapacity"
          placeholder="Enter Capacity"
          className="newEventInput"
          required
        />
        <br />
        <label for="eventPrice" className="newEventLabel">
          Ticket Price :
        </label>
        <br />
        <input
          type="number"
          id="eventPriceInput"
          name="eventPrice"
          placeholder="Enter Price"
          className="newEventInput"
          required
        />
        <br />
        <label for="eventDate" className="newEventLabel">
          Date :
        </label>
        <br />
        <input
          type="date"
          id="eventDateInput"
          name="eventDate"
          className="newEventInput"
          required
        />
        <br />
        <label for="eventAttending" className="newEventLabel">
          Will you be attending the event :
          <input
            type="checkbox"
            id="eventAttendingInput"
            name="eventAttending"
            className="newEventInput"
          />
        </label>
        <br />
        <br />
        <input type="submit" value="Submit" />
      </form>
    </div>
  );
};

export default CreateEventPanel;
