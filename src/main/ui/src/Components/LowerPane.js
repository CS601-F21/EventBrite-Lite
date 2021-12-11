import EventBlocks from "./EventBlocks";
const LowerPane = (props) => {
  const events = props.events;
  const setEvents = props.setEvents;
  let modifiableEvents = events;
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
  return <div className="lowerPane">{modifiableEvents}</div>;
};

export default LowerPane;
