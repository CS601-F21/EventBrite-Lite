/**
 * Author : Shubham Pareek
 * Purpose : Component for the lower pane, which will show all the events
 */
import EventBlocks from "./EventBlocks";
const LowerPane = (props) => {
  const events = props.events;
  const setEvents = props.setEvents;

  //we cannot make changes to a state directly hence we create this new variable
  let modifiableEvents = events;
  //mapping the contents of the modifiableEvents to an EventBlock
  modifiableEvents = modifiableEvents.map((event) => (
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
      events={props.events}
      purchasingTicket={true}
    />
  ));

  //showcasing all the events
  return <div className="lowerPane">{modifiableEvents}</div>;
};

export default LowerPane;
