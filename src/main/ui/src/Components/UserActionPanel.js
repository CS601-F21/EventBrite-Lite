import EventBlocks from "./EventBlocks";
const UserActionPanel = (props) => {
    const userEvents = props.userEvents;
    console.log(userEvents);
    let modifiableEvents = userEvents.map((event) => (
        <EventBlocks
          key={event.id}
          name={event.Name}
          attending={event.Attending}
          capacity={event.Capacity}
          date={event.Date}
          location={event.Location}
          id={event.id}
          organizer={event.Organizer}
          price={event.Price}
    
          events = {props.events}
          purchasingTicket = {false}
          sessionId = {props.sessionId}
        />
      ));
    return (
        <div className = "userActionPane">
            {modifiableEvents}
        </div>
    )
}

export default UserActionPanel;